package com.example.admin.mappinerary;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Admin on 11/7/2015.
 */
public class WeatherActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new WeatherFragment())
                    .commit();
        }
    }

}
