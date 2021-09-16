package com.example.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;

public class BoardSurfaceView extends SurfaceView implements View.OnTouchListener{
    private Card cardArray[][];
    private int numsArray[];


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
        int nums[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        Card cArr[][] = new Card[4][4];
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
}
