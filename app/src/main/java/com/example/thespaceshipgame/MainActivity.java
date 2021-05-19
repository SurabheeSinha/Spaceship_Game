package com.example.thespaceshipgame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.analytics.FirebaseAnalytics.Event;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private SpaceshipView spaceView;
    private Handler handler = new Handler();
    private final static long Interval = 30;
    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        spaceView = new SpaceshipView(this);
        setContentView(spaceView);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Timer timer = new Timer();
        timer.schedule(new TimerTask(){

            @Override
            public void run() {
                handler.post(new Runnable(){
                    @Override
                    public void run(){
                        spaceView.invalidate();
                    }

                });
            }
        }, 0, Interval);


        Bundle params = new Bundle();
        mFirebaseAnalytics.logEvent("GameStarted", params);

    }
}