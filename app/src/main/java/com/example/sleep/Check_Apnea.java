package com.example.sleep;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.lang.String;
import java.util.Calendar;

public class Check_Apnea extends AppCompatActivity {

    EditText year, month, day;
    String st_year, st_month, st_day;

    private Socket socket;

    private Handler mHandler;

    private DataOutputStream dos;
    private DataInputStream dis;

    private String ip = "192.168.0.38";            // IP 번호
    private int port = 9998;                          // port 번호
    private int Year;
    private int Month;
    private int Date;

    // Widget ID
    private DatePicker datePicker;


    TextView show_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check__apnea);
        datePicker = findViewById(R.id.datePicker);
        Calendar cal = Calendar.getInstance();
        Year = cal.get(Calendar.YEAR);
        Month = cal.get(Calendar.MONTH);
        Date = cal.get(Calendar.DATE);

        st_year = Integer.toString(Year);
        st_month = Integer.toString(Month+1);
        st_day = Integer.toString(Date);
        show_text = (TextView)findViewById(R.id.show_text);
        datePicker.init(Year, Month, Date, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Toast.makeText(Check_Apnea.this, "Year: "+year+" / Month: "+(monthOfYear + 1)+" / Day: "+dayOfMonth, Toast.LENGTH_LONG).show();

                st_year = Integer.toString(year);
                st_month = Integer.toString(monthOfYear+1);
                st_day = Integer.toString(dayOfMonth);
            }
        });

        final Button sbtn = (Button)findViewById(R.id.submit_button);

    }

    public void submit_button(View view){
        /*st_year = year.getText().toString();
        st_month = month.getText().toString();
        st_day = day.getText().toString();*/

        connect(st_year, st_month, st_day);
    }

    void connect(final String syear, final String smonth, final String sday){
        mHandler = new Handler();
        Log.w("connect","연결 하는중");
        // 받아오는거
        Thread checkUpdate = new Thread() {
            public void run() {

                String send_value = "S"+syear+"-"+smonth+"-"+sday;
                // 서버 접속
                try {
                    socket = new Socket(ip, port);
                    Log.w("서버 접속됨", "서버 접속됨");
                } catch (IOException e1) {
                    Log.w("서버접속못함", "서버접속못함");
                    e1.printStackTrace();
                }

                Log.w("edit 넘어가야 할 값 : ","안드로이드에서 서버로 연결요청");

                try {
                    dos = new DataOutputStream(socket.getOutputStream());   // output에 보낼꺼 넣음
                    dis = new DataInputStream(socket.getInputStream());     // input에 받을꺼 넣어짐
                    dos.writeUTF(send_value);

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.w("버퍼", "버퍼생성 잘못됨");
                }
                Log.w("버퍼","버퍼생성 잘됨");

                // 서버에서 계속 받아옴 - 한번은 문자, 한번은 숫자를 읽음. 순서 맞춰줘야 함.
                try {
                    while(true) {
                        String line = (String)dis.readUTF();

                        System.out.println(""+line.length());
                        int val = line.length();
                        if(val == 1){
                            Intent intent = new Intent(getApplicationContext(), pop4apnea.class);
                            startActivity(intent);
                        }
                        else {
                            if(val == 0)
                                continue;
                            final String Info[] = line.split("\\+");
                            show_text.post(new Runnable() {
                                public void run() {
                                    show_text.setText("수면중 무호흡 횟수는");
                                    show_text.append(Info[0]);
                                    show_text.append("이며 \n");
                                    Info_print(Info[1]);
                                    show_text.append("수면중 가장 큰 소음은");
                                    show_text.append(Info[2]);
                                    show_text.append("dB이며 \n");
                                    sound_print(Info[2]);
                                    show_text.append("평균 코골이의 정도는");
                                    show_text.append(Info[3]);
                                    show_text.append("dB 입니다.\n");

                                }
                            });

                        }

                    }

                }catch (Exception e){
                }
            }
        };
        // 소켓 접속 시도, 버퍼생성
        checkUpdate.start();
    }
    public void Info_print(String val)
    {
        int value = Integer.parseInt(val);
        if (value >= 1) {
            // show_text.setText("무호흡"); //글자출력칸에 서버가 보낸 메시지를 받는다.
            show_text.append("무호흡 환자입니다.\n");
        }
        else{
            show_text.append("정상 입니다.\n");
            //show_text.setText("정상");
        }
    }
    public void sound_print(String val)
    {
        int decibel = Integer.parseInt(val);
        if(decibel < 30)
        {
            show_text.append("나뭇잎 부딫히는 정도의 소리입니다.\n");
        }
        else if(decibel >= 30 && decibel <=35)
        {
            show_text.append("조용한 농촌, 조용한 공원 정도의 소리입니다.\n");
        }
        else if(decibel >35 && decibel < 55)
        {
            show_text.append("조용한 사무실, 주택의 거실 정도의 소리입니다.\n");
        }
        else if(decibel >= 55 && decibel < 70)
        {
            show_text.append("보통의 대화소리, 백화점내 소음 정도의 소리입니다.\n");
        }
        else if(decibel >= 70 && decibel <80)
        {
            show_text.append("전화벨소리, 시끄러운 사무실 소음 정도의 소리입니다.\n");
        }
        else if(decibel >=80 && decibel <90) {
            show_text.append("철로변 및 지하철 소음 정도의 소리입니다.\n");
        }
        else {
            show_text.append("소음이 심한 공장의 소음보다 큰 소리입니다.\n");
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Sleepinfo.class);
        startActivity(intent);
    }
}