package com.aswifter.material.mathviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.aswifter.material.R;

/**
 * Created by Administrator on 2017/1/9.
 */

public class SinView extends View implements
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener{
    private Paint picPaint;
    private int timecount=0;
    private int centx;
    private int centy;
    private int[] colors={R.color.google_red,R.color.google_yellow,R.color.google_green, R.color.google_blue};
    private GestureDetectorCompat mDetector;
    private String DEBUG_TAG="SinView";

    public SinView(Context C){
        super(C);
        mDetector = new GestureDetectorCompat(C,this);
        mDetector.setOnDoubleTapListener(this);
        // Other setup code you want here
    }

    public SinView(Context C, AttributeSet attribs){
        super(C, attribs);
        mDetector = new GestureDetectorCompat(C,this);
        mDetector.setOnDoubleTapListener(this);
        // Other setup code you want here
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);
        switch(action) {
            case (MotionEvent.ACTION_DOWN) :
                break;
            case (MotionEvent.ACTION_MOVE) :
                break;
            case (MotionEvent.ACTION_UP) :
                break;
            case (MotionEvent.ACTION_CANCEL) :
                break;
            case (MotionEvent.ACTION_OUTSIDE) :
                break;
        }
        return super.onTouchEvent(event);
    }




    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(picPaint==null)
            picPaint = new Paint();
        centy=(getTop()+getBottom())/2;
        centx=(getRight()+getLeft())/2;
        int colorindex=((int)timecount/50)%4;
        picPaint.setColor(getResources().getColor(colors[colorindex]));
        //double dy=0.03*timecount*timecount;
        //canvas.drawCircle(centx,(float)(getTop()+dy),(float)15,picPaint);
        double dy=400*Math.sin(2*Math.PI*timecount/200);
        canvas.drawCircle(centx,(float)(centy+dy),(float)15,picPaint);
        timecount=(timecount+1)%200;
        postInvalidateDelayed(20);
    }

    @Override
    public boolean onDown(MotionEvent event) {
        Log.d(DEBUG_TAG,"onDown: " + event.toString());
        return true;
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,
                           float velocityX, float velocityY) {
        Log.d(DEBUG_TAG, "onFling: " + event1.toString()+event2.toString());
        return true;
    }

    @Override
    public void onLongPress(MotionEvent event) {
        Log.d(DEBUG_TAG, "onLongPress: " + event.toString());
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        Log.d(DEBUG_TAG, "onScroll: " + e1.toString()+e2.toString());
        return true;
    }

    @Override
    public void onShowPress(MotionEvent event) {
        Log.d(DEBUG_TAG, "onShowPress: " + event.toString());
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        Log.d(DEBUG_TAG, "onSingleTapUp: " + event.toString());
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent event) {
        Log.d(DEBUG_TAG, "onDoubleTap: " + event.toString());
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent event) {
        Log.d(DEBUG_TAG, "onDoubleTapEvent: " + event.toString());
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        Log.d(DEBUG_TAG, "onSingleTapConfirmed: " + event.toString());
        return true;
    }

}
