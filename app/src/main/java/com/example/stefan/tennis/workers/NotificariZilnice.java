package com.example.stefan.tennis.workers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.evernote.android.job.DailyJob;
import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;
import com.example.stefan.tennis.R;
import com.example.stefan.tennis.activities.MainActivity;
import com.example.stefan.tennis.core.ApplicationContext;
import com.example.stefan.tennis.models.IstoricAntrenamente;
import com.example.stefan.tennis.models.Notificare;
import com.example.stefan.tennis.utils.Setari;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class NotificariZilnice extends DailyJob {

    public static final String TAG = "NotificariZilnice";

    public static void schedule() {
        Setari setare = Setari.getInstance(ApplicationContext.get());
        Log.v(TAG, "Ora notificare: " + setare.getOra() + ":" + setare.getMinut());
        long timeStart = TimeUnit.HOURS.toMillis(setare.getOra()) + TimeUnit.MINUTES.toMillis(setare.getMinut());
        long timeEnd = TimeUnit.HOURS.toMillis(setare.getOra()) + TimeUnit.MINUTES.toMillis(setare.getMinut() + 1);
        if (setare.getJobId() != 0) {
            Log.v(TAG, "JobId not null " + setare.getJobId());
            JobManager.instance().cancel(setare.getJobId());
            int id = DailyJob.schedule(new JobRequest.Builder(TAG), timeStart, timeEnd);
            Log.v(TAG, "JobId not null " + id + "new");
            setare.setJobId(id);
        }
        if (setare.getJobId() == 0) {
            int id = DailyJob.schedule(new JobRequest.Builder(TAG), timeStart, timeEnd);
            setare.setJobId(id);
            Log.v(TAG, "new jobId " + id);
        }
    }

    @NonNull
    @Override
    protected DailyJobResult onRunDailyJob(@NonNull Params params) {
        Log.v(TAG, "Running job at specific time");
        displayNotification("Antrenamente neevaluate",
                "Aveti de evaluat " + IstoricAntrenamente.getNumarAntrenamenteWithoutRating() + " antrenamente.");

        return DailyJobResult.SUCCESS;
    }

    /*
     * The method is doing nothing but only generating
     * a simple notification
     * If you are confused about it
     * you should check the Android Notification Tutorial
     * */
    private void displayNotification(String title, String task) {
        NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(getContext(), MainActivity.class);
        notificationIntent.putExtra("neefectuate", true);

        PendingIntent contentIntent = PendingIntent.getActivity(getContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("simplifiedcoding", "simplifiedcoding", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notification = new NotificationCompat.Builder(getContext(), "simplifiedcoding")
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
