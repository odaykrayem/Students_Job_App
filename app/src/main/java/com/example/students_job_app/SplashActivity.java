package com.example.students_job_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import com.example.students_job_app.advertiser.AdvertiserMain;
import com.example.students_job_app.student.StudentMain;
import com.example.students_job_app.utils.Constants;
import com.example.students_job_app.utils.SharedPrefManager;


public class SplashActivity extends AppCompatActivity {

    public static final int TIME_TO_START = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {

                nextPhase();
            }
        }, TIME_TO_START);

    }

    private void nextPhase() {
        if(SharedPrefManager.getInstance(this).isLoggedIn()){
            if(SharedPrefManager.getInstance(this).getUserType() == Constants.USER_TYPE_STUDENT){
                startActivity(new Intent(this, StudentMain.class));
            }else{
                startActivity(new Intent(this, AdvertiserMain.class));
            }
        }else{
            startActivity(new Intent(this, LoginActivity.class));
        }
        finish();
    }
}