/**
 * TimePicker
 *
 * Reference:
 * https://developer88.tistory.com/39
 */


package com.example.sleep;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.view.View;
import android.widget.Button;
import android.widget.ArrayAdapter;
import android.widget.TimePicker;
import android.widget.Toast;


import java.util.ArrayList;


public class sleepytime extends AppCompatActivity {
    private static final String TAG = sleepytime.class.getSimpleName();

    private Spinner spinner_hour;
    private Spinner spinner_min;
    private Spinner spinner_am_pm;

    Button btn1;

    ArrayList<String> arrayList_hour;
    ArrayList<String> arrayList_min;
    ArrayList<String> arrayList_am_pm;

    ArrayAdapter<String> arrayAdapter_hour;
    ArrayAdapter<String> arrayAdapter_min;
    ArrayAdapter<String> arrayAdapter_am_pm;

    int h = 0, m = 0, am_pm =0;
    //sleepytime_managing.init();

    // 시간
    private int Hour;
    private int Min;

    // Widget ID
    private TimePicker TimePicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleepytime);

        TimePicker = (TimePicker) findViewById(R.id.TimePicker);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Hour = TimePicker.getHour();
            Min = TimePicker.getMinute();
        }else {
            Hour = TimePicker.getCurrentHour();
            Min = TimePicker.getMinute();
        }

        TimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(android.widget.TimePicker view, int hourOfDay, int minute) {
                String str = hourOfDay + " : " + minute;
                Toast.makeText(sleepytime.this, str, Toast.LENGTH_LONG).show();
            }
        });

        arrayList_hour = new ArrayList<>();
        arrayList_hour.add("(hour)");
        for(int i= 1; i<=12; i++)
        {
            arrayList_hour.add(""+i);
        }
        arrayList_min = new ArrayList<>();
        arrayList_min.add("(min)");
        for(int i=0;i<=11; i++ )
        {
            int temp = i*5;
            arrayList_min.add(""+temp);
        }
        arrayList_am_pm = new ArrayList<>();
        arrayList_am_pm.add("AM");
        arrayList_am_pm.add("PM");


        spinner_hour = (Spinner)findViewById(R.id.hour);
        spinner_min = (Spinner)findViewById(R.id.min);
        spinner_am_pm = (Spinner)findViewById(R.id.am_pm);


        arrayAdapter_hour =new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,arrayList_hour);
        arrayAdapter_min =new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,arrayList_min);
        arrayAdapter_am_pm =new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,arrayList_am_pm);


        spinner_hour.setAdapter(arrayAdapter_hour);
        spinner_min.setAdapter(arrayAdapter_min);
        spinner_am_pm.setAdapter(arrayAdapter_am_pm);


        btn1 = (Button)findViewById(R.id.calculate_button);

        spinner_hour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
               h = position;
           }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spinner_min.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                m = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        spinner_am_pm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                am_pm = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        btn1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(),sleepytime_cal.class);
                sleepytime_managing.set_time(h,m,am_pm);
                startActivity(intent);

            }
        });

    }
}
