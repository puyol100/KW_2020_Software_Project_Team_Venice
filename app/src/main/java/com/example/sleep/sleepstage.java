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

public class sleepstage extends AppCompatActivity {

    String st_year, st_month, st_day;
    private Socket socket;

    private Handler mHandler;

    private DataOutputStream dos;
    private DataInputStream dis;

    private String ip = "192.168.0.38";
    private int port = 9998;

    private int Year;
    private int Month;
    private int Date;
    private int deep=0 , light =0;
    // Widget ID
    private DatePicker datePicker;
    private Button sbtn;

    TextView show_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleepstage);

        datePicker = findViewById(R.id.datePicker);
        sbtn = (Button)findViewById(R.id.submit_button);

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

                Toast.makeText(sleepstage.this, "Year: "+year+" / Month: "+(monthOfYear + 1)+" / Day: "+dayOfMonth, Toast.LENGTH_LONG).show();
                st_year = Integer.toString(year);
                st_month = Integer.toString(monthOfYear+1);
                st_day = Integer.toString(dayOfMonth);
            }
        });

        sbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                connect(st_year, st_month, st_day);
            }
        });
    }

    public void connect(final String s_year, final String s_month, final String s_day){
        mHandler = new Handler();
        Log.w("connect","연결 하는중");

        Thread checkUpdate = new Thread(){
            public void run(){
                String send_value = "A"+s_year+"-"+s_month+"-"+s_day;
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

                try{
                    while(true){

                        String line = (String)dis.readUTF();

                        System.out.println(""+line.length());
                        int val = line.length();

                        if(val == 0){
                            System.out.println(""+"whyyy");
                            continue;
                        }

                        else if(val == 1){
                            Intent intent = new Intent(getApplicationContext(), pop4apnea.class);
                            startActivity(intent);
                        }

                        else {
                            GraphView graph = (GraphView) findViewById(R.id.graph);

                            final String Info[] = line.split("\\+");

                            BarGraphSeries<DataPoint> series = new BarGraphSeries<>();
                            BarGraphSeries<DataPoint> series2 = new BarGraphSeries<>();

                            for (int i = 4; i < Integer.parseInt(Info[(Info.length) - 2]) + 5; i++) {
                                if (Integer.parseInt(Info[i]) == 0)
                                {
                                    deep++;
                                    series.appendData(new DataPoint(i - 4, 90), true, 1000);
                                }
                                else
                                {
                                    light++;
                                    series2.appendData(new DataPoint(i - 4, 90), true, 1000);
                                }
                            }

                            graph.getViewport().setYAxisBoundsManual(true);
                            graph.getViewport().setMinY(0);
                            graph.getViewport().setMaxY(90);

                            graph.getViewport().setXAxisBoundsManual(true);
                            graph.getViewport().setMinX(0);
                            graph.getViewport().setMaxX(Integer.parseInt(Info[(Info.length) - 1]));


                            series.setDataWidth(1.5);

                            graph.addSeries(series);
                        }

                        show_text.post(new Runnable() {
                            public void run() {
                                show_text.setText("총 수면 시간"+ (deep+light)*5 + "분 중 \n");
                                show_text.append("얕은 수면은 "+(light*5)+"분입니다.\n");
                                show_text.append("깊은 수면은 "+ (deep*5)+"분입니다.");

                            }
                        });
                    }


                }catch(IOException e){
                    Log.w("fuckk", "fyckkk");
                }
            }
        };
        checkUpdate.start();
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Sleepinfo.class);
        startActivity(intent);
    }
}
