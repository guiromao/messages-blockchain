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

    private MongoClient client;
    private DB database;
    private DBCollection collection;

    public MongoDBData(){
        try {
            client = new MongoClient("localhost");//new MongoClientURI("mongodb://gromao:Tech@cluster0.hespl.mongodb.net"));

            database = client.getDB("messages-blockchain");
            collection = database.getCollection("blocks");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
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
        DBObject object = new BasicDBObject("message", message.getMessage())
                            .append("previousHash", message.getPreviousHash())
                            .append("currentHash", message.getCurrentHash());
        collection.insert(object);
    }

    @Override
    public Optional<Block> getByHash(int hash) {
        DBObject query = new BasicDBObject("currentHash", hash);
        DBCursor cursor = collection.find(query);
        Block block = new Block((String)cursor.one().get("message"), (int)cursor.one().get("previousHash"), hash);

        return Optional.ofNullable(block);
    }

}
