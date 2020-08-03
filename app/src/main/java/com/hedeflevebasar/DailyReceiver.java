package com.hedeflevebasar;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import androidx.core.app.NotificationCompat;

public class DailyReceiver extends BroadcastReceiver {

    NotificationManager mNotifyManager;
    NotificationCompat.Builder mBuilder;

    @Override
    public void onReceive(Context context, Intent intent) {

        int id = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String quote ;

            long when = System.currentTimeMillis();
            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);

            Intent notificationIntent = new Intent(context, MainActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, id,
                    notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            // get your quote here
            quote = context.getResources().getString(R.string.NotificationWord);

            NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(
                    context).setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(context.getResources().getString(R.string.NotificationTitle))
                    .setContentText(quote)
                    .setSound(alarmSound)
                    .setAutoCancel(true).setWhen(when)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(quote))
                    .setContentIntent(pendingIntent)
                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});  // Declair VIBRATOR Permission in AndroidManifest.xml
            notificationManager.notify(id, mNotifyBuilder.build());

        }
        else {
            Intent notificationIntent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, id, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            String quote = context.getResources().getString(R.string.NotificationWord);
            long when = System.currentTimeMillis();

            mNotifyManager = (NotificationManager) context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            mBuilder = new NotificationCompat.Builder(context, null);
            mBuilder.setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(context.getResources().getString(R.string.NotificationTitle))
                    .setContentText(quote)
                    .setSound(alarmSound)
                    .setAutoCancel(true).setWhen(when)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(quote))
                    .setContentIntent(pendingIntent)
                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
            mNotifyManager.notify(id, mBuilder.build());
        }
        Log.e("ALARM_MANAGER", "COME all pending intents " + id);
    }
}