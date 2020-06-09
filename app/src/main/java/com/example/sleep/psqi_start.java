package com.example.sleep;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;

public class psqi_start extends AppCompatActivity {

    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_psqi_start);
        btn = (Button)findViewById(R.id.psqi_start);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getApplicationContext(),psqi_survey_page1.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        Intent intent = new Intent(getApplicationContext(), Subactivity.class);
        startActivity(intent);
        return;
    }
}
