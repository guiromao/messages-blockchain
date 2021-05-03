package co.messagesblockchain.app.data;

import co.messagesblockchain.app.model.Block;

import java.util.List;
import java.util.Optional;

public interface Data {

    List<Block> list();

    void add(Block message);

    Optional<Block> getByHash(int hash);

    Optional<Block> lastBlock();

    void deleteAll();

}
