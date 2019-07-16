package com.example.coolweather.gson;
import com.google.gson.annotations.SerializedName;

public class Now {
    private String cloud;
    @SerializedName("cond_code")
    private String condCode;
    @SerializedName("cond_txt")
    private String condTxt;
    private String fl;
    private String hum;
    private String pcpn;
    private String pres;
    private String tmp;
    private String vis;
    @SerializedName("wind_deg")
    private String windDeg;
    @SerializedName("wind_dir")
    private String windDir;
    @SerializedName("wind_sc")
    private String windSc;
    @SerializedName("wind_spd")
    private String windSpd;
    private Cond cond;
    public void setCloud(String cloud) {
        this.cloud = cloud;
    }
    public String getCloud() {
        return cloud;
    }

    public void setCondCode(String condCode) {
        this.condCode = condCode;
    }
    public String getCondCode() {
        return condCode;
    }

    public void setCondTxt(String condTxt) {
        this.condTxt = condTxt;
    }
    public String getCondTxt() {
        return condTxt;
    }

    public void setFl(String fl) {
        this.fl = fl;
    }
    public String getFl() {
        return fl;
    }

    public void setHum(String hum) {
        this.hum = hum;
    }
    public String getHum() {
        return hum;
    }

    public void setPcpn(String pcpn) {
        this.pcpn = pcpn;
    }
    public String getPcpn() {
        return pcpn;
    }

    public void setPres(String pres) {
        this.pres = pres;
    }
    public String getPres() {
        return pres;
    }

    public void setTmp(String tmp) {
        this.tmp = tmp;
    }
    public String getTmp() {
        return tmp;
    }

    public void setVis(String vis) {
        this.vis = vis;
    }
    public String getVis() {
        return vis;
    }

    public void setWindDeg(String windDeg) {
        this.windDeg = windDeg;
    }
    public String getWindDeg() {
        return windDeg;
    }

    public void setWindDir(String windDir) {
        this.windDir = windDir;
    }
    public String getWindDir() {
        return windDir;
    }

    public void setWindSc(String windSc) {
        this.windSc = windSc;
    }
    public String getWindSc() {
        return windSc;
    }

    public void setWindSpd(String windSpd) {
        this.windSpd = windSpd;
    }
    public String getWindSpd() {
        return windSpd;
    }

    public void setCond(Cond cond) {
        this.cond = cond;
    }
    public Cond getCond() {
        return cond;
    }
}
