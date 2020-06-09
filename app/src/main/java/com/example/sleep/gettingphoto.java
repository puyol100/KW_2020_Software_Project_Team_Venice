package com.example.sleep;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.content.Intent;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.IOException;
import java.net.Socket;
import android.os.Handler;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.security.Key;
import java.util.ArrayList;
import java.util.Calendar;

import android.widget.CheckBox;
import android.content.SharedPreferences;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

public class gettingphoto extends AppCompatActivity {
    private static final String TAG = gettingphoto.class.getSimpleName();

    private Handler mHandler;

    private DataOutputStream dos;
    private DataInputStream dis;

    private String ip = "192.168.0.38";            // IP 번호
    private int port = 9998;
    private Socket socket;
    String response = "";
    EditText year, month, day;
    String st_year, st_month, st_day;

    // 날짜
    private int Year;
    private int Month;
    private int Date;

    // Widget ID
    private DatePicker datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gettingphoto);

        datePicker = findViewById(R.id.datePicker);
//        year = (EditText)findViewById(R.id.yearInput);
//        month = (EditText)findViewById(R.id.monthInput);
//        day = (EditText)findViewById(R.id.dayInput);
        final Button pbtn = (Button)findViewById(R.id.photo_button);

        Calendar cal = Calendar.getInstance();
        Year = cal.get(Calendar.YEAR);
        Month = cal.get(Calendar.MONTH);
        Date = cal.get(Calendar.DATE);

        st_year = Integer.toString(Year);
        st_month = Integer.toString(Month+1);
        st_day = Integer.toString(Date);
        Log.w("year", st_year);
        Log.w("month", st_month);
        datePicker.init(Year, Month, Date, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Toast.makeText(gettingphoto.this, "Year: "+year+" / Month: "+(monthOfYear + 1)+" / Day: "+dayOfMonth, Toast.LENGTH_LONG).show();

                st_year = Integer.toString(year);
                st_month = Integer.toString(monthOfYear+1);
                st_day = Integer.toString(dayOfMonth);
            }
        });



    }

    public void photo_button(View view){
        connect(st_year, st_month, st_day);
    }
    void connect(final String syear, final String smonth, final String sday) {
        mHandler = new Handler();
        Log.w("connect", "연결하는중");

        Thread checkUpdate = new Thread() {
            public void run() {
                String send_value = "G"+syear+"-"+smonth+"-"+sday;
                try {
                    socket = new Socket(ip, port);
                    Log.w("server connected ", "server connected");
                } catch (IOException e1) {
                    e1.printStackTrace();
                    Log.w("server not connected", "server not connected");
                }

                try {
                    dos = new DataOutputStream(socket.getOutputStream());
                    dis = new DataInputStream(socket.getInputStream());
                    dos.writeUTF(send_value);
                    Log.w("message sent", "message sent");
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.w("wrong buffer", "wrong buffer");
                }

                try {
                    byte[] buffer = new byte[256];
                    int byteRead=0;
                    int Btye;
                    int count = 0;
                    DataInputStream input = dis;

                    Btye = input.read(buffer);

                    System.out.println(Btye);

                    if(Btye == 3) {
                        Intent intent = new Intent(getApplicationContext(), PopupActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Log.w("message gotten", "message gotten");
                        ByteArrayOutputStream bao = new ByteArrayOutputStream();
                        bao.write(buffer,0,256);
                        response +=bao.toString("utf-8");
                        while ((byteRead = dis.read(buffer))!=-1) {
                            Log.w("message gotten", "message gotten");
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            byteArrayOutputStream.write(buffer, 0, byteRead);
                            //final int val = Integer.parseInt(line);
                            response+=byteArrayOutputStream.toString("utf-8");
                            System.out.println("gotten message: "+response);
                            count += 1;
                        }
                        //System.out.println("response: "+response);
                        LineChart lineChart = (LineChart) findViewById(R.id.chart);
                        //lineChart.setRotation(90);

                        ArrayList<Entry> entries = new ArrayList<>();
                        System.out.println(response.indexOf("\n"));
                        String st_x = response.substring(0,response.indexOf("\n"));
                        String st_y = response.substring(response.indexOf("\n")+1,response.length()-1);

                        Log.w("in make_graph", "in make_graph");
                        String[] arr_x = st_x.split(",");
                        String[] arr_y = st_y.split(",");
                        int[] x_label = new int[arr_x.length];
                        int[] y_label = new int[arr_y.length];
                        //System.out.println(Integer.parseInt(arr_x[0]));
                        for(int i=0 ;i <arr_x.length; i++){
                            x_label[i] = Math.round(Float.parseFloat(arr_x[i]));
                        }
                        Log.w("???", "???");
                        for(int i=0 ; i<arr_y.length; i++){
                            y_label[i] = Math.round(Float.parseFloat(arr_y[i]));
                        }

                        for(int i=0; i<arr_x.length ; i++){
                            entries.add(new Entry(y_label[i],i)); // y_label이 y축으로 나오긴
                        }

                        LineDataSet dataset1 = new LineDataSet(entries, "큰 코골이 구간입니다.");

                        ArrayList<String> labels = new ArrayList<String>(); // x 축입니다.
                        for(int i=0; i<x_label.length; i++){
                            labels.add(String.valueOf(x_label[i]));
                        }
                        Log.w("???", "???");
                        LineData data = new LineData(labels,dataset1);
                        dataset1.setColor(Color.parseColor("BLACK"));   // graph line color
                        dataset1.setCircleColor(Color.parseColor("RED"));   //point color

                        XAxis xAxis = lineChart.getXAxis();
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        xAxis.setTextColor(Color.BLACK);
                        xAxis.enableGridDashedLine(8, 24, 0);

                        YAxis yLAxis = lineChart.getAxisLeft();
                        yLAxis.setTextColor(Color.BLACK);

                        YAxis yRAxis = lineChart.getAxisRight();
                        yRAxis.setDrawLabels(true);
                        yRAxis.setDrawAxisLine(false);
                        yRAxis.setDrawGridLines(false);

                        lineChart.setData(data);
                        lineChart.setDoubleTapToZoomEnabled(true);
                        lineChart.setDragEnabled(true);
                        lineChart.animateY(5000);

                        Log.w("end of chart ", "end of chart");
                    }
                }
                catch (Exception e) {

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