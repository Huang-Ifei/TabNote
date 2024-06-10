package com.tabnote.operation;

import com.alibaba.fastjson2.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class AiOperation {

    public AiOperation() {

    }

    public static BufferedReader post(ArrayList<Mess> messes,String token) throws IOException {
        JSONObject json = new JSONObject();
        json.putArray("messages");
        for(Mess mess:messes){
            JSONObject jsonMess = new JSONObject();
            if(!mess.getMess().isEmpty()){
                if (mess.getRole()==0){
                    jsonMess.put("role","assistant");
                    jsonMess.put("content",mess.getMess());
                } else if (mess.getRole()==1) {
                    jsonMess.put("role","user");
                    jsonMess.put("content",mess.getMess());
                }
                json.getJSONArray("messages").add(jsonMess);
            }
        }
        json.put("token",token);
        HttpURLConnection connection;
        OutputStream os;
        InputStream is;
        URL url = new URL(Set.ip+"Ai_Messages");
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setConnectTimeout(8000);
        connection.setReadTimeout(100000);
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
        os = connection.getOutputStream();
        os.write(json.toString().getBytes(StandardCharsets.UTF_8));
        os.flush();
        if (connection.getResponseCode() == 200) {
            is = connection.getInputStream();
            if (null != is) {
                return new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            }
        }
        return null;
    }

    public static BufferedReader post(String mess,String token) throws IOException {
        JSONObject json = new JSONObject();
        json.put("mess",mess);
        json.put("token",token);
        HttpURLConnection connection;
        OutputStream os;
        InputStream is;
        URL url = new URL(Set.ip+"Ai_talk");
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setConnectTimeout(8000);
        connection.setReadTimeout(30000);
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
        os = connection.getOutputStream();
        os.write(json.toString().getBytes(StandardCharsets.UTF_8));
        os.flush();
        if (connection.getResponseCode() == 200) {
            is = connection.getInputStream();
            if (null != is) {
                return new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            }
        }
        return null;
    }

    public static ArrayList<Mess> addText(ArrayList<Mess> old,String mess,int role){
        ArrayList<Mess> list = new ArrayList<>();
        list.addAll(old);
        list.add(new Mess(role,mess,0));
        return list;
    }

    public static ArrayList<Mess> addWord(ArrayList<Mess> old,String word){
        ArrayList<Mess> list = new ArrayList<>();
        list.addAll(old);

        list.remove(list.size()-1);
        Mess mess = new Mess(0,old.get(old.size()-1).getMess()+word,0);
        list.add(mess);

        return list;
    }
}
