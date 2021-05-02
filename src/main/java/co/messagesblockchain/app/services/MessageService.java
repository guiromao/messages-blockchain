package co.messagesblockchain.app.services;

import co.messagesblockchain.app.dto.MessageDto;
import co.messagesblockchain.app.model.Block;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {

    private List<Block> messageBlocks;

    public MessageService(){
        messageBlocks = new ArrayList<>();
        Block block1 = new Block("Hello, World!", 0);
        messageBlocks.add(block1);

        Block block2 = new Block("I'm a new message yeahhh!", messageBlocks.get(messageBlocks.size() - 1).getCurrentHash());
        messageBlocks.add(block2);

        Block block3 = new Block("And this is an even newer message!", messageBlocks.get(messageBlocks.size() - 1).getCurrentHash());
        messageBlocks.add(block3);

        Block block4 = new Block("Yet a new block to the chain!", messageBlocks.get(messageBlocks.size() - 1).getCurrentHash());
        messageBlocks.add(block4);
    }

    public List<Block> getMessages() {
        return messageBlocks;
    }

    public void addBlock(MessageDto message) {
        Block blockToAdd = new Block(message.getMessage(), messageBlocks.get(messageBlocks.size() - 1).getCurrentHash());
        messageBlocks.add(blockToAdd);
    }
}
