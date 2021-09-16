package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create the board
        BoardSurfaceView boardSurfaceView = (BoardSurfaceView) findViewById(R.id.boardSurfaceView);

        boardSurfaceView.setOnTouchListener(boardSurfaceView);
    }
}