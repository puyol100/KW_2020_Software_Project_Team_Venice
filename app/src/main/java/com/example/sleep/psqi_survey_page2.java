package com.example.sleep;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import android.widget.EditText;


public class psqi_survey_page2 extends AppCompatActivity {

    EditText et_q3_hour,et_q3_min, et_q4;
    int answer3_hour,answer3_min, answer4;
    Button btn_next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_psqi_survey_page2);

        et_q3_hour = (EditText)findViewById(R.id.psqi_a3_hour);
        et_q3_min = (EditText)findViewById(R.id.psqi_a3_min);
        et_q4 = (EditText)findViewById(R.id.psqi_a4);
        btn_next = (Button)findViewById(R.id.psqi_next);
        btn_next.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                answer3_hour = Integer.parseInt(et_q3_hour.getText().toString());
                answer3_min = Integer.parseInt(et_q3_min.getText().toString());
                answer4 = Integer.parseInt(et_q4.getText().toString());
                psqi_managing.setq3_q4_a(answer3_hour,answer3_min,answer4);
                Intent intent = new Intent(getApplicationContext(),psqi_survey_page3.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
}