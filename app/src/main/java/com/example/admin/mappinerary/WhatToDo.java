package com.example.admin.mappinerary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class WhatToDo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_what_to_do);
        Intent myIntent = getIntent();
        String location = myIntent.getStringExtra("location");
        initiate(location);
    }

    private void initiate(String location) {
        WebView myWebView = (WebView) findViewById(R.id.webView);
        myWebView.loadUrl((new TodoData()).toDoList.get(location));
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }
}
