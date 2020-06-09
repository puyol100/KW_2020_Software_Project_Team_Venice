package com.example.sleep;

import androidx.appcompat.app.AppCompatActivity;

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

public class MainActivity extends AppCompatActivity {

    String st_email, st_pw;
    EditText et_email, et_pw;
    private boolean saveLoginData;
    private SharedPreferences appData;
    private CheckBox checkBox;

    private Socket socket;

    private Handler mHandler;

    private DataOutputStream dos;
    private DataInputStream dis;

    private String ip = "192.168.0.38";            // IP 번호
    private int port = 9998;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_email = (EditText)findViewById(R.id.emailInput);
        et_pw = (EditText)findViewById(R.id.passwordInput);

        /*if(SaveSharedPreference.getUserName(MainActivity.this).length() != 0) { // 로그인이 되어 있을 시 로그인 다음 화면으로 ㄲ
            // call Login Activity
            Intent intent;
            intent = new Intent(MainActivity.this, Subactivity.class);
            startActivity(intent);
            this.finish();
        }*/

        final Button button_login = (Button) findViewById(R.id.loginButton);
        Button button_signup = (Button) findViewById(R.id.signupButton);
        button_signup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), Singup.class);
                startActivity(intent);
            }
        });

        et_email.setOnKeyListener(new View.OnKeyListener(){
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
                if((event.getAction() == KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER){
                    button_login.performClick();
                    return true;
                }
                return false;
            }
        });

    }
    public void button_login(View view){
        st_email = et_email.getText().toString();
        st_pw = et_pw.getText().toString();

        connect(st_email, st_pw);

    }

    private void save(){
        Log.w("save", "save");
        SharedPreferences.Editor editor = appData.edit();
        editor.putBoolean("SAVE_LOGIN_DATA", checkBox.isChecked());
        editor.putString("ID", et_email.getText().toString().trim());
        editor.putString("PWD", et_pw.getText().toString().trim());

        editor.apply();
    }

    private void load(){
        saveLoginData = appData.getBoolean("SAVE_LOGIN_DATA",false);
        st_email = appData.getString("ID","");
        st_pw = appData.getString("PWD", "");
    }

    void connect(final String st_email, final String st_pw){
        mHandler = new Handler();
        Log.w("connect","연결하는중");

        Thread checkUpdate = new Thread(){
            public void run(){
                String send_value="L"+"'"+st_email+"',"+"'"+st_pw+"'";
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
                        if(val == 1) {
                            SaveSharedPreference.setUserName(MainActivity.this, et_email.getText().toString());
                            String name = SaveSharedPreference.getUserName(MainActivity.this);
                            Log.w("user name", ""+name);
                            Intent intent = new Intent(getApplicationContext(), Subactivity.class);
                            startActivity(intent);
                            Log.w("login successful!", "login successful!!");
                            //save();
                        }
                        else{
                            Intent intent = new Intent(getApplicationContext(), popuplogin.class);
                            startActivity(intent);
                            Log.w("login Fail", "login Fail");
                        }
                    }
                }catch(Exception e){

                }

            }
        };
        checkUpdate.start();

        /*
        Button button_login = (Button) findViewById(R.id.loginButton);
        Button button_signup = (Button) findViewById(R.id.signupButton);

        button_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), Subactivity.class);
                startActivity(intent);
            }
        });
        Button button_signup = (Button) findViewById(R.id.signupButton);
        button_signup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), Singup.class);
                startActivity(intent);
            }
        });
        */
    }
    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
}