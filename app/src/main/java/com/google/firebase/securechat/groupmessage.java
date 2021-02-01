package com.google.firebase.securechat;

public class groupmessage {
    String username;
    String message;
    groupmessage(String a,String b){
        username=a;
        message=b;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
