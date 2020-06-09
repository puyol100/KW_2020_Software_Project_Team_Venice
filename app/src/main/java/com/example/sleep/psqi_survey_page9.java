package com.example.sleep;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import android.widget.EditText;
import android.widget.CheckBox;

import static com.example.sleep.psqi_managing.setq10_d;


public class psqi_survey_page9 extends AppCompatActivity {
    CheckBox d_cb1, d_cb2, d_cb3, d_cb4;

    int d_result = 0;

    Button btn_home;
    Button btn_end;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_psqi_survey_page9);

        d_cb1 = (CheckBox)findViewById(R.id.d_checkBox1);
        d_cb2 = (CheckBox)findViewById(R.id.d_checkBox2);
        d_cb3 = (CheckBox)findViewById(R.id.d_checkBox3);
        d_cb4 = (CheckBox)findViewById(R.id.d_checkBox4);

        btn_home = (Button)findViewById(R.id.psqi_home);
        btn_home.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(),Subactivity.class);
                startActivity(intent);
            }
        });

        btn_end = (Button)findViewById(R.id.psqi_end);
        btn_end.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                setq10_d(d_result);
                Intent intent = new Intent(getApplicationContext(),psqi_main.class);
                startActivity(intent);
            }
        });
    }

    public void onChecked_10_d(View v){
        boolean checked = ((CheckBox) v).isChecked();
        switch (v.getId()){
            case R.id.d_checkBox1:
                if(checked){
                    d_result = 1;
                    d_cb2.setChecked(false);
                    d_cb3.setChecked(false);
                    d_cb4.setChecked(false);
                }
                break;
            case R.id.d_checkBox2:
                if(checked){
                    d_result = 2;
                    d_cb1.setChecked(false);
                    d_cb3.setChecked(false);
                    d_cb4.setChecked(false);
                }
                break;
            case R.id.d_checkBox3:
                if(checked){
                    d_result = 3;
                    d_cb1.setChecked(false);
                    d_cb2.setChecked(false);
                    d_cb4.setChecked(false);
                }
                break;
            case R.id.d_checkBox4:
                if(checked){
                    d_result = 4;
                    d_cb1.setChecked(false);
                    d_cb2.setChecked(false);
                    d_cb3.setChecked(false);
                }
                break;
        }

    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
}