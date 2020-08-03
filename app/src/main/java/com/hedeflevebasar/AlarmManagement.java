package com.hedeflevebasar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AlarmManagement extends AppCompatActivity {

    public void setAlarm(Context context, int hour, int minute, int ALARM_ID) {

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Intent myIntent = new Intent(context, DailyReceiver.class);


        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, ALARM_ID, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);


        Log.e("ALARM_MANAGER", "ADDED ALARM " + ALARM_ID +" at "+hour+":"+minute);

    }

    public void cancelAlarm(Context context, int id){

        try {

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent myIntent = new Intent(context, DailyReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context, id, myIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            alarmManager.cancel(pendingIntent);

            Log.e("ALARM_MANAGER", "CANCEL ALARM " + id);
        }
        catch (Exception exp){
            Log.e("ALARM_MANAGER", "ALARM HAD BEEN OFF" + id);
        }
    }

}
