package com.example.admin.mappinerary;

import android.app.Application;

/**
 * Created by Admin on 11/6/2015.
 */
public class MyApplication extends Application {
    private String[] interestedLocations = new String[] {"Marina Bay Sands", "Merlion Park",
            "The Buddha Tooth Relic Temple", "Items to bring"};

    public String[] getInterestedLocations() {
        return interestedLocations;
    }

    public void setInterestedLocations(String[] locations) {
        interestedLocations = locations;
    }
}
