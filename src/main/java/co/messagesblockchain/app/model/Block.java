package co.messagesblockchain.app.model;

import java.util.Arrays;

public class Block {

    private String message;
    private int previousHash;
    private int currentHash;

    public Block(String msg, int prevHash){
        message = msg;
        previousHash = prevHash;
        currentHash = Arrays.hashCode(new int [] { message.hashCode(), previousHash });
    }

    @Override
    public String toString() {
        return "Block{" +
                "message='" + message + '\'' +
                ", previousHash=" + previousHash +
                ", currentHash=" + currentHash +
                '}';
    }

    public String getMessage() {
        return message;
    }

    public int getPreviousHash() {
        return previousHash;
    }

    public int getCurrentHash() {
        return currentHash;
    }

    public void setPreviousHash(int previousHash) {
        this.previousHash = previousHash;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCurrentHash(int currentHash) {
        this.currentHash = currentHash;
    }

}
