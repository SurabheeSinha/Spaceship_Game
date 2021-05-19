package com.example.thespaceshipgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.OverScroller;
import android.view.View.OnClickListener;
import android.graphics.Canvas;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

public class GameOver extends AppCompatActivity {

    private Button playAgain, quit;
    private FirebaseAnalytics mFirebaseAnalytics;
    private Paint score = new Paint();
    private TextView totalScore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        score.setColor(Color.WHITE);
        score.setTextSize(90);
        score.setTypeface(Typeface.DEFAULT_BOLD);
        score.setAntiAlias(true);

        Bundle params = new Bundle();
        mFirebaseAnalytics.logEvent("GameOver", params);


        playAgain = (Button) findViewById(R.id.playAgain);

        Intent intent = getIntent();
       String total_score = intent.getStringExtra("scoreValue");
        //canvas.drawText("SCORE :"+ total_score, 30, 80, score);
        totalScore = (TextView) findViewById(R.id.score);
        totalScore.setText(total_score);
        Log.d("ScoreValue",String.valueOf(total_score));


        playAgain.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Bundle param = new Bundle();
                mFirebaseAnalytics.logEvent("play_again",param);
                Intent intent = new Intent(GameOver.this, MainActivity.class);
                startActivity(intent);
            }
        });



        quit =(Button) findViewById(R.id.quit);
        quit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick (View view){
            Bundle param = new Bundle();
            mFirebaseAnalytics.logEvent("quit",param);
            finish();
            System.exit(0);
            }
        });
    }




}





