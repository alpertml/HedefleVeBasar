package com.hedeflevebasar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class Database extends AppCompatActivity {

    private static Database database;
    private ArrayList<Goal> myGoals;

    private Database(){

    }

    public static Database getDatabase(){

        if (database == null){
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            database = new Database();
        }
        return database;

    }

    public void saveData(Context context){

        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.DATABASE_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Gson gson = new Gson();
            String json = gson.toJson(myGoals);
            editor.putString(Constants.DATABASE_KEY, json);
            editor.apply();

        } catch(NullPointerException e){
            Log.d(Constants.LOG_LOCATION_DATABASE, Constants.INVOKE_NOT_SAVED);
        }

    }

    public void loadData(Context context){

        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.DATABASE_NAME, MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedPreferences.getString(Constants.DATABASE_KEY, null);
            Type type = new TypeToken<ArrayList<Goal>>() {}.getType();
            myGoals = gson.fromJson(json, type);

        } catch(NullPointerException e){
            Log.d(Constants.LOG_LOCATION_DATABASE, Constants.INVOKE_EMPTY);
        }

        if (myGoals == null)
            myGoals = new ArrayList<>();
    }

    public void saveGoalTempData(Goal goal, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.TEMP_DATABASE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(goal);
        editor.putString(Constants.TEMP_DATABASE_KEY, json);
        editor.apply();
    }
    public Goal loadGoalTempData(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.TEMP_DATABASE_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(Constants.TEMP_DATABASE_KEY, null);
        Type type = new TypeToken<Goal>() {}.getType();

        return gson.fromJson(json, type);
    }
    public void clearGoalTempData(Context context){
        // clear temp data
        saveGoalTempData(null, context);
    }

    public boolean canAddFreeGoal(){
        return myGoals.size() < Constants.NUMB_FREE_GOAL;
    }

    public ArrayList<Goal> getMyGoals(){
        return myGoals;
    }

}
