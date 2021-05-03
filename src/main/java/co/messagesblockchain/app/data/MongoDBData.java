package co.messagesblockchain.app.data;

import co.messagesblockchain.app.model.Block;
import com.mongodb.*;
import org.springframework.stereotype.Component;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class MongoDBData implements Data {

    private final String LAST_BLOCK = "last-block";
    private final String MESSAGE = "message";
    private final String PREVIOUS_HASH = "previousHash";
    private final String CURRENT_HASH = "currentHash";

    private MongoClient client;
    private DB database;
    private DBCollection collection;
    private Block lastBlock;

    private static Logger logger = Logger.getLogger("co.messages-blockchain");

    public MongoDBData(){
        try {
            client = new MongoClient("localhost");//new MongoClientURI("mongodb://gromao:Tech@cluster0.hespl.mongodb.net"));

            database = client.getDB("messages-blockchain");
            collection = database.getCollection("blocks");

            //lastBlock = getLastBlock();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Block> getLastBlock() {
        Block result = null;

        DBObject query = new BasicDBObject(LAST_BLOCK, true);
        DBCursor last = collection.find(query);

        if(last.hasNext()){
            result = new Block((String) last.one().get(MESSAGE), (int) last.one().get(PREVIOUS_HASH), (int) last.one().get(CURRENT_HASH));
        }

        return Optional.ofNullable(result);
    }

    @Override
    public List<Block> list() {
        DBCursor results = collection.find();
        List<Block> allBlocks = new ArrayList<>();

        for(DBObject message: results){
            Block block = new Block((String)message.get(MESSAGE), (int)message.get(PREVIOUS_HASH), (int)message.get(CURRENT_HASH));
            allBlocks.add(block);
        }

        return allBlocks;
    }

    @Override
    public void add(Block message) {
        Optional<Block> maybeLastBlock = getLastBlock();
        int lastHash = (maybeLastBlock.isPresent()) ? maybeLastBlock.get().getCurrentHash() : 0;

        DBObject object = new BasicDBObject(MESSAGE, message.getMessage())
                            .append(PREVIOUS_HASH, lastHash)
                            .append(CURRENT_HASH, message.getCurrentHash())
                            .append(LAST_BLOCK, true);
        collection.insert(object);

        //remove last block
        if(maybeLastBlock.isPresent()){
            lastBlock = maybeLastBlock.get();
            DBObject query = new BasicDBObject(CURRENT_HASH, lastBlock.getCurrentHash());
            DBCursor cursor = collection.find(query);
            DBObject previousLast = cursor.one();
            previousLast.removeField(LAST_BLOCK);

            collection.update(query, previousLast);
        }

        lastBlock = message;
    }

    @Override
    public Optional<Block> getByHash(int hash) {
        DBObject query = new BasicDBObject(CURRENT_HASH, hash);
        DBCursor cursor = collection.find(query);
        Block block = new Block((String)cursor.one().get(MESSAGE), (int)cursor.one().get(PREVIOUS_HASH), hash);

        return Optional.ofNullable(block);
    }

    @Override
    public void deleteAll() {
        DBCursor cursor = collection.find();

        for(DBObject message: cursor){
            collection.remove(message);
        }
    }

    @Override
    public boolean isChainValid() {
        DBCursor chain = collection.find();
        int previousHash = 0;

        if(chain.size() > 1){
            long countBlocks = 0;
            int currentHash = 0;
            for(DBObject message: chain){

                if(countBlocks > 0){
                    previousHash = (int) message.get(PREVIOUS_HASH);
                    if(previousHash == currentHash){
                        currentHash = (int) message.get(CURRENT_HASH);
                    }
                    else {
                        return false;
                    }
                }
                else {
                    currentHash = (int) message.get(CURRENT_HASH);
                }
                countBlocks++;

                if(countBlocks == chain.size() - 1){
                    return true;
                }
            }
        }
        return true;
    }


}
