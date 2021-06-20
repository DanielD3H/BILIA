package com.example.biliagui;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.LinkedList;
import java.util.Queue;

public class HandleMsgs implements Runnable {
    private boolean running;
    private ServerHandler sh;
    public Queue<String> input;
    Context context;

    /**
     * HandleMsgs constructor
     */
    public HandleMsgs(Context context){
        sh = new ServerHandler();
        this.running = true;
        input = new LinkedList<String>();
        this.context = context;
    }


    @Override
    /**
     * the function goes through the loop on thread.
     * gets and returns messages to the queue in the class variables
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void run() {
        if (sh.connect("192.168.1.15", 1234))
            Log.d("MYTAG", "connected");
        while (running) {
            if (!input.isEmpty()) {
                String request = input.remove();
                sh.send(request);
                CommandImpl.getCommandDone(request, sh.recvLine());
            }
        }
    }
}