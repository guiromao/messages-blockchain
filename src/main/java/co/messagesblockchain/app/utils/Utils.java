package co.messagesblockchain.app.utils;

import co.messagesblockchain.app.model.Block;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static List<Block> reverseList(List<Block> blocks){
        List<Block> resultBlocks = new ArrayList<>();

        for(int i = blocks.size() - 1; i >= 0; i--){
            resultBlocks.add(blocks.get(i));
        }

        return resultBlocks;
    }

}
