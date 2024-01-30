package com.vivekapps.mathspuzzles;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class login extends AppCompatActivity {
EditText editText, editText1;
TextView textView;
Button button;
FirebaseAuth auth;
FirebaseDatabase database;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        editText = findViewById(R.id.editTextTextEmailAddress2);
        editText1 = findViewById(R.id.editTextNumberPassword3);
        textView = findViewById(R.id.textView16);
        button = findViewById(R.id.button4);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        MobileAds.initialize(this, initializationStatus -> {});
        AdView adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editText.getText().toString().trim();
                String password = editText1.getText().toString().trim();
                if(email.isEmpty()){
                    editText.setError("Email address required");
                } else if (password.isEmpty()) {
                    editText1.setError("Password required");
                }
                else if (!isNetworkAvailable()) {
                    // Show a message to the user indicating no internet connectivity
                    Toast.makeText(login.this, "Internet required", Toast.LENGTH_SHORT).show();
                }
                else {
                auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(login.this, play.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            String errorMessage = "Sign-up failed. Please try again.";
                            if (task.getException() != null) {
                                errorMessage = task.getException().getMessage();
                            }
                            Toast.makeText(login.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
              }
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login.this, singup.class);
                startActivity(intent);
                finish();
            }
        });

    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}