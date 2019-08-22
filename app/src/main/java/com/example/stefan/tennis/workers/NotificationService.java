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
//        Context context = this.getApplicationContext();
////        notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
////        Intent mIntent = new Intent(this, NotificariFragment.class);
////        Bundle bundle = new Bundle();
////        bundle.putString("test", "test");
////        mIntent.putExtras(bundle);
////        pendingIntent = PendingIntent.getActivity(context, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
////
////        Resources res = this.getResources();
////        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
////        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
////        notification = new NotificationCompat.Builder(this)
////                .setContentIntent(pendingIntent)
////                .setSmallIcon(R.drawable.acces_alarm)
////                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.acces_alarm))
////                .setTicker("ticker value")
////                .setAutoCancel(true)
////                .setPriority(8)
////                .setSound(soundUri)
////                .setContentTitle("Notif title")
////                .setContentText("Text").build();
////        notification.flags |= Notification.FLAG_AUTO_CANCEL | Notification.FLAG_SHOW_LIGHTS;
////        notification.defaults |= Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
////        notification.ledARGB = 0xFFFFA500;
////        notification.ledOnMS = 800;
////        notification.ledOffMS = 1000;
////        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
////        notificationManager.notify(NOTIFICATION_ID, notification);

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
