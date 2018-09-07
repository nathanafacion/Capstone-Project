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

import com.example.android.remedyme.utils.Remedy;

import java.sql.Time;
import java.util.Calendar;

public class AlarmReceiver extends WakefulBroadcastReceiver {


    @Override
    public void onReceive(final Context context, Intent intent) {

        Remedy remedy = (Remedy) intent.getExtras().get("remedy");

        Intent mainActivityIntent = new Intent(context, MainActivity.class);
        mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0 /* Request code */, mainActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, "REMEDY_ME");
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        notificationBuilder.setSound(alarmUri).setContentText(context.getResources().getString(R.string.description_remedy));

        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(context.getResources().getString(R.string.alarm_title))
                .setContentText(context.getResources().getString(R.string.alarm_content) + remedy.getRemedy_name())
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int timeInMillis = (int) Calendar.getInstance().getTimeInMillis();

        notificationManager.notify(timeInMillis /* ID of notification */, notificationBuilder.build());

        Log.d("AlarmService", "Notification sent.");

        setResultCode(Activity.RESULT_OK);

    }
}