package com.example.sleep;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;

import java.lang.String;

import java.lang.InterruptedException;

import java.net.Socket;

public class waiting extends AppCompatActivity {
    private Socket socket;
    private String ip = "192.168.0.38";            // IP 번호
    private int port = 9998;                          // port 번호
    DataOutputStream dos;
    private Handler mHandler;

    private String mSignalpath = Environment.getExternalStorageDirectory().getAbsolutePath();
    private String mSignalname = "/0ECG2";
    private String mSignalext = ".csv";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);
        Sending();

        Intent intent = new Intent(getApplicationContext(), Subactivity.class);
        startActivity(intent);
    }

    public void Sending(){


        mHandler = new Handler();
        Log.w("connect","연결하는중");

        Thread checkUpdate = new Thread(){
            public void run(){
                //String send_value="P";
                try{
                    socket = new Socket(ip, port);
                    Log.w("server connected ", "server connected");
                }
                catch(IOException e1){
                    e1.printStackTrace();
                    Log.w("server not connected", "server not connected");
                }

                try {
                    dos = new DataOutputStream(socket.getOutputStream());
                    System.out.println("over here");
                    File file = new File(mSignalpath + mSignalname + mSignalext);
                    FileInputStream fin = null;
                    BufferedInputStream bufis = null ;
                    int data = 0 ;


                    if(file.exists() && file.canRead()) {
                        try {
                            fin = new FileInputStream(mSignalpath + mSignalname + mSignalext);
                            bufis = new BufferedInputStream(fin);

                            data = bufis.read();
                            String send = "P";
                            dos.writeUTF(send);
                            String send_value = "";

                            while ((data = bufis.read()) != -1) {
                                // TODO : use data
                                send_value =  ""+data; //////////////
                                dos.writeUTF(send_value);
                            }

                            dos.writeUTF("N");
                            System.out.println(send);
                            bufis.close();
                            fin.close();
                            dos.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    socket.close();
                    Log.w("message sent", "message sent");
                }catch(IOException e){
                    e.printStackTrace();
                    Log.w("wrong buffer","wrong buffer");
                }



            }
        };
        checkUpdate.start();


    }
}
