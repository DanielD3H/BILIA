package com.example.biliagui;

import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

public class CommandImpl {
    public static final String[] stop = {"close", "stop", "finish", "turn off", "shut up", "down", "cut"};
    public CommandImpl(){

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void getCommandDone(String original, String command) {
        //Log.d("Seq2seq", MainActivity.seqHandle.evaluate(command));


        if (BatteryHandle.isCammand(command)) {
            float battery = BatteryHandle.getBatteryLevel();
            MainActivity.tts.talk("you have " + battery + " percents");
            MainActivity.tvResponse.setText("you have " + battery + "%");
            MainActivity.pbBattery.setProgress((int) battery);
        } else if (CameraHandle.isCammand(command)) {
            CameraHandle.openCamera();
        } else if (FlashlightHandle.isCammand(command)) {
            FlashlightHandle fl = new FlashlightHandle();
            if (checkAction(original) == 1) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    fl.switchFlashLight(true);//turn on
                }
            } else if (checkAction(original) == 2) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    fl.switchFlashLight(false);//turn off
                }
            }
        }else if (command.contains("music") && checkAction(original) == 1) {
            //start sound track in background:
            MainActivity.musicIntent = new Intent(MainActivity.getContext(), MusicService.class);
            MainActivity.getContext().startService(MainActivity.musicIntent);
        } else if (command.contains("music") && checkAction(original) == 2) {
            MainActivity.getContext().stopService(MainActivity.musicIntent);
        } else if(command.contains("weather")){
            WeatherHandle.getWeather();

        }

    }

    public static int checkAction(String command){
        int result = 1;
        for(int i=0; i<stop.length; i++)
            if(command.contains(stop[i]))
                result = 2;
        return result;
    }
}
