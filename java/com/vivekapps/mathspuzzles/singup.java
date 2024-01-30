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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class singup extends AppCompatActivity {
EditText editText1, editText2, editText3, editText4;
Button button;
TextView textView;
FirebaseAuth auth;
FirebaseDatabase database;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup);
        if(getSupportActionBar() !=null){
            getSupportActionBar().hide();
        }

        editText1 = findViewById(R.id.editTextText);
        editText2 = findViewById(R.id.editTextTextEmailAddress);
        editText3 = findViewById(R.id.editTextNumberPassword);
        editText4 = findViewById(R.id.editTextNumberPassword2);
        textView = findViewById(R.id.textView13);
        button = findViewById(R.id.button3);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        MobileAds.initialize(this, initializationStatus -> {});
        AdView adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = editText1.getText().toString().trim();
                String email = editText2.getText().toString().trim();
                String password = editText3.getText().toString().trim();
                String Cpassowrd = editText4.getText().toString().trim();
                if (username.isEmpty()) {
                    editText1.setError("username required");
                } else if (email.isEmpty()) {
                    editText2.setError("Email address required");
                } else if (password.isEmpty()) {
                    editText3.setError("Password required");
                } else if (Cpassowrd.isEmpty()) {
                    editText4.setError("Conform required");
                } else if (!password.equals(Cpassowrd)) {
                    Toast.makeText(singup.this, "Password doesn't Match", Toast.LENGTH_SHORT).show();


                }
                else if (!isNetworkAvailable()) {
                    // Show a message to the user indicating no internet connectivity
                    Toast.makeText(singup.this, "Internet required", Toast.LENGTH_SHORT).show();
                }else {
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String id = task.getResult().getUser().getUid();
                                DatabaseReference reference = database.getReference().child("users").child(id);
                                Users users = new Users(username, email, password, id);
                                reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Intent intent = new Intent(singup.this, play.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                });

                            } else {
                                String errorMessage = "Sign-up failed. Please try again.";
                                if (task.getException() != null) {
                                    errorMessage = task.getException().getMessage();
                                }
                                Toast.makeText(singup.this, errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(singup.this, login.class);
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