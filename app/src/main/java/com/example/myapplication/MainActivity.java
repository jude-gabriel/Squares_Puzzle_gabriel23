package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    Timer theTimer;
    TimerTask theTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        theTimer = new Timer();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create the board
        BoardSurfaceView boardSurfaceView = (BoardSurfaceView) findViewById(R.id.boardSurfaceView);

        //Set the board to respond to the On Touch Listener
        boardSurfaceView.setOnTouchListener(boardSurfaceView);

        Button resetButton = (Button) findViewById(R.id.resetButton);
        resetButton.setOnClickListener(boardSurfaceView);

        theTask = new TimerTask() {
            @Override
            public void run() {
                makeTimedClick(boardSurfaceView);
            }
        };

        theTimer.schedule(theTask, 1000, 10000);


    }

    public void makeTimedClick(BoardSurfaceView bsv){
        float[] xyVals = bsv.findXY();
        boolean performClick;

        performClick = bsv.performContextClick(xyVals[0], xyVals[1]);

    }
}
