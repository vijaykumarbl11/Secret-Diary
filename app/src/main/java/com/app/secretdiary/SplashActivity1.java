package com.app.secretdiary;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class SplashActivity1 extends AppCompatActivity {
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash1);
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            Thread thread = new Thread() {
                public void run() {
                    try {
                        sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        Intent intent = new Intent(SplashActivity1.this, MainActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    }
                }
            };
            thread.start();


        }
        else
        {
            Thread thread = new Thread() {
                public void run() {
                    try {
                        sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        Intent intent = new Intent(SplashActivity1.this, SignUpActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    }
                }
            };

            thread.start();

        }

    }



}