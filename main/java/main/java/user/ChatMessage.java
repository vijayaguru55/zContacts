package main.java.user;

import java.util.concurrent.atomic.AtomicLong;

public class ChatMessage {
    private String message;
    private String senderId;
    private String receiverId;
    private String date;
    private String time;
    public ChatMessage(String text, String sender, String recipient,String date,String time) {
        this.message = text;
        this.senderId = sender;
        this.receiverId = recipient;
        this.time=time;
        this.date=date;
    }
   
    public String getTime(){
        return this.time;
    }
    public String getDate(){
        return this.date;
    }
    public String getText() {
        return this.message;
    }

    public void setText(String text) {
        this.message = text;
    }

    public String getSender() {
        return this.senderId;
    }

    public void setSender(String sender) {
        this.senderId = sender;
    }

    public String getRecipient() {
        return this.receiverId;
    }

    public void setRecipient(String recipient) {
        this.receiverId = recipient;
    }
}
