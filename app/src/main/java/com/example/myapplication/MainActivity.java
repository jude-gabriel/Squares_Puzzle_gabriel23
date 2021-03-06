/**
 * MainActivity.java
 *
 * This file contains the view set up so that the interactive board
 * displays on screen. This file also contains all necessary listeners
 * for user interaction
 *
 * Author: Jude Gabriel
 * Version: 9.23.2021
 *
 * Enhancements: A countdown timer where the computer makes a move every 10
 *                  seconds if the user does not
 *
 *                Draggable Squares
 *
 *                Invalid squares snap into their respective spots
 *
 *                Squares being dragged light up blue
 *
 *                The square being dragged is always on top of other objects so
 *                  it does not get hidden
 *
 *                Toast on screen so the user can see which move the computer made
 *
 * All code was written solely by me using Android API unless cited otherwise
 */


package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    //Create the board and timer
    private CountDownTimer theTimer;
    private BoardSurfaceView boardSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Call super and set the content view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create the board that the game will be displayed on
        //and set the board to respond to the onTouch listener
        boardSurfaceView = (BoardSurfaceView) findViewById(R.id.boardSurfaceView);
        boardSurfaceView.setOnTouchListener(boardSurfaceView);

        //Create the reset button and set it's onCLick listener
        Button resetButton = (Button) findViewById(R.id.resetButton);
        resetButton.setOnClickListener(boardSurfaceView);

        //Create a text view for the timer's countdown
        TextView timerText = (TextView) findViewById(R.id.timerCounter);


        /**
         * External Citation
         *  Date:       16 September 2021
         *  Problem:    Could not get a timer to be functional. i.e no timer would
         *              correctly count down and make a move
         *  Resource:   Dr. Tribelhorn
         *  Solution:   Use a countdown timer instead of a normal timer, thus eliminating
         *              the need for a timer task
         */

        //Create a countdown timer and set-up the onTick and onFinish methods
        theTimer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //Check if the timer text exists, if it does countdown on each tick
                //if the user clicks the screen, restart the timer
                if (timerText != null) {
                    timerText.setText("" + (int) (millisUntilFinished / 1000));
                    if(boardSurfaceView.didUserClick() == true){
                        theTimer.cancel();
                        theTimer.start();
                    }
                }
            }

            @Override
            public void onFinish() {
                //Check if the user clicked the screen. If not have the computer
                //player make a move. After, restart the timer
                if(boardSurfaceView.didUserClick() == false) {
                    makeTimedClick();
                }
                theTimer.start();
            }
        };

        //Start the timer to begin the countdown
        theTimer.start();
    }


    /**
     * Has the computer player make a move meaning the computer will try to find
     * a red card next to the empty card and then swap it for the empty card
     */
    public void makeTimedClick(){
        //Check if the board exists
        if(boardSurfaceView != null){

            //Find the coordinates of a nearby card and then use the coordinates to find
            //the indices and number of the card
            float[] xyVals = boardSurfaceView.findXY();
            int[] ijVals = boardSurfaceView.findCard(xyVals[0], xyVals[1]);
            String cardNum =  boardSurfaceView.getCardNum(ijVals[0], ijVals[1]);

            //Perform the card swap
            if(boardSurfaceView.arraySwap(ijVals[0], ijVals[1]) == true){
                boardSurfaceView.invalidate();
            }

            //Create a toast showing the user which card the computer swapped
            int duration = Toast.LENGTH_LONG;
            Context context = getApplicationContext();
            CharSequence chars = "Computer moved card " + cardNum;
            Toast.makeText(context, chars, duration).show();
        }

    }
}
