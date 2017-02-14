package com.aswifter.material.mathviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.aswifter.material.R;

/**
 * Created by Administrator on 2017/1/10.
 */

public class ClockView extends View {

    private Paint picPaint;
    private int timecount=0;
    private int centx;
    private int centy;
    private int[] colors={R.color.google_red,R.color.google_yellow,R.color.google_green, R.color.google_blue};

    public ClockView(Context C) {
        super(C);
    }
    public ClockView(Context C, AttributeSet attribs) {
        super(C,attribs);
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
        double dy=0.03*timecount*timecount;
        canvas.drawCircle(centx,(float)(getTop()+dy),(float)15,picPaint);
        timecount=(timecount+1)%200;
        postInvalidateDelayed(20);
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
}
