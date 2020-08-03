package com.hedeflevebasar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class WakeUpAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

            rebootAlarms(context);
            Log.e("ALARM_MANAGER", "REBOOT ALARMS ADDED ");

        }
    }

    public void rebootAlarms(Context context){

        Database db = Database.getDatabase();
        db.loadData(context);

        for (Goal every : db.getMyGoals()){

            if (every.isNotification()){
                initAlarm(every, context);
            }

        }

    }

    private void initAlarm(Goal every, Context context){
        String[] parsed = every.getReminderTime().split(":");

        int hour = Integer.valueOf(parsed[0]);
        int minute = Integer.valueOf(parsed[1]);
        int id = every.getUniqueID();

        new AlarmManagement().setAlarm(context, hour, minute, id);
    }
}