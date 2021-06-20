package com.example.biliagui;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

public class WeatherHandle {

    public static void getWeather() {
        String city = "";
        //get city
        double[] location = gpsLoc(MainActivity.getContext());
        try {
            Geocoder geocoder = new Geocoder(MainActivity.getContext(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location[0], location[1], 1);
            city = addresses.get(0).getLocality();
            if(city.contains("-"))
                city = city.split("-")[0];
            city = city.replace(" ", "%20");
            //city = city.split(",")[0];
        }catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("weather", "city: "+city);
        RequestTask request = new RequestTask();
        request.execute("http://api.openweathermap.org/data/2.5/weather?appid=0c42f7f6b53b244c78a418f4f181282a&q=" + city);
    }

    public static void setResponseToActivity(JSONObject response){
        if(response != null) {
            int temp = 0;
            String description = "", city = "", icon = "";
            try {
                temp = (int) (response.getJSONObject("main").getDouble("temp") - 273.15);
                icon = ((JSONObject) response.getJSONArray("weather").get(0)).getString("icon");
                description = ((JSONObject) response.getJSONArray("weather").get(0)).getString("description");
                city = response.getString("name");
            } catch (Exception e) {
                e.printStackTrace();
            }
            MainActivity.tts.talk("The temperature at " + city + " is " + temp + " Celsius, " + description);
            setPicture(icon);
            //Log.d("weather", weather);
            MainActivity.tvResponse.setText("The temperature at " + city + " is " + temp + "°C, " + description);
        }
        else{
            MainActivity.tts.talk("Sorry, something went wrong");
            //Log.d("weather", weather);
            MainActivity.tvResponse.setText("Sorry, something went wrong");
        }
    }

    public static void setPicture(String icon){
        if(icon.contains("01d"))
            MainActivity.ivPic.setImageResource(R.drawable.clear);
        if(icon.contains("01n"))
            MainActivity.ivPic.setImageResource(R.drawable.night);
        if(icon.contains("02"))
            MainActivity.ivPic.setImageResource(R.drawable.fewclouds);
        if(icon.contains("03") || icon.contains("04"))
            MainActivity.ivPic.setImageResource(R.drawable.scattered);
        if(icon.contains("09"))
            MainActivity.ivPic.setImageResource(R.drawable.showerain);
        if(icon.contains("10"))
            MainActivity.ivPic.setImageResource(R.drawable.rain);
        if(icon.contains("11"))
            MainActivity.ivPic.setImageResource(R.drawable.storm);
        if(icon.contains("13"))
            MainActivity.ivPic.setImageResource(R.drawable.snow);
        if(icon.contains("50"))
            MainActivity.ivPic.setImageResource(R.drawable.mist);
    }

    public static double[] gpsLoc(Context context) {
        android.location.LocationManager manager = (android.location.LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (manager != null) {
            for (String provider : manager.getAllProviders()) {
                if (ActivityCompat.checkSelfPermission(MainActivity.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                }
                Location location = manager.getLastKnownLocation(provider);
                if (location != null) {
                    double lat = location.getLatitude();
                    double lon = location.getLongitude();
                    return new double[]{lat, lon};
                }
            }
        }
        return null;
    }
}
