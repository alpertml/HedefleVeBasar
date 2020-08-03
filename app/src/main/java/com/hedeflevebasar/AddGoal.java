package com.hedeflevebasar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Calendar;

public class AddGoal extends AppCompatActivity implements View.OnClickListener{

    private Database database;

    private TextView startDate, endDate, timeReminder;
    private ImageView selectIcon;

    private EditText name, note, quote;

    private CheckBox checkBoxNotification;

    private Button add, cancel;
    private Button mon, tue, wed, thu, fri, sat, sun, everyday;

    private int hour, minute, day, month, year;
    private int i, j;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goal);

        init();

        checkTempData();

        listenDaysButtons();

        selectIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.saveGoalTempData(getCurrentGoal(), AddGoal.this);
                startActivity(new Intent(AddGoal.this, SelectIcon.class));
                finish();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (!areInputsRight())
                        return;
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Goal currentGoal = getCurrentGoal();
                currentGoal.initItem();
                if (currentGoal.isNotification())
                        new AlarmManagement().setAlarm(AddGoal.this, hour, minute, currentGoal.getUniqueID());

                database.getMyGoals().add(currentGoal);
                database.saveData(AddGoal.this);
                database.clearGoalTempData(AddGoal.this);
                startActivity(new Intent(AddGoal.this, MainActivity.class));
                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.clearGoalTempData(AddGoal.this);
                startActivity(new Intent(AddGoal.this, MainActivity.class));
                finish();
            }
        });

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String input = "startDate";
                datePicker(input);
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String input = "endDate";
                datePicker(input);
            }
        });

        timeReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker();
            }
        });
    }

    private void datePicker(final String setInput){
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {
                        String date_time = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        if (setInput.equals("startDate"))
                            startDate.setText(date_time);
                        else if (setInput.equals("endDate"))
                            endDate.setText(date_time);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    private void timePicker(){
        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,int Minute) {
                        hour = hourOfDay;
                        minute = Minute;
                        NumberFormat formatter = new DecimalFormat("00");
                        String hourText = formatter.format(hourOfDay);
                        String minuteText = formatter.format(Minute);
                        String time = hourText + ":" + minuteText;
                        timeReminder.setText(time);
                    }
                }, hour, minute, true);
        timePickerDialog.show();
    }

    // days buttons listener
    @Override
    public void onClick(View v) {
        Button button = (Button)v;
        turnButton(button);
        if (button.getId() != R.id.EveryDay){
            if (isDaySelected(button)){
                everyday.setBackground(ContextCompat.getDrawable(AddGoal.this, R.drawable.days_button));
            }
        } else {
            if (isEveryDaySelected()){
                updateDaysButtonNotSelected();
            }
        }
    }

    private void turnButton(Button button){
        if (button.getBackground().getConstantState() ==
                ContextCompat.getDrawable(AddGoal.this, R.drawable.days_button).getConstantState()){
            button.setBackground(ContextCompat.getDrawable(AddGoal.this, R.drawable.selected_days_button));
            return;
        }
        button.setBackground(ContextCompat.getDrawable(AddGoal.this, R.drawable.days_button));
    }

    private void updateDaysButtonNotSelected(){
        mon.setBackground(ContextCompat.getDrawable(AddGoal.this, R.drawable.days_button));
        tue.setBackground(ContextCompat.getDrawable(AddGoal.this, R.drawable.days_button));
        wed.setBackground(ContextCompat.getDrawable(AddGoal.this, R.drawable.days_button));
        thu.setBackground(ContextCompat.getDrawable(AddGoal.this, R.drawable.days_button));
        fri.setBackground(ContextCompat.getDrawable(AddGoal.this, R.drawable.days_button));
        sat.setBackground(ContextCompat.getDrawable(AddGoal.this, R.drawable.days_button));
        sun.setBackground(ContextCompat.getDrawable(AddGoal.this, R.drawable.days_button));
    }

    private boolean isEveryDaySelected(){
        return everyday.getBackground().getConstantState() ==
                ContextCompat.getDrawable(AddGoal.this, R.drawable.selected_days_button).getConstantState();
    }

    private boolean isDaySelected(Button button){
        return  button.getBackground().getConstantState() ==
                ContextCompat.getDrawable(AddGoal.this, R.drawable.selected_days_button).getConstantState();
    }

    private void checkTempData(){
        try {
            Intent intent = getIntent();
            i = intent.getIntExtra("i", 0);
            j = intent.getIntExtra("j", 0);

            Goal tempGoal = database.loadGoalTempData(AddGoal.this);
            database.clearGoalTempData(AddGoal.this);
            if(tempGoal == null)
                return;
            updateScreen(tempGoal);

        } catch (RuntimeException e){
            Log.d(Constants.INTENT_NOT_SELECT_ICON, Constants.LOG_LOCATION_ADD_GOAL);
        }
    }

    private void updateScreen(Goal goal){

        selectIcon.setImageResource(new SelectIcon().getDrawableIdByMatrix(i, j));

        startDate.setText(goal.getStartDate());
        endDate.setText(goal.getEndDate());
        timeReminder.setText(goal.getReminderTime());
        checkBoxNotification.setChecked(goal.isNotification());
        name.setText(goal.getName());
        note.setText(goal.getNote());
        quote.setText(goal.getQuote());

        updateAllDaysButtons(goal.getDays());
    }

    private void updateAllDaysButtons(boolean[] tempDays){
        updateButton(mon, tempDays[1]);
        updateButton(tue, tempDays[2]);
        updateButton(wed, tempDays[3]);
        updateButton(thu, tempDays[4]);
        updateButton(fri, tempDays[5]);
        updateButton(sat, tempDays[6]);
        updateButton(sun, tempDays[7]);
        updateButton(everyday, tempDays[0]);
    }

    private void updateButton(Button button, boolean isDayFull){
        if (isDayFull)
            button.setBackground(ContextCompat.getDrawable(AddGoal.this, R.drawable.selected_days_button));
        else
            button.setBackground(ContextCompat.getDrawable(AddGoal.this, R.drawable.days_button));
    }

    private void init(){

        database = Database.getDatabase();
        checkBoxNotification = findViewById(R.id.CheckBoxNotification);

        // TextViews
        startDate = findViewById(R.id.StartDate);
        endDate = findViewById(R.id.EndDate);
        timeReminder = findViewById(R.id.TimeReminder);
        selectIcon = findViewById(R.id.SelectIcon);

        // EditTexts
        name = findViewById(R.id.Name);
        note = findViewById(R.id.Note);
        quote = findViewById(R.id.Quote);

        // Buttons
        add = findViewById(R.id.Add);
        cancel = findViewById(R.id.Cancel);

        mon = findViewById(R.id.mon);
        tue = findViewById(R.id.tue);
        wed = findViewById(R.id.wed);
        thu = findViewById(R.id.thu);
        fri = findViewById(R.id.fri);
        sat = findViewById(R.id.sat);
        sun = findViewById(R.id.sun);
        everyday = findViewById(R.id.EveryDay);

        // Update dates on the screen
        updateDates();
    }

    private void updateDates(){
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        String todayDate = day + "/" + (month + 1) + "/" + year;
        // update textViews with current date
        startDate.setText(todayDate);
        endDate.setText(todayDate);
    }

    private void listenDaysButtons(){
        mon.setOnClickListener(this);
        tue.setOnClickListener(this);
        wed.setOnClickListener(this);
        thu.setOnClickListener(this);
        fri.setOnClickListener(this);
        sat.setOnClickListener(this);
        sun.setOnClickListener(this);
        everyday.setOnClickListener(this);
    }

    private Goal getCurrentGoal(){

        String currentName = name.getText().toString();
        String currentNote = note.getText().toString();
        String currentQuote = quote.getText().toString();
        String currentStartDate = startDate.getText().toString();
        String currentEndDate = endDate.getText().toString();
        String currentTimeReminder = timeReminder.getText().toString();
        boolean currentNotification = checkBoxNotification.isChecked();
        boolean[] currentDays = getCurrentDays();

        Goal goal = new Goal(currentName, currentNote, currentQuote, currentStartDate,
                currentEndDate, currentTimeReminder, currentDays, currentNotification);

        goal.setI(i);
        goal.setJ(j);
        goal.setNotification(checkBoxNotification.isChecked());

        return goal;
    }

    private boolean[] getCurrentDays(){

        boolean[] tempDays = new boolean[Constants.NUMB_DAYS];

        tempDays[0] = isEveryDaySelected();
        tempDays[1] = isDaySelected(mon);
        tempDays[2] = isDaySelected(tue);
        tempDays[3] = isDaySelected(wed);
        tempDays[4] = isDaySelected(thu);
        tempDays[5] = isDaySelected(fri);
        tempDays[6] = isDaySelected(sat);
        tempDays[7] = isDaySelected(sun);

        return tempDays;
    }

    private boolean areInputsRight() throws ParseException {

        if (isStartAndEndDateRight()){
            Toast.makeText(AddGoal.this, getResources().getString(R.string.InputRuleDates), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (isCalendarOverOneYear()){
            Toast.makeText(AddGoal.this, getResources().getString(R.string.InputRulePerformanceProblem), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!isAtLeastDayFilled()){
            Toast.makeText(AddGoal.this, getResources().getString(R.string.InputRuleMinDay), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean isStartAndEndDateRight() throws ParseException {
        return new Operation().getDifference(startDate.getText().toString(), endDate.getText().toString()) <= 0;
    }
    private boolean isCalendarOverOneYear() throws ParseException {
        return new Operation().getDifference(startDate.getText().toString(), endDate.getText().toString()) >= Constants.DAYS_OF_A_YEAR;
    }
    @SuppressWarnings("ForLoopReplaceableByForEach")
    private boolean isAtLeastDayFilled(){
        boolean[] tempDays = getCurrentDays();
        for (int i = 0; i < tempDays.length; i++)
            if (tempDays[i])
                return true;
            return false;
    }
}