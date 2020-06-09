package com.example.sleep;

import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import java.io.IOException;
import java.net.Socket;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.View;
import android.content.Intent;

public class Singup extends AppCompatActivity {
    EditText et_id, et_pw, et_birth, et_name, et_age, et_height, et_weight;
    String st_id, st_pw, st_birth, st_name;
    String it_age, it_height, it_weight;

    private Socket socket;

    private Handler mHandler;

    private DataOutputStream dos;
    private DataInputStream dis;

    private String ip = "192.168.0.38";            // IP 번호
    private int port = 9998;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup);

        et_id = (EditText)findViewById(R.id.emailInput);
        et_pw = (EditText)findViewById(R.id.passwordInput);
        et_birth = (EditText)findViewById(R.id.birthInput);
        et_name = (EditText)findViewById(R.id.nameInput);
        et_age = (EditText)findViewById(R.id.ageInput);
        et_height = (EditText)findViewById(R.id.heightInput);
        et_weight = (EditText)findViewById(R.id.weightInput);

        final Button button_register = (Button) findViewById(R.id.registerButton);
        Button button_back = (Button) findViewById(R.id.BackButton);
        button_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        et_id.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event){
                if((event.getAction() == KeyEvent.ACTION_DOWN)&&keyCode == KeyEvent.KEYCODE_ENTER){
                    EditText editText = (EditText) findViewById(R.id.emailInput);
                    et_pw.requestFocus();
                    return true;
                }
                return false;
            }
        });

        et_pw.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event){
                if((event.getAction() == KeyEvent.ACTION_DOWN)&&keyCode == KeyEvent.KEYCODE_ENTER){
                    EditText editText = (EditText) findViewById(R.id.passwordInput);
                    et_name.requestFocus();
                    return true;
                }
                return false;
            }
        });

        et_name.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event){
                if((event.getAction() == KeyEvent.ACTION_DOWN)&&keyCode == KeyEvent.KEYCODE_ENTER){
                    EditText editText = (EditText) findViewById(R.id.nameInput);
                    et_age.requestFocus();
                    return true;
                }
                return false;
            }
        });

        et_age.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event){
                if((event.getAction() == KeyEvent.ACTION_DOWN)&&keyCode == KeyEvent.KEYCODE_ENTER){
                    EditText editText = (EditText) findViewById(R.id.ageInput);
                    et_birth.requestFocus();
                    return true;
                }
                return false;
            }
        });

        et_birth.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event){
                if((event.getAction() == KeyEvent.ACTION_DOWN)&&keyCode == KeyEvent.KEYCODE_ENTER){
                    EditText editText = (EditText) findViewById(R.id.birthInput);
                    et_height.requestFocus();
                    return true;
                }
                return false;
            }
        });

        et_height.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event){
                if((event.getAction() == KeyEvent.ACTION_DOWN)&&keyCode == KeyEvent.KEYCODE_ENTER){
                    EditText editText = (EditText) findViewById(R.id.heightInput);
                    et_weight.requestFocus();
                    return true;
                }
                return false;
            }
        });

        et_weight.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event){
                if((event.getAction() == KeyEvent.ACTION_DOWN)&&keyCode == KeyEvent.KEYCODE_ENTER){

                    button_register.requestFocus();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==2){
            if(resultCode==RESULT_OK){
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        }
    }

    public void button_register(View view){
        st_id = et_id.getText().toString();
        st_pw = et_pw.getText().toString();
        st_name = et_name.getText().toString();
        it_age = et_age.getText().toString();
        st_birth = et_birth.getText().toString();
        it_height = et_height.getText().toString();
        it_weight = et_weight.getText().toString();

        connect(st_id, st_pw, st_name, it_age, st_birth, it_height, it_weight);

    }
    void connect(final String sid, final String spw, final String sname, final String iage,final  String sbirth, final String iheight, final String iweight){
        mHandler = new Handler();
        Log.w("connect","연결하는중");

        Thread checkUpdate = new Thread(){
            public void run(){
                int id_length = sid.length();
                String si_lenght = String.valueOf(id_length);
                String send_value = "R" + si_lenght+"'"+sid+"',"+"'"+sname+"',"+iage+","+"'"+sbirth+"',"+iheight+","+iweight+","+"'"+spw+"'";
                System.out.println(send_value);
                try{
                    socket = new Socket(ip, port);
                    Log.w("server connected ", "server connected");
                }
                catch(IOException e1){
                    e1.printStackTrace();
                    Log.w("server not connected", "server not connected");
                }

                try {
                    dos = new DataOutputStream(socket.getOutputStream());
                    dis = new DataInputStream(socket.getInputStream());
                    dos.writeUTF(send_value);
                    Log.w("message sent", "message sent");
                }catch(IOException e){
                    e.printStackTrace();
                    Log.w("wrong buffer","wrong buffer");
                }

                try{
                    while(true){
                        String line = (String)dis.readUTF();
                        final int val = Integer.parseInt(line);
                        System.out.println(val);
                        if(val >= 1) {
                            Intent intent = new Intent(getApplicationContext(), popupsign.class);
                            startActivity(intent);

                        }
                        else{
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }
                    }
                }catch(Exception e){

                }

            }
        };
        checkUpdate.start();

    }

}