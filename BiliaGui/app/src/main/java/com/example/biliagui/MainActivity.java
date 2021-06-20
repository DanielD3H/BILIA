package com.example.biliagui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    View decorView;
    public static HandleMsgs handleMsgs;
    Thread thread;
    public static TextView tvResponse, tvRequest;
    public static ImageView ivPic;
    VariableGravityButton btnListen;
    public static TTS_Manager tts;
    public static ArrayList<Integer> songs;
    public static Intent musicIntent;
    boolean pressed;
    public static Context main_activity_context;
    private static final Integer RecordAudioRequestCode = 1;
    public static ProgressBar pbBattery;
    private final int REQUEST_CODE = 400;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener(){
            public void onSystemUiVisibilityChange(int visibility){
                if(visibility == 0)
                    decorView.setSystemUiVisibility(hideSystemBars());
            }
        });

        ivPic = findViewById(R.id.ivPic);
        pbBattery = findViewById(R.id.pbBattery);
        tvResponse = findViewById(R.id.tvResponse);
        tvRequest = findViewById(R.id.tvRequest);
        btnListen = findViewById(R.id.btnListen);
        btnListen.setOnClickListener(this);
        pressed = false;
        handleMsgs = new HandleMsgs(this);
        thread = new Thread(handleMsgs);
        thread.start();

        main_activity_context = this;

        //init songs
        songs = new ArrayList<Integer>();
        songs.add(R.raw.a);
        songs.add(R.raw.b);
        songs.add(R.raw.c);
        songs.add(R.raw.d);
        songs.add(R.raw.e);
        songs.add(R.raw.f);
        songs.add(R.raw.g);
        tts = new TTS_Manager();

        String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
        ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                //show rational to the user
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            }
        }

        //ask for permission if it didn't already
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            checkPermission();
        }
        else if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            checkPermission();
        }
        else if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            checkPermission();
        }
    }

    /**
     * This function will return the context.
     * @param
     * @return context
     */
    public static Context getContext() {
        return main_activity_context;
    }

    public void onWindowFocusChanged(boolean hasFocus){
        //enter - is the window on focus
        //exit - hides the system bars if has focus
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus)
            decorView.setSystemUiVisibility(hideSystemBars());
    }

    private int hideSystemBars() {
        //enter - none
        //exit - returns the consts of the system for hiding the navigation and status bars
        return  View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
    }

    @Override
    public void onClick(View v) {
        pbBattery.setProgress(0);
        ivPic.setImageResource(R.drawable.nothing);
        tvResponse.setText("");
        tvRequest.setText("");

        if(!pressed) {
            pressed = true;
            btnListen.setText("stop listening");
            Intent intent = new Intent(getApplicationContext(), SttService.class);
            startService(intent);
        }
        else{
            pressed = false;
            btnListen.setText("start listening");
            tts.talk("stop listening");
            Intent intent = new Intent(getApplicationContext(), SttService.class);
            stopService(intent);
        }
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE},RecordAudioRequestCode);
        }
    }
    /**
     * This function will ask for permission.
     * @param requestCode, permissions, grantResults
     * @return
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RecordAudioRequestCode && grantResults.length > 0 ){
            for(int i = 0; i < grantResults.length; i++)
            {
                if(grantResults[i] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
            }
        }
        if(requestCode == REQUEST_CODE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //permission granted
            }
            else {
                //permission not granted
            }
        }
    }

}