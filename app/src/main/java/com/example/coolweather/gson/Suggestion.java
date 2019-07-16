package com.example.coolweather.gson;

public class Suggestion {

    private Comf comf;
    private Sport sport;
    private Cw cw;
    public void setComf(Comf comf) {
        this.comf = comf;
    }
    public Comf getComf() {
        return comf;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }
    public Sport getSport() {
        return sport;
    }

    public void setCw(Cw cw) {
        this.cw = cw;
    }
    public Cw getCw() {
        return cw;
    }
}
