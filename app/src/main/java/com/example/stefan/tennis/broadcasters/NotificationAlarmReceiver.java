package com.example.stefan.tennis.broadcasters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.stefan.tennis.utils.ProjectUtils;
import com.example.stefan.tennis.utils.Setari;
import com.example.stefan.tennis.workers.NotificationService;

import java.util.Calendar;

public class NotificationAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, Setari.getInstance(context).getOra());
        c.set(Calendar.MINUTE, Setari.getInstance(context).getMinut());
        c.set(Calendar.SECOND, 0);
        c.add(Calendar.DATE, 1);
        ProjectUtils.rescheduele(context, c);

        Intent service1 = new Intent(context, NotificationService.class);
        //service1.setData((Uri.parse("custom://" + System.currentTimeMillis())));
        context.startService(service1);
        Log.v("notificare", "Hai odataaaa");
    }
}