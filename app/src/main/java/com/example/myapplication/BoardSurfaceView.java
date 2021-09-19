package com.example.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

public class BoardSurfaceView extends SurfaceView implements View.OnTouchListener, View.OnClickListener{
    private Card[][] cardArray;
    private int[] numsArray;
    int xCard = -1;
    int yCard = -1;
    float xLoc = -1;
    float yLoc = -1;
    float origX = -1;
    float origY = -1;
    float origBottomX = -1;
    float origBottomY = -1;
    float blankCardTopX = -1;
    float blankCardTopY = -1;
    float blankCardBottomX = -1;
    float blankCardBottomY = -1;


    Canvas myCanvas;


    public BoardSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        numsArray = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
        cardArray = createArray();
        Log.d("Make Array", "here");


        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas){
        myCanvas = canvas;
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

        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                if(cardArray[i][j].getCardNum() == 16){
                    blankCardTopX = cardArray[i][j].getXVal();
                    blankCardTopY = cardArray[i][j].getYVal();
                    blankCardBottomX = cardArray[i][j].getBottomX();
                    blankCardBottomY = cardArray[i][j].getBottomY();
                }
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
        int[] thisCard;


        switch(event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                Log.d("ACTION DOWN", "ACTION DOWN was hit");
                xLoc = event.getX();
                yLoc =  event.getY();
                thisCard = findCard(xLoc, yLoc);
                xCard = thisCard[0];
                yCard = thisCard[1];
                if(xCard == -1){
                    return false;
                }
                if(cardArray[xCard][yCard].getCardNum() == 16){
                    return false;
                }
                origX = cardArray[xCard][yCard].getXVal();
                origY = cardArray[xCard][yCard].getYVal();
                origBottomX = cardArray[xCard][yCard].getBottomX();
                origBottomY = cardArray[xCard][yCard].getBottomY();
                break;

            case MotionEvent.ACTION_MOVE:
                Log.d("ACTION MOVE", "ACTION MOVE was hit");

                cardArray[xCard][yCard].setXVal(event.getX() - 100);
                cardArray[xCard][yCard].setYVal(event.getY() - 100);
                cardArray[xCard][yCard].setBottomX(event.getX() + 200 - 100);
                cardArray[xCard][yCard].setBottomY(event.getY() + 200 - 100);
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                Log.d("ACTION UP", "ACTION UP was hit");
                if(arraySwap(xCard, yCard) == true){
                    invalidate();
                    return true;
                }

                else{
                    cardArray[xCard][yCard].setXVal(origX);
                    cardArray[xCard][yCard].setYVal(origY);
                    cardArray[xCard][yCard].setBottomX(origX + 200);
                    cardArray[xCard][yCard].setBottomY(origY + 200);
                    invalidate();
                }
        }
        return true;









//        //Existing Code
//        float xLoc = event.getX();
//        float yLoc =  event.getY();
//
//
//        if(arraySwap(xLoc, yLoc) == true){
//            invalidate();
//            return true;
//        }
//        return false;
    }




    //New Method
    public int[] findCard(float x, float y){
        int[] cardLoc = {-1, -1};
        for(int i = 0; i < cardArray.length; i++){
            for(int j = 0; j < cardArray.length; j++){
                if((x >= cardArray[i][j].getXVal()) && (x <= cardArray[i][j].getBottomX())){
                    if((y >= cardArray[i][j].getYVal()) && (y <= cardArray[i][j].getBottomY())){
                        cardLoc[0] = i;
                        cardLoc[1] = j;
                    }
                }
            }
        }

        return cardLoc;
    }






















    public boolean arraySwap (int i, int j){
        Card cardToSwitch = null;
        int iNum = -1;
        int jNum = -1;



//        for (int i = 0; i < cardArray.length; i++){
//            for(int j = 0; j < cardArray.length; j++){
//                if((xClick >= cardArray[i][j].getXVal()) && (xClick <= cardArray[i][j].getBottomX())){
//                    if((yClick >= cardArray[i][j].getYVal()) && (yClick <= cardArray[i][j].getBottomY())){
//                        float x = cardArray[i][j].getXVal();
//                        float y = cardArray[i][j].getYVal();
//                        float bottomX = cardArray[i][j].getBottomX();
//                        float bottomY = cardArray[i][j].getBottomY();
//
//                        cardToSwitch = cardArray[i][j];
//                        iNum = i;
//                        jNum = j;
//                        break;
//                    }
//                }
//            }
//        }

        cardToSwitch = cardArray[i][j];
        iNum = i;
        jNum = j;

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

        if(origX == -1){
            origX = secondCard.getXVal();
            origY = secondCard.getYVal();
            origBottomX = secondCard.getBottomX();
            origBottomY = secondCard.getBottomY();
            blankCardTopX = firstCard.getXVal();
            blankCardTopY = firstCard.getYVal();
            blankCardBottomX = firstCard.getBottomX();
            blankCardBottomY = firstCard.getBottomY();
        }

        secondCard.setXVal(blankCardTopX);
        secondCard.setYVal(blankCardTopY);
        secondCard.setBottomX(blankCardBottomX);
        secondCard.setBottomY(blankCardBottomY);

        firstCard.setXVal(origX);
        firstCard.setYVal(origY);
        firstCard.setBottomX(origBottomX);
        firstCard.setBottomY(origBottomY);

        origX = -1;
        origY = -1;
        origBottomX = -1;
        origBottomY = -1;
        blankCardTopX = -1;
        blankCardTopY = -1;
        blankCardBottomX = -1;
        blankCardBottomY = -1;

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
        if((x == cardArray.length - 1) && (y == 0)){
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
                location[1] = cardArray[x + 1][y].getYVal();
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





//    @Override
//    public boolean onDrag(View v, DragEvent event) {
//        float xStart = 0;
//        float yStart = 0;
//        float xEnd = 0;
//        float yEnd = 0;
//        float cardXTop = 0;
//        float cardXBottom = 0;
//        float cardYTop = 0;
//        float cardYBottom = 0;
//
//        if(event.getAction() == DragEvent.ACTION_DRAG_STARTED){
//            xStart = event.getX();
//            yStart = event.getY();
//        }
//
//        if (event.getAction() == DragEvent.ACTION_DRAG_ENTERED){
//            xEnd = event.getX();
//            yEnd = event.getY();
//        }
//
//        for(int i = 0; i < cardArray.length; i++){
//            for(int j = 0; j < cardArray.length; j++){
//                if(cardArray[i][j].getCardNum() == 16){
//                    cardXTop = cardArray[i][j].getXVal();
//                    cardXBottom = cardArray[i][j].getBottomX();
//                    cardYTop = cardArray[i][j].getYVal();
//                    cardYBottom = cardArray[i][j].getBottomY();
//                }
//            }
//        }
//
//        if((xEnd >= cardXTop) && (xEnd <= cardXBottom)){
//            if((yEnd >= cardYTop) && (yEnd <= cardYBottom)){
//                if(arraySwap(xStart, yStart) == true){
//                    invalidate();
//                    return true;
//                }
//            }
//        }
//
//        return false;
//    }
}
