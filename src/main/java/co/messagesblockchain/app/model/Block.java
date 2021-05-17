package co.messagesblockchain.app.model;

import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import java.util.Arrays;

public class Block {

    private String message;
    private int nonce;
    private int previousHash;
    private int currentHash;

    public Block(){

    }

    public Block(String msg, int prevHash){
        message = msg;
        previousHash = prevHash;

        int nZeros = sizeZeros();
        int goal = setGoal(nZeros);
        System.out.println("Goal is: " + goal);
        currentHash = 1;

        for(nonce = (int) (Math.random() * 10000); Math.abs(currentHash) < goal; nonce = (int) (Math.random() * 10000)){
            currentHash = Arrays.hashCode(new int [] { message.hashCode(), previousHash, nonce });
        }

    }

    public Block(DBCursor msgCursor){
        message = (String) msgCursor.one().get("message");
        previousHash = (int) msgCursor.one().get("previousHash");
        currentHash = (int) msgCursor.one().get("currentHash");
    }

    public Block(DBCursor msgCursor, int hash){
        message = (String) msgCursor.one().get("message");
        previousHash = (int) msgCursor.one().get("previousHash");
        currentHash = hash;
    }

    public Block(DBObject msgObject){
        message = (String) msgObject.get("message");
        previousHash = (int) msgObject.get("previousHash");
        currentHash = (int) msgObject.get("currentHash");
    }

    private int setGoal(int nZeros){
        String string = "";

        for(int i = 0; i != 9; i++){
            if(nZeros > 0){
                string += "0";
                nZeros--;
            }
            else {
                string += "9";
            }
        }

        return Integer.parseInt(string);
    }

    private int sizeZeros(){
        return (int) (Math.random() * 5);
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
