package co.messagesblockchain.app.data;

import co.messagesblockchain.app.model.Block;

import java.util.List;
import java.util.Optional;

public interface IData {

    List<Block> list();

    void add(Block message);

    Optional<Block> getByHash(int hash);

    Optional<Block> getByNumber(Integer number);

    Optional<Block> getLastBlock();

    void deleteAll();

    boolean isChainValid();

    List<Block> getRangeOfBlocks();
}
