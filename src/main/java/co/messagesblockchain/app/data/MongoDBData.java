package co.messagesblockchain.app.data;

import co.messagesblockchain.app.model.Block;
import com.mongodb.*;
import org.springframework.stereotype.Component;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Component
public class MongoDBData implements Data {

    private final String LAST_BLOCK = "last-block";

    private MongoClient client;
    private DB database;
    private DBCollection collection;
    private Block lastBlock;

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

    private Block getLastBlock() {
        Block result = null;

        DBObject query = new BasicDBObject(LAST_BLOCK, true);
        DBCursor all = collection.find(query);

        if(all.hasNext()){
            result = new Block((String) all.one().get("message"), (int) all.one().get("previousHash"), (int) all.one().get("currentHash"));
        }

        return result;
    }

    @Override
    public List<Block> list() {
        DBCursor results = collection.find();
        List<Block> allBlocks = new ArrayList<>();

        for(DBObject message: results){
            Block block = new Block((String)message.get("message"), (int)message.get("previousHash"), (int)message.get("currentHash"));
            allBlocks.add(block);
        }

        return allBlocks;
    }

    @Override
    public void add(Block message) {
        lastBlock = getLastBlock();
        int lastHash = (lastBlock != null) ? lastBlock.getCurrentHash() : 0;

        DBObject object = new BasicDBObject("message", message.getMessage())
                            .append("previousHash", lastHash)
                            .append("currentHash", message.getCurrentHash())
                            .append(LAST_BLOCK, true);
        collection.insert(object);

        //remove last block
        if(lastBlock != null){
            DBObject query = new BasicDBObject("currentHash", lastBlock.getCurrentHash());
            DBCursor cursor = collection.find(query);
            DBObject previousLast = cursor.one();
            previousLast.removeField(LAST_BLOCK);

            collection.update(query, previousLast);
        }

        lastBlock = message;
    }

    @Override
    public Optional<Block> getByHash(int hash) {
        DBObject query = new BasicDBObject("currentHash", hash);
        DBCursor cursor = collection.find(query);
        Block block = new Block((String)cursor.one().get("message"), (int)cursor.one().get("previousHash"), hash);

        return Optional.ofNullable(block);
    }

    @Override
    public Optional<Block> lastBlock() {
        return Optional.ofNullable(lastBlock);
    }

    @Override
    public void deleteAll() {
        DBCursor cursor = collection.find();

        for(DBObject message: cursor){
            collection.remove(message);
        }
    }


}
