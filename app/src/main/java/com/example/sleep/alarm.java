/**
 * Alarm
 *
 * Reference:
 * https://webnautes.tistory.com/m/1365
 */


package com.example.sleep;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class alarm extends AppCompatActivity {
    private static final String TAG = sleepytime.class.getSimpleName();

    private Context context;

    // Alarm
    private PackageManager packageManager;
    private AlarmManager alarmManager;
    private Intent alarmIntent;
    private ComponentName receiver;
    private PendingIntent pendingIntent;
    private boolean AlarmSet = false;

    // 시간 정보를 저장할 변수
    private int Hour = 0;
    private int Min = 0;
    private int AmPm = 0;

    // Widget ID
    private TimePicker TimePicker;
    private Button AlarmBtn;
    private TextView AlarmTxt1;
    private TextView AlarmTxt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        context = this;

        TimePicker = (TimePicker) findViewById(R.id.TimePicker);
        AlarmBtn = (Button) findViewById(R.id.AlarmBtn);
        AlarmTxt1 = (TextView) findViewById(R.id.AlarmTxt1);
        AlarmTxt2 = (TextView) findViewById(R.id.AlarmTxt2);

        SharedPreferences sharedPreferences = getSharedPreferences("daily alarm", MODE_PRIVATE);
        Calendar nextNotifyTime = new GregorianCalendar();
        nextNotifyTime.setTimeInMillis(
                sharedPreferences.getLong(
                        "nextNotifyTime",
                        Calendar.getInstance().getTimeInMillis()
                )
        );

        TimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(android.widget.TimePicker view, int hourOfDay, int minute) {
                Intent intent = new Intent(context, AlarmReceiver.class);
                PendingIntent sender = PendingIntent.getBroadcast(
                        context,
                        0,
                        intent,
                        PendingIntent.FLAG_NO_CREATE
                );
                if(sender != null) {
                    // 이미 설정된 알람이 있는 경우
                    AlarmSet = true;
                    AlarmBtn.setText("알람 해제");
                }else {
                    AlarmSet = false;
                    AlarmBtn.setText("알람 설정");
                }
                Toast.makeText(alarm.this, hourOfDay+":"+minute, Toast.LENGTH_LONG).show();
            }
        });

        AlarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AlarmSet) {
                    AlarmSet = false;
                    AlarmBtn.setText("알람 설정");
                    UnregisterAlarm(context);
                }else {
                    AlarmSet = true;
                    AlarmBtn.setText("알람 해제");
                    RegisterAlarm(context);
                }
            }
        });
    }

    /**
     * 알람을 등록
     * @param context
     */
    private void RegisterAlarm(Context context) {
        // 시간 설정
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Hour = TimePicker.getHour();
            Min = TimePicker.getMinute();
        }else {
            Hour = TimePicker.getCurrentHour();
            Min = TimePicker.getCurrentMinute();
        }

        if(Hour > 12)   AmPm = 1;
        else            AmPm = 0;

        // 현재 지정된 시간으로 알람 시간 설정
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, Hour);
        calendar.set(Calendar.MINUTE, Min);
        calendar.set(Calendar.SECOND, 0);

        // 이미 지난 시간을 지정했다면 다음날 같은 시간으로 설정
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }

        SharedPreferences.Editor editor = getSharedPreferences("daily alarm", MODE_PRIVATE).edit();
        editor.putLong("nextNotifyTime", (long)calendar.getTimeInMillis());
        editor.apply();

        // AlarmReceiver
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmIntent = new Intent(context, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                alarmIntent,
                0
        );

        // 알림 세팅 : AlarmManager 인스턴스에서 set 메소드를 실행시키는 것은 일회성 Alarm을 생성하는 것
        // RTC_WAKEUP : UTC 표준시간을 기준으로 하는 명시적인 시간에 intent를 발생, 장치를 깨움
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (Build.VERSION.SDK_INT >= 19) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            } else {
                // 알람셋팅
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        } else {  // 23 이상
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            // interval : 다음 알람이 울리기까지의 시간
            // setRepeating() lets you specify a precise custom interval--in this case, 20 minutes.
            //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * 20, pendingIntent);
        }

        receiver = new ComponentName(context, DeviceBootReceiver.class);
        packageManager = context.getPackageManager();

        // 부팅 후 실행되는 리시버 사용가능하게 설정
        packageManager.setComponentEnabledSetting(
                receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
        );

        sleepytime_managing.set_time(Hour, Min, AmPm);
        AlarmTxt1.setText("You should try to fall asleep at one of the following times:");
        AlarmTxt2.setText(sleepytime_managing.get_time());
    }

    /**
     * 알람을 해제
     * @param context
     */
    private void UnregisterAlarm(Context context) {
        if (PendingIntent.getBroadcast(context, 0, alarmIntent, 0) != null && alarmManager != null) {
            alarmManager.cancel(pendingIntent);
            Toast.makeText(context,"Notifications were disabled", Toast.LENGTH_LONG).show();
        }
    }
}
