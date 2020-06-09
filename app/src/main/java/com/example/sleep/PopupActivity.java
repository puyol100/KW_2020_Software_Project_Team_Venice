package com.example.sleep;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.content.Intent;
import java.io.IOException;
import java.net.Socket;
import android.os.Handler;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.security.Key;

import android.widget.CheckBox;
import android.content.SharedPreferences;
import android.widget.TextView;

public class PopupActivity extends AppCompatActivity {
    Button btn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);

        btn1 = (Button) findViewById(R.id.okbutton);
        btn1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), gettingphoto.class);
                startActivity(intent);
            }
        });
    }
}