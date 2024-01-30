package com.vivekapps.mathspuzzles;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
TextView textView1, textView2, textView3, textView4, textView5, textView6, textView7;
int result  ;
long time = 30000;
long durations = 1000;
MediaPlayer mediaPlayer, gameover, tick;
    CountDownTimer countDownTimer;
    int Score = 0;
    int wrong = 0;
    InterstitialAd mInterstitialAd;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        textView4 = findViewById(R.id.textView4);
        textView5 = findViewById(R.id.textView5);
        textView6 = findViewById(R.id.textView6);
        textView7 = findViewById(R.id.textView7);
        database = FirebaseDatabase.getInstance();
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        MobileAds.initialize(this, initializationStatus -> {

        });
        AdView adView = findViewById(R.id.adView);
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
                        nextclass();

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
        mediaPlayer = MediaPlayer.create(this, R.raw.click);
        tick = MediaPlayer.create(this, R.raw.clock);
        gameover = MediaPlayer.create(this, R.raw.gameover2);
        textView7.setText("Score : " + Score);
        result = ques();
        starttimer();
    }

    private void starttimer() {

        countDownTimer = new CountDownTimer(time, durations) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long l) {
                long seconds = l / 1000;
                textView6.setText(seconds + "s");
                tick.start();


            }

            @Override
            public void onFinish() {
                tick.stop();
                if(mInterstitialAd !=null){
                    mInterstitialAd.show(MainActivity.this);
                }
                else {
                    Log.e("Ad Error", "Ad is not ready");
                    nextclass();
                }

            }
        };
        countDownTimer.start();
    }
    private void stop(){
        if(countDownTimer!=null){
            countDownTimer.cancel();
        }
    }
    private void nextclass(){
        countDownTimer.cancel();
        tick.stop();
        Intent intent = new Intent(this, Gameover.class);
        intent.putExtra("Key", Score);
        startActivity(intent);
        finish();
        gameover.start();

    }



    public void action1(View view){
            int value1 = Integer.parseInt(textView2.getText().toString());
            mediaPlayer.start();
        if (value1 != result) {
            wrong += 1;
            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 500 milliseconds
            vibrator.vibrate(500);
            wrong();
            textView2.setVisibility(View.INVISIBLE);

        } else {
            Score += 5;
            textView7.setText("Score : " + Score);
            result = ques();
             stop();
             starttimer();
                wrong = 0;
            MobileAds.initialize(this, initializationStatus -> {

            });
            AdView adView = findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);

            }


    }
    public void action2(View view){
        int value1 = Integer.parseInt((String) textView3.getText());
        mediaPlayer.start();
        if(value1 == result){
            Score += 5;
            textView7.setText("Score : " + Score);
            result = ques();
            stop();
            starttimer();
                wrong = 0;

            MobileAds.initialize(this, initializationStatus -> {

            });
            AdView adView = findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);


        }
        else {
            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 500 milliseconds
            vibrator.vibrate(500);
            wrong += 1;
            wrong();
            textView3.setVisibility(View.INVISIBLE);

        }

    }
    public void action3(View view){
        int value1 = Integer.parseInt((String) textView4.getText());
        mediaPlayer.start();
        if(value1 == result){
            Score += 5;
            textView7.setText("Score : " + Score);
            stop();
            starttimer();

           result = ques();
                wrong = 0;

            MobileAds.initialize(this, initializationStatus -> {

            });
            AdView adView = findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);

        }
        else {
            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 500 milliseconds
            vibrator.vibrate(500);
            wrong += 1;
            wrong();
            textView4.setVisibility(View.INVISIBLE);

        }

    }
    public void action4(View view){
        int value1 = Integer.parseInt((String) textView5.getText());
        mediaPlayer.start();
        if(value1 == result){
            Score += 5;
            textView7.setText("Score : " + Score);
            stop();
            starttimer();

            result=ques();
            wrong = 0;

            MobileAds.initialize(this, initializationStatus -> {

            });
            AdView adView = findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);

        }
        else {
            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 500 milliseconds
            vibrator.vibrate(500);
            wrong += 1;
            wrong();
            textView5.setVisibility(View.INVISIBLE);
        }

    }
    public void wrong(){
        if(wrong == 2){
            tick.stop();
            countDownTimer.cancel();
            if(mInterstitialAd !=null){
                mInterstitialAd.show(MainActivity.this);
            }
            else {
                Log.e("Ad Error", "Ad is not ready");
                nextclass();
            }
        } else if (wrong == 1) {
            Toast.makeText(this, "Last Chance", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("SetTextI18n")
    public int ques(){
        int puzzle = (int)(Math.random() * 2) + 1;
        textView5.setVisibility(View.VISIBLE);
        textView4.setVisibility(View.VISIBLE);
        textView3.setVisibility(View.VISIBLE);
        textView2.setVisibility(View.VISIBLE);
        List<String> choices = new ArrayList<>();
        List<Integer> number = new ArrayList<>();
        choices.add("+");
        choices.add("-");
        choices.add("×");
        choices.add("÷");

        Random random = new Random();

        int max = 100;
        int min = 0;
        int range = max - min + 1;
        int num4 = (int) (Math.random() * range) + min;
        int num5 = (int) (Math.random() * range) + min;
        int num8 = (int) (Math.random() * range) + min;

        int addition = num4 + num5;
        int subtraction = num4 - num5;

        String question1 = String.valueOf(num4);
        String question2 = String.valueOf(num5);


        if(puzzle == 1) {
            choices.add("+");
            choices.add("-");
            choices.add("×");
            choices.add("÷");
            Collections.shuffle(choices);
            textView1.setText(question1 + " " + choices.get(0) + " " + question2 + " = ?");
            if (Objects.equals(choices.get(0), "+")) {
                number.add(addition);
                number.add(addition + 1);
                number.add(addition - 1);
                number.add(addition + 2);
                Collections.shuffle(number);
                textView2.setText(String.valueOf(number.get(0)));
                textView3.setText(String.valueOf(number.get(1)));
                textView4.setText(String.valueOf(number.get(2)));
                textView5.setText(String.valueOf(number.get(3)));
                return addition;
            } else if (Objects.equals(choices.get(0), "×")) {
                int num6 = random.nextInt(20);
                int num7 = random.nextInt(20);
                int multiply = num7 * num6;
                question1 = String.valueOf(num6);
                question2 = String.valueOf(num7);
                textView1.setText(question1 + " " + choices.get(0) + " " + question2 + " = ?");
                number.add(multiply);
                number.add(multiply + 1);
                number.add(multiply + 2);
                number.add(multiply - 1);
                Collections.shuffle(number);
                textView2.setText(String.valueOf(number.get(0)));
                textView3.setText(String.valueOf(number.get(1)));
                textView4.setText(String.valueOf(number.get(2)));
                textView5.setText(String.valueOf(number.get(3)));
                return multiply;
            } else if (Objects.equals(choices.get(0), "-")) {
                number.add(subtraction);
                number.add(subtraction - 1);
                number.add(subtraction + 1);
                number.add(subtraction + 2);
                Collections.shuffle(number);
                textView2.setText(String.valueOf(number.get(0)));
                textView3.setText(String.valueOf(number.get(1)));
                textView4.setText(String.valueOf(number.get(2)));
                textView5.setText(String.valueOf(number.get(3)));
                return subtraction;
            } else if (Objects.equals(choices.get(0), "÷")) {
                int num6 = random.nextInt(100);
                int num7 = random.nextInt(20);
                int division = Math.abs(num6 / num7);
                question1 = String.valueOf(num6);
                question2 = String.valueOf(num7);
                textView1.setText(question1 + " " + choices.get(0) + " " + question2 + " = ?");
                number.add(division);
                number.add(division + 2);
                number.add(division + 1);
                number.add(division - 1);
                Collections.shuffle(number);
                textView2.setText(String.valueOf(number.get(0)));
                textView3.setText(String.valueOf(number.get(1)));
                textView4.setText(String.valueOf(number.get(2)));
                textView5.setText(String.valueOf(number.get(3)));
                return division;
            }

        }
        else {
            String question3 = String.valueOf(num8);
            int num6 = random.nextInt(10);
            int num7 = random.nextInt(10);
            int num10 = random.nextInt(10);
            choices.remove("÷");
            choices.add("+");
            choices.add("-");
            choices.add("×");
            Collections.shuffle(choices);
            textView1.setText(question1 + " " + choices.get(0) + " " + question2 + " "+choices.get(1) +" "+ question3 + " = ?");
            if(Objects.equals(choices.get(0), "+") && Objects.equals(choices.get(1), "+")){
                number.add(num4 + num5 + num8);
                number.add(num4 + num5 + num8+1);
                number.add(num4 + num5 + num8+2);
                number.add(num4 + num5 + num8-1);
                Collections.shuffle(number);
                textView2.setText(String.valueOf(number.get(0)));
                textView3.setText(String.valueOf(number.get(1)));
                textView4.setText(String.valueOf(number.get(2)));
                textView5.setText(String.valueOf(number.get(3)));
                return num4 + num5 + num8;
            } else if (Objects.equals(choices.get(0), "×") && Objects.equals(choices.get(1), "×") ){
                question1 = String.valueOf(num6);
                question2 = String.valueOf(num7);
                question3 = String.valueOf(num10);
                textView1.setText(question1 + " " + choices.get(0) + " " + question2 + " "+choices.get(1) +" "+ question3 + " = ?");
                number.add(num6 * num7 * num10);
                number.add(num6 * num7 * num10 + 1);
                number.add(num6 * num7 * num10 + 2);
                number.add(num6 * num7 * num10 - 1);
                Collections.shuffle(number);
                textView2.setText(String.valueOf(number.get(0)));
                textView3.setText(String.valueOf(number.get(1)));
                textView4.setText(String.valueOf(number.get(2)));
                textView5.setText(String.valueOf(number.get(3)));
                return num6 * num7 * num10;
            }
            else if (Objects.equals(choices.get(0), "×") && Objects.equals(choices.get(1), "+") ){
                question1 = String.valueOf(num6);
                question2 = String.valueOf(num7);
                question3 = String.valueOf(num10);
                textView1.setText(question1 + " " + choices.get(0) + " " + question2 + " "+choices.get(1) +" "+ question3 + " = ?");
                number.add(num6 * num7 + num10);
                number.add(num6 * (num7 + num10));
                number.add((num6 + num10) * num7);
                number.add((num6 * num10) + num7);
                Collections.shuffle(number);
                textView2.setText(String.valueOf(number.get(0)));
                textView3.setText(String.valueOf(number.get(1)));
                textView4.setText(String.valueOf(number.get(2)));
                textView5.setText(String.valueOf(number.get(3)));
                return num6 * num7 + num10;
            }
            else if (Objects.equals(choices.get(0), "+") && Objects.equals(choices.get(1), "×") ){
                question1 = String.valueOf(num6);
                question2 = String.valueOf(num7);
                question3 = String.valueOf(num10);
                textView1.setText(question1 + " " + choices.get(0) + " " + question2 + " "+choices.get(1) +" "+ question3 + " = ?");
                number.add(num6 + num7 * num10);
                number.add((num6 + num7) * num10);
                number.add(num6 * num10 + num7);
                number.add((num6 + num10) * num7);
                Collections.shuffle(number);
                textView2.setText(String.valueOf(number.get(0)));
                textView3.setText(String.valueOf(number.get(1)));
                textView4.setText(String.valueOf(number.get(2)));
                textView5.setText(String.valueOf(number.get(3)));
                return num6 + num7 * num10;
            }
            else if (Objects.equals(choices.get(0), "-") && Objects.equals(choices.get(1), "-") ){
                number.add(num4 - num5 - num8);
                number.add(num5 - num8 - num4);
                number.add(num8 - num4 - num5);
                number.add(num4 - num5 - num8+1);
                Collections.shuffle(number);
                textView2.setText(String.valueOf(number.get(0)));
                textView3.setText(String.valueOf(number.get(1)));
                textView4.setText(String.valueOf(number.get(2)));
                textView5.setText(String.valueOf(number.get(3)));
                return num4 - num5 - num8;
            }
            else if (Objects.equals(choices.get(0), "-") && Objects.equals(choices.get(1), "+") ){
                number.add(num4 - num5 + num8);
                number.add(num5 - num8 + num4);
                number.add(num8 - num4 + num5);
                number.add(num4 + num8 - num5+10);
                Collections.shuffle(number);
                textView2.setText(String.valueOf(number.get(0)));
                textView3.setText(String.valueOf(number.get(1)));
                textView4.setText(String.valueOf(number.get(2)));
                textView5.setText(String.valueOf(number.get(3)));
                return num4 - num5 + num8;
            }
            else if (Objects.equals(choices.get(0), "+") && Objects.equals(choices.get(1), "-") ){
                number.add(num4 + num5 - num8);
                number.add(num5 + num8 - num4 );
                number.add(num8 + num4 - num5);
                number.add(num4 - num8 + num5+10);
                Collections.shuffle(number);
                textView2.setText(String.valueOf(number.get(0)));
                textView3.setText(String.valueOf(number.get(1)));
                textView4.setText(String.valueOf(number.get(2)));
                textView5.setText(String.valueOf(number.get(3)));
                return num4 + num5 - num8;
            }
            else if (Objects.equals(choices.get(0), "-") && Objects.equals(choices.get(1), "×") ){
                question1 = String.valueOf(num6);
                question2 = String.valueOf(num7);
                question3 = String.valueOf(num10);
                textView1.setText(question1 + " " + choices.get(0) + " " + question2 + " "+choices.get(1) +" "+ question3 + " = ?");
                number.add(num6 - num7 * num10);
                number.add((num6 - num7) * num10);
                number.add(num6 * num10 - num7 );
                number.add((num6 - num10) * num7);
                Collections.shuffle(number);
                textView2.setText(String.valueOf(number.get(0)));
                textView3.setText(String.valueOf(number.get(1)));
                textView4.setText(String.valueOf(number.get(2)));
                textView5.setText(String.valueOf(number.get(3)));
                return num6 - num7 * num10;
            }
            else if (Objects.equals(choices.get(0), "×") && Objects.equals(choices.get(1), "-") ){
                question1 = String.valueOf(num6);
                question2 = String.valueOf(num7);
                question3 = String.valueOf(num10);
                textView1.setText(question1 + " " + choices.get(0) + " " + question2 + " "+choices.get(1) +" "+ question3 + " = ?");
                number.add(num6 * num7 - num10);
                number.add(num6 * (num7 - num10));
                number.add((num6 - num10) * num7 );
                number.add((num6 * num10) - num7);
                Collections.shuffle(number);
                textView2.setText(String.valueOf(number.get(0)));
                textView3.setText(String.valueOf(number.get(1)));
                textView4.setText(String.valueOf(number.get(2)));
                textView5.setText(String.valueOf(number.get(3)));
                return num6 * num7 - num10;
            }


        }
        return 0;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Cancel the countdown timer to prevent any potential memory leaks
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        tick.stop();
    }
    public void menu(View view){
        Intent intent = new Intent(this, play.class);
        startActivity(intent);
        finish();
        tick.stop();
        countDownTimer.cancel();
    }
    public void exit(View view){

        finishAffinity();
    }
    @Override
    public void onBackPressed() {

    }
    public int score(){
        DatabaseReference reference = database.getReference("users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return 0;
    }


}