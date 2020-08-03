package com.hedeflevebasar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements ListAdapter.OnNoteListener{

    private Database database;

    private FloatingActionButton addButton;
    private BottomAppBar bottomAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        database = Database.getDatabase();
        database.loadData(MainActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();


       addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (database.canAddFreeGoal()){
                    startActivity(new Intent(MainActivity.this, AddGoal.class));
                    finish();
                } else {
                    new Advertisement().askWatchAdd(MainActivity.this);
                }
            }
        });

        bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.nav_home){
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.UserIsAlreadyHere), Toast.LENGTH_SHORT).show();
                }
                else if (item.getItemId() == R.id.nav_statistics){
                    startActivity(new Intent(MainActivity.this, Statistics.class));
                    finish();
                }
                else if (item.getItemId() == R.id.nav_settings){
                    startActivity(new Intent(MainActivity.this, Settings.class));
                    finish();
                }
                return false;
            }
        });

    }

    private void init(){
        bottomAppBar = findViewById(R.id.BottomAppBar);
        addButton = findViewById(R.id.FloatingActionButton);

        RecyclerView myGoals_View;
        ListAdapter listAdapter;
        myGoals_View = findViewById(R.id.Goal_Recycler);
        myGoals_View.setLayoutManager(new LinearLayoutManager(this));

        listAdapter = new ListAdapter(MainActivity.this, database.getMyGoals(),MainActivity.this);
        myGoals_View.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void OnNoteClick(int position) {
        Intent GoalActivity = new Intent(MainActivity.this, MyGoal.class);
        GoalActivity.putExtra("GoalIndex", position);
        startActivity(GoalActivity);
    }



}
