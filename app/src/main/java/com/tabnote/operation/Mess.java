package com.tabnote.operation;

public class Mess {
    private int role=0;
    private String mess="";
    private int messType=0;
    public Mess(int role,String mess,int messType){
        this.mess=mess;
        this.role=role;
        this.messType=messType;
    }
    public String getMess() {
        return mess;
    }

    public int getRole() {
        return role;
    }

    public int getMessType() {
        return messType;
    }

    public void setMess(String mess) {
        this.mess = mess;
    }
    public void addWord(String word) {
        this.mess += word;
    }
}
