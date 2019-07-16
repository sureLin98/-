package com.example.coolweather.gson;

import java.util.Date;

public class DailyForecast {
    private Date date;
    private Cond cond;
    private Tmp tmp;
    public void setDate(Date date) {
        this.date = date;
    }
    public Date getDate() {
        return date;
    }

    public void setCond(Cond cond) {
        this.cond = cond;
    }
    public Cond getCond() {
        return cond;
    }

    public void setTmp(Tmp tmp) {
        this.tmp = tmp;
    }
    public Tmp getTmp() {
        return tmp;
    }
}
