package co.messagesblockchain.app.services;

import co.messagesblockchain.app.data.MongoDBData;
import co.messagesblockchain.app.dto.MessageDto;
import co.messagesblockchain.app.model.Block;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService implements BlocksService {

    @Autowired
    private MongoDBData mongoDb;

    public MessageService(){


    }

    @Override
    public List<Block> getMessages() {
        return mongoDb.list();
    }

    @Override
    public void addBlock(Block message) {
        Optional<Block> maybeLastBlock = mongoDb.getLastBlock();
        int hash = (maybeLastBlock.isPresent()) ? maybeLastBlock.get().getCurrentHash() : 0;

        Block blockToAdd = new Block(message.getMessage(), hash);

        mongoDb.add(blockToAdd);
    }

    @Override
    public Optional<Block> getByHash(int hash) {
        return mongoDb.getByHash(hash);

                /*messageBlocks.stream()
                .filter((block) -> block.getCurrentHash() == hash)
                .findAny();*/
    }

    @Override
    public void deleteAll() {
        mongoDb.deleteAll();
    }

    @Override
    public boolean isChainValid() {
        return mongoDb.isChainValid();
    }

}
