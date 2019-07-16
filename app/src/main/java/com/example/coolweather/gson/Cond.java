package com.example.coolweather.gson;
import com.google.gson.annotations.SerializedName;

public class Cond {
    @SerializedName("txt_d")
    private String txtD;
    public void setTxtD(String txtD) {
        this.txtD = txtD;
    }
    public String getTxtD() {
        return txtD;
    }

}
