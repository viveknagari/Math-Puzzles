package com.vivekapps.mathspuzzles;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
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

import java.util.Timer;
import java.util.TimerTask;

public class Gameover extends AppCompatActivity {
    Button button, button2;
    ImageView imageView;
    InterstitialAd minterstitialAd;

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameover);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        MobileAds.initialize(this, initializationStatus -> {

        });
        AdView adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Gameover.this.runOnUiThread(new Runnable() {
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

        int Value = getIntent().getIntExtra("Key",0);
        TextView textView = findViewById(R.id.textView8);
        textView.setText("Score : " + Value);
        button = findViewById(R.id.button2);
        button2 = findViewById(R.id.button);
        imageView = findViewById(R.id.imageView);
        button.setOnClickListener(view -> activity());
        button2.setOnClickListener(view -> finishAffinity());
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                // You can perform actions based on the selected rating here.
                // For example, you might want to save the rating to a database or send it to a server.
                // In this example, we'll just show a toast with the selected rating.
                Toast.makeText(Gameover.this, "Rating: " + rating, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void activity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    public void shareAppViaWhatsApp(View view) {
        String apkFileLink = "C:\\Users\\nagar\\AndroidStudioProjects\\MathsPuzzles\\app\\build\\outputs\\apk\\debug\\app-debug.apk"; // Replace this with the actual link to your app's APK file

        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("application/vnd.android.package-archive");
        whatsappIntent.setPackage("com.whatsapp");
        whatsappIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(apkFileLink));
        try {
            startActivity(whatsappIntent);
        } catch (ActivityNotFoundException ex) {
            // Handle the case where WhatsApp is not installed on the user's device.
        }
    }
    @Override
    public void onBackPressed() {

    }

}