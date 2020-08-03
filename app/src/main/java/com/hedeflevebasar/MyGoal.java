package com.hedeflevebasar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import com.google.android.material.bottomappbar.BottomAppBar;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MyGoal extends AppCompatActivity {

    private Database database;
    private AlarmManagement alarm;
    private int goalIndex;
    private int hour, minute;

    private BottomAppBar upperAppBar;
    private TextView editQuote, editNote;
    private TextView editTimeReminder;
    private CheckBox editNotification;

    private LinearLayout MainLinear;
    private ArrayList<LinearLayout> CalendarPages;
    private ImageView prev, next;
    private ArrayList<Item> items;
    private boolean[] tempItem;
    private final int CalendarIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_goal);

        init();

        updateScreen(database.getMyGoals().get(goalIndex));
        try {
            FillCalendar();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        MainLinear.addView(CalendarPages.get(CalendarIndex));
        tempItem = database.getMyGoals().get(goalIndex).getItems();

        Item prevItem = null;
        // 22/05 22/05 is same but one year after 22/05 22/05 also same
        // only one today has been checked with this boolean
        boolean isTodayAvailable = true;
        // fill the calendar with their initial values
        for (Item every : items){

            Operation op = new Operation();
            // paint the colours
            every.setTextColor(ContextCompat.getColor(MyGoal.this, R.color.colorWhite));
            every.getDate().setTextColor(ContextCompat.getColor(MyGoal.this, R.color.colorWhite));
            if (op.areDatesEqual(String.valueOf(every.getDate().getText()),op.getTodayDate()) & isTodayAvailable){
                // today's colour
                every.setTextColor(ContextCompat.getColor(MyGoal.this, R.color.colorToday));
                every.getDate().setTextColor(ContextCompat.getColor(MyGoal.this, R.color.colorToday));
                isTodayAvailable = false;
            }
            every.load(tempItem[every.getNumber() - 1]);

            if (!every.isOffDay() & tempItem[every.getNumber() - 1]){
                filledPrevItems(prevItem, every);
            }
            if (!every.isOffDay())
                prevItem = every;
        }

        // clickable calendar items
        for (Item every : items){
            every.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Item i = (Item)v;
                    tempItem[i.getNumber() - 1] = i.turn();

                    updateItems();
                    checkIsCalendarCompleted();
                    database.saveData(MyGoal.this);
                }
            });
        }

        upperAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.nav_back){
                    startActivity(new Intent(MyGoal.this, MainActivity.class));
                    finish();
                }
                else if (item.getItemId() == R.id.nav_delete){
                    sendDeleteConfirmDialog(getResources().getString(R.string.DeleteGoalConfirmQuestion));
                }
                return false;
            }
        });

        editQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditTexts(Constants.QUOTE);
            }
        });
        editNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditTexts(Constants.NOTE);
            }
        });
        editTimeReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker();
            }
        });
        editNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.getMyGoals().get(goalIndex).setNotification(editNotification.isChecked());
                database.saveData(MyGoal.this);

                if (editNotification.isChecked()){
                    initAlarm(database.getMyGoals().get(goalIndex));
                }
                else{
                    alarm.cancelAlarm(MyGoal.this, database.getMyGoals().get(goalIndex).getUniqueID());
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainLinear.removeAllViews();
                MainLinear.addView(CalendarPages.get(new Calendar().nextCalendarPage(CalendarIndex, CalendarPages.size())));
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainLinear.removeAllViews();
                MainLinear.addView(CalendarPages.get(new Calendar().prevCalendarPage(CalendarIndex)));
            }
        });
    }

    private void timePicker(){
        final java.util.Calendar c = java.util.Calendar.getInstance();
        hour = c.get(java.util.Calendar.HOUR_OF_DAY);
        minute = c.get(java.util.Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int Minute) {
                        hour = hourOfDay;
                        minute = Minute;
                        NumberFormat formatter = new DecimalFormat("00");
                        String hourText = formatter.format(hourOfDay);
                        String minuteText = formatter.format(Minute);
                        String time = hourText + ":" + minuteText;
                        alarm.cancelAlarm(MyGoal.this, database.getMyGoals().get(goalIndex).getUniqueID());
                        database.getMyGoals().get(goalIndex).setReminderTime(time);
                        if (editNotification.isChecked())
                            initAlarm(database.getMyGoals().get(goalIndex));
                        editTimeReminder.setText(time);
                        database.saveData(MyGoal.this);
                    }
                }, hour, minute, true);
        timePickerDialog.show();
    }

    private void initAlarm(Goal goal){
        String[] parsed = goal.getReminderTime().split(":");

        int hour = Integer.valueOf(parsed[0]);
        int minute = Integer.valueOf(parsed[1]);
        int id = goal.getUniqueID();

        alarm.setAlarm(MyGoal.this, hour, minute, id);
    }

    private void updateItems(){
        Item prevItem = null;
        for (Item every : items){
            filledPrevItems(prevItem, every);
            if (!every.isOffDay())
                prevItem = every;
        }
    }
    private void filledPrevItems(Item prevItem, Item every){
        if (prevItem == null){
            int index = every.getNumber() - 1;
            while (!(index <= 0)){
                if (tempItem[every.getNumber() - 1]){
                    items.get(index - 1).setBackground(items.get(index - 1).getOffFilled());
                }
                else
                    items.get(index - 1).setBackground(items.get(index - 1).getOffEmpty());
                index--;
            }
        }
        else {
            int prevIndex = prevItem.getNumber() - 1;
            int nowIndex = every.getNumber() - 1;
            for (int i = prevIndex + 1; i < nowIndex; i++){
                if (tempItem[prevItem.getNumber() - 1])
                    items.get(i).setBackground(items.get(i).getOffFilled());
                else
                    items.get(i).setBackground(items.get(i).getOffEmpty());
            }
        }
    }
    private void checkIsCalendarCompleted(){
        for (int i = 0; i < tempItem.length; i++)
            if (!tempItem[i] & !items.get(i).isOffDay())
                return;
        sendDeleteConfirmDialog(getResources().getString(R.string.CalendarCompletedWord));
    }

    private void init(){
        database = Database.getDatabase();
        database.loadData(MyGoal.this);
        alarm = new AlarmManagement();

        getIndexFromIntent();

        upperAppBar = findViewById(R.id.UpAppBar);
        prev = findViewById(R.id.Prev);
        next = findViewById(R.id.Next);
        editQuote = findViewById(R.id.EditQuote);
        editNote = findViewById(R.id.EditNote);
        editTimeReminder = findViewById(R.id.EditTimeReminder);
        editNotification = findViewById(R.id.EditNotification);

        MainLinear = findViewById(R.id.MainLinear);
        items = new ArrayList<>();
    }

    private void getIndexFromIntent(){
        Intent intent = getIntent();
        goalIndex = intent.getIntExtra("GoalIndex", Constants.DEFAULT_GOAL_INDEX);
    }

    private void updateScreen(Goal goal){

        editQuote.setText(goal.getQuote());
        editNote.setText(goal.getNote());
        editTimeReminder.setText(goal.getReminderTime());
        editNotification.setChecked(goal.isNotification());
    }

    private void sendDeleteConfirmDialog(String dialog){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(dialog)
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alarm.cancelAlarm(MyGoal.this, database.getMyGoals().get(goalIndex).getUniqueID());
                        database.getMyGoals().remove(goalIndex);
                        database.saveData(MyGoal.this);
                        startActivity(new Intent(MyGoal.this, MainActivity.class));
                        finish();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.No), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void EditTexts(final String whichText){
        AlertDialog.Builder mydialog = new AlertDialog.Builder(MyGoal.this);
        mydialog.setTitle(getResources().getString(R.string.Update));
        final EditText updateValue = new EditText(MyGoal.this);
        mydialog.setView(updateValue);
        mydialog.setPositiveButton(getResources().getString(R.string.Confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (whichText.equals(Constants.NOTE)){
                    database.getMyGoals().get(goalIndex).setNote(updateValue.getText().toString());
                    editNote.setText(updateValue.getText().toString());
                }
                if (whichText.equals(Constants.QUOTE)){
                    database.getMyGoals().get(goalIndex).setQuote(updateValue.getText().toString());
                    editQuote.setText(updateValue.getText().toString());
                }
                database.saveData(MyGoal.this);
                hideKeyboard();
            }
        });
        mydialog.setNegativeButton(getResources().getString(R.string.Cancel_lowercase), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        mydialog.show();
    }

    public void hideKeyboard(){
        try {
            InputMethodManager imm = (InputMethodManager) this.getSystemService(INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0); // hide
        }
        catch (NullPointerException e){
            Log.d("HIDEKEYBOARD", "NULLPOINTEXCEPTION");
        }
    }

    private void FillCalendar() throws ParseException, NullPointerException {
        List<Date> dates = new ArrayList<>();

        String str_date = database.getMyGoals().get(goalIndex).getStartDate();
        String end_date = database.getMyGoals().get(goalIndex).getEndDate();

        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault());
        Date startDate = formatter.parse(str_date);
        Date endDate = formatter.parse(end_date);

        long interval = 24*1000 * 60 * 60; // 1 hour in millis
        long endTime = endDate.getTime() ; // create your endtime here, possibly using Calendar or Date
        long curTime = startDate.getTime();
        while (curTime <= endTime) {
            dates.add(new Date(curTime));
            curTime += interval;
        }
        long diff = endDate.getTime() - startDate.getTime();

        CalendarPages = getMainLayout(dates, (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)+ 1);
    }

    private ArrayList<LinearLayout> getMainLayout(List<Date> dates, int days) throws ParseException {
        Calendar calendar = new Calendar();

        ArrayList<LinearLayout> lists = new ArrayList<>();

        LinearLayout newLinear3 = calendar.getLinearType3(MyGoal.this);

        int no = 0;
        boolean flag = false;

        int counter = 0;
        for (int j = 0; j < days/7 + 1; j++) {
            if (counter == 28){
                lists.add(newLinear3);
                counter = 0;
                newLinear3 = calendar.getLinearType3(MyGoal.this);

            }
            LinearLayout newLinear2 = calendar.getLinearType2(MyGoal.this);
            newLinear3.addView(newLinear2);

            LinearLayout newLinear = calendar.getLinearType(MyGoal.this);
            newLinear3.addView(newLinear);
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault());

            for (int i = 0; i < 7; i++) {
                Date lDate =dates.get(no++);
                String ds = formatter.format(lDate);
                String[] temp = ds.split("/");

                if (!calendar.isDateOff(ds, database, goalIndex)){
                    Item b = new Item(MyGoal.this, no, temp[0]+"/"+temp[1], true);
                    newLinear.addView(b);
                    newLinear2.addView(b.getDate());
                    items.add(b);
                }
                else {
                    Item b = new Item(MyGoal.this, no, temp[0]+"/"+temp[1], false);
                    newLinear.addView(b);
                    newLinear2.addView(b.getDate());
                    items.add(b);
                }

                if (no == days){
                    if (days % 7 != 0)
                        for (int k = 0; k <  7 - (days % 7); k++){
                            Item bt = new Item(MyGoal.this, no, "", true);
                            bt.setVisibility(View.INVISIBLE);
                            newLinear.addView(bt);
                            newLinear2.addView(bt.getDate());
                        }
                    lists.add(newLinear3);
                    flag = true;
                    break;
                }
                counter++;
            }
            if (flag)
                break;
        }
        return lists;
    }
}