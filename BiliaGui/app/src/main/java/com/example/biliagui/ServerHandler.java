package com.example.biliagui;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteOrder;

public class ServerHandler extends Thread {
    private Socket socket;
    private BufferedReader in;
    private InputStream input;
    private PrintStream out;
    private int length;

    public ServerHandler() {
        socket = new Socket();
    }
    /**
     * Tries to connect to a server
     * @return True if successfully connected, otherwise False
     */
    public boolean connect(String ip, int port) {
        try {
            socket.connect(new InetSocketAddress(ip, port));
            input = socket.getInputStream();
            in = new BufferedReader(new InputStreamReader(input));
            out = new PrintStream(socket.getOutputStream(), true /* If this is false, you need to call out.flush() for the PrintWriter to send*/);
            return true;
        } catch (IOException e) {
            e.printStackTrace();  //<- can add this to debug
            Log.d("MYTAG", e.toString());
            return false;
        }
    }

    /**
     * Closes the socket
     */
    public void disconnect() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     Sends a string *without* a \n
     */
    public void send(String s) {
        Log.d("MYTAG", "sent: "+s);
        out.print(s);
    }

    /**
     Sends a string with a \n
     */
    public void sendLine(String s) {
        out.println(s);
    }

    /**
     Receives a line. NOTE: This method will lock the program until a **line** (\n) is received.
     */
    public String recvLine() {
        try {
            String s = in.readLine();
            Log.d("MYTAG", "received: "+s);
            return s;
        } catch (IOException e) {
            Log.d("MYTAG", "did not receive "+e.toString());
            return null;
        }
    }

    public byte[] recvPic(){
        try {
            char[] chs = new char[10];
            Log.d("MYTAG", "reading length");
            in.read(chs, 0, 10);
            length = Integer.parseInt(String.copyValueOf(chs));
            Log.d("MYTAG", "length: "+length);
            byte[] buf = new byte[length];
            Log.d("MYTAG", "reading picture");
            int i=0;
            while(buf[length-1] == 0 && (i*1460)+1460 < length) {
                int numRead = input.read(buf, (i*1460), 1460);
                i++;
                Log.d("MYTAG", "received picture buffer, at length: "+numRead);
            }
            return buf;
        } catch (IOException e) {
            Log.d("MYTAG", "did not receive picture: "+e.toString());
            return null;
        }
    }
}