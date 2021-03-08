package com.ankit.endlessservice.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;


import com.ankit.endlessservice.MainActivity;
import com.ankit.endlessservice.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;


import static androidx.core.app.NotificationCompat.PRIORITY_HIGH;
import static androidx.core.app.NotificationCompat.PRIORITY_MAX;


public class EndlessService extends Service {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
    private static final String TAG =  EndlessService.class.getSimpleName();
    private Handler mHandler = new Handler();
    private Timer mTimer = null;
    Intent intent;
    //final long notify_interval = 1000 * 10 * 60;
    final long notify_interval = 1000 * 2;
    final long notify_in = 1000 * 2;
//    final long notify_interval = 1000 * 60*15 ; //for 15 min

    //long notify_interval_loc = 1000*10*60;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    boolean isRunning = false;
    String batteryLevel;
    boolean isStop = true;
    boolean isrunningBackground = false;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        SharedPreferences pref;
        pref = getSharedPreferences("EndlessService", MODE_PRIVATE);

        String isStart = pref.getString("service_start_stop", "");
        Log.e("TS line no 103 ", "on start command:  " + isStart);

        if (isStart.equalsIgnoreCase("stop")) {

            Log.e("TS line no 122 ", "on stop command");
//            new OfflineAttendanceUpload().execute();
            stopSelf();
            stopSelf(startId);

        }

        return Service.START_STICKY;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();

        sharedPreferences = getSharedPreferences("EndlessService", 0);
        editor = sharedPreferences.edit();
        Log.e("TS line no 176 ", "on create command" + new Date().getTime());
//        Intent startIntent = new Intent(TrackingService.this, UploadingService.class);
//        startService(startIntent);
        editor.putLong("ldateTime", new Date().getTime());
        editor.putString("TimeStart", "stop");


        editor.commit();

        isRunning = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            startForeground(12345678, getNotification());
            getNotification();
            Log.e("TS line no 191 ", "on create command" + new Date().getTime());
            if (mTimer == null)
                mTimer = new Timer();
            isStop = true;
            isRunning = true;
//            mTimer.schedule(new TimerTaskToGetLocation(), notify_interval, notify_interval);
            mTimer.schedule(new TimerTaskToGetLocation(), notify_in, notify_in);
        } else {
            if (mTimer == null)
                mTimer = new Timer();
            isRunning = true;
            isStop = true;
            getNotification();
//            mTimer.schedule(new TimerTaskToGetLocation(), notify_interval, notify_interval);
            mTimer.schedule(new TimerTaskToGetLocation(), notify_in, notify_in);
        }

        startRun();


        Log.e("create service", "create Service");


    }

    public void startRun() {


        SharedPreferences sharedPreferences = getSharedPreferences("EndlessService", 0);

        String service_start_stop = sharedPreferences.getString("service_start_stop", "");

        if (service_start_stop.equalsIgnoreCase("start")) {

            long cDateTime = new Date().getTime();
            long lDateTime = sharedPreferences.getLong("ldateTime", 0);

            long diffDateTime = cDateTime - lDateTime;
            Log.e("val 1st", cDateTime + "::" + lDateTime + "::" + diffDateTime);
            if (sharedPreferences.getString("TimeStart", "").equalsIgnoreCase("start")) {
                if (diffDateTime < 1000 * 60) {
                    Log.e("retuen", "retuen val");

                    return;
                }
            }

            editor.putString("TimeStart", "start");
            editor.putLong("ldateTime", new Date().getTime());
            editor.commit();


        }


        Log.e("line no 375", "data inserting befor uploding");


    }


    private void getNotification() {

        SharedPreferences pref;
        pref = getSharedPreferences("EndlessService", MODE_PRIVATE);

        String isStart = pref.getString("service_start_stop", "");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (isStart.equalsIgnoreCase("start")) {


//
//                Log.e("TS line no 238 ","on create command"+new Date().getTime());
                Intent notIntent = new Intent(this, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notIntent, 0);
                Log.e("TS line no 241 ", "on create command" + new Date().getTime());
                Notification notification = new NotificationCompat.Builder(this, "EndlessService")
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setWhen(System.currentTimeMillis())
                        .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background))
                        .setPriority(PRIORITY_HIGH)
                        .setContentText("Endless Service Running")
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setPriority(PRIORITY_MAX)

                        .build();
//                Log.e("TS line no 255 ","on create command"+new Date().getTime());
                startForeground(1, notification);
                Log.e("TS line no 257 ", "on create command" + new Date().getTime());
            }

        } else {
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(this, "EndlessService")
                            .setAutoCancel(true)
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setWhen(System.currentTimeMillis())
                            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background))
                            .setPriority(PRIORITY_HIGH)
                            .setContentText("Endless Service Running")
                            .setSmallIcon(R.drawable.ic_launcher_background)
                            .setPriority(PRIORITY_MAX)
                            .setContentTitle("Endless Service");

            Intent notificationIntent = new Intent(this, MainActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(contentIntent);

            // Add as notification
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(0, builder.build());
        }

//        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "EndlessService").setAutoCancel(false)
//                .setPriority(PRIORITY_MAX);


    }



    private class TimerTaskToGetLocation extends TimerTask {
        @Override
        public void run() {

            mHandler.post(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void run() {
                    Log.i("line no 561", "inside TimerTaskToGetLocation");
                    startRun();


                    //showNotification();
                }
            });

        }
    }


    //
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.e(TAG, "TASK REMOVED");
        sharedPreferences = getSharedPreferences("EndlessService", MODE_PRIVATE);
        String isStart = sharedPreferences.getString("service_start_stop", "");
        Intent restartServiceIntent = new Intent(getApplicationContext(), MainActivity.class);

        if (isStart.equalsIgnoreCase("start")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Log.e("line no 214", "start service background");
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("isOpen", "Service");
                editor.apply();
                restartServiceIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                restartServiceIntent.putExtra("isOpen", "Service");
                startActivity(restartServiceIntent);
//                startForegroundService(restartServiceIntent);

            } else {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("isOpen", "Service");
                editor.apply();
                restartServiceIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                restartServiceIntent.putExtra("isOpen", "Service");
                startActivity(restartServiceIntent);
//                startService(restartServiceIntent);
            }
            Log.e("line no 214", "start service background");
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("isOpen", "Service");
            editor.apply();
            Log.e("TS line no 137", "Service  start");
        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("isOpen", "Self");
            editor.apply();
//            stopService(restartServiceIntent);
            Log.e("line no 141", "Service stop");
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        startRun();

        if (mTimer != null) {
            mTimer.cancel();
        }


        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("isOpen", "Self");
        editor.commit();

        Log.e("Line 652", "service end");

    }


}