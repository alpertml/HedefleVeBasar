package com.hedeflevebasar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.text.ParseException;
import java.util.Calendar;

public class Statistics extends AppCompatActivity {

    private Database db;
    private Operation op;

    private Spinner spinner;
    private TextView goalName, startDate, endDate, numOfDay;
    private TextView completedDay, completedTask, completedStreak, completedSuccess;

    private ProgressBar PB1, PB2, PB3, PB4;

    private FloatingActionButton addButton;
    private BottomAppBar bottomAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = Database.getDatabase();
        op = new Operation();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        init();

        initializeArrayAdapter();

       addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Database.getDatabase().canAddFreeGoal()){
                    startActivity(new Intent(Statistics.this, AddGoal.class));
                    finish();
                } else {
                   new Advertisement().askWatchAdd(Statistics.this);
                }
            }
        });

        bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.nav_home){
                    startActivity(new Intent(Statistics.this, MainActivity.class));
                    finish();
                }
                else if (item.getItemId() == R.id.nav_statistics){
                    Toast.makeText(Statistics.this, getResources().getString(R.string.UserIsAlreadyHere), Toast.LENGTH_SHORT).show();
                }
                else if (item.getItemId() == R.id.nav_settings){
                    startActivity(new Intent(Statistics.this, Settings.class));
                    finish();
                }
                return false;
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ((TextView) parent.getChildAt(0)).setTextColor(ContextCompat.getColor(Statistics.this, R.color.colorBlack));
                try {
                    updateScreen((Goal) parent.getSelectedItem());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }

    private void updateScreen(Goal goal) throws ParseException {

        goalName.setText(goal.getName());

        String startDateText = getResources().getString(R.string.StatisticStartDate)+"\n"+goal.getStartDate();
        startDate.setText(startDateText);

        String endDateText = getResources().getString(R.string.StatisticEndDate)+"\n"+goal.getEndDate();
        endDate.setText(endDateText);

        String totalDayText = getResources().getString(R.string.TotalDay)+"\n"+goal.getNumbOfDay();
        numOfDay.setText(totalDayText);

        // PROGRESS BAR 3
        int maxStreak = op.getTaskMaxStreak(goal.getItems());
        completedStreak.setText(String.valueOf(maxStreak));
        if (maxStreak > 0)
            PB3.setProgress(100);
        else
            PB3.setProgress(0);

        // PROGRESS BAR 1
        int percentCompletedDay = op.getProgressBarPercent(goal.getStartDate(), goal.getEndDate());
        PB1.setProgress(percentCompletedDay);
        int year = Calendar.getInstance().get(Calendar.YEAR);
        String CompletedDayText = String.valueOf((int)op.getDifference(goal.getStartDate(), op.getTodayDate()+"/"+ year) + 1);
        completedDay.setText(CompletedDayText);

        // PROGRESS BAR 2
        int percentCompletedTask = op.getCompletedTaskPercent(goal.getItems());
        PB2.setProgress(percentCompletedTask);
        completedTask.setText(String.valueOf(op.getCompletedNumOfTask(goal.getItems())));

        // PROGRESS BAR 4
        int defaultSuccessRate = 0;
        if (Integer.parseInt(completedDay.getText().toString()) < goal.getNumbOfDay())
            defaultSuccessRate = op.getSuccessRate(Integer.parseInt(completedDay.getText().toString()), Integer.parseInt(completedTask.getText().toString()));

        completedSuccess.setText(String.valueOf(defaultSuccessRate));
        PB4.setProgress(defaultSuccessRate);
    }

    private void initializeArrayAdapter(){
        ArrayAdapter<Goal> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, db.getMyGoals());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
    }

    private void init(){
        spinner = findViewById(R.id.Spinner);

        goalName = findViewById(R.id.StatisticsGoalName);
        startDate = findViewById(R.id.StatisticsStartDate);
        endDate = findViewById(R.id.StatisticsEndDate);
        numOfDay = findViewById(R.id.StatisticsNumOfDay);

        completedDay = findViewById(R.id.CompletedDayNum);
        completedTask = findViewById(R.id.CompletedTaskNum);
        completedStreak = findViewById(R.id.CompletedStreakNum);
        completedSuccess = findViewById(R.id.CompletedSuccessRate);

        PB1 = findViewById(R.id.progressBar1);
        PB2 = findViewById(R.id.progressBar2);
        PB3 = findViewById(R.id.progressBar3);
        PB4 = findViewById(R.id.progressBar4);

        addButton = findViewById(R.id.FloatingActionButton);
        bottomAppBar = findViewById(R.id.BottomAppBar);
    }
}