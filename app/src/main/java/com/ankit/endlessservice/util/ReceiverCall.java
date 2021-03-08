package com.ankit.endlessservice.util;
/**
 * Created by Lalit on 27-11-2017.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ankit.endlessservice.services.EndlessService;


public class ReceiverCall extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("Service Stops", "Ohhhhhhh");
        Intent intent1=new Intent(context, EndlessService.class);
        intent1.setAction("startservice");
        context.startService(intent1);


    }



}