package com.example.biliagui;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Locale;


public class SttService extends Service {
    public static SpeechRecognizer speechRecognizer;
    static Intent speechRecognizerIntent = null;

    /**
     * This function will create the sevice.
     * @param
     * @return
     */
    @Override
    public void onCreate() {
        super.onCreate();
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(MainActivity.getContext());
        speechRecognizer.startListening(speechRecognizerIntent);

    }
    /**
     * This function will start the sevice.
     * @param intent, flag, startId
     * @return integer
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {}
            @Override
            public void onBeginningOfSpeech() {}
            @Override
            public void onRmsChanged(float v) {}
            @Override
            public void onBufferReceived(byte[] bytes) {}
            @Override
            public void onEndOfSpeech() {}
            @Override
            public void onError(int i) {
                Log.d("aaaaErrorFunction","error number is "+i); //https://developer.android.com/reference/android/speech/SpeechRecognizer for getting wats wrong
            }
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onResults(Bundle bundle) {

                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                Log.d("aaaaInputIn", data.get(0)); //prints what the user says
                if(!data.get(0).contains("stop listening") && (!data.get(0).contains(MainActivity.tvResponse.getText()) || MainActivity.tvResponse.getText() == "")) {
                    MainActivity.tvRequest.setText(data.get(0));
                    MainActivity.handleMsgs.input.add(data.get(0).toLowerCase());
                }
                //SharedPreferences pref = getApplicationContext().getSharedPreferences(MainActivity.FILENAME, 0); // 0 - for private mode
                //String name = pref.getString("assistantName", null); // getting String
                speechRecognizer.startListening(speechRecognizerIntent);

            }
            @Override
            public void onPartialResults(Bundle partialResults) {}
            @Override
            public void onEvent(int eventType, Bundle params) {}
        });
        return START_STICKY;
    }

    /**
     * This function will destroy the sevice.
     * @param
     * @return
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

