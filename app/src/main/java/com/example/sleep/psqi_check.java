package com.example.sleep;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;
import android.os.Bundle;
import com.example.sleep.psqi_managing;
public class psqi_check extends AppCompatActivity {

    TextView show_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_psqi_check);
        show_result = (TextView)findViewById(R.id.show_result);
        psqi_managing.record();
        int value =  psqi_managing.result;
        show_result.setText(""+value);
    }
}
