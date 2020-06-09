package com.example.sleep;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.PowerManager;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = AlarmReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(context, alarm.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingI = PendingIntent.getActivity(
                context,
                0,
                notificationIntent,
                0
        );

        // ChannelID가 null인 경우
        // Builer에서 설정한 진동 패턴이나 음원이 notification에 저장 가능
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "");

        //OREO API 26 이상에서는 채널 필요
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            //mipmap 사용시 Oreo 이상에서 시스템 UI 에러남
            builder.setSmallIcon(R.drawable.ic_launcher_foreground);

            //소리와 알림메시지를 같이 보여줌
            String channelName ="알람 채널";
            String description = "정해진 시간에 알람합니다.";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(
                    "",
                    channelName,
                    importance
            );
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            //channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000});
            channel.setDescription(description);

            // Notification Channel을 시스템에 등록
            notificationManager.createNotificationChannel(channel);
        }else {
            // Oreo 이하에서 mipmap 사용하지 않는 경우
            // Couldn't create icon: StatusBarIcon 에러 발생
            builder.setSmallIcon(R.mipmap.ic_launcher);
        }

        String date_text = new SimpleDateFormat(
                "알람 시간 : MM월 dd일 EE요일 a hh시 mm분",
                Locale.getDefault()
        ).format(Calendar.getInstance().getTime());

        builder.setAutoCancel(true);
        builder.setVibrate(new long[]{1000, 1000});
        builder.setWhen(System.currentTimeMillis());
        builder.setTicker("{Time to watch some cool stuff!}");
        builder.setContentTitle("알람 메시지");
        builder.setContentText(date_text);
        builder.setContentInfo("INFO");
        builder.setContentIntent(pendingI);
        builder.setDefaults(NotificationCompat.DEFAULT_SOUND);

        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_INSISTENT;

        if (notificationManager != null) {
            // 안드로이드폰이 꺼진 상태에서 발생한 알람 메시지를 화면에 보여주기 위해 화면을 켜는 방법
            PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wakeLock = powerManager.newWakeLock(
                    PowerManager.PARTIAL_WAKE_LOCK  |
                            PowerManager.ACQUIRE_CAUSES_WAKEUP |
                            PowerManager.ON_AFTER_RELEASE, "My:Tag");
            wakeLock.acquire(5000);

            // Notification Activate
            notificationManager.notify(111, notification);

            Calendar nextNotifyTime = Calendar.getInstance();

            SharedPreferences.Editor editor = context.getSharedPreferences("daily alarm", Context.MODE_PRIVATE).edit();
            editor.putLong("nextNotifyTime", nextNotifyTime.getTimeInMillis());
            editor.apply();
        }
    }
}