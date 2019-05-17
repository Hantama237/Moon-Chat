package com.hantama.climberschat;

public class Chatlist {
    private String id;
    String lastMessage;
    int countMessage;
//    private boolean read;

    public Chatlist(String id,String lastMessage,int countMessage/*, boolean read*/) {
        this.id = id;
        this.lastMessage=lastMessage;
        this.countMessage=countMessage;
//        this.read = read;
    }

    public int getCountMessage() {
        return countMessage;
    }

    public void setCountMessage(int countMessage) {
        this.countMessage = countMessage;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public Chatlist(){
        id="";
        countMessage=0;
        lastMessage="No Last Message Data...";
    }

    /*public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }*/

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
