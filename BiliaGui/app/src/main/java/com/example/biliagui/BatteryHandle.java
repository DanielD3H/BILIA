package com.example.biliagui;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

public class BatteryHandle {

    /**
     * This function will get the battery % using intent and return it.
     * @param
     * @return returns a float number -> battery %.
     */
    public static float getBatteryLevel()
    {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = MainActivity.getContext().registerReceiver(null, ifilter);
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        float batteryPct = level * 100 / (float)scale;
        return batteryPct;
    }
    /**
     * This function return if this command fits this class.
     * @param command
     * @return return if this command fits this class.
     */
    public static boolean isCammand(String command){
        return command.contains("battery");
    }
}