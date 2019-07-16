package com.example.coolweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HeWeather {
    private  Basic basic;
    private Update update;
    public String status;
    private Now now;
    @SerializedName("daily_forecast")
    private List<DailyForecast> dailyForecast;
    private Aqi aqi;
    private Suggestion suggestion;
    private String msg;
    public void setBasic(Basic basic) {
        this.basic = basic;
    }
    public Basic getBasic() {
        return basic;
    }

    public void setUpdate(Update update) {
        this.update = update;
    }
    public Update getUpdate() {
        return update;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }

    public void setNow(Now now) {
        this.now = now;
    }
    public Now getNow() {
        return now;
    }

    public void setDailyForecast(List<DailyForecast> dailyForecast) {
        this.dailyForecast = dailyForecast;
    }
    public List<DailyForecast> getDailyForecast() {
        return dailyForecast;
    }

    public void setAqi(Aqi aqi) {
        this.aqi = aqi;
    }
    public Aqi getAqi() {
        return aqi;
    }

    public void setSuggestion(Suggestion suggestion) {
        this.suggestion = suggestion;
    }
    public Suggestion getSuggestion() {
        return suggestion;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    public String getMsg() {
        return msg;
    }

}
