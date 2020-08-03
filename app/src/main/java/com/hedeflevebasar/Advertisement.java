package com.hedeflevebasar;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class Advertisement extends AppCompatActivity {

    private LoadingDialog loadingDialog;
    private RewardedAd rewardedAd;

    private Context context;

    public void askWatchAdd(Context c){
        this.context = c;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getResources().getString(R.string.AskWatchAd))
                .setCancelable(false)
                .setPositiveButton(context.getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loadingDialog = new LoadingDialog((Activity) context);
                        loadingDialog.startLoadingDialog();
                        Toast.makeText(context,context.getResources().getString(R.string.WaitForAd),Toast.LENGTH_SHORT).show();
                        loadAd();
                    }
                })
                .setNegativeButton(context.getResources().getString(R.string.No), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void playVideoAdd(){

        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
        showAd();
    }
    public void loadAd(){
        rewardedAd = new RewardedAd(context, Constants.REWARDED_AD_UNIT_ID);
        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback(){
            @Override
            public void onRewardedAdLoaded() {
                super.onRewardedAdLoaded();
                Log.i(Constants.AD_TAG, "RewardedAd Loaded Successfully");
                loadingDialog.dismissDialog();
                playVideoAdd();
            }

            @Override
            public void onRewardedAdFailedToLoad(LoadAdError loadAdError) {
                super.onRewardedAdFailedToLoad(loadAdError);
                Log.i(Constants.AD_TAG, "RewardedAd Loaded Failed");
            }

        };
        this.rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
    }

    private void showAd(){
        if (this.rewardedAd.isLoaded()){

            RewardedAdCallback adCallback = new RewardedAdCallback() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    Log.i(Constants.AD_TAG, "User Rewarded");
                    getAddReward();
                }
                @Override
                public void onRewardedAdOpened() {
                    super.onRewardedAdOpened();
                    Log.i(Constants.AD_TAG, "RewardedAd Opened");
                }

                @Override
                public void onRewardedAdClosed() {
                    super.onRewardedAdClosed();
                    Log.i(Constants.AD_TAG, "RewardedAd Closes");
                }

                @Override
                public void onRewardedAdFailedToShow(AdError adError) {
                    super.onRewardedAdFailedToShow(adError);
                    Log.i(Constants.AD_TAG, "RewardedAd Failed to Show");
                }
            };
            this.rewardedAd.show((Activity) context, adCallback);
        }
        else{
            Log.i(Constants.AD_TAG, "RewardedAd Loaded Failed");
            Toast.makeText(context,context.getResources().getString(R.string.AdFailed),Toast.LENGTH_SHORT).show();
        }
    }

    private void getAddReward(){
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent addActivity = new Intent(context, AddGoal.class);
                Toast.makeText(context ,context.getResources().getString(R.string.ThanksForAd),Toast.LENGTH_SHORT).show();
                context.startActivity(addActivity);
                finish();
            }
        }, 3000 );//time in milisecond
    }
}
