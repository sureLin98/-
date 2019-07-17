package com.example.coolweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import com.example.coolweather.gson.HeWeather;
import com.example.coolweather.util.HttpUtil;
import com.example.coolweather.util.Utility;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AutoUpdateService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        UpdateWeather();
        UpdatePic();
        AlarmManager manager=(AlarmManager) getSystemService(ALARM_SERVICE);
        int anhour=8*60*60*1000;//8小时
        long triggerAttime= SystemClock.elapsedRealtime()+anhour;
        Intent i=new Intent(this,AutoUpdateService.class);
        PendingIntent pendingIntent=PendingIntent.getService(this,0,i,0);
        manager.cancel(pendingIntent);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAttime,pendingIntent);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 更新天气
     */
    public void UpdateWeather(){
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString=prefs.getString("weather",null);
        if(weatherString!=null){
            //有缓存直接解析天气
            HeWeather weather= Utility.handleWeatherResponseByGSON(weatherString);
            String address="http://guolin.tech/api/weather?cityid="+weather.getBasic().getCid()
                    +"&key=bc0418b57b2d4918819d3974ac1285d9";
            HttpUtil.sendOkHttpRequest(address, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String weatherContent=response.body().string();
                    HeWeather weather1=Utility.handleWeatherResponseByGSON(weatherContent);
                    if(weather1!=null && "ok".equals(weather1.getStatus())){
                        SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                        editor.putString("weather",weatherContent);
                        editor.apply();
                    }
                }
            });
        }
    }

    /**
     * 更新bing每日一图
     */
    public void UpdatePic(){
        String address="http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String pic=response.body().string();
                SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                editor.putString("bing_pic",pic);
                editor.apply();
            }
        });
    }
}
