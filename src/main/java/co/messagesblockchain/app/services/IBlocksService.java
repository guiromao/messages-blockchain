package co.messagesblockchain.app.services;

import co.messagesblockchain.app.model.Block;

import java.util.List;
import java.util.Optional;

public interface IBlocksService {

    List<Block> getMessages();

    void addBlock(Block message);

    Optional<Block> getByHash(int hash);

    Optional<Block> getByNumber(Integer number);

    void deleteAll();

    boolean isChainValid();

    List<Block> getRangeOfBlocks();
}
