package com.vivekapps.mathspuzzles;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Timer;
import java.util.TimerTask;

public class play extends AppCompatActivity {
MediaPlayer mediaPlayer;
TextView textView;
FirebaseAuth auth;
private AdView adView;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }


        mediaPlayer = MediaPlayer.create(this,R.raw.click);
        textView = findViewById(R.id.play);
        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() == null){
            Intent intent = new Intent(play.this, login.class);
            startActivity(intent);
            finish();
        }


        MobileAds.initialize(this, initializationStatus -> {

        });
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        InterstitialAd.load(this, "ca-app-pub-8561131066122988/4107827129", adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                Log.e("Error", loadAdError.toString());
            }

            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                super.onAdLoaded(interstitialAd);

                mInterstitialAd = interstitialAd;
                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdClicked() {
                        super.onAdClicked();
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent();
                        mInterstitialAd = null;
                        Intent intent = new Intent(play.this, MainActivity.class);
                        mediaPlayer.start();
                        startActivity(intent);
                        finish();

                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        super.onAdFailedToShowFullScreenContent(adError);
                        mInterstitialAd = null;
                    }

                    @Override
                    public void onAdImpression() {
                        super.onAdImpression();
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        super.onAdShowedFullScreenContent();

                    }
                });

            }
        });
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                play.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AdRequest adRequest = new AdRequest.Builder().build();
                        adView.loadAd(adRequest);
                    }
                });
            }
        };
        Timer t = new Timer();
        t.scheduleAtFixedRate(timerTask, 0, 1000 * 60);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isNetworkAvailable()) {
                    // Show a message to the user indicating no internet connectivity
                    Toast.makeText(play.this, "Internet required", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(play.this, "Internet Connected", Toast.LENGTH_SHORT).show();
                    if(mInterstitialAd !=null){
                        mInterstitialAd.show(play.this);
                    }
                    else {
                        Log.e("Ad Error", "Ad is not ready");
                        Intent intent = new Intent(play.this, MainActivity.class);
                        mediaPlayer.start();
                        startActivity(intent);
                        finish();

                    }
                }
            }
        });


    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }




}