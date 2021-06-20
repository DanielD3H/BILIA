package com.example.biliagui;

import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

public class TTS_Manager {
    TextToSpeech tts;
    AudioManager audioManager;
    /**
     * TTS_Manager constructor
     */
    public TTS_Manager()
    {
        if(MainActivity.getContext() != null)
        {
            tts = new TextToSpeech(MainActivity.getContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status != TextToSpeech.ERROR) {
                        tts.setLanguage(Locale.US);
                    }
                }
            });
        }
        audioManager =  (AudioManager) MainActivity.getContext().getSystemService(Context.AUDIO_SERVICE);
    }
    /**
     * This function will convert text to speech.
     * @param text- string to say
     * @return if succeed
     */
    public boolean talk(String text){

        int volume = audioManager.getStreamVolume(AudioManager.STREAM_ALARM);
        if(volume==0)
            volume = audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
        audioManager.setStreamVolume(AudioManager.STREAM_ALARM, volume,AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
        tts.setPitch(0.8f);
        tts.setSpeechRate(1.0f);
        if(tts!=null && text != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
            } else {
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            }
            return true;
        }else{
            return false;
        }
    }

    public boolean isSpeaking(){ return audioManager.isMusicActive(); }
}