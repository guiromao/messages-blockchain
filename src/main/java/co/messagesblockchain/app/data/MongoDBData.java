package co.messagesblockchain.app.data;

import co.messagesblockchain.app.model.Block;
import co.messagesblockchain.app.utils.Utils;
import com.mongodb.*;
import org.springframework.stereotype.Component;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Component
public class MongoDBData implements Data {

    private final String LAST_BLOCK = "last-block";
    private final String MESSAGE = "message";
    private final String PREVIOUS_HASH = "previousHash";
    private final String CURRENT_HASH = "currentHash";

    private final int OFFSET = 2;

    private MongoClient client;
    private DB database;
    private DBCollection collection;
    private Block lastBlock;

    private int offsetHash;
    private Block lastRangedBlock;

    private static Logger logger = Logger.getLogger("co.messages-blockchain");

    public MongoDBData() {
        try {
            client = new MongoClient("localhost");//new MongoClientURI("mongodb://gromao:***@cluster0.hespl.mongodb.net"));

            database = client.getDB("messages-blockchain");
            collection = database.getCollection("blocks");

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        offsetHash = -1;
    }

    @Override
    public Optional<Block> getLastBlock() {
        Block result = null;

        DBObject query = new BasicDBObject(LAST_BLOCK, true);
        DBCursor last = collection.find(query);

        if (last.hasNext()) {
            result = new Block(last);
        }

        return Optional.ofNullable(result);
    }

    @Override
    public List<Block> list() {
        DBCursor results = collection.find();
        List<Block> allBlocks = new ArrayList<>();

        for (DBObject message : results) {
            Block block = new Block(message);
            allBlocks.add(block);
        }

        return allBlocks;
    }

    @Override
    public synchronized void add(Block message) {
        Optional<Block> maybeLastBlock = getLastBlock();
        int lastHash = (maybeLastBlock.isPresent()) ? maybeLastBlock.get().getCurrentHash() : 0;

        DBObject object = new BasicDBObject(MESSAGE, message.getMessage())
                .append(PREVIOUS_HASH, lastHash)
                .append(CURRENT_HASH, message.getCurrentHash())
                .append(LAST_BLOCK, true);
        collection.insert(object);

        //remove last registered block
        if (maybeLastBlock.isPresent()) {
            lastBlock = maybeLastBlock.get();
            DBObject query = new BasicDBObject(CURRENT_HASH, lastBlock.getCurrentHash());
            DBCursor cursor = collection.find(query);
            DBObject previousLast = cursor.one();
            previousLast.removeField(LAST_BLOCK);

            collection.update(query, previousLast);
        }

        //update last block
        lastBlock = message;
    }

    @Override
    public Optional<Block> getByHash(int hash) {
        DBObject query = new BasicDBObject(CURRENT_HASH, hash);
        DBCursor cursor = collection.find(query);
        Block block = new Block(cursor, hash);

        return Optional.ofNullable(block);
    }

    @Override
    public void deleteAll() {
        DBCursor cursor = collection.find();

        for (DBObject message : cursor) {
            collection.remove(message);
        }
    }

    @Override
    public boolean isChainValid() {
        DBCursor chain = collection.find();
        int previousHash = 0;

        if (chain.size() > 1) {
            long countBlocks = 0;
            int currentHash = 0;
            for (DBObject message : chain) {

                if (countBlocks > 0) {
                    previousHash = (int) message.get(PREVIOUS_HASH);
                    if (previousHash == currentHash) {
                        currentHash = (int) message.get(CURRENT_HASH);
                    } else {
                        return false;
                    }
                } else {
                    currentHash = (int) message.get(CURRENT_HASH);
                }
                countBlocks++;
            }
        }
        return true;
    }

    @Override
    public List<Block> getRangeOfBlocks() {
        List<Block> listBlocks = new ArrayList<>();
        Block block;

        if (offsetHash == -1) {
            Optional<Block> maybeBlock = getLastBlock();

            if (maybeBlock.isPresent()) {
                block = maybeBlock.get();
                listBlocks.add(block);
                lastRangedBlock = block;
                offsetHash = lastRangedBlock.getCurrentHash();
            }
        }

        for (int i = listBlocks.size(); i < OFFSET && lastRangedBlock.getPreviousHash() != 0; i++) {
            DBObject query = new BasicDBObject(CURRENT_HASH, lastRangedBlock.getPreviousHash());
            DBCursor nextCursor = collection.find(query);

            block = new Block(nextCursor);
            listBlocks.add(block);
            lastRangedBlock = block;
            offsetHash = lastRangedBlock.getCurrentHash();
        }

        return Utils.reverseList(listBlocks);
    }




}
