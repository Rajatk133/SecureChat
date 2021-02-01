package com.google.firebase.securechat;

public class groupNameInfo {
    String groupname;
    String UesrsNo;
    String Admin;
    groupNameInfo(String a,String b,String c){
        groupname=a;
        UesrsNo=b;
        Admin=c;
    }

    public String getUesrsNo() {
        return UesrsNo;
    }

    public String getAdmin() {
        return Admin;
    }

    public void setAdmin(String admin) {
        Admin = admin;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public void setUesrsNo(String uesrsNo) {
        UesrsNo = uesrsNo;
    }
}
