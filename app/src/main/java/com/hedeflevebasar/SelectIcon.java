package com.hedeflevebasar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class SelectIcon extends AppCompatActivity {

    private ImageView[][] icons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_icon);

        init();

        for (int i = 0; i < Constants.ROW_SIZE; i++)
            for (int j = 0; j < Constants.COLUMN_SIZE; j++){

                String stringID = "i"+i+j;
                int resID = getResources().getIdentifier(stringID, "id", getPackageName());
                icons[i][j] = findViewById(resID);

                icons[i][j].setTag(i+":"+j);

                icons[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        backToAddGoalActivity((ImageView)v);
                    }
                });

            }
    }

    private void backToAddGoalActivity(ImageView imageView){
        String[] parsed = imageView.getTag().toString().split(":");

        Intent backToAddGoal = new Intent(SelectIcon.this, AddGoal.class);
        backToAddGoal.putExtra("i", Integer.valueOf(parsed[0]));
        backToAddGoal.putExtra("j", Integer.valueOf(parsed[1]));

        startActivity(backToAddGoal);
        finish();
    }

    private void init(){

        icons = new ImageView[Constants.ROW_SIZE][Constants.COLUMN_SIZE];

    }

    public int getDrawableIdByMatrix(int i, int j){
        if (i == 0 & j == 0)
            return R.drawable.icon_sport;
        if (i == 0 & j == 1)
            return R.drawable.icon_gym;
        if (i == 0 & j == 2)
            return R.drawable.icon_basketball;
        if (i == 0 & j == 3)
            return R.drawable.icon_football;
        if (i == 0 & j == 4)
            return R.drawable.icon_exercise;

        if (i == 1 & j == 0)
            return R.drawable.icon_cyclist;
        if (i == 1 & j == 1)
            return R.drawable.icon_water;
        if (i == 1 & j == 2)
            return R.drawable.icon_paint;
        if (i == 1 & j == 3)
            return R.drawable.icon_book;
        if (i == 1 & j == 4)
            return R.drawable.icon_jigsaw;

        if (i == 2 & j == 0)
            return R.drawable.icon_headphone;
        if (i == 2 & j == 1)
            return R.drawable.icon_skateboard;
        if (i == 2 & j == 2)
            return R.drawable.icon_skate;
        if (i == 2 & j == 3)
            return R.drawable.icon_scuba;
        if (i == 2 & j == 4)
            return R.drawable.icon_yoga;

        if (i == 3 & j == 0)
            return R.drawable.icon_not_smoking;
        if (i == 3 & j == 1)
            return R.drawable.icon_camera;
        if (i == 3 & j == 2)
            return R.drawable.icon_mountain;
        if (i == 3 & j == 3)
            return R.drawable.icon_smile;
        if (i == 3 & j == 4)
            return R.drawable.icon_phone;

        if (i == 4 & j == 0)
            return R.drawable.icon_baking;
        if (i == 4 & j == 1)
            return R.drawable.icon_garden;
        if (i == 4 & j == 2)
            return R.drawable.icon_dart;
        if (i == 4 & j == 3)
            return R.drawable.icon_guitar;
        if (i == 4 & j == 4)
            return R.drawable.icon_violin;

        if (i == 5 & j == 0)
            return R.drawable.icon_piano;
        if (i == 5 & j == 1)
            return R.drawable.icon_no_beer;
        if (i == 5 & j == 2)
            return R.drawable.icon_diet;
        if (i == 5 & j == 3)
            return R.drawable.icon_chess;
        if (i == 5 & j == 4)
            return R.drawable.icon_skiing;

        // default drawable id
        return R.drawable.icon_sport;
    }
}