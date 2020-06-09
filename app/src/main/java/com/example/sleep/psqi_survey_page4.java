package com.example.sleep;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import android.widget.EditText;
import android.widget.CheckBox;

import static com.example.sleep.psqi_managing.setq5_d_e_f;


public class psqi_survey_page4 extends AppCompatActivity {
    CheckBox d_cb1, e_cb1, f_cb1;
    CheckBox d_cb2, e_cb2, f_cb2;
    CheckBox d_cb3, e_cb3, f_cb3;
    CheckBox d_cb4, e_cb4, f_cb4;

    int d_result = 0, e_result = 0, f_result = 0;

    Button btn_next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_psqi_survey_page4);

        d_cb1 = (CheckBox)findViewById(R.id.d_checkBox1);
        d_cb2 = (CheckBox)findViewById(R.id.d_checkBox2);
        d_cb3 = (CheckBox)findViewById(R.id.d_checkBox3);
        d_cb4 = (CheckBox)findViewById(R.id.d_checkBox4);


        e_cb1 = (CheckBox)findViewById(R.id.e_checkBox1);
        e_cb2 = (CheckBox)findViewById(R.id.e_checkBox2);
        e_cb3 = (CheckBox)findViewById(R.id.e_checkBox3);
        e_cb4 = (CheckBox)findViewById(R.id.e_checkBox4);

        f_cb1 = (CheckBox)findViewById(R.id.f_checkBox1);
        f_cb2 = (CheckBox)findViewById(R.id.f_checkBox2);
        f_cb3 = (CheckBox)findViewById(R.id.f_checkBox3);
        f_cb4 = (CheckBox)findViewById(R.id.f_checkBox4);


        btn_next = (Button)findViewById(R.id.psqi_next);
        btn_next.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                // onChecked();
                setq5_d_e_f(d_result,e_result,f_result);
                Intent intent = new Intent(getApplicationContext(),psqi_survey_page5.class);
                startActivity(intent);
            }
        });
    }
    public void onChecked_d(View v) {

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
    public void onChecked_e(View v){
        boolean checked = ((CheckBox) v).isChecked();
        switch (v.getId()){
            case R.id.e_checkBox1:
                if(checked){
                    e_result = 1;
                    e_cb2.setChecked(false);
                    e_cb3.setChecked(false);
                    e_cb4.setChecked(false);
                }
                break;
            case R.id.e_checkBox2:
                if(checked){
                    e_result = 2;
                    e_cb1.setChecked(false);
                    e_cb3.setChecked(false);
                    e_cb4.setChecked(false);
                }
                break;
            case R.id.e_checkBox3:
                if(checked){
                    e_result = 3;
                    e_cb1.setChecked(false);
                    e_cb2.setChecked(false);
                    e_cb4.setChecked(false);
                }
                break;
            case R.id.e_checkBox4:
                if(checked){
                    e_result = 4;
                    e_cb1.setChecked(false);
                    e_cb2.setChecked(false);
                    e_cb3.setChecked(false);
                }
                break;
        }
    }
    public void onChecked_f(View v){
        boolean checked = ((CheckBox) v).isChecked();
        switch (v.getId()){
            case R.id.f_checkBox1:
                if(checked){
                    f_result = 1;
                    f_cb2.setChecked(false);
                    f_cb3.setChecked(false);
                    f_cb4.setChecked(false);
                }
                break;
            case R.id.f_checkBox2:
                if(checked){
                    f_result = 2;
                    f_cb1.setChecked(false);
                    f_cb3.setChecked(false);
                    f_cb4.setChecked(false);
                }
                break;
            case R.id.f_checkBox3:
                if(checked){
                    f_result = 3;
                    f_cb1.setChecked(false);
                    f_cb2.setChecked(false);
                    f_cb4.setChecked(false);
                }
                break;
            case R.id.f_checkBox4:
                if(checked){
                    f_result = 4;
                    f_cb1.setChecked(false);
                    f_cb2.setChecked(false);
                    f_cb3.setChecked(false);
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