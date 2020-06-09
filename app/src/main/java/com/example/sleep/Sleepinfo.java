package com.example.sleep;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Sleepinfo extends AppCompatActivity {
    Button btn4apnea;
    Button btn4graph;
    Button btn4stage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleepinfo);

        btn4apnea = (Button)findViewById(R.id.Sleep_apnea_Button);
        btn4graph = (Button)findViewById(R.id.graph_Button);
        btn4stage = (Button)findViewById(R.id.stage_button);

        this.SetListener();

    }

    public void SetListener(){
        btn4apnea.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(),Check_Apnea.class);
                startActivity(intent);
            }
        });

        btn4graph.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(),gettingphoto.class);
                startActivity(intent);
            }
        });

        btn4stage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(),sleepstage.class);  //// -> stage class 필요
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Subactivity.class);
        startActivity(intent);
    }
}
