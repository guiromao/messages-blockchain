package co.messagesblockchain.app.services;

import co.messagesblockchain.app.data.MongoDBData;
import co.messagesblockchain.app.model.Block;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService implements IBlocksService {

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
        int hash = maybeLastBlock.map(Block::getCurrentHash).orElse(0);
        int previousNumber = maybeLastBlock.isPresent() ? maybeLastBlock.get().getBlockNumber() : 0;

        Block blockToAdd = new Block(message.getMessage(), hash, previousNumber);

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
    public Optional<Block> getByNumber(Integer number) {
        return mongoDb.getByNumber(number);
    }

    @Override
    public void deleteAll() {
        mongoDb.deleteAll();
    }

    @Override
    public boolean isChainValid() {
        return mongoDb.isChainValid();
    }

    @Override
    public List<Block> getRangeOfBlocks() {
        return mongoDb.getRangeOfBlocks();
    }

}
