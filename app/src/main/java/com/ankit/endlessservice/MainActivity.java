package com.ankit.endlessservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import com.ankit.endlessservice.services.EndlessService;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final Intent[] POWERMANAGER_INTENTS = {
            new Intent().setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity")),
            new Intent().setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity")),
            new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity")),
            new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.appcontrol.activity.StartupAppControlActivity")),
            new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity")),
            new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.startupapp.StartupAppListActivity")),
            new Intent().setComponent(new ComponentName("com.oppo.safe", "com.oppo.safe.permission.startup.StartupAppListActivity")),
            new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity")),
            new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager")),
            new Intent().setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity")),
            new Intent().setComponent(new ComponentName("com.samsung.android.lool", "com.samsung.android.sm.ui.battery.BatteryActivity")),
            new Intent().setComponent(new ComponentName("com.htc.pitroad", "com.htc.pitroad.landingpage.activity.LandingPageActivity")),
            new Intent().setComponent(new ComponentName("com.asus.mobilemanager", "com.asus.mobilemanager.MainActivity"))
    };
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref = getSharedPreferences("EndlessService", MODE_PRIVATE);
        Intent i = getIntent();
        String is = i.getStringExtra("isOpen");
        if (is != null) {
            if (is.equalsIgnoreCase("Service")) {
                finish();
            }
        }

            Log.e("data","data: "+Build.SERIAL);


        try {
            for (Intent intent : POWERMANAGER_INTENTS)

                if (getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
                    startActivity(intent);
                    break;
                }

            initOPPO();
            oppopermission();
            autoLaunchVivo(getApplicationContext());
        } catch (Exception e) {

        }
    }

    @Override
    protected void onStart() {


        // Battery Restrictions are not allow to run services continousily ,, remove these restrictions  below code//
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent();
            String packageName = getPackageName();
            Log.e("package name: ", packageName + ":");
            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);

            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                startActivity(intent);
            }

//            Log.e("data h",pm.isIgnoringBatteryOptimizations(packageName)+":");
        }

        super.onStart();
    }

    private void initOPPO() {
        try {

            Intent i = new Intent(Intent.ACTION_MAIN);
            i.setComponent(new ComponentName("com.oppo.safe", "com.oppo.safe.permission.floatwindow.FloatWindowListActivity"));
            startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
            try {

                Intent intent = new Intent("action.coloros.safecenter.FloatWindowListActivity");
                intent.setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.floatwindow.FloatWindowListActivity"));
                startActivity(intent);
            } catch (Exception ee) {

                ee.printStackTrace();
                try {

                    Intent i = new Intent("com.coloros.safecenter");
                    i.setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.sysfloatwindow.FloatWindowListActivity"));
                    startActivity(i);
                } catch (Exception e1) {

                    e1.printStackTrace();
                }
            }

        }
    }

    public void oppopermission() {
        if (Build.MANUFACTURER.equalsIgnoreCase("oppo")) {
            try {
                Intent intent = new Intent();
                intent.setClassName("com.coloros.safecenter",
                        "com.coloros.safecenter.permission.startup.StartupAppListActivity");
                startActivity(intent);
            } catch (Exception e) {
                try {
                    Intent intent = new Intent();
                    intent.setClassName("com.oppo.safe",
                            "com.oppo.safe.permission.startup.StartupAppListActivity");
                    startActivity(intent);

                } catch (Exception ex) {
                    try {
                        Intent intent = new Intent();
                        intent.setClassName("com.coloros.safecenter",
                                "com.coloros.safecenter.startupapp.StartupAppListActivity");
                        startActivity(intent);
                    } catch (Exception exx) {

                    }
                }
            }
        }
    }

    private static void autoLaunchVivo(Context context) {
        try {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.iqoo.secure",
                    "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity"));
            context.startActivity(intent);
        } catch (Exception e) {
            try {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.vivo.permissionmanager",
                        "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
                context.startActivity(intent);
            } catch (Exception ex) {
                try {
                    Intent intent = new Intent();
                    intent.setClassName("com.iqoo.secure",
                            "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager");
                    context.startActivity(intent);
                } catch (Exception exx) {
                    ex.printStackTrace();
                }

            }
        }
    }

    public void StartService(View view) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("startLoc", "start");
        editor.putString("TimeStart", "stop");
        editor.putString("service_start_stop", "start");
        editor.putLong("ldateTime", new Date().getTime());
        editor.apply();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.e("line no 214", "start service background");

            Intent startIntent = new Intent(MainActivity.this, EndlessService.class);
            startForegroundService(startIntent);

        } else {
            Log.e("line no 214", "start service normal");
            Intent startIntent = new Intent(MainActivity.this, EndlessService.class);
            startService(startIntent);
        }
    }


    public void StopService(View view) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("startLoc", "stop");
        editor.putString("TimeStart", "stop");
        editor.putString("service_start_stop", "stop");
        editor.putLong("ldateTime", new Date().getTime());
        editor.apply();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.e("line no 214", "start service background");

            Intent startIntent = new Intent(MainActivity.this, EndlessService.class);
            stopService(startIntent);

        } else {
            Log.e("line no 214", "start service normal");
            Intent startIntent = new Intent(MainActivity.this, EndlessService.class);
            stopService(startIntent);
        }
    }
}
