package com.hedeflevebasar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

public class Settings extends AppCompatActivity {

    private TextView sendMail, openWebsite, openYoutube;
    private ImageView TR, UK;

    private FloatingActionButton addButton;
    private BottomAppBar bottomAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        init();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Database.getDatabase().canAddFreeGoal()){
                    startActivity(new Intent(Settings.this, AddGoal.class));
                    finish();
                } else {
                    new Advertisement().askWatchAdd(Settings.this);
                }
            }
        });

        bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.nav_home){
                    startActivity(new Intent(Settings.this, MainActivity.class));
                    finish();
                }
                else if (item.getItemId() == R.id.nav_statistics){
                    startActivity(new Intent(Settings.this, Statistics.class));
                    finish();
                }
                else if (item.getItemId() == R.id.nav_settings){
                    Toast.makeText(Settings.this, getResources().getString(R.string.UserIsAlreadyHere), Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        openYoutube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openURL(Constants.YoutubeURL);
            }
        });

        openWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openURL(Constants.WebsiteURL);
            }
        });

        sendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail();
            }
        });

        TR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askUserConfirmLanguage(Constants.TR);
            }
        });

        UK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askUserConfirmLanguage(Constants.EN);
            }
        });
    }

    private void openURL(String url){
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

    private void sendMail(){
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto",Constants.MailAddress, null));
        intent.putExtra(Intent.EXTRA_SUBJECT, "KONU");
        intent.putExtra(Intent.EXTRA_TEXT, "MESAJ");
        startActivity(Intent.createChooser(intent, "Choose an Email client :"));
    }

    private void init(){
        TR = findViewById(R.id.TR);
        UK = findViewById(R.id.UK);

        sendMail = findViewById(R.id.SendMail);
        openWebsite = findViewById(R.id.OpenWebsite);
        openYoutube = findViewById(R.id.OpenYoutube);

        addButton = findViewById(R.id.FloatingActionButton);
        bottomAppBar = findViewById(R.id.BottomAppBar);
    }

    private void refreshActivity(){
        Intent refresh = new Intent(Settings.this, Settings.class);
        startActivity(refresh);
    }

    private void askUserConfirmLanguage(final String lang){

        String messageTitle = getTranslateMessage(lang);

        AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);
        builder.setMessage(messageTitle)
                .setCancelable(false)
                .setPositiveButton(this.getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        changeLanguage(lang);
                        Toast.makeText(Settings.this, getResources().getString(R.string.SuccessfullyTranslated), Toast.LENGTH_SHORT).show();
                        refreshActivity();
                    }
                })
                .setNegativeButton(this.getResources().getString(R.string.No), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private String getTranslateMessage(String lang){
        if (lang.equals("tr"))
            return getResources().getString(R.string.TranslateToTurkish);
        if (lang.equals("en"))
            return getResources().getString(R.string.TranslateToEnglish);
        return " ";
    }

    private void changeLanguage(String lan){
        Locale locale = new Locale(lan);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(
                config,
                getResources().getDisplayMetrics()
        );
    }
}