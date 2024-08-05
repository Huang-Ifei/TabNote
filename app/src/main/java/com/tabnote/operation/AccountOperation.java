package com.tabnote.operation;

import com.alibaba.fastjson2.JSONObject;
import com.tabnote.service.MesType;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class AccountOperation {

    public static JSONObject post(JSONObject json) throws IOException {
        StringBuffer result = new StringBuffer();
        HttpURLConnection connection;
        OutputStream os;
        InputStream is;
        BufferedReader br;
        URL url = new URL(Set.ip + "account");
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

    public static JSONObject login(String id, String password) {
        JSONObject jsonObject = new JSONObject();
        try {
            JSONObject send = new JSONObject();
            String pwd = Cryptic.getDynamicPublicKeyAndEncrypt(password);
            send.put("mesType",MesType.logIn);
            send.put("id",id);
            send.put("password",pwd);
            jsonObject = post(send);
            return jsonObject;
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            jsonObject.put("response","网络错误");
            return jsonObject;
        } catch (Exception e){
            e.printStackTrace();
            jsonObject.put("response","程序错误");
            return jsonObject;
        }
    }

    public static JSONObject signUp(String name, String id, String password) {
        JSONObject jsonObject = new JSONObject();
        try {
            JSONObject send = new JSONObject();
            send.put("mesType",MesType.signUp);
            send.put("name",name);
            send.put("id",id);
            send.put("password",Cryptic.getDynamicPublicKeyAndEncrypt(password));
            jsonObject = post(send);
            return jsonObject;
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            jsonObject.put("response","网络错误");
            return jsonObject;
        } catch (Exception e){
            e.printStackTrace();
            jsonObject.put("response","程序错误");
            return jsonObject;
        }
    }

    public static String cancelLogin(String token){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("mesType",MesType.cancelLogIn);
        jsonObject.put("token",token);
        String s;
        try {
            s = post(jsonObject).getString("response");
        } catch (IOException e) {
            e.printStackTrace();
            s = "网络错误";
        }catch (Exception e){
            e.printStackTrace();
            s = "错误";
        }
        return s;
    }

    public static JSONObject resetName(String token,String name){
        JSONObject jsonObject = new JSONObject();
        JSONObject json = new JSONObject();

        jsonObject.put("mesType",MesType.resetName);
        jsonObject.put("token",Cryptic.getDynamicPublicKeyAndEncrypt(token));
        jsonObject.put("name",name);
        try {
            json = post(jsonObject);
        } catch (IOException e) {
            e.printStackTrace();
            json.put("response" ,"网络错误");
        } catch (Exception e){
            e.printStackTrace();
            json.put("response" ,"错误");
        }
        return json;
    }

    public static JSONObject resetID(String token,String id){
        JSONObject jsonObject = new JSONObject();
        JSONObject json = new JSONObject();
        jsonObject.put("mesType",MesType.resetID);
        jsonObject.put("token",Cryptic.getDynamicPublicKeyAndEncrypt(token));
        jsonObject.put("id",id);
        try {
            json = post(jsonObject);
        } catch (IOException e) {
            e.printStackTrace();
            json.put("response" ,"网络错误");
        }catch (Exception e){
            e.printStackTrace();
            json.put("response" ,"错误");
        }
        return json;
    }

    public static JSONObject resetPassword(String id,String old_password,String password){
        JSONObject jsonObject = new JSONObject();
        JSONObject json = new JSONObject();
        jsonObject.put("mesType",MesType.resetPassword);
        jsonObject.put("id",id);
        jsonObject.put("old_password",old_password);
        jsonObject.put("password",password);
        try {
            json = post(jsonObject);
        } catch (IOException e) {
            e.printStackTrace();
            json.put("response" ,"网络错误");
        }catch (Exception e){
            e.printStackTrace();
            json.put("response" ,"错误");
        }
        return json;
    }
    public static JSONObject getTokensById(String id,String token){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("mesType",MesType.getTokensById);
        jsonObject.put("id",id);
        jsonObject.put("token",Cryptic.getDynamicPublicKeyAndEncrypt(token));
        JSONObject json = new JSONObject();
        try{
            json = post(jsonObject);
        } catch (IOException e) {
            e.printStackTrace();
            json.put("response" ,"网络错误");
        }catch (Exception e){
            e.printStackTrace();
            json.put("response" ,"错误");
        }
        return json;
    }
}
