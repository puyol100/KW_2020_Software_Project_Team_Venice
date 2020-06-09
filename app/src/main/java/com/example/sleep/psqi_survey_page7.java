package com.example.sleep;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.CheckBox;

import static com.example.sleep.psqi_managing.setq9_q10;


public class psqi_survey_page7 extends AppCompatActivity {
    CheckBox q9_cb1, q10_cb1;
    CheckBox q9_cb2, q10_cb2;
    CheckBox q9_cb3, q10_cb3;
    CheckBox q9_cb4, q10_cb4;

    int q9_result = 0, q10_result = 0;

    Button btn_next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_psqi_survey_page7);

        q9_cb1 = (CheckBox)findViewById(R.id.q9_checkBox1);
        q9_cb2 = (CheckBox)findViewById(R.id.q9_checkBox2);
        q9_cb3 = (CheckBox)findViewById(R.id.q9_checkBox3);
        q9_cb4 = (CheckBox)findViewById(R.id.q9_checkBox4);


        q10_cb1 = (CheckBox)findViewById(R.id.q10_checkBox1);
        q10_cb2 = (CheckBox)findViewById(R.id.q10_checkBox2);
        q10_cb3 = (CheckBox)findViewById(R.id.q10_checkBox3);
        q10_cb4 = (CheckBox)findViewById(R.id.q10_checkBox4);

        btn_next = (Button)findViewById(R.id.psqi_next);
        btn_next.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                //onChecked();
                setq9_q10(q9_result,q10_result);
                Intent intent = new Intent(getApplicationContext(),psqi_survey_page8.class);
                startActivity(intent);
            }
        });
    }

    public void onChecked_q9(View v) {

        boolean checked = ((CheckBox) v).isChecked();
        switch (v.getId()){
            case R.id.q9_checkBox1:
                if(checked){
                    q9_result = 1;
                    q9_cb2.setChecked(false);
                    q9_cb3.setChecked(false);
                    q9_cb4.setChecked(false);
                }
                break;
            case R.id.q9_checkBox2:
                if(checked){
                    q9_result = 2;
                    q9_cb1.setChecked(false);
                    q9_cb3.setChecked(false);
                    q9_cb4.setChecked(false);
                }
                break;
            case R.id.q9_checkBox3:
                if(checked){
                    q9_result = 3;
                    q9_cb1.setChecked(false);
                    q9_cb2.setChecked(false);
                    q9_cb4.setChecked(false);
                }
                break;
            case R.id.q9_checkBox4:
                if(checked){
                    q9_result = 4;
                    q9_cb1.setChecked(false);
                    q9_cb2.setChecked(false);
                    q9_cb3.setChecked(false);
                }
                break;
        }
    }
    public void onChecked_q10(View v){
        boolean checked = ((CheckBox) v).isChecked();
        switch (v.getId()){
            case R.id.q10_checkBox1:
                if(checked){
                    q10_result = 1;
                    q10_cb2.setChecked(false);
                    q10_cb3.setChecked(false);
                    q10_cb4.setChecked(false);
                }
                break;
            case R.id.q10_checkBox2:
                if(checked){
                    q10_result = 2;
                    q10_cb1.setChecked(false);
                    q10_cb3.setChecked(false);
                    q10_cb4.setChecked(false);
                }
                break;
            case R.id.q10_checkBox3:
                if(checked){
                    q10_result = 3;
                    q10_cb1.setChecked(false);
                    q10_cb2.setChecked(false);
                    q10_cb4.setChecked(false);
                }
                break;
            case R.id.q10_checkBox4:
                if(checked){
                    q10_result = 4;
                    q10_cb1.setChecked(false);
                    q10_cb2.setChecked(false);
                    q10_cb3.setChecked(false);
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