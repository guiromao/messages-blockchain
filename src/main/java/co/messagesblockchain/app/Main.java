package co.messagesblockchain.app;

import co.messagesblockchain.app.model.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Main {

    public static void main(String[] args) {
        List<Block> blockchain = new ArrayList<>();

        Block block1 = new Block("Hello, World!", 0);
        blockchain.add(block1);

        Block block2 = new Block("I'm a new message yeahhh!", blockchain.get(blockchain.size() - 1).getCurrentHash());
        blockchain.add(block2);

        Block block3 = new Block("And this is an even newer message!", blockchain.get(blockchain.size() - 1).getCurrentHash());
        blockchain.add(block3);

        Block block4 = new Block("Yet a new block to the chain!", blockchain.get(blockchain.size() - 1).getCurrentHash());
        blockchain.add(block4);

        blockchain.stream()
                .forEach(System.out::println);

        System.out.println();

        Optional<Block> maybeBlock = blockchain.stream()
                .filter((block) -> block.getCurrentHash() == 887402152)
                .findFirst();

        if(maybeBlock.isPresent()){
            Block foundBlock = maybeBlock.get();
            System.out.println("Found block:\n" + foundBlock.toString() + "\nBlock number: " + (blockchain.indexOf(foundBlock) + 1));
        }
        else {
            System.out.println("Searched block not found");
        }

        System.out.println();

        long initTimeOne = System.currentTimeMillis();
        long chainSize = blockchain.stream().count(); // == blockchain.size()
        long timeOne = System.currentTimeMillis() - initTimeOne;

        System.out.println("Stream count made in " + timeOne + " milliseconds");

        long initTimeTwo = System.currentTimeMillis();
        long chainSizeBySize = blockchain.size();
        long timeTwo = System.currentTimeMillis() - initTimeTwo;
        System.out.println("Count by List size done in " + timeTwo + " milliseconds\n");

        System.out.println("Size has this amount of blocks: " + chainSize);

        System.out.println();

        initTimeOne = System.currentTimeMillis();
        long positiveHashes = blockchain.stream()
                                .filter((block) -> block.getCurrentHash() > 0)
                                .count();
        timeOne = System.currentTimeMillis() - initTimeOne;

        initTimeTwo = System.currentTimeMillis();
        int count = 0;
        for(int i = 0; i < blockchain.size(); i++){
            if(blockchain.get(i).getCurrentHash() > 0){
                count++;
            }
        }
        timeTwo = System.currentTimeMillis() - initTimeTwo;

        System.out.println("Counting positive hashes with Stream done in " + timeOne + " milliseconds");
        System.out.println("Counting positive hashes with \"for\" done in " + timeTwo + " milliseconds\n");

        System.out.println("Number of blocks with positive hashes: " + positiveHashes);
    }

}
