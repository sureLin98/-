package com.example.coolweather.gson;
import com.google.gson.annotations.SerializedName;


import java.util.List;

public class Wea {
    @SerializedName("HeWeather")
    private List<HeWeather
                > heweather;
    public void setHeweather(List<HeWeather> heweather) {
        this.heweather = heweather;
    }
    public List<HeWeather> getHeweather() {
        return heweather;
    }
}
