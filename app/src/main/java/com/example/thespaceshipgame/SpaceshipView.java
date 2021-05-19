package com.example.thespaceshipgame;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

public class SpaceshipView extends View
{
    private Bitmap spaceship[] = new Bitmap[2];
    private int spaceshipX = 10;
    private int spaceshipY;
    private int spaceshipSpeed;
    private int canvasWidth, canvasHeight;
    public int scoreValue, lifeCounter;

    private int charge1_X, charge1_Y, charge1_Speed = 11;
    private Paint charge1_Paint = new Paint();

    private int charge2_X, charge2_Y, charge2_Speed= 9;
    private Paint charge2_Paint = new Paint();

    private int alien1_X, alien1_Y, alien1_Speed = 9;
    private Paint alien1_Paint = new Paint();

    private boolean touch = false;
    private FirebaseAnalytics mFirebaseAnalytics;


    private Bitmap backgroudImage;
    private Paint score = new Paint();
    private Bitmap life[] = new Bitmap[3];
    private Bitmap charge;
    private Bitmap charge2;
    private Bitmap alien;




    public SpaceshipView(Context context) {
        super(context);
        spaceship[0] = BitmapFactory.decodeResource(getResources(),R.drawable.clipart_spaceship1_resized);
        spaceship[1] = BitmapFactory.decodeResource(getResources(),R.drawable.clipart_spaceship2_resized);

        backgroudImage = BitmapFactory.decodeResource(getResources(), R.drawable.space_bg5);

        charge = BitmapFactory.decodeResource(getResources(), R.drawable.charge2);
        charge2 = BitmapFactory.decodeResource(getResources(), R.drawable.charge2shine);
        alien = BitmapFactory.decodeResource(getResources(),R.drawable.alien);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());

        //charge1_Paint.setColor(Color.YELLOW);
        //charge1_Paint.setAntiAlias(false);

        score.setColor(Color.WHITE);
        score.setTextSize(90);
        score.setTypeface(Typeface.DEFAULT_BOLD);
        score.setAntiAlias(true);


        life[0] = BitmapFactory.decodeResource(getResources(), R.drawable.hearts_resized);
        life[1] = BitmapFactory.decodeResource(getResources(), R.drawable.heart_grey_resized);

        spaceshipY = 500;
        scoreValue = 0;
        lifeCounter = 3;


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvasWidth= canvas.getWidth();
        canvasHeight= canvas.getHeight();
        canvas.drawBitmap(backgroudImage, 0, 0, null);
        //canvas.drawBitmap(spaceship,0,0, null);

        int minPositionY = spaceship[0].getHeight();
        int maxPositionY = canvasHeight - spaceship[0].getHeight()*3;
        spaceshipY = spaceshipY + spaceshipSpeed;

        if(spaceshipY < minPositionY){
            spaceshipY = minPositionY;
        }
        if(spaceshipY > maxPositionY){
            spaceshipY = maxPositionY;
        }

        spaceshipSpeed = spaceshipSpeed + 2;

        if(touch){
            canvas.drawBitmap(spaceship[0], spaceshipX, spaceshipY, null);
            touch = false;

        }
        else{
            canvas.drawBitmap(spaceship[1], spaceshipX, spaceshipY, null);
        }
        //----
        charge1_X = charge1_X - charge1_Speed;

        if (hit(charge1_X, charge1_Y))
        {
            scoreValue = scoreValue + 100;
            charge1_X = -100;
            Toast.makeText(this.getContext(), "+100",Toast.LENGTH_SHORT).show();

        }

        if (charge1_X < 0)
        {
            charge1_X = canvasWidth+21;
            charge1_Y = (int) Math.floor(Math.random()* (maxPositionY-minPositionY))+ minPositionY;
        }
        canvas.drawBitmap(charge, charge1_X, charge1_Y, null);

        //---------------

        charge2_X = charge2_X - charge2_Speed;

        if (hit(charge2_X, charge2_Y))
        {
            scoreValue = scoreValue + 50;
            charge2_X = -100;
            Toast.makeText(this.getContext(), "+50",Toast.LENGTH_SHORT).show();

        }

        if (charge2_X < 0)
        {
            charge2_X = canvasWidth+21;
            charge2_Y = (int) Math.floor(Math.random()* (maxPositionY-minPositionY))+ minPositionY;
        }
        canvas.drawBitmap(charge2, charge2_X, charge2_Y, null);


        //canvas.drawCircle(charge1_X, charge1_Y, 30, charge1_Paint);


        //------
        alien1_X = alien1_X - alien1_Speed;

        if (hit(alien1_X, alien1_Y))
        {
            alien1_X = -100;
            lifeCounter--;
            if(lifeCounter == 0) {
                String scoreValueString = String.valueOf(scoreValue);

                //Total_Scores are evaluated
                if(scoreValue > 200)
                {
                    mFirebaseAnalytics.setUserProperty("Score_200", scoreValueString);
                }

                if(scoreValue > 500)
                {
                    mFirebaseAnalytics.setUserProperty("Score_500", scoreValueString);
                }

                Intent intent = new Intent(getContext(), GameOver.class);
                intent.putExtra("scoreValue",scoreValueString);
                Log.d("ScoreValue",scoreValueString);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getContext().startActivity(intent);
            }
        }



        if (alien1_X < 0)
        {
            alien1_X = canvasWidth+21;
            alien1_Y = (int) Math.floor(Math.random()* (maxPositionY-minPositionY))+ minPositionY;
        }
        canvas.drawBitmap(alien, alien1_X, alien1_Y, null);

        canvas.drawText("SCORE :"+ scoreValue, 30, 80, score);


        for( int i = 0; i < 3; i++){
            int x = (int) (580 + life[0].getWidth() * 1.5 * i);
            int y = 30;

            if(i < lifeCounter){
                canvas.drawBitmap(life[0],x, y, null);
            }
            else {
                canvas.drawBitmap(life[1],x, y, null);
            }

        }




       // canvas.drawBitmap(life[0],880, 30, null);
       // canvas.drawBitmap(life[0],980, 30, null);
       // canvas.drawBitmap(life[0],1080, 30, null);

    }

    public boolean hit(int x, int y)
    {
        if(spaceshipX < x && x < (spaceshipX + spaceship[0].getWidth()) && spaceshipY < y && y < (spaceshipY + spaceship[0].getHeight()))
        {
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_UP){
            touch = true;
            spaceshipSpeed = -35;
        }
        return true;
    }

}
