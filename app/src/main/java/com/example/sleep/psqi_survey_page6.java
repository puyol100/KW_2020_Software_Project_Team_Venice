package com.example.sleep;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.CheckBox;

import static com.example.sleep.psqi_managing.setq6_q7_q8;


public class psqi_survey_page6 extends AppCompatActivity {
    CheckBox q6_cb1, q7_cb1, q8_cb1;
    CheckBox q6_cb2, q7_cb2, q8_cb2;
    CheckBox q6_cb3, q7_cb3, q8_cb3;
    CheckBox q6_cb4, q7_cb4, q8_cb4;

    int q6_result = 0, q7_result = 0, q8_result = 0;

    Button btn_next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_psqi_survey_page6);

        q6_cb1 = (CheckBox)findViewById(R.id.q6_checkBox1);
        q6_cb2 = (CheckBox)findViewById(R.id.q6_checkBox2);
        q6_cb3 = (CheckBox)findViewById(R.id.q6_checkBox3);
        q6_cb4 = (CheckBox)findViewById(R.id.q6_checkBox4);


        q7_cb1 = (CheckBox)findViewById(R.id.q7_checkBox1);
        q7_cb2 = (CheckBox)findViewById(R.id.q7_checkBox2);
        q7_cb3 = (CheckBox)findViewById(R.id.q7_checkBox3);
        q7_cb4 = (CheckBox)findViewById(R.id.q7_checkBox4);

        q8_cb1 = (CheckBox)findViewById(R.id.q8_checkBox1);
        q8_cb2 = (CheckBox)findViewById(R.id.q8_checkBox2);
        q8_cb3 = (CheckBox)findViewById(R.id.q8_checkBox3);
        q8_cb4 = (CheckBox)findViewById(R.id.q8_checkBox4);


        btn_next = (Button)findViewById(R.id.psqi_next);
        btn_next.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                //onChecked();
                setq6_q7_q8(q6_result,q7_result,q8_result);
                Intent intent = new Intent(getApplicationContext(),psqi_survey_page7.class);
                startActivity(intent);
            }
        });
    }

    public void onChecked_q6(View v) {

        boolean checked = ((CheckBox) v).isChecked();
        switch (v.getId()){
            case R.id.q6_checkBox1:
                if(checked){
                    q6_result = 1;
                    q6_cb2.setChecked(false);
                    q6_cb3.setChecked(false);
                    q6_cb4.setChecked(false);
                }
                break;
            case R.id.q6_checkBox2:
                if(checked){
                    q6_result = 2;
                    q6_cb1.setChecked(false);
                    q6_cb3.setChecked(false);
                    q6_cb4.setChecked(false);
                }
                break;
            case R.id.q6_checkBox3:
                if(checked){
                    q6_result = 3;
                    q6_cb1.setChecked(false);
                    q6_cb2.setChecked(false);
                    q6_cb4.setChecked(false);
                }
                break;
            case R.id.q6_checkBox4:
                if(checked){
                    q6_result = 4;
                    q6_cb1.setChecked(false);
                    q6_cb2.setChecked(false);
                    q6_cb3.setChecked(false);
                }
                break;
        }
    }
    public void onChecked_q7(View v){
        boolean checked = ((CheckBox) v).isChecked();
        switch (v.getId()){
            case R.id.q7_checkBox1:
                if(checked){
                    q7_result = 1;
                    q7_cb2.setChecked(false);
                    q7_cb3.setChecked(false);
                    q7_cb4.setChecked(false);
                }
                break;
            case R.id.q7_checkBox2:
                if(checked){
                    q7_result = 2;
                    q7_cb1.setChecked(false);
                    q7_cb3.setChecked(false);
                    q7_cb4.setChecked(false);
                }
                break;
            case R.id.q7_checkBox3:
                if(checked){
                    q7_result = 3;
                    q7_cb1.setChecked(false);
                    q7_cb2.setChecked(false);
                    q7_cb4.setChecked(false);
                }
                break;
            case R.id.q7_checkBox4:
                if(checked){
                    q7_result = 4;
                    q7_cb1.setChecked(false);
                    q7_cb2.setChecked(false);
                    q7_cb3.setChecked(false);
                }
                break;
        }
    }
    public void onChecked_q8(View v){
        boolean checked = ((CheckBox) v).isChecked();
        switch (v.getId()){
            case R.id.q8_checkBox1:
                if(checked){
                    q8_result = 1;
                    q8_cb2.setChecked(false);
                    q8_cb3.setChecked(false);
                    q8_cb4.setChecked(false);
                }
                break;
            case R.id.q8_checkBox2:
                if(checked){
                    q8_result = 2;
                    q8_cb1.setChecked(false);
                    q8_cb3.setChecked(false);
                    q8_cb4.setChecked(false);
                }
                break;
            case R.id.q8_checkBox3:
                if(checked){
                    q8_result = 3;
                    q8_cb1.setChecked(false);
                    q8_cb2.setChecked(false);
                    q8_cb4.setChecked(false);
                }
                break;
            case R.id.q8_checkBox4:
                if(checked){
                    q8_result = 4;
                    q8_cb1.setChecked(false);
                    q8_cb2.setChecked(false);
                    q8_cb3.setChecked(false);
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