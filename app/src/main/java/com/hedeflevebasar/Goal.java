package com.hedeflevebasar;

import androidx.annotation.NonNull;
import java.util.Random;

public class Goal {

    private final String name;
    private String note, quote, reminderTime;
    private final String startDate, endDate;

    private int uniqueID;
    private int i, j; // Logo matrix location
    private int NumbOfDay;

    private final boolean[] days; // which days will be done
    private boolean notification;
    private boolean[] items;

    public Goal(String name, String note, String quote, String startDate, String endDate,
                String reminderTime, boolean[] days, boolean notification) {
        this.name = name;
        this.note = note;
        this.quote = quote;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reminderTime = reminderTime;
        this.days = days;
        this.notification = notification;
    }

    public String getName() {
        return name;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(String reminderTime) {
        this.reminderTime = reminderTime;
    }

    public int getUniqueID() {
        return uniqueID;
    }

    public boolean[] getDays() {
        return days;
    }

    public boolean isNotification() {
        return notification;
    }

    public void setNotification(boolean notification) {
        this.notification = notification;
    }

    public boolean[] getItems() {
        return items;
    }

    @NonNull
    public String toString() {
        return name;
    }

    public int getNumbOfDay() {
        return NumbOfDay;
    }

    public void initItem(){
        this.uniqueID = new Random().nextInt(100000);
        this.NumbOfDay = new Operation().getNumbDays(startDate, endDate);
        this.items = new boolean[this.NumbOfDay];
    }
}
