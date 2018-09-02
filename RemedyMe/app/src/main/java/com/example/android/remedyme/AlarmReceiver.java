package com.example.android.remedyme;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

public class AlarmReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        sendNotification(context);
        setResultCode(Activity.RESULT_OK);

    }

    private void sendNotification(Context context) {
        Log.d("AlarmService", "Preparing to send notification...: " );

        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, "REMEDY_ME")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Alarm")
                .setContentText("Hora de tomar remedio")
                .setAutoCancel(true)
                .setSound(alarmUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

        Log.d("AlarmService", "Notification sent.");
    }
}