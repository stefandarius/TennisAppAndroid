package com.example.stefan.tennis.workers;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.stefan.tennis.R;
import com.example.stefan.tennis.activities.MainActivity;
import com.example.stefan.tennis.models.IstoricAntrenamente;
import com.example.stefan.tennis.models.Notificare;

import java.util.Date;

public class NotificationService extends IntentService {

    private static int NOTIFICATION_ID = 1;
    Notification notification;
    private NotificationManager notificationManager;
    private PendingIntent pendingIntent;

    public NotificationService() {
        super("DisplayNotifications");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        displayNotification("Antrenamente neevaluate",
                "Aveti de evaluat " + IstoricAntrenamente.getNumarAntrenamenteWithoutRating() + " antrenamente.");

        Log.i("notif", "Notifications sent.");

    }

    private void displayNotification(String title, String task) {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
        notificationIntent.putExtra("neefectuate", true);

        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("simplifiedcoding", "simplifiedcoding", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(), "simplifiedcoding")
                .setContentTitle(title)
                .setContentText(task)
                .setContentIntent(contentIntent)
                .setSmallIcon(R.mipmap.ic_launcher);
        Notificare n = new Notificare();
        n.setTip(Notificare.Type.EVALUARE_ANTRENAMENT);
        n.setDataPrimire(new Date());
        n.setContinut("Aveti de evaluat " + IstoricAntrenamente.getNumarAntrenamenteWithoutRating() + " antrenamente.");
        n.save();
        notificationManager.notify(1, notification.build());

    }
}
