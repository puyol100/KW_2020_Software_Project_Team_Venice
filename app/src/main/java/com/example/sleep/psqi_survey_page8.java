    package com.example.sleep;

    import androidx.appcompat.app.AppCompatActivity;

    import android.os.Bundle;
    import android.view.View;
    import android.widget.Button;
    import android.content.Intent;
    import android.widget.CheckBox;

    import static com.example.sleep.psqi_managing.setq10_a_b_c;


    public class psqi_survey_page8 extends AppCompatActivity {
        CheckBox a_cb1, b_cb1, c_cb1;
        CheckBox a_cb2, b_cb2, c_cb2;
        CheckBox a_cb3, b_cb3, c_cb3;
        CheckBox a_cb4, b_cb4, c_cb4;

        int a_result = 0, b_result = 0, c_result = 0;

        Button btn_next;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_psqi_survey_page8);

            a_cb1 = (CheckBox)findViewById(R.id.a_checkBox1);
            a_cb2 = (CheckBox)findViewById(R.id.a_checkBox2);
            a_cb3 = (CheckBox)findViewById(R.id.a_checkBox3);
            a_cb4 = (CheckBox)findViewById(R.id.a_checkBox4);


            b_cb1 = (CheckBox)findViewById(R.id.b_checkBox1);
            b_cb2 = (CheckBox)findViewById(R.id.b_checkBox2);
            b_cb3 = (CheckBox)findViewById(R.id.b_checkBox3);
            b_cb4 = (CheckBox)findViewById(R.id.b_checkBox4);

            c_cb1 = (CheckBox)findViewById(R.id.c_checkBox1);
            c_cb2 = (CheckBox)findViewById(R.id.c_checkBox2);
            c_cb3 = (CheckBox)findViewById(R.id.c_checkBox3);
            c_cb4 = (CheckBox)findViewById(R.id.c_checkBox4);


            btn_next = (Button)findViewById(R.id.psqi_next);
            btn_next.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v){
                    setq10_a_b_c(a_result,b_result,c_result);
                    Intent intent = new Intent(getApplicationContext(),psqi_survey_page9.class);
                    startActivity(intent);
                }
            });
        }
        public void onChecked_10_a(View v){
            boolean checked = ((CheckBox) v).isChecked();
            switch (v.getId()){
                case R.id.a_checkBox1:
                    if(checked){
                        a_result = 1;
                        a_cb2.setChecked(false);
                        a_cb3.setChecked(false);
                        a_cb4.setChecked(false);
                    }
                    break;
                case R.id.a_checkBox2:
                    if(checked){
                        a_result = 2;
                        a_cb1.setChecked(false);
                        a_cb3.setChecked(false);
                        a_cb4.setChecked(false);
                    }
                    break;
                case R.id.a_checkBox3:
                    if(checked){
                        a_result = 3;
                        a_cb1.setChecked(false);
                        a_cb2.setChecked(false);
                        a_cb4.setChecked(false);
                    }
                    break;
                case R.id.a_checkBox4:
                    if(checked){
                        a_result = 4;
                        a_cb1.setChecked(false);
                        a_cb2.setChecked(false);
                        a_cb3.setChecked(false);
                    }
                    break;
            }

        }
        public void onChecked_10_b(View v){
            boolean checked = ((CheckBox) v).isChecked();
            switch (v.getId()){
                case R.id.b_checkBox1:
                    if(checked){
                        b_result = 1;
                        b_cb2.setChecked(false);
                        b_cb3.setChecked(false);
                        b_cb4.setChecked(false);
                    }
                    break;
                case R.id.b_checkBox2:
                    if(checked){
                        b_result = 2;
                        b_cb1.setChecked(false);
                        b_cb3.setChecked(false);
                        b_cb4.setChecked(false);
                    }
                    break;
                case R.id.b_checkBox3:
                    if(checked){
                        b_result = 3;
                        b_cb1.setChecked(false);
                        b_cb2.setChecked(false);
                        b_cb4.setChecked(false);
                    }
                    break;
                case R.id.b_checkBox4:
                    if(checked){
                        b_result = 4;
                        b_cb1.setChecked(false);
                        b_cb2.setChecked(false);
                        b_cb3.setChecked(false);
                    }
                    break;
            }

        }
        public void onChecked_10_c(View v){
            boolean checked = ((CheckBox) v).isChecked();
            switch (v.getId()){
                case R.id.c_checkBox1:
                    if(checked){
                        c_result = 1;
                        c_cb2.setChecked(false);
                        c_cb3.setChecked(false);
                        c_cb4.setChecked(false);
                    }
                    break;
                case R.id.c_checkBox2:
                    if(checked){
                        c_result = 2;
                        c_cb1.setChecked(false);
                        c_cb3.setChecked(false);
                        c_cb4.setChecked(false);
                    }
                    break;
                case R.id.c_checkBox3:
                    if(checked){
                        c_result = 3;
                        c_cb1.setChecked(false);
                        c_cb2.setChecked(false);
                        c_cb4.setChecked(false);
                    }
                    break;
                case R.id.c_checkBox4:
                    if(checked){
                        c_result = 4;
                        c_cb1.setChecked(false);
                        c_cb2.setChecked(false);
                        c_cb3.setChecked(false);
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