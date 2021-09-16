package com.example.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

public class BoardSurfaceView extends SurfaceView implements View.OnTouchListener, View.OnClickListener{

    private Card[][] cardArray;
    private int[] numsArray;


    public BoardSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        numsArray = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
        cardArray = createArray();
        Log.d("Make Array", "here");


        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas){
        for(int i = 0; i < cardArray.length; i ++){
            for(int j = 0; j < cardArray.length; j++){
                if(cardArray[i][j].getCardNum() == (i * 4) + j + 1){
                    cardArray[i][j].setColor(0, 255, 0);
                }
                else{
                    cardArray[i][j].setColor(255, 0, 0);
                }
            }
        }
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                cardArray[i][j].draw(canvas);
            }
        }
    }

    public Card[][] createArray() {
        numsArray = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
        Card[][] cArr = new Card[4][4];
        int randNum;
        int xLoc = 0;
        int yLoc = 0;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                randNum = randNums();
                cArr[i][j] = new Card((j * 350) + 250,(i * 350) + 20, randNum);

            }
        }
        return cArr;
    }


    public int randNums() {
        int randNum = (int)(Math.random() * 16);
        if(numsArray[randNum] != 0){
            numsArray[randNum] = 0;
            return randNum + 1;
        }
        else{
             return randNum = randNums();
        }

    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float xLoc = event.getX();
        float yLoc =  event.getY();

        if(arraySwap(xLoc, yLoc) == true){
            invalidate();
            return true;
        }
        return false;
    }






    public boolean arraySwap(float xClick, float yClick){
        Card cardToSwitch = null;
        int iNum = -1;
        int jNum = -1;



        for (int i = 0; i < cardArray.length; i++){
            for(int j = 0; j < cardArray.length; j++){
                if((xClick > cardArray[i][j].getXVal()) && (xClick < cardArray[i][j].getBottomX())){
                    if((yClick > cardArray[i][j].getYVal()) && (yClick < cardArray[i][j].getBottomY())){
                        float x = cardArray[i][j].getXVal();
                        float y = cardArray[i][j].getYVal();
                        float bottomX = cardArray[i][j].getBottomX();
                        float bottomY = cardArray[i][j].getBottomY();

                        cardToSwitch = cardArray[i][j];
                        iNum = i;
                        jNum = j;
                        break;
                    }
                }
            }
        }
        if(cardToSwitch == null){
            return false;
        }
        else{

            //Case 1 it is not a corner or an edge
            if((iNum > 0) && (iNum < cardArray.length - 1) && (jNum > 0) && (jNum < cardArray.length - 1)){
                if(cardArray[iNum][jNum - 1].getCardNum() == 16){
                    cardArray[iNum][jNum] = cardArray[iNum][jNum - 1];
                    cardArray[iNum][jNum - 1] = cardToSwitch;
                    swapLocation(cardArray[iNum][jNum], cardArray[iNum][jNum - 1]);
                    return true;
                }

                if(cardArray[iNum][jNum + 1].getCardNum() == 16){
                    cardArray[iNum][jNum] = cardArray[iNum][jNum + 1];
                    cardArray[iNum][jNum + 1] = cardToSwitch;
                    swapLocation(cardArray[iNum][jNum], cardArray[iNum][jNum + 1]);
                    return true;
                }

                if(cardArray[iNum + 1][jNum].getCardNum() == 16){
                    cardArray[iNum][jNum] = cardArray[iNum + 1][jNum];
                    cardArray[iNum + 1][jNum] = cardToSwitch;
                    swapLocation(cardArray[iNum][jNum], cardArray[iNum + 1][jNum]);
                    return true;
                }

                if(cardArray[iNum - 1][jNum].getCardNum() == 16){
                    cardArray[iNum][jNum] = cardArray[iNum - 1][jNum];
                    cardArray[iNum - 1][jNum] = cardToSwitch;
                    swapLocation(cardArray[iNum][jNum], cardArray[iNum - 1][jNum]);
                    return true;
                }
            }

            //Case 2: it is on the top edge but not a corner
            else if((iNum == 0) && (jNum > 0) && (jNum < cardArray.length - 1)){
                if(cardArray[iNum][jNum - 1].getCardNum() == 16){
                    cardArray[iNum][jNum] = cardArray[iNum][jNum - 1];
                    cardArray[iNum][jNum - 1] = cardToSwitch;
                    swapLocation(cardArray[iNum][jNum], cardArray[iNum][jNum - 1]);
                    return true;
                }
                if(cardArray[iNum][jNum + 1].getCardNum() == 16){
                    cardArray[iNum][jNum] = cardArray[iNum][jNum + 1];
                    cardArray[iNum][jNum + 1] = cardToSwitch;
                    swapLocation(cardArray[iNum][jNum], cardArray[iNum][jNum + 1]);
                    return true;
                }

                if(cardArray[iNum + 1][jNum].getCardNum() == 16){
                    cardArray[iNum][jNum] = cardArray[iNum + 1][jNum];
                    cardArray[iNum + 1][jNum] = cardToSwitch;
                    swapLocation(cardArray[iNum][jNum], cardArray[iNum + 1][jNum]);
                    return true;
                }
            }

            //Case 3: it is on the bottom edge but not a corner
            else if((iNum == cardArray.length - 1) && (jNum > 0) && (jNum < cardArray.length - 1)){
                if(cardArray[iNum][jNum - 1].getCardNum() == 16){
                    cardArray[iNum][jNum] = cardArray[iNum][jNum - 1];
                    cardArray[iNum][jNum - 1] = cardToSwitch;
                    swapLocation(cardArray[iNum][jNum], cardArray[iNum][jNum - 1]);
                    return true;
                }
                if(cardArray[iNum][jNum + 1].getCardNum() == 16){
                    cardArray[iNum][jNum] = cardArray[iNum][jNum + 1];
                    cardArray[iNum][jNum + 1] = cardToSwitch;
                    swapLocation(cardArray[iNum][jNum], cardArray[iNum][jNum + 1]);
                    return true;
                }

                if(cardArray[iNum - 1][jNum].getCardNum() == 16){
                    cardArray[iNum][jNum] = cardArray[iNum - 1][jNum];
                    cardArray[iNum - 1][jNum] = cardToSwitch;
                    swapLocation(cardArray[iNum][jNum], cardArray[iNum - 1][jNum]);
                    return true;
                }
            }

            //Case 4: it is on the left edge but not a corner
            else  if((iNum > 0) && (iNum < cardArray.length - 1) && (jNum == 0)){
                if(cardArray[iNum][jNum + 1].getCardNum() == 16){
                    cardArray[iNum][jNum] = cardArray[iNum][jNum + 1];
                    cardArray[iNum][jNum + 1] = cardToSwitch;
                    swapLocation(cardArray[iNum][jNum], cardArray[iNum][jNum + 1]);
                    return true;
                }
                if(cardArray[iNum - 1][jNum].getCardNum() == 16){
                    cardArray[iNum][jNum] = cardArray[iNum - 1][jNum];
                    cardArray[iNum - 1][jNum] = cardToSwitch;
                    swapLocation(cardArray[iNum][jNum], cardArray[iNum - 1][jNum]);
                    return true;
                }
                if(cardArray[iNum + 1][jNum].getCardNum() == 16){
                    cardArray[iNum][jNum] = cardArray[iNum + 1][jNum];
                    cardArray[iNum + 1][jNum] = cardToSwitch;
                    swapLocation(cardArray[iNum][jNum], cardArray[iNum + 1][jNum]);
                    return true;
                }
            }

            //Case 5: It is on the right edge but not a corner
            else  if((iNum > 0) && (iNum < cardArray.length - 1) && (jNum == cardArray.length - 1)){
                if(cardArray[iNum][jNum - 1].getCardNum() == 16){
                    cardArray[iNum][jNum] = cardArray[iNum][jNum - 1];
                    cardArray[iNum][jNum - 1] = cardToSwitch;
                    swapLocation(cardArray[iNum][jNum], cardArray[iNum][jNum - 1]);
                    return true;
                }
                if(cardArray[iNum - 1][jNum].getCardNum() == 16){
                    cardArray[iNum][jNum] = cardArray[iNum - 1][jNum];
                    cardArray[iNum - 1][jNum] = cardToSwitch;
                    swapLocation(cardArray[iNum][jNum], cardArray[iNum - 1][jNum]);
                    return true;
                }
                if(cardArray[iNum + 1][jNum].getCardNum() == 16){
                    cardArray[iNum][jNum] = cardArray[iNum + 1][jNum];
                    cardArray[iNum + 1][jNum] = cardToSwitch;
                    swapLocation(cardArray[iNum][jNum], cardArray[iNum + 1][jNum]);
                    return true;
                }
            }


            //Case 6: it is the top left corner
            else if((iNum == 0) && (jNum == 0)){
                if(cardArray[iNum + 1][jNum].getCardNum() == 16){
                    cardArray[iNum][jNum] = cardArray[iNum + 1][jNum];
                    cardArray[iNum + 1][jNum] = cardToSwitch;
                    swapLocation(cardArray[iNum][jNum], cardArray[iNum + 1][jNum]);
                    return true;
                }
                if(cardArray[iNum][jNum + 1].getCardNum() == 16){
                    cardArray[iNum][jNum] = cardArray[iNum][jNum + 1];
                    cardArray[iNum][jNum + 1] = cardToSwitch;
                    swapLocation(cardArray[iNum][jNum], cardArray[iNum][jNum + 1]);
                    return true;
                }
            }

            //Case 7: It is the top right corner
            else if((iNum == 0) && (jNum == cardArray.length - 1)){
                if(cardArray[iNum][jNum - 1].getCardNum() == 16){
                    cardArray[iNum][jNum] = cardArray[iNum][jNum - 1];
                    cardArray[iNum][jNum - 1] = cardToSwitch;
                    swapLocation(cardArray[iNum][jNum], cardArray[iNum][jNum - 1]);
                    return true;
                }

                if(cardArray[iNum + 1][jNum].getCardNum() == 16){
                    cardArray[iNum][jNum] = cardArray[iNum + 1][jNum];
                    cardArray[iNum + 1][jNum] = cardToSwitch;
                    swapLocation(cardArray[iNum][jNum], cardArray[iNum + 1][jNum]);
                    return true;
                }
            }

            //Case 8: It is the bottom left corner
            else if((iNum == cardArray.length - 1) && (jNum == 0)){
                if(cardArray[iNum -1][jNum].getCardNum() == 16){
                    cardArray[iNum][jNum] = cardArray[iNum - 1][jNum];
                    cardArray[iNum - 1][jNum] = cardToSwitch;
                    swapLocation(cardArray[iNum][jNum], cardArray[iNum - 1][jNum]);
                    return true;
                }
                if(cardArray[iNum][jNum + 1].getCardNum() == 16){
                    cardArray[iNum][jNum] = cardArray[iNum][jNum + 1];
                    cardArray[iNum][jNum + 1] = cardToSwitch;
                    swapLocation(cardArray[iNum][jNum], cardArray[iNum][jNum +1]);
                    return true;
                }
            }

            //Case 9: It is the bottom right corner
            else if((iNum == cardArray.length - 1) && (jNum == cardArray.length - 1)){
                if(cardArray[iNum][jNum - 1].getCardNum() == 16){
                    cardArray[iNum][jNum] = cardArray[iNum][jNum - 1];
                    cardArray[iNum][jNum - 1] = cardToSwitch;
                    swapLocation(cardArray[iNum][jNum], cardArray[iNum][jNum - 1]);
                    return true;
                }
                if(cardArray[iNum - 1][jNum].getCardNum() == 16){
                    cardArray[iNum][jNum] = cardArray[iNum - 1][jNum];
                    cardArray[iNum - 1][jNum] = cardToSwitch;
                    swapLocation(cardArray[iNum][jNum], cardArray[iNum - 1][jNum]);
                    return true;
                }
            }

            //Case 10: This case should never be hit
            else{
                return false;
            }

        }

        //This case should never be hit
        return false;
    }

    public void swapLocation(Card firstCard, Card secondCard){
        Card tempCard = new Card(firstCard.getXVal(), firstCard.getYVal(), 0);

        firstCard.setXVal(secondCard.getXVal());
        firstCard.setYVal(secondCard.getYVal());
        firstCard.setBottomX(secondCard.getBottomX());
        firstCard.setBottomY(secondCard.getBottomY());

        secondCard.setXVal(tempCard.getXVal());
        secondCard.setYVal(tempCard.getYVal());
        secondCard.setBottomX(tempCard.getBottomX());
        secondCard.setBottomY(tempCard.getBottomY());

        return;

    }

    @Override
    public void onClick(View v) {
        cardArray = createArray();
        invalidate();
    }



    public float[] findXY(){
        int x = -1;
        int y = -1;
        float[] location = new float[2];

        //Find the blank card
        for(int i = 0; i < cardArray.length; i++){
            for(int j = 0; j < cardArray.length; j++){
                if(cardArray[i][j].getCardNum() == 16){
                    x = i;
                    y = j;
                }
            }
        }

        //Case 1: it is in the top left corner
        if((x == 0) && (y == 0)){
            if(cardArray[x+1][y].getColor() == 255){
                location[0] = cardArray[x + 1][y].getXVal();
                location[1] = cardArray[x + 1][y].getYVal();
            }
            else{
                location[0] = cardArray[x][y + 1].getXVal();
                location[1] = cardArray[x][y + 1].getYVal();
            }
        }

        //Case 2: it is in the top right corner
        if((x == 0) && (y == cardArray.length - 1)){
            if(cardArray[x + 1][y].getColor() == 255){
                location[0] = cardArray[x + 1][y].getXVal();
                location[1] = cardArray[x + 1][y].getYVal();
            }
            else{
                location[0] = cardArray[x][y - 1].getXVal();
                location[1] = cardArray[x][y - 1].getYVal();
            }
        }

        //Case 3: It is in the bottom left corner
        if((x == cardArray.length) && (y == 0)){
            if(cardArray[x - 1][y].getColor() == 255){
                location[0] = cardArray[x - 1][y].getXVal();
                location[1] = cardArray[x - 1][y].getYVal();
            }
            else{
                location[0] = cardArray[x][y + 1].getXVal();
                location[1] = cardArray[x][y + 1].getYVal();
            }
        }

        //Case 4: It is in the bottom right corner
        if((x == cardArray.length - 1) && (y == cardArray.length - 1)){
            if(cardArray[x - 1][y].getColor() == 255){
                location[0] = cardArray[x - 1][y].getXVal();
                location[1] = cardArray[x - 1][y].getYVal();
            }
            else{
                location[0] = cardArray[x][y - 1].getXVal();
                location[1] = cardArray[x][y - 1].getYVal();
            }
        }

        //Case 5: It is in the top row
        if((x == 0) && (y > 0) && ( y < cardArray.length - 1)){
            if(cardArray[x][y + 1].getColor() == 255){
                location[0] = cardArray[x][y + 1].getXVal();
                location[1] = cardArray[x][y + 1].getYVal();
            }
            else if(cardArray[x][y - 1].getColor() == 255){
                location[0] = cardArray[x][y - 1].getXVal();
                location[1] = cardArray[x][y - 1].getYVal();
            }
            else{
                location[0] = cardArray[x + 1][y].getXVal();
                location[1] = cardArray[x + 1][y].getYVal();
            }
        }

        //Case 6: It is on the bottom row
        if((x == cardArray.length - 1) && (y > 0) && (y < cardArray.length- 1)){
            if (cardArray[x][y + 1].getColor() == 255){
                location[0] = cardArray[x][y + 1].getXVal();
                location[1] = cardArray[x][y + 1].getYVal();
            }
            else if (cardArray[x][y - 1].getColor() == 255){
                location[0] = cardArray[x][y - 1].getXVal();
                location[1] = cardArray[x][y - 1].getYVal();
            }
            else{
                location[0] = cardArray[x + 1][y].getXVal();
                location[1] = cardArray[x + 1][y].getYVal();
            }
        }

        //Case 7: It is on the left column
        if((x > 0) && (x < cardArray.length - 1) && (y == 0)){
            if(cardArray[x + 1][y].getColor() == 255){
                location[0] = cardArray[x + 1][y].getXVal();
                location[1] = cardArray[x + 1][y].getYVal();
            }
            else if(cardArray[x - 1][y].getColor() == 255){
                location[0] = cardArray[x - 1][y].getXVal();
                location[1] = cardArray[x - 1][y].getYVal();
            }
            else{
                location[0] = cardArray[x][y + 1].getXVal();
                location[1] = cardArray[x][y + 1].getYVal();
            }
        }

        //Case 8: It is on the right column
        if((x > 0) && (x < cardArray.length - 1) && (y == cardArray.length - 1)){
            if(cardArray[x + 1][y].getColor() == 255){
                location[0] = cardArray[x + 1][y].getXVal();
                location[1] = cardArray[x + 1][y].getYVal();
            }
            else if(cardArray[x - 1][y].getColor() == 255){
                location[0] = cardArray[x - 1][y].getXVal();
                location[1] = cardArray[x - 1][y].getYVal();
            }
            else{
                location[0] = cardArray[x][y - 1].getXVal();
                location[1] = cardArray[x][y - 1].getYVal();
            }
        }

        //Case 9: It is not an edge or corner
        if((x > 0) && (x < cardArray.length - 1) && (y > 0) && (y < cardArray.length -1)){
            if(cardArray[x + 1][y].getColor() == 255){
                location[0] = cardArray[x + 1][y].getXVal();
                location[1] = cardArray[x + 1][y].getXVal();
            }
            else if(cardArray[x - 1][y].getColor() == 255){
                location[0] = cardArray[x - 1][y].getXVal();
                location[1] = cardArray[x - 1][y].getYVal();
            }
            else if(cardArray[x][y + 1].getColor() == 255){
                location[0] = cardArray[x][y + 1].getXVal();
                location[1] = cardArray[x][y + 1].getYVal();
            }
            else{
                location[0] = cardArray[x][y - 1].getXVal();
                location[1] = cardArray[x][y - 1].getYVal();
            }
        }

        return location;
    }

}
