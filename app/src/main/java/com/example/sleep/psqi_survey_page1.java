package com.example.sleep;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import android.widget.EditText;


public class psqi_survey_page1 extends AppCompatActivity {
    EditText et_q1_hour,et_q1_min, et_q2;
    int answer1_hour,answer1_min, answer2;
    Button btn_next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_psqi_survey_page1);

        et_q1_hour = (EditText)findViewById(R.id.psqi_a1_hour);
        et_q1_min = (EditText)findViewById(R.id.psqi_a1_min);
        et_q2 = (EditText)findViewById(R.id.psqi_a2);
        btn_next = (Button)findViewById(R.id.psqi_next);
        btn_next.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                answer1_hour = Integer.parseInt(et_q1_hour.getText().toString());
                answer1_min = Integer.parseInt(et_q1_min.getText().toString());
                answer2 = Integer.parseInt(et_q2.getText().toString());
                psqi_managing.setq1_q2_a(answer1_hour,answer1_min,answer2);
                Intent intent = new Intent(getApplicationContext(),psqi_survey_page2.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),Subactivity.class);
        startActivity(intent);
    }
}