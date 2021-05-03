package co.messagesblockchain.app.services;

import co.messagesblockchain.app.dto.MessageDto;
import co.messagesblockchain.app.model.Block;

import java.util.List;
import java.util.Optional;

public interface BlocksService {

    List<Block> getMessages();

    void addBlock(Block message);

    Optional<Block> getByHash(int hash);

    void deleteAll();

    boolean isChainValid();

    List<Block> getRangeOfBlocks();
}
