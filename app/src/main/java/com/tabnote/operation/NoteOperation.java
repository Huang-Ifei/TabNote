package com.tabnote.operation;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.tabnote.room.Contact;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class NoteOperation {
    public static JSONObject post(JSONObject json,String route) throws IOException {
        StringBuffer result = new StringBuffer();
        HttpURLConnection connection;
        OutputStream os;
        InputStream is;
        BufferedReader br;
        URL url = new URL(Set.ip+route);
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setConnectTimeout(8000);
        connection.setReadTimeout(15000);
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
        os = connection.getOutputStream();
        os.write(json.toString().getBytes(StandardCharsets.UTF_8));
        os.flush();
        if (connection.getResponseCode() == 200) {
            is = connection.getInputStream();
            if (null != is) {
                br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                String temp;
                while (null != (temp = br.readLine())) {
                    result.append(temp);
                    result.append("\r\n");
                }
                br.close();
            }
        }
        return JSONObject.parseObject(result.toString());
    }

    public static JSONArray notesRequest(List<Contact> list, String id, String token){
        JSONArray jsonArray = null;

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token",token);
        jsonObject.put("id",id);
        jsonObject.putArray("plans");
        for(int i=0;i<list.size();i++){
            JSONObject json = new JSONObject();
            Contact contact = list.get(i);
            json.put("plan_id",contact.getId());
            json.put("content",contact.getContext());
            json.put("date",contact.getDate());
            json.put("link",contact.getLink());
            json.put("done",Boolean.valueOf(contact.getDone()));

            jsonObject.getJSONArray("plans").add(json);
        }
        try {
            JSONObject getJson = post(jsonObject,"synchronous_plans");
            jsonArray = getJson.getJSONArray("return_plan");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    public static void addNote(String plan_id,String content,String link,String date,String usr_id,String token){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("plan_id",plan_id);
        jsonObject.put("id",usr_id);
        jsonObject.put("token",token);
        jsonObject.put("content",content);
        jsonObject.put("link",link);
        jsonObject.put("date",date);
        try {
            post(jsonObject,"add_plan");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void finishNote(Contact contact,String id,String token){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",id);
        jsonObject.put("token",token);
        jsonObject.put("plan_id",contact.getId());
        jsonObject.put("content",contact.getContext());
        jsonObject.put("link",contact.getLink());
        jsonObject.put("date",contact.getDate());
        try {
            post(jsonObject,"finish_plan");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void resetNote(String id,String token,String plan_id,String new_content,String new_link,String new_date){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token",token);
        jsonObject.put("id",id);
        jsonObject.put("plan_id",plan_id);
        jsonObject.put("content",new_content);
        jsonObject.put("link",new_link);
        jsonObject.put("date",new_date);
        try {
            post(jsonObject,"change_plan");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteNote(String id,String token,String plan_id,String content,String link,String date){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",id);
        jsonObject.put("plan_id",plan_id);
        jsonObject.put("token",token);
        jsonObject.put("content",content);
        jsonObject.put("link",link);
        jsonObject.put("date",date);
        try {
            post(jsonObject,"delete_plan");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
