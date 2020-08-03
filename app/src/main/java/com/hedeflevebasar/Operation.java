package com.hedeflevebasar;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Operation {

    public int getNumbDays(String goalStartDate, String goalEndDate) {

        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault());
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = formatter.parse(goalStartDate);
            endDate = formatter.parse(goalEndDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            long interval = 24 * 1000 * 60 * 60; // 1 hour in millis
            long endTime = endDate.getTime(); // create your endtime here, possibly using Calendar or Date
            long curTime = startDate.getTime();
            while (curTime <= endTime) {
                curTime += interval;
            }

            long diff = endDate.getTime() - startDate.getTime();

            return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + 1;
        } catch (NullPointerException ex) {
            Log.d("NULLPOINTEXCEPTION", "OPERATION");
            return 1;
        }

    }

    public boolean areDatesEqual(String d, String d1){
        String[] temp, temp1;
        temp = d.split("/");
        temp1 = d1.split("/");
        return (Integer.valueOf(temp[0]).equals(Integer.valueOf(temp1[0])) &
                Integer.valueOf(temp[1]).equals(Integer.valueOf(temp1[1])));
    }

    public String getTodayDate(){
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        int month = calendar.get(java.util.Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return day+"/"+(month+1);
    }

    public long getDifference(String start, String end) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault());
        Date startDate = sdf.parse(start);
        Date endDate = sdf.parse(end);
        if (startDate == null | endDate == null)
            return 0;
        return getUnitBetweenDates(startDate, endDate);
    }

    private long getUnitBetweenDates(Date startDate, Date endDate) {
        long timeDiff = endDate.getTime() - startDate.getTime();
        return TimeUnit.DAYS.convert(timeDiff, TimeUnit.MILLISECONDS);
    }

    public boolean isTaskTodayByGoalID(int i){

        Calendar calendar = Calendar.getInstance();
        boolean[] days = Database.getDatabase().getMyGoals().get(i).getDays();

        if (days[0]) // everyday task should be done
            return true;
        if (calendar.get(Calendar.DAY_OF_WEEK) - 1 > 0) // 6 other days
            return days[calendar.get(Calendar.DAY_OF_WEEK) - 1];
        return days[7]; // sunday
    }

    public boolean isTodayTaskCompleted(Goal goal) throws ParseException {

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int diffDates = (int) getDifference(goal.getStartDate()+"/"+year, getTodayDate()+"/"+year);

        if (diffDates < 0){
            return false;
        }

        boolean[] items = goal.getItems();
        try {
            if (items[diffDates]) {
                return true;
            }
        }
        catch(ArrayIndexOutOfBoundsException e){ // don't care (as i know, it not effect the app)
            Log.d("ARRAY_INDEX_EXP", "OPERATIONS");
        }
        return false;
    }

    @SuppressWarnings("ForLoopReplaceableByForEach")
    public int getTaskMaxStreak(boolean[] task){
        int maxStreak = 0;
        int currentStreak = 0;
        for(int i = 0; i < task.length; i++){
            if (task[i]){
                currentStreak++;
            }
            else {
                if (maxStreak < currentStreak)
                    maxStreak = currentStreak;
                currentStreak = 0;
            }
        }
        if (maxStreak < currentStreak)
            maxStreak = currentStreak;
        return maxStreak;
    }

    public int getProgressBarPercent(String startDate, String endDate) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);

        int totalDay = (int)getDifference(startDate, endDate) + 1;
        int doneDay = (int)getDifference(startDate, getTodayDate()+"/"+year) + 1;

        return 100*doneDay/totalDay;
    }

    public int getCompletedTaskPercent(boolean[] task){

        int completedTask = getCompletedNumOfTask(task);
        return completedTask*100/task.length;
    }

    @SuppressWarnings("ForLoopReplaceableByForEach")
    public int getCompletedNumOfTask(boolean[] task){
        int counter = 0;
        for (int i = 0; i < task.length; i++)
            if (task[i])
                counter++;
        return counter;
    }

    public int getSuccessRate(int totalDay, int completedDay){
        int successRate;
        try {
            successRate =  completedDay * 100 / totalDay;
        }
        catch (ArithmeticException e){
            return 0;
        }
        //noinspection ManualMinMaxCalculation
        return successRate >= 100 ? 100 : successRate;
    }
}
