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
     * @param x     The x coordinate of the card
     * @param y     The y coordinate of the card
     * @param num   The card's number
     */
    public Card(float x, float y, int num){
        //set variables equal to constructor values
        xVal = x;
        yVal = y;
        cardNum = num;

        //Create the card color
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

        //Initialize the bottom coordinates and red intensity
        bottomY = 0;
        bottomX = 0;
        redNum = 0;
    }


    /**
     * Draws the card on the canvas
     *
     * @param canvas        The canvas to draw on
     */
    public void draw(Canvas canvas){
        //Set the coordinates for the bottom right corner of the rectangle
        bottomX = width + xVal;
        bottomY = height + yVal;

        //Draw the rectangle on the canvas
        canvas.drawRect(xVal, yVal, bottomX, bottomY, color);

        //Center text in teh rectangle and then draw on canvas
        float textRight = xVal + (width / 2) - 20;
        float textDown = yVal + (height / 2);
        canvas.drawText(numString, textRight, textDown, text);
    }


    /**
     * Setter for the x value
     *
     * @param x     The new float value of the coordinate
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
     * @param y     The new float value of the coordinate
     */
    public void setYVal(float y){
        yVal = y;
        return;
    }


    /**
     * Getter for the y value
     *
     * @return the upper y coordinate of the recatngle
     */
    public float getYVal(){
        return yVal;
    }


    /**
     * Getter for the card number
     *
     * @return the integer value of the card number
     */
    public int getCardNum(){
        return cardNum;
    }


    /**
     * Getter for the bottom x coordinate value
     *
     * @return the float value of the lower x coordinate
     */
    public float getBottomX(){
        return bottomX;
    }


    /**
     * Setter for the lower x coordinate value
     *
     * @param x     The new float value for the coordinate
     */
    public void setBottomX(float x){
        bottomX = x;
    }


    /**
     * Getter for the bottom y coordinate value
     *
     * @return the float value of the lower y coordinate
     */
    public float getBottomY(){
        return bottomY;
    }


    /**
     * Setter for the lower y coordinate
     *
     * @param y     The new float value for the coordinate
     */
    public void setBottomY(float y){
        bottomY = y;
    }


    /**
     * Setter for the cards color
     *
     * @param r     The red intensity of the color
     * @param g     The green intensity of the color
     * @param b     The blue intensity of the color
     */
    public void setColor(int r, int g, int b){
        //Initialize the cards color with the parameters
        color.setARGB(255, r, g, b);

        //Update redNum accordingly so we can see if the card is in the correct place
        if(r == 0){
            redNum = 0;
        }
        if(r == 255){
            redNum = 255;
        }
    }


    /**
     * Getter for the red intensity of the color
     *
     * @return an integer value representing the colors intensity
     */
    public int getColor(){
        return redNum;
    }


    /**
     * Getter for the string representation of the cards number
     *
     * @return a string containing the cards number
     */
    public String getNumString() {
        return numString;
    }
}
