package com.example.sleep;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import android.widget.EditText;
import android.widget.CheckBox;

import static com.example.sleep.psqi_managing.setq5_g_h_i;


public class psqi_survey_page5 extends AppCompatActivity {
    CheckBox g_cb1, h_cb1, i_cb1;
    CheckBox g_cb2, h_cb2, i_cb2;
    CheckBox g_cb3, h_cb3, i_cb3;
    CheckBox g_cb4, h_cb4, i_cb4;

    int g_result = 0, h_result = 0, i_result = 0;

    Button btn_next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_psqi_survey_page5);

        g_cb1 = (CheckBox)findViewById(R.id.g_checkBox1);
        g_cb2 = (CheckBox)findViewById(R.id.g_checkBox2);
        g_cb3 = (CheckBox)findViewById(R.id.g_checkBox3);
        g_cb4 = (CheckBox)findViewById(R.id.g_checkBox4);


        h_cb1 = (CheckBox)findViewById(R.id.h_checkBox1);
        h_cb2 = (CheckBox)findViewById(R.id.h_checkBox2);
        h_cb3 = (CheckBox)findViewById(R.id.h_checkBox3);
        h_cb4 = (CheckBox)findViewById(R.id.h_checkBox4);

        i_cb1 = (CheckBox)findViewById(R.id.i_checkBox1);
        i_cb2 = (CheckBox)findViewById(R.id.i_checkBox2);
        i_cb3 = (CheckBox)findViewById(R.id.i_checkBox3);
        i_cb4 = (CheckBox)findViewById(R.id.i_checkBox4);


        btn_next = (Button)findViewById(R.id.psqi_next);
        btn_next.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                // onChecked();
                setq5_g_h_i(g_result,h_result,i_result);
                Intent intent = new Intent(getApplicationContext(),psqi_survey_page6.class);
                startActivity(intent);
            }
        });
    }

    public void onChecked_g(View v) {

        boolean checked = ((CheckBox) v).isChecked();
        switch (v.getId()){
            case R.id.g_checkBox1:
                if(checked){
                    g_result = 1;
                    g_cb2.setChecked(false);
                    g_cb3.setChecked(false);
                    g_cb4.setChecked(false);
                }
                break;
            case R.id.g_checkBox2:
                if(checked){
                    g_result = 2;
                    g_cb1.setChecked(false);
                    g_cb3.setChecked(false);
                    g_cb4.setChecked(false);
                }
                break;
            case R.id.g_checkBox3:
                if(checked){
                    g_result = 3;
                    g_cb1.setChecked(false);
                    g_cb2.setChecked(false);
                    g_cb4.setChecked(false);
                }
                break;
            case R.id.g_checkBox4:
                if(checked){
                    g_result = 4;
                    g_cb1.setChecked(false);
                    g_cb2.setChecked(false);
                    g_cb3.setChecked(false);
                }
                break;
        }
    }
    public void onChecked_h(View v){
        boolean checked = ((CheckBox) v).isChecked();
        switch (v.getId()){
            case R.id.h_checkBox1:
                if(checked){
                    h_result = 1;
                    h_cb2.setChecked(false);
                    h_cb3.setChecked(false);
                    h_cb4.setChecked(false);
                }
                break;
            case R.id.h_checkBox2:
                if(checked){
                    h_result = 2;
                    h_cb1.setChecked(false);
                    h_cb3.setChecked(false);
                    h_cb4.setChecked(false);
                }
                break;
            case R.id.h_checkBox3:
                if(checked){
                    h_result = 3;
                    h_cb1.setChecked(false);
                    h_cb2.setChecked(false);
                    h_cb4.setChecked(false);
                }
                break;
            case R.id.h_checkBox4:
                if(checked){
                    h_result = 4;
                    h_cb1.setChecked(false);
                    h_cb2.setChecked(false);
                    h_cb3.setChecked(false);
                }
                break;
        }
    }
    public void onChecked_i(View v){
        boolean checked = ((CheckBox) v).isChecked();
        switch (v.getId()){
            case R.id.i_checkBox1:
                if(checked){
                    i_result = 1;
                    i_cb2.setChecked(false);
                    i_cb3.setChecked(false);
                    i_cb4.setChecked(false);
                }
                break;
            case R.id.i_checkBox2:
                if(checked){
                    i_result = 2;
                    i_cb1.setChecked(false);
                    i_cb3.setChecked(false);
                    i_cb4.setChecked(false);
                }
                break;
            case R.id.i_checkBox3:
                if(checked){
                    i_result = 3;
                    i_cb1.setChecked(false);
                    i_cb2.setChecked(false);
                    i_cb4.setChecked(false);
                }
                break;
            case R.id.i_checkBox4:
                if(checked){
                    i_result = 4;
                    i_cb1.setChecked(false);
                    i_cb2.setChecked(false);
                    i_cb3.setChecked(false);
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