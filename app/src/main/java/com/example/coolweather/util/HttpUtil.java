package com.example.coolweather.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/***
 *从服务器获取数据
 */
public class HttpUtil {

    public static void sendOkHttpRequest(String address,okhttp3.Callback callback){

        OkHttpClient client=new OkHttpClient();   //创建客户端

        Request request=new Request.Builder().url(address).build();  //传入地址创建请求

        client.newCall(request).enqueue(callback);  //发送请求并回调callback

    }

}
