package com.example.coolweather.gson;

public class City {
    private String aqi;
    private String pm25;
    private String qlty;
    public void setAqi(String aqi) {
        this.aqi = aqi;
    }
    public String getAqi() {
        return aqi;
    }

    public void setPm25(String pm25) {
        this.pm25 = pm25;
    }
    public String getPm25() {
        return pm25;
    }

    public void setQlty(String qlty) {
        this.qlty = qlty;
    }
    public String getQlty() {
        return qlty;
    }

}
