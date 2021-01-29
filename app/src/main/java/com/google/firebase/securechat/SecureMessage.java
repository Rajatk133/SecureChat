package com.google.firebase.securechat;

public class SecureMessage {

    private String text;
   private int oid;
    private String sender;
    private  String receiver;
    public SecureMessage() {
    }

    public SecureMessage(String text, String a,String b,int c) {
        this.text = text;
        this.sender = a;
        this.receiver=b;
     //  this.user=c;
        this.oid=c;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSender() {
        return sender;
    }

    public int getOid() {
        return oid;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }


}
