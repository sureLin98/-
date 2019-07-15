package com.example.coolweather;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coolweather.gson.Forecast;
import com.example.coolweather.gson.Weather;
import com.example.coolweather.util.HttpUtil;
import com.example.coolweather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    public ScrollView weatherLayout;

    public TextView titleCity,titleUpdateTime,degreeText,weatherInfoText,
            aqiText,pm25Text,comfortText,carWashText,sportText;

    public LinearLayout forecasstLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        /**初始化控件**/
        weatherLayout=findViewById(R.id.weather_layout);
        titleCity=findViewById(R.id.title_city);
        titleUpdateTime = findViewById(R.id.title_update_time);
        degreeText=findViewById(R.id.degree_text);
        weatherInfoText=findViewById(R.id.weather_info_text);
        aqiText=findViewById(R.id.aqi_text);
        pm25Text=findViewById(R.id.pm25_text);
        comfortText=findViewById(R.id.comfort_text);
        carWashText=findViewById(R.id.car_wash_text);
        sportText=findViewById(R.id.sport_text);
        forecasstLayout=findViewById(R.id.forecast_layout);

        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this);
        String weatherString=prefs.getString("weather",null);
        if(weatherString!=null){
            //有缓存直接读取天气
            Weather weather= Utility.handleWeatherResponse(weatherString);
            showWeatherInfo(weather);
        }else {
            String weatherId=getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(weatherId);
        }
    }

    /**
     * 根据天气id请求天气数据
     */
    public void requestWeather(String weatherId){
        String address="https://free-api.heweather.net/s6/weather/now?location="+weatherId
                +"&key=e70bf939f0fc4181b9d61d48bc0c3101";
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"加载天气失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                final String resposeText=response.body().string(); //解析出来的天气数据

                final Weather weather=Utility.handleWeatherResponse(resposeText); //获取weather实体类

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(weather!=null && "ok".equals(weather.status)){

                            /**缓存天气数据**/
                            SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather",resposeText);
                            editor.apply();

                            showWeatherInfo(weather);

                        }else{

                            Toast.makeText(WeatherActivity.this,"加载天气失败",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });
    }

    /**
     * 展示Weather实体类的天气数据
     */
    public void showWeatherInfo(Weather weather){
        String cityName=weather.basic.cityName;
        String updateTime=weather.basic.update.updateTime.split(" ")[1];
        String degree=weather.now.temperature+"C";
        String weatherInfo=weather.now.more.info;
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        forecasstLayout.removeAllViews();
        for(Forecast forecast : weather.forecastList){
            View view= LayoutInflater.from(this).inflate(R.layout.forecast_item,weatherLayout,false);
            TextView dateText=view.findViewById(R.id.date_text);
            TextView infText=view.findViewById(R.id.info_text);
            TextView maxText=view.findViewById(R.id.max_text);
            TextView minText=view.findViewById(R.id.min_text);
            dateText.setText(forecast.data);
            infText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            forecasstLayout.addView(view);
        }
        if(weather.aqi!=null){
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
        }
        String comfort="舒适度："+weather.suggestion.comfort;
        String car_wash="洗车指数："+weather.suggestion.carWash;
        String sport="运动指数："+weather.suggestion.sport;
        comfortText.setText(comfort);
        carWashText.setText(car_wash);
        sportText.setText(sport);
        weatherLayout.setVisibility(View.VISIBLE);
    }
}
