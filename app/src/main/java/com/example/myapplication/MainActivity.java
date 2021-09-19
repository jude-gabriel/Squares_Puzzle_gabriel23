package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private CountDownTimer theTimer;
    private BoardSurfaceView boardSurfaceView;
    private TextView xyLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create the board
        boardSurfaceView = (BoardSurfaceView) findViewById(R.id.boardSurfaceView);

        //Set the board to respond to the On Touch Listener
        boardSurfaceView.setOnTouchListener(boardSurfaceView);


        Button resetButton = (Button) findViewById(R.id.resetButton);
        resetButton.setOnClickListener(boardSurfaceView);

        TextView timerText = (TextView) findViewById(R.id.timerCounter);
        xyLocations = (TextView) findViewById(R.id.xyLocations);

        theTimer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(timerText != null){
                    timerText.setText("" + (int)(millisUntilFinished / 1000));
                }
            }

            @Override
            public void onFinish() {
                makeTimedClick();
                xyLocations.setText("Hit onFinished");
                theTimer.start();
            }
        };

        theTimer.start();

    }

    public void makeTimedClick(){
        if(boardSurfaceView != null){
            float[] xyVals = boardSurfaceView.findXY();
            int[] ijVals = boardSurfaceView.findCard(xyVals[0], xyVals[1]);

            if(boardSurfaceView.arraySwap(ijVals[0], ijVals[1]) == true){
                boardSurfaceView.invalidate();
            }

            xyLocations.setText("" + xyVals[0] + ", " + xyVals[1]);

            int duration = Toast.LENGTH_LONG;
            Context context = getApplicationContext();
            CharSequence chars = "At " + xyVals[0] + ", " + xyVals[1];

            Toast.makeText(context, chars, duration).show();
        }

    }
}
