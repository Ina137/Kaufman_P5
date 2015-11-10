package com.slkaufma3gmail.tictactoe;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * This is a demo of a customview.  I'm ignoring some things like
 * all the xml attributes that could be set.
 */
public class DrawView extends View {

    Paint black, other;
    public int incr, size = 3;
    int mheight = 0, mwidth = 0;
    int leftside, rightside, boardwidth;
    public int maze[][];
    public Context myContext;
    public int player = 0;
    public int scoreX = 0;
    public int scoreO = 0;
    //public String winner;
    //public boolean drawn = false;
    public int moves = 0;

    public float[] colorX = {210, 89, 98}; //hue saturation value
    public float[] colorO = {122,56, 70}; //h ue saturation value

    public int hueX = Color.DKGRAY;
    public int hueO = Color.GRAY;
    public int paletteCanvas = Color.LTGRAY;


    /*
     * default constructor
     */
    public DrawView(Context context) {
        super(context);
        myContext = context;
        setup();
    }

    /*
     * should be the constructor that I'm calling!
     */
    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        myContext = context;
        setup();
    }

    /*
     * no clue on this constructor, it came with the example.
     */
    public DrawView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        myContext = context;
        setup();
    }

    /*
     * Setups all the default variables.  Since I'm not sure which of the constructors is
     * called, I'm using this method and have all of them call it.
     */
    public void setup() {

        black = new Paint();
        black.setColor(Color.BLACK); //defaults to black, but just making sure.
        black.setStyle(Paint.Style.STROKE);

        other = new Paint();  //I'll set  this one as I needed it.
        other.setStyle(Paint.Style.FILL);

        if (maze != null) {
            maze = null;
        }
        maze = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                maze[i][j] = Color.BLACK;
            }
        }

        if (mheight > 0) setsizes();  //in case not on screen.
    }

    /*
     * Setups up the default sizes for whatever the size of the screen is
     * so that the grid takes up most of the space with a margin around it.
     *
     */
    public void setsizes() {

        incr = mwidth / (size + 2);  //give a margin.
        leftside = incr - 1;
        rightside = incr * 9;
        boardwidth = incr * size;
        Log.i("setsizes", "incr is " + incr);
    }

	/*
     * clears the grid and then has the view redraw.
	 */

    void clearmaze() {

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                maze[i][j] = Color.BLACK;
            }
        }
        player = 0;
        moves = 0;
        invalidate();
    }

    /*
     * (non-Javadoc)
     * @see android.view.View#onDraw(android.graphics.Canvas)
     *
     * This is one of two methods I overrode.   The onDraw method
     * is the one that will draw everything for this view.
     * in this case, an 8x8 grid on the screen.
     *
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int x = incr;
        int y = incr;

        canvas.drawColor(paletteCanvas);

        //top line  not needed anymore.
        //canvas.drawLine(x, y -1, rightside, y-1, black);
        //draw squares across, then down
        for (int yi = 0; yi < size; yi++) {
            for (int xi = 0; xi < size; xi++) {

                canvas.drawRect(x, y, x + incr, y + incr, black);  //draw black box.
                if (maze[xi][yi] != Color.BLACK) {
                    Log.i("onDraw", "Drawing different color");
                    other.setColor(maze[xi][yi]);
                    canvas.drawRect(x + 1, y + 1, x + incr, y + incr, other);
                }

                x += incr; //move to next square across
            }
            x = incr;
            y += incr;
        }
        //bottom line  not needed anymore.
        //canvas.drawLine(leftside, rightside, rightside, rightside,black);
    }

    /*
     * used by the ontouch event to figure out which box (if any) was "touched"
     *
     */
    boolean where(int x, int y, int color, boolean recolor) {

        int cx = -1, cy = -1;


        if (y >= leftside && y < rightside &&
                x >= leftside && x < rightside) {
            y -= incr;
            x -= incr; //simplifies the math here.
            cx = (int) x / incr;
            cy = (int) y / incr;

            if (cx < size && cy < size) {
                if (maze[cx][cy] == Color.BLACK) {
                    maze[cx][cy] = color;
                    // moves++;
                } else if (recolor) {
                    maze[cx][cy] = color;
                }
            } else {
                Log.i("where", "Error in Where, cx=" + cx + " cy=" + cy);
            }
            return true;
        }
        reset();
        return false;
    }


    /*
     * (non-Javadoc)
     * @see android.view.View#onTouchEvent(android.view.MotionEvent)
     *
     * overrode this event to get all the touch events for this view.
     *
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        // Retrieve the new x and y touch positions
        int x = (int) event.getX();
        int y = (int) event.getY();

        if (y >= leftside && y < rightside &&
                x >= leftside && x < rightside) {
            if (action == 0) {
                switch (player) {
                    case 0:
                        //case MotionEvent.ACTION_DOWN:
                        //Log.i("onTouchEvent", "Action_down");

                        playerSwitch(x, y);

                        //Check to see if this is the winning touch
                        where(x, y, hueX, false);
                        invalidate();
                        if (winCondition(x, y, hueX)) {
                            moves = 0;
                            winner(hueX);

                        }
                        break;

                    //       case MotionEvent. .CLICK:  //down click.  color RED
                    //           where(x,y,Color.RED, true);
                    //  return super.touchEvent(message);
                    case 1:
                        //case MotionEvent.ACTION_MOVE:  //move not sure what to do yet.

                        playerSwitch(x, y);

                        //Check to see if this is the winning touch
                        where(x, y, hueO, false); //Color.YELLOW, false);
                        invalidate();
                        if (winCondition(x, y, hueO)) {
                            moves = 0;
                            winner(hueO);

                        }
                        break;
                }

                if (moves == size * size) {
                    moves = 0;
                    winner(-1);
                }
                /*if (player == 1) {
                    player--;
                } else
                    player++;*/

            }
            return true;
        }
        reset();
        return true;
        //return false;
    }

    /*
     * (non-Javadoc)
     * @see android.view.View#onMeasure(int, int)
     *
     *
     * using this to get the size of the view.
     * now... I should likely set the height if the
     * view is using wrapcontent instead matchparent
     * ie likely it comes back with a zero height or width!
     * and then nothing will be draw (no call to onDraw either).
     */
    @Override
    protected void onMeasure ( int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        Log.i("MSW", "" + getMeasuredWidth());
        Log.i("MSH", "" + getMeasuredHeight());
        mwidth = getMeasuredWidth();
        mheight = getMeasuredHeight();
        if (mheight > 0 && mwidth > mheight) {
            mwidth = mheight;
        } else if (mheight == 0) {
            mheight = mwidth;
        }
        setsizes();

        //setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
        setMeasuredDimension(mwidth, mheight);

    }

    boolean winCondition(int xLast, int yLast, int color) {
        yLast -= incr;
        xLast -= incr; //simplifies the math here.
        int x = (int) xLast / incr;
        int y = (int) yLast / incr;

        //Check for a win
//        if (y >= leftside && y < rightside &&
//                x >= leftside && x < rightside) {
        for (int i = 0; i < size; i++) {
            if (maze[x][i] != color)
                break;
            if (i == size - 1)
                return true;
        }
        for (int i = 0; i < size; i++) {
            if (maze[i][y] != color)
                break;
            if (i == size - 1)
                return true;
        }
        if (x == y) {
            for (int i = 0; i < size; i++) {
                if (maze[i][i] != color)
                    break;
                if (i == size - 1)
                    return true;
            }
        }
        for (int i = 0; i < size; i++) {
            if (maze[i][(size - 1) - i] != color)
                break;
            if (i == size - 1)
                return true;
        }
//        }
        //reset();
        return false;
    }

    void winner(int color) {

        String winner;

        if (color == hueX) {
            winner = "X is the Winner!";
            scoreX += 1;
        } else if (color == hueO) {
            winner = "O is the winner!";
            scoreO += 1;
        } else {
            winner = "It's a Tie!";
        }
        Dialog dialog = null;
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(myContext);
        builder.setMessage(winner + "\n Score X: " + scoreX + "\t Score O: " +
                scoreO + "\n\nPlay again?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        clearmaze();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                System.exit(0);
            }
        });
        dialog = builder.create();
        dialog.show();

    }


    boolean reset() {
    /*
     * If outside the lines, then popup a dialog and ask about reseting the board.
     * Interestingly, nothing is deprecated in the view
     */
        Dialog dialog = null;
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(myContext);
        builder.setMessage("Reset board?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                clearmaze();
                            }
                        }

                ).

                setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        }

                );
        dialog = builder.create();
        dialog.show();
        return false;
    }

    void playerSwitch(int xLast, int yLast) {
        yLast -= incr;
        xLast -= incr; //simplifies the math here.
        int x = (int) xLast / incr;
        int y = (int) yLast / incr;

        if (maze[x][y] == Color.BLACK) {
            moves++;
            if (player == 1) {
                player--;
            } else
                player++;

        }

    }

    boolean about()
    {
    Dialog dialog = null;
    AlertDialog.Builder builder;
    builder = new AlertDialog.Builder(myContext);
    builder.setMessage("About: \n\t The goal of Tic Tac Toe is to \n\t be the first player to get" +
            " three \n\t in a row on a 3x3 grid. \n\n X is Dark Gray. \n O is Light Gray. " +
            "\n X always goes first.")
            .setCancelable(false)
    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
        }
    });

    dialog = builder.create();
    dialog.show();
        return false;
    }
}