/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * For a given BLE device, this Activity provides the user interface to connect, display data,
 * and display GATT services and characteristics supported by the device.
 * The Activity communicates with {@code BluetoothLeService}, which in turn interacts with the Bluetooth LE API.
 */


package com.example.sleep;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.yalantis.waves.util.Horizon;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.lang.String;

import java.net.Socket;
import java.util.Random;


/*
프로그램 코드 수정3
 */
public class Recording extends AppCompatActivity {
    private static final String TAG = Recording.class.getSimpleName();

    // 접근 권한 Code
    private final int PERMISSIONS_RECORD_AUDIO = 1;

    //Socekt programming
    private Socket socket;
    private String ip = "192.168.0.38";            // IP 번호
    private int port = 9998;                          // port 번호
    DataOutputStream dos;

    // AudioRecord Parameter
    private static final int mAudioSource = MediaRecorder.AudioSource.MIC;
    private static final int mSampleRate = 44100;
    private static final int mChannelCount = AudioFormat.CHANNEL_IN_MONO;
    private static final int mAudioFormat = AudioFormat.ENCODING_PCM_16BIT;
    private static final int RECORDER_ENCODING_BIT = 16;
    private int mBufferSize = 17640;

    private AudioRecord mAudioRecord = null;

    // Record에 필요한 변수
    private Thread mRecordThread = null;
    private boolean isRecording = false;
    private byte[] readData;
    private int f_cnt;

    // PCM Data가 저장될 위치
    private String mFilepath = Environment.getExternalStorageDirectory().getAbsolutePath();
    private String mFilename = "/record";
    private String mFileext = ".pcm";

    // Widget ID
    private Button BtnRecord;
    private TextView ConState;
    private TextView DeviceMAC;
    private TextView PPGData;

    // Device information
    private BluetoothDevice device;
    private String mDeviceName;
    private String mDeviceAddress;

    // Graph
    private final Handler mHandler = new Handler();
    private Runnable mTimer;
    private LineGraphSeries<DataPoint> mSeries;
    private double graphLastXValue = 0;
    private float graphLastYValue = 0;
    private GraphView graph;

    // Audio Viewer(Horizon)
    private Horizon mHorizon;
    private GLSurfaceView glSurfaceView;

    // Bluetooth Service
    private BluetoothLeService bluetoothLeService;

    // PPG Data를 저장할 파일 스트림
    private FileOutputStream outfs;
    private String mSignalpath = Environment.getExternalStorageDirectory().getAbsolutePath();
    private String mSignalname = "/SignalSample";
    private String mSignalext = ".csv";


    /*
    타이머 상수
    44100Hz로 샘플링시 실제로 신호를 샘플링할 때는 Nyquist 이론에 기반해 88200Hz로 샘플링 수행
    17660byte 크기의 버퍼로 5번 가져와야 1초 분량의 소리를 가져올 수 있음
    5번 read로 1초이므로 1분은 300번 read
     */
    // 30s동안 데이터 수집
    public int TimeLimit = 150;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {
                            Manifest.permission.RECORD_AUDIO},
                    PERMISSIONS_RECORD_AUDIO
            );
        }
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MODE_PRIVATE
            );
        }

        // 화면 켜짐 상태 유지
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        BtnRecord = (Button) findViewById(R.id.BtnRecord);
        ConState = (TextView) findViewById(R.id.ConState);
        DeviceMAC = (TextView) findViewById(R.id.DeviceMAC);
        PPGData = (TextView) findViewById(R.id.PPGData);
        graph = (GraphView) findViewById(R.id.graph);
        glSurfaceView = (GLSurfaceView) findViewById(R.id.gl_surface);

        // 연결한 Bluetooth 장치의 BluetoothDevice Object 얻기
        device = getIntent().getExtras().getParcelable("Bluetooth_Device");
        mDeviceName = device.getName();
        mDeviceAddress = device.getAddress();
        DeviceMAC.setText(mDeviceName + "[MAC]: " + mDeviceAddress);



        // PPG Graph
        mSeries = new LineGraphSeries<>();
        graph.addSeries(mSeries);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(70);

        // activate horizontal and vertical zooming and scrolling
        graph.getViewport().setScalable(true);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setScalableY(true);
        graph.getViewport().setScrollableY(true);

        // Audio Graph
        mHorizon = new Horizon(
                glSurfaceView,
                getResources().getColor(R.color.background),
                mSampleRate,
                1,
                RECORDER_ENCODING_BIT
        );

        // Recording Ready
        mAudioRecord = new AudioRecord(
                mAudioSource,
                mSampleRate,
                mChannelCount,
                mAudioFormat,
                mBufferSize
        );
        mAudioRecord.startRecording();

        try {
            outfs = new FileOutputStream(mSignalpath + mSignalname + mSignalext);
            String str = "Time, PPG\n";
            outfs.write(str.getBytes());
            outfs.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Receiver 등록
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if(bluetoothLeService != null) {
            final boolean result = bluetoothLeService.connect(mDeviceAddress);
            Log.d(TAG, "Connect request result=" + result);
        }
        glSurfaceView.onResume();

    }

    @Override
    protected void onPause() {
        mHandler.removeCallbacks(mTimer);
        super.onPause();
        glSurfaceView.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAudioRecord != null) {
            mAudioRecord.release();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        bluetoothLeService = null;

        // 화면 켜짐 상태 해제
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public void onRecord(View view) {
        if(isRecording == true) {
            isRecording = false;
            mHandler.removeCallbacks(mTimer);
            graphLastXValue = 0;
            mSeries.resetData(generateData());
            BtnRecord.setText("측정 시작");
        }else {
            isRecording = true;
            BtnRecord.setText("측정 종료");

            if(mAudioRecord == null) {
                mAudioRecord = new AudioRecord(
                        mAudioSource,
                        mSampleRate,
                        mChannelCount,
                        mAudioFormat,
                        mBufferSize
                );
                mAudioRecord.startRecording();
            }

            // Bluetooth Service
            bluetoothLeService = new BluetoothLeService();
            Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
            bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

            // PPG Data 가져오기
            mTimer = new Runnable() {
                @Override
                public void run() {
                    graphLastXValue += 1d;
                    mSeries.appendData(
                            new DataPoint(graphLastXValue, graphLastYValue),
                            true,
                            70
                    );
                    mHandler.postDelayed(this, 6);
                }
            };
            mHandler.postDelayed(mTimer, 1000);

            // PCM Data를 가져오는 부분에서 UI가 멈춰보이지 않도록 하기 위해 Thread로 분리
            mRecordThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Recording(); // Thread는 자신의 run() 메소드가 모두 실행되면 자동적으로 종료된다.
                }
            });
            mRecordThread.start();
        }
    }

    public void Recording() {

        ///////////////////////////////////////Socket 연결//////////////////////////
        try {
            if (socket == null) {
                socket = new Socket(ip,port);
            }
            dos  = new DataOutputStream(socket.getOutputStream());
            Log.w("서버 접속됨", "서버 접속됨");
        } catch (IOException e1) {
            Log.w("서버접속못함", "서버접속못함");
            e1.printStackTrace();
        }
        /////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////Recording///////////////////////////
        int cnt = 0;
        f_cnt = 1;
        readData = new byte[mBufferSize];
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(mFilepath + mFilename + f_cnt + mFileext);
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while(isRecording) {
            if(cnt != TimeLimit) {
                // AudioRecord의 read 함수를 통해서 PCM Data를 읽음
                int ret = mAudioRecord.read(readData, 0, mBufferSize);
                Log.d(TAG, "read bytes is " + ret);

                try {
                    // 읽은 PCM Data를 파일로 저장
                    fos.write(readData, 0, mBufferSize);
                    dos.write(readData);
                }catch (IOException e) {
                    e.printStackTrace();
                }
                mHorizon.updateView(readData);
                cnt++;
            }
            // 수정한 부분
            // Sample Rate에 따라서 BufferSize를 조절하여 시간의 경과를 알아낼 수 있었다
            // 추가로, 30초 동안 새로운 파일을 생성하여 신호 정보를 저장하는 코드 구현
            else{
                cnt = 0;
                f_cnt++;
                try {
                    fos.close();
                    dos.writeUTF("N");
                    fos = new FileOutputStream(mFilepath + mFilename + f_cnt + mFileext);
                }catch (FileNotFoundException e) {
                    e.printStackTrace();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        mAudioRecord.stop();
        mAudioRecord.release();
        mAudioRecord = null;
        try {
            fos.close();
            dos.close();
            /*
            dos.close();//이 자식하면 서버의 if not newbuf문으로 들어가게
            String send_value = "Search";
            dos = new DataOutputStream(socket.getOutputStream());
            dos.writeUTF(send_value);
            socket.close();
            Intent intent = new Intent(getApplicationContext(), Subactivity.class);
            startActivity(intent);
             */
            socket.close();
            Intent intent = new Intent(getApplicationContext(), Subactivity.class);
            startActivity(intent);

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * UART service connected/disconnected
     * Code to manage Service lifecycle.
     * Service : Lifecycle을 관리하는 코드
     * ServiceConnection : 응용 프로그램 서비스의 상태를 모니터링하기 위한 인터페이스
     */
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            bluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if(!bluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            bluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            bluetoothLeService = null;
        }
    };

    /**
     * Handles various events fired by the Service.
     * ACTION_GATT_CONNECTED: connected to a GATT server.
     * ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
     * ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
     * ACTION_DATA_AVAILABLE: received data from the device.
     * This can be a result of read or notification operations.
     */
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if(BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ConState.setText("Connected");
                    }
                });
            }else if(BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ConState.setText("Disconnected");
                        bluetoothLeService.close();
                    }
                });
            }else if(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                bluetoothLeService.enableTXNotification();
            }else if(BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                // 블루투스로 연결된 장치로부터 받은 데이터
                // Arduino로부터 Serial 통신으로 받은 데이터
                try {
                    outfs = new FileOutputStream(mSignalpath + mSignalname + mSignalext, true);
                    // the difference, measured in milliseconds, between the current time and midnight, January 1, 1970 UTC.
                    // long -> 8Byte
                    // Arduino unsigned long -> 4Byte
                    graphLastYValue = intent.getFloatExtra(BluetoothLeService.EXTRA_DATA, 0);
                    String str = "" + System.currentTimeMillis() + ", " + graphLastYValue + "\n";
                    outfs.write(str.getBytes());
                    outfs.close();
                    PPGData.setText(str);
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private DataPoint[] generateData() {
        int count = 3;
        DataPoint[] values = new DataPoint[count];
        for (int i=0; i<count; i++) {
            DataPoint v = new DataPoint(0, 0);
            values[i] = v;
        }
        return values;
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }
}