package com.example.admin.mappinerary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;


public class MainActivity extends AppCompatActivity {
    ListView locationListView;
    public static final String LOCATION = "location";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        updateUI();
    }

    public void updateUI() {
        ArrayAdapter<String> locationAdapter = new ArrayAdapter<String>(this,
                R.layout.location_view,
                R.id.location_button,
                ((MyApplication) getApplication()).getInterestedLocations());
        locationListView = (ListView) findViewById(R.id.location_list);
        locationListView.setAdapter(locationAdapter);
    }

    public void onLocationClick(View v) {
        String location = ((Button)v).getText().toString();
        if (location.equals("Items to bring")) {
            Intent detailIntent = new Intent(this, Detail.class);
            detailIntent.putExtra(LOCATION, location);
            startActivity(detailIntent);
        } else {
            Intent detailIntent = new Intent(this, WhatToDo.class);
            detailIntent.putExtra(LOCATION, location);
            startActivity(detailIntent);
        }
    }
}