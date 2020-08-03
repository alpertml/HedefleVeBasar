package com.hedeflevebasar;

import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Calendar {

    public LinearLayout getLinearType3(Context context){
        LinearLayout newLinear3 = new LinearLayout(context);
        newLinear3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT , LinearLayout.LayoutParams.WRAP_CONTENT));
        newLinear3.setOrientation(LinearLayout.VERTICAL);
        newLinear3.setBackgroundColor(ContextCompat.getColor(context, R.color.colorBottomAppBar));
        return newLinear3;
    }

    public LinearLayout getLinearType2(Context context){
        LinearLayout newLinear2 = new LinearLayout( context);
        newLinear2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        newLinear2.setBackgroundColor(ContextCompat.getColor(context, R.color.colorBottomAppBar));
        newLinear2.setGravity(Gravity.CENTER);
        newLinear2.setOrientation(LinearLayout.HORIZONTAL);
        newLinear2.setPadding(5, 5, 5, 5);
        return newLinear2;
    }

    public LinearLayout getLinearType(Context context){
        LinearLayout newLinear = new LinearLayout(context);
        newLinear.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        newLinear.setOrientation(LinearLayout.HORIZONTAL);
        return newLinear;
    }

    public boolean isDateOff(String itemDate, Database db, int goalIndex) throws ParseException {
        itemDate = itemDate.replace('/', '-');
        SimpleDateFormat inFormat = new SimpleDateFormat("dd-MM-yyyy", java.util.Locale.getDefault());
        Date date = inFormat.parse(itemDate);
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(date);

        boolean[] days = db.getMyGoals().get(goalIndex).getDays();
        if (days[0])
            return true;
        if (calendar.get(java.util.Calendar.DAY_OF_WEEK) - 1 > 0)
            return days[calendar.get(java.util.Calendar.DAY_OF_WEEK) - 1];
        return days[7];
    }

    // calendar management
    public int nextCalendarPage(int index, int size){
        int temp = index;
        temp++;
        if (size <= temp)
            return index;
        return ++index;
    }
    public int prevCalendarPage(int index){
        if (index <= 0)
            return 0;
        return --index;
    }


}
