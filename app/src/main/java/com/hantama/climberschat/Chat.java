package com.hantama.climberschat;

public class Chat {
    private String sender;
    private String receiver;
    private String message;
    private boolean read;
    private String date;

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public Chat(String sender, String receiver, String message, boolean read,String date) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.read=read;
        this.date=date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date =date;
    }

    public Chat(){
        read=true;
        date="No Date";
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
