package com.example.myapplication;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Card {
    //Initialize instance variables
    private float xVal;
    private float yVal;
    private float bottomX;
    private float bottomY;
    private int cardNum;
    private Paint color;
    private Paint text;
    private final int height;
    private final int width;
    private String numString;
    private int redNum;

    /**
     * Constructor for the Card class
     *
     * @param x
     * @param y
     * @param num
     */
    public Card(float x, float y, int num){
        //set variables equal to constructor values
        xVal = x;
        yVal = y;
        cardNum = num;

        //Create the color
        color = new Paint();
        color.setARGB(255, 255, 0, 0);

        //Create the text
        text = new Paint();
        text.setTextSize(50);
        text.setARGB(255, 255, 255, 255);


        //Set the height and width
        height = 200;
        width = 200;

        //Set the text;
        if(num == 16){
            numString = "";
        }
        else {
            numString = Integer.toString(num);
        }

        cardNum = num;

        bottomY = 0;
        bottomX = 0;
        redNum = 0;

    }

    public void draw(Canvas canvas){
        bottomX = width + xVal;
        bottomY = height + yVal;
        canvas.drawRect(xVal, yVal, bottomX, bottomY, color);

        //Get text centered
        float textRight = xVal + (width / 2) - 20;
        float textDown = yVal + (height / 2);
        canvas.drawText(numString, textRight, textDown, text);
    }


    /**
     * Setter for the x value
     *
     * @param x
     */
    public void setXVal(float x){
        xVal = x;
        return;
    }


    /**
     * Getter for the x value
     *
     * @return
     */
    public float getXVal(){
        return xVal;
    }


    /**
     * Setter for the y value
     *
     * @param y
     */
    public void setYVal(float y){
        yVal = y;
        return;
    }


    /**
     * Getter for the y value
     *
     * @return
     */
    public float getYVal(){
        return yVal;
    }

    public int getCardNum(){
        return cardNum;
    }

    public float getBottomX(){
        return bottomX;
    }

    public void setBottomX(float x){
        bottomX = x;
    }

    public float getBottomY(){
        return bottomY;
    }
    public void setBottomY(float y){
        bottomY = y;
    }

    public void setColor(int r, int g, int b){
        color.setARGB(255, r, g, b);
        if(r == 0){
            redNum = 0;
        }
        if(r == 255){
            redNum = 255;
        }
    }

    public int getColor(){
        return redNum;
    }
}
