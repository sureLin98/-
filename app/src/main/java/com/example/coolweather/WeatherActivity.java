package com.example.coolweather;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coolweather.gson.DailyForecast;
import com.example.coolweather.gson.HeWeather;
import com.example.coolweather.util.HttpUtil;
import com.example.coolweather.util.Utility;

import java.io.IOException;
import java.text.SimpleDateFormat;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    public ScrollView weatherLayout;

    public TextView titleCity,titleUpdateTime,degreeText,weatherInfoText,
            aqiText,pm25Text,comfortText,carWashText,sportText;

    public LinearLayout forecastLayout;

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
        forecastLayout=findViewById(R.id.forecast_layout);

        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this);
        String weatherString=prefs.getString("weather",null);
        if(weatherString!=null){
            //有缓存直接读取天气
            HeWeather weather= Utility.handleWeatherResponseByGSON(weatherString);
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
        String address="http://guolin.tech/api/weather?cityid="+weatherId
                +"&key=bc0418b57b2d4918819d3974ac1285d9";
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"加载天气失败",Toast.LENGTH_SHORT).show();
                       // Log.d("WeatherActivity", "onFailure() called");
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                final String responseText=response.body().string(); //解析出来的天气数据

                Log.d("WeatherActivity", "responseText="+responseText);

                final HeWeather weather=Utility.handleWeatherResponseByGSON(responseText); //获取weather实体类

                Log.d("WeatherActivity", "weather="+weather);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(weather!=null && "ok".equals(weather.getStatus())){

                            /**缓存天气数据**/
                            SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather",responseText);
                            editor.apply();

                            showWeatherInfo(weather);

                        }else{

                            Toast.makeText(WeatherActivity.this,"加载天气失败",Toast.LENGTH_SHORT).show();
                            Log.d("WeatherActivity", "onSuccess() else called");
                            Log.d("WeatherActivity", "weather="+weather);
                        }
                    }
                });
            }
        });
    }

    /**
     * 展示HeWeather实体类的天气数据
     */
    public void showWeatherInfo(HeWeather weather){
        String cityName=weather.getBasic().getLocation();
        String updateTime=weather.getBasic().getUpdate().getLoc().split(" ")[1];
        String degree=weather.getNow().getTmp()+"℃";
        String weatherInfo=weather.getNow().getCondTxt();
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        forecastLayout.removeAllViews();
        for(DailyForecast forecast : weather.getDailyForecast()){
            View view= LayoutInflater.from(this).inflate(R.layout.forecast_item,weatherLayout,false);
            TextView dateText=view.findViewById(R.id.date_text);
            TextView infText=view.findViewById(R.id.info_text);
            TextView maxText=view.findViewById(R.id.max_text);
            TextView minText=view.findViewById(R.id.min_text);
            dateText.setText(new SimpleDateFormat("MM-dd").format(forecast.getDate()));
            infText.setText(forecast.getCond().getTxtD());
            maxText.setText(forecast.getTmp().getMax()+"℃");
            minText.setText(forecast.getTmp().getMin()+"℃");
            forecastLayout.addView(view);
        }
        if(weather.getAqi()!=null){
            aqiText.setText(weather.getAqi().getCity().getAqi());
            pm25Text.setText(weather.getAqi().getCity().getPm25());
        }
        String comfort="舒适度："+weather.getSuggestion().getComf().getTxt();
        String car_wash="洗车指数："+weather.getSuggestion().getCw().getTxt();
        String sport="运动指数："+weather.getSuggestion().getSport().getTxt();
        comfortText.setText(comfort);
        carWashText.setText(car_wash);
        sportText.setText(sport);
        weatherLayout.setVisibility(View.VISIBLE);
    }
}
