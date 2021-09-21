package com.example.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class BoardSurfaceView extends SurfaceView implements View.OnTouchListener, View.OnClickListener{
    private Card[][] cardArray;
    private int[] numsArray;
    private int xCard;
    private int yCard;
    private float xLoc;
    private float yLoc;
    private float origX;
    private float origY;
    private float origBottomX;
    private float origBottomY;
    private float blankCardTopX;
    private float blankCardTopY;
    private float blankCardBottomX;
    private float blankCardBottomY;
    private boolean userClick;
    private boolean didDrag;


    /**
     * Constructor for the BoardSurfaceView. Initializes all variables
     * with class-wide scope
     *
     * @param context       Global information of the current state
     * @param attrs         Attributes associated in the XML file
     */
    public BoardSurfaceView(Context context, AttributeSet attrs) {
        //Call super to initialize the parameters
        super(context, attrs);

        //Create the random number array and then create the array of cards
        numsArray = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
        cardArray = createArray();

        //Initialize the xy index values for the card the user drags
        xCard = -1;
        yCard = -1;

        //Initialize the xy coordinates of where the user begins the drag
        xLoc = -1;
        yLoc = -1;

        //Initialize the coordinates that create a rectangle. These will be populated by
        //the set of xy coordinates contained by the card the user drags
        origX = -1;
        origY = -1;
        origBottomX = -1;
        origBottomY = -1;

        //Initialize the coordinates that create a rectangle. These will be populated by
        //the set of xy coordinates contained by the blank card
        blankCardTopX = -1;
        blankCardTopY = -1;
        blankCardBottomX = -1;
        blankCardBottomY = -1;

        //Set userClick to true so that the clock initially does not start timing
        userClick = true;

        //Set didDrag to false so that if a user clicks a square instead of drags
        //nothing will happen
        didDrag = false;

        //Set setWillNotDraw to false so we can draw on the surface view
        setWillNotDraw(false);
    }


    /**
     * Draws the array of cards on the Board's surface view
     *
     * @param canvas        The canvas to draw on
     */
    @Override
    protected void onDraw(Canvas canvas){



        //Check if a card is in the correct spot. If it is set the color to green.
        //Otherwise set the color to red
        for(int i = 0; i < cardArray.length; i ++){
            for(int j = 0; j < cardArray.length; j++){
                if(cardArray[i][j].getCardNum() == (i * 4) + j + 1){
                    cardArray[i][j].setColor(0, 255, 0);
                }
            }
        }

        //Iterate through the card array and call the draw method for each card
        //to draw it on the board's surface view
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                cardArray[i][j].draw(canvas);
            }
        }

        //Find the rectangular coordinates of the blank square.
        //This is done here so that we know the location of the blank card after every move
        //and so that it is stored in a constant for easier access
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


    /**
     * Creates an array of cards each having a random number between 1-15 with
     * no repeats.
     *
     * @return a 2D array of cards
     */
    public Card[][] createArray() {
        //Reset the random number array
        numsArray = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};

        //Initialize a 4x4 array of cards and an integer to hold the random number
        Card[][] cArr = new Card[4][4];
        int randNum;

        //Iterate through the array of cards and assign each card a random number
        //and location in the surface view
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                randNum = randNums();
                cArr[i][j] = new Card((j * 350) + 250,(i * 350) + 20, randNum);

            }
        }

        //Return the 2D array of cards
        return cArr;
    }


    /**
     * Generates a random number by between 1-15 with no repeats.
     *
     * @return the non-repeated random number
     */
    public int randNums() {
        //Create a random number between 0-16
        int randNum = (int)(Math.random() * 16);

        //Go to the random number's index in the number array. Pull the random number and
        //set the index value to zero. Return the random number
        if(numsArray[randNum] != 0){
            numsArray[randNum] = 0;
            return randNum + 1;
        }

        //If the index value is zero then it was already chosen. Recursively try again
        else{
             return randNum = randNums();
        }

    }


    /**
     * Collects the information pertaining to a user dragging a card.
     * Checks if the user drags it, display the drag and card swap, and reset the clock
     *
     * @param v         The Board surface view
     * @param event     The drag event
     * @return
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //Initialize an array to store the indices of the card being dragged
        //and set userClick to true to reset the clock
        int[] thisCard;
        userClick = true;

        //Switch case for the action to find analyze which state of the drag we are in
        switch(event.getActionMasked()) {

            //Case 1: User initially clicks card
            case MotionEvent.ACTION_DOWN:
                //Get the xy coordinates of the click and use it to find the
                //indices of the clicked card. Store these indices in new variables
                xLoc = event.getX();
                yLoc = event.getY();
                thisCard = findCard(xLoc, yLoc);
                xCard = thisCard[0];
                yCard = thisCard[1];

                //Error check to see if the user grabbed an invalid card or no card at all
                if (xCard == -1) {
                    return false;
                }
                if (cardArray[xCard][yCard].getCardNum() == 16) {
                    return false;
                }

                //Populate the coordinate variables of the original card
                origX = cardArray[xCard][yCard].getXVal();
                origY = cardArray[xCard][yCard].getYVal();
                origBottomX = cardArray[xCard][yCard].getBottomX();
                origBottomY = cardArray[xCard][yCard].getBottomY();
                userClick = true;
                break;

            //Case 2: User is dragging
            case MotionEvent.ACTION_MOVE:
                //Update the coordinates of the card to map the users drag
                //Give an offset value so that the card is dragged from the middle
                cardArray[xCard][yCard].setXVal(event.getX() - 100);
                cardArray[xCard][yCard].setYVal(event.getY() - 100);
                cardArray[xCard][yCard].setBottomX(event.getX() + 200 - 100);
                cardArray[xCard][yCard].setBottomY(event.getY() + 200 - 100);

                //Update userClick to reset clock and didDrag so that just a click is invalid
                userClick = true;
                didDrag = true;


                //Change the cards color to blue to show it is being dragged and call invalidate
                //to reflect the dragging motion and change in color
                cardArray[xCard][yCard].setColor(0, 0, 255);
                invalidate();
                break;

            //Case 3: The user releases the drag
            case MotionEvent.ACTION_UP:
                //Set the card color back to red
                cardArray[xCard][yCard].setColor(255, 0, 0);

                //Error check to ensure the user dragged and did not just click
                //Then check if user dropped the card within the empty card
                if(didDrag == true) {
                    if ((event.getX() >= blankCardTopX) && (event.getX() <= blankCardBottomX) &&
                            (event.getY() >= blankCardTopY) &&
                            (event.getY() <= blankCardBottomY)) {

                        //Check if the card and the empty card are neighbors
                        if (arraySwap(xCard, yCard) == true) {

                            //Update userClick adn didDrag to false to continue the clock
                            //and reset didDrag for the next drag
                            userClick = false;
                            didDrag = false;

                            //Call invalidate to show the swap change
                            invalidate();
                            return true;
                        }

                        //If arraySwap is not true then the card was not a neighbor of the blank
                        //card. Send the card back to it's original location and reset the
                        //original coordinates for the next iteration. Call invalidate to reflect
                        //the changes
                        else {
                            cardArray[xCard][yCard].setXVal(origX);
                            cardArray[xCard][yCard].setYVal(origY);
                            cardArray[xCard][yCard].setBottomX(origX + 200);
                            cardArray[xCard][yCard].setBottomY(origY + 200);
                            origX = -1;
                            origY = -1;
                            origBottomX = -1;
                            origBottomY = -1;
                            blankCardTopX = -1;
                            blankCardTopY = -1;
                            blankCardBottomX = -1;
                            blankCardBottomY = -1;
                            userClick = false;
                            invalidate();
                        }
                    }

                    //If the user did not drop the card into the empty cards bounds then
                    //send the card back to its original location
                    else {
                        cardArray[xCard][yCard].setXVal(origX);
                        cardArray[xCard][yCard].setYVal(origY);
                        cardArray[xCard][yCard].setBottomX(origX + 200);
                        cardArray[xCard][yCard].setBottomY(origY + 200);
                        origX = -1;
                        origY = -1;
                        origBottomX = -1;
                        origBottomY = -1;
                        blankCardTopX = -1;
                        blankCardTopY = -1;
                        blankCardBottomX = -1;
                        blankCardBottomY = -1;
                        userClick = false;
                        invalidate();
                    }
                }

                /* MAY NOT BE NEEDED */
//                else{
//                    cardArray[xCard][yCard].setXVal(origX);
//                    cardArray[xCard][yCard].setYVal(origY);
//                    cardArray[xCard][yCard].setBottomX(origX + 200);
//                    cardArray[xCard][yCard].setBottomY(origY + 200);
//                    origX = -1;
//                    origY = -1;
//                    origBottomX = -1;
//                    origBottomY = -1;
//                    blankCardTopX = -1;
//                    blankCardTopY = -1;
//                    blankCardBottomX = -1;
//                    blankCardBottomY = -1;
//                    userClick = false;
//                    invalidate();
//                }
                break;
        }

        //Return true to recursively hit switch cases
        return true;
    }


    /**
     * Find the card contained located at the x and y coordinates the user clicked
     *
     * @param x     The x coordinate of the users click
     * @param y     The y coordinate of the users click
     *
     * @return an integer array containing the index values in the 2D array of the
     *          card that was clicked
     */
    public int[] findCard(float x, float y){
        //Initalize the card's indices at zero
        int[] cardLoc = {-1, -1};

        //Iterate through the card array and find which card contains the xy coordinates
        //of the users click
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

        //Return the array containing the indices of the card clicked
        return cardLoc;
    }


    /**
     * Checks if the dragged card is neighbors with the blank card and swaps their locations
     * if they are neighbors
     *
     * @param i     The row index of the dragged card
     * @param j     The column index of the dragged card
     *
     * @return true if the card was swapped with the blank card, return false otherwise
     */
    public boolean arraySwap (int i, int j){
        //Initialize a temporary card for switching values and index values for row and column
        Card cardToSwitch = null;
        int iNum = -1;
        int jNum = -1;

        //Populate the temporary card and indices
        cardToSwitch = cardArray[i][j];
        iNum = i;
        jNum = j;

        //Error check to see if the temporary card was populated
        if(cardToSwitch == null){
            return false;
        }

        /* For each case, check the surrounnding neighbors of the card. If one of the neighbors is
            the blank carduse the temporary values to switch the cards values, then call
            swapLocation to swap the location values of the cards
         */
        else{
            //Case 1 it is not a corner or an edge, check if it is a neighbor
            if((iNum > 0) && (iNum < cardArray.length - 1) && (jNum > 0) &&
                    (jNum < cardArray.length - 1)){
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

            //Case 2: it is on the top edge but not a corner, check if it is a neighbor
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

            //Case 3: it is on the bottom edge but not a corner, check if it is a neighbor
            else if((iNum == cardArray.length - 1) && (jNum > 0) &&
                    (jNum < cardArray.length - 1)){
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

            //Case 4: it is on the left edge but not a corner, check if it is a neighbor
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

            //Case 5: It is on the right edge but not a corner, check if it is a neighbor
            else  if((iNum > 0) && (iNum < cardArray.length - 1) &&
                    (jNum == cardArray.length - 1)){
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


            //Case 6: it is the top left corner, check if it is a neighbor
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

            //Case 7: It is the top right corner, check if it is a neighbor
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

            //Case 8: It is the bottom left corner, check if it is a neighbor
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

            //Case 9: It is the bottom right corner, check if it is a neighbor
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

            //Case 10: The card is not a neighbor of the blank card, return false
            else{
                return false;
            }

        }

        //This case should never be hit, if it is, something is wrong
        return false;
    }

    /**
     * Swap the locations of the fist and secoind card.
     *
     * @param firstCard
     * @param secondCard
     */
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


    /**
     * Resets the board and the clock when the user clicks the reset button
     *
     * @param v     The BoardSurfaceView that is being drawn on
     */
    @Override
    public void onClick(View v) {
        //Set user click to true so the clock reset and reorder the card array
        userClick = true;
        cardArray = createArray();

        //Call invalidate so that the board is redrawn
        invalidate();
    }


    /**
     * Find the location of a card neighboring the blank card. This allows the computer
     * player to make it's random selection
     *
     * @return a float array containg the xy coordinates of the neighboring card
     */
    public float[] findXY(){
        //Initialize the xy coordinates and the array
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

        /* For each of the following cases, find the neighbors of the
           blank card. Select only neighbors that are in the incorrect spot so that
           the computer player does not mess up the game. If both are in the correct spot
           choose one to move
         */
        //Case 1: it is in the top left corner, find a neighbor
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

        //Case 2: it is in the top right corner, find a neighbor
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

        //Case 3: It is in the bottom left corner, find a neighbor
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

        //Case 4: It is in the bottom right corner, find a neighbor
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

        //Case 5: It is in the top row, find a neighbor
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

        //Case 6: It is on the bottom row, find a neighbor
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

        //Case 7: It is on the left column, find a neighbor
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

        //Case 8: It is on the right column, find a neighbor
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

        //Case 9: It is not an edge or corner, find a neighbor
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

        //Return the array containing the xy coordinates of the blank square's neighbor
        return location;
    }


    /**
     * Finds and returns the card number of the card at the specified indices
     *
     * @param i     The row index of the specified card
     * @param j     The column index of the specified card
     *
     * @return      The number string of the specified card
     */
    public String getCardNum(int i, int j){
        //Return the number string of the card at ij
        return cardArray[i][j].getNumString();
    }


    /**
     * Returns the truth value of userClick, allowing for access to see if the user
     * clicked the square or not. This truth value is accessed to reset the clock
     *
     * @return true if the user made a move, false if they didn't
     */
    public boolean didUserClick(){
        return userClick;
    }
}
