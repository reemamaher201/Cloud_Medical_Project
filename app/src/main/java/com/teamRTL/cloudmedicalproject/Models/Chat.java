package com.teamRTL.cloudmedicalproject.Models;


public class Chat {
    private  String sender;
    private String receiver;
    private String message;


    private int isSeen;

    public Chat(String sender, String receiver, String message, int isSeen) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.isSeen =isSeen;
    }

    public Chat() {
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




    public int getIsSeen() {
        return isSeen;
    }

    public void setIsSeen(int isSeen) {
        this.isSeen = isSeen;
    }
}
