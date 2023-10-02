package com.ankit.endlessservice.util;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.util.Log;

import com.ankit.endlessservice.services.EndlessService;

import java.util.Date;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Start the MyAlarmService when the alarm triggers.
        SharedPreferences sharedPreferences = context.getSharedPreferences("EndlessService", MODE_PRIVATE);
        String service_start_stop = sharedPreferences.getString("service_start_stop", "");
        String TimeStart=sharedPreferences.getString("TimeStart", "");
        long cDateTime = new Date().getTime();
        long lDateTime = sharedPreferences.getLong("ldateTime", 0);
        long diffDateTime = cDateTime - lDateTime;
        Log.e("val 1st", "alarm run in broadcast::"+cDateTime + "::" + lDateTime + "::" + diffDateTime);
        Log.e("line no 16", "alarm run in broadcast: "+TimeStart+"::"+service_start_stop);

        if(service_start_stop.equalsIgnoreCase("start")) {
            scheduleAlarm(context);

            if (diffDateTime > 1000 * 50) {
                Intent serviceIntent = new Intent(context, EndlessService.class);
                context.startService(serviceIntent);
            }
        }

    }

    public static void scheduleAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);

        // Set the time at which the alarm will trigger (every 30 seconds).
        long intervalMillis = 10 * 1000; // 30 seconds
        long alarmTriggerTime = SystemClock.elapsedRealtime() + intervalMillis;

        // Schedule the repeating alarm.
        alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, alarmTriggerTime, pendingIntent);
    }
}