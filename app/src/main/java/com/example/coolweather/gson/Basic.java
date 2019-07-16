package com.example.coolweather.gson;
import com.google.gson.annotations.SerializedName;

public class Basic {
    private String cid;
    private String location;
    @SerializedName("parent_city")
    private String parentCity;
    @SerializedName("admin_area")
    private String adminArea;
    private String cnty;
    private String lat;
    private String lon;
    private String tz;
    private String city;
    private String id;
    private Update update;
    public void setCid(String cid) {
        this.cid = cid;
    }
    public String getCid() {
        return cid;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    public String getLocation() {
        return location;
    }

    public void setParentCity(String parentCity) {
        this.parentCity = parentCity;
    }
    public String getParentCity() {
        return parentCity;
    }

    public void setAdminArea(String adminArea) {
        this.adminArea = adminArea;
    }
    public String getAdminArea() {
        return adminArea;
    }

    public void setCnty(String cnty) {
        this.cnty = cnty;
    }
    public String getCnty() {
        return cnty;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }
    public String getLat() {
        return lat;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }
    public String getLon() {
        return lon;
    }

    public void setTz(String tz) {
        this.tz = tz;
    }
    public String getTz() {
        return tz;
    }

    public void setCity(String city) {
        this.city = city;
    }
    public String getCity() {
        return city;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }

    public void setUpdate(Update update) {
        this.update = update;
    }
    public Update getUpdate() {
        return update;
    }

}
