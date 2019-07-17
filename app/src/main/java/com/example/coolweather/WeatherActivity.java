package com.example.coolweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.coolweather.gson.DailyForecast;
import com.example.coolweather.gson.HeWeather;
import com.example.coolweather.service.AutoUpdateService;
import com.example.coolweather.util.HttpUtil;
import com.example.coolweather.util.Utility;

import org.jetbrains.annotations.NotNull;

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

    public ImageView bing_pic;

    public SwipeRefreshLayout swipeRefresh;

    private String mweatherId;

    public DrawerLayout drawerLayout;

    public Button nav_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Build.VERSION.SDK_INT>=21){
            View decorView=getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

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
        swipeRefresh=findViewById(R.id.swipeRefresh);
        drawerLayout=findViewById(R.id.drawer_layout);
        nav_button=findViewById(R.id.nav_button);

        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this);
        String weatherString=prefs.getString("weather",null);
        if(weatherString!=null){
            //有缓存直接读取天气
            HeWeather weather= Utility.handleWeatherResponseByGSON(weatherString);
            showWeatherInfo(weather);
            mweatherId=weather.getBasic().getCid();
        }else {
            String weatherId=getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            mweatherId=weatherId;
            requestWeather(weatherId);
        }

        bing_pic=findViewById(R.id.bing_pic_img);
        String picString=prefs.getString("bing_pic",null);
        if(picString!=null){
            //显示已缓存的图片
            Glide.with(this).load(picString).into(bing_pic);
        }else{
            LoadBingPic();
        }

        /**启动服务**/
        Intent intent=new Intent(this, AutoUpdateService.class);
        startService(intent);

        /**下拉刷新**/
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(mweatherId);

                //LoadBingPic();
            }
        });

        /**侧边栏，选中区域**/
        nav_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

    }

    /**
     * 根据天气id请求天气数据
     */
    public void requestWeather(final String weatherId){
        String address="http://guolin.tech/api/weather?cityid="+weatherId
                +"&key=bc0418b57b2d4918819d3974ac1285d9";
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"加载天气失败",Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);//隐藏进度条
                    }
                });

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                final String responseText=response.body().string(); //解析出来的天气数据

                final HeWeather weather=Utility.handleWeatherResponseByGSON(responseText); //获取weather实体类

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(weather!=null && "ok".equals(weather.getStatus())){

                            /**缓存天气数据**/
                            SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather",responseText);
                            editor.apply();
                            mweatherId=weather.getBasic().getCid();//天气更新，mweatherId也要更新
                            showWeatherInfo(weather);

                        }else{

                            Toast.makeText(WeatherActivity.this,"加载天气失败",Toast.LENGTH_SHORT).show();

                        }
                        swipeRefresh.setRefreshing(false);
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
            dateText.setText(new SimpleDateFormat("yyyy-MM-dd").format(forecast.getDate()));
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

    /**
     * 加载bing图片
     */
    public void LoadBingPic(){
        String address="http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"图片加载失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull final Response response) throws IOException {

                final String pic_string=response.body().string();

                SharedPreferences.Editor editor=PreferenceManager
                        .getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic",pic_string);
                editor.apply();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Glide.with(WeatherActivity.this).load(pic_string).into(bing_pic);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }
}

