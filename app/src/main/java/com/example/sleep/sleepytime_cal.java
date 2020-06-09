package com.example.sleep;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class sleepytime_cal extends AppCompatActivity {

    TextView text_result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleepytime_cal);
        text_result = (TextView)findViewById(R.id.text2);
        text_result.setText(sleepytime_managing.get_time());
    }
}
