package com.example.pavel.moneyflow.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.pavel.moneyflow.R;
import com.example.pavel.moneyflow.util.Prefs;

import java.util.Random;

public class RoundChart extends View {

    int plan = 1;
    int current = 1;

    float percent = 0.0f;

    int diameter;
    RectF rectF;

    public RoundChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        rectF = new RectF(0, 0, diameter, diameter);

        int colorPrimary;
        int colorAscend;

        float angle = (current *360)/plan;

        Paint paintPrimary = new Paint();
        paintPrimary.setAntiAlias(true);

        Paint paintAscend = new Paint();
        paintAscend.setAntiAlias(true);

        if (angle < 360){
            colorPrimary = getResources().getColor(R.color.darkGreen);
            colorAscend = getResources().getColor(R.color.lightGreen);
        } else {
            angle -= 360;
            colorPrimary = getResources().getColor(R.color.darkRed);
            colorAscend = getResources().getColor(R.color.lightRed);
        }


        paintPrimary.setColor(colorPrimary);
        paintAscend.setColor(colorAscend);


        canvas.drawArc(rectF, 0, 360, true, paintPrimary);
        canvas.drawArc(rectF, 270, angle, true, paintAscend);

        RectF innerCircle = new RectF(0, 0, diameter/2, diameter/2);

        Paint whitePaint = new Paint();
        whitePaint.setColor(Color.WHITE);
        whitePaint.setAntiAlias(true);
        canvas.drawCircle(diameter/2, diameter/2, diameter/3, whitePaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        diameter = ((int) getMeasuredHeight()/2);
        setMeasuredDimension(diameter, diameter);
//        Log.d(Prefs.LOG_TAG, "Width - " +  + ", height - " + height);
    }

    public void setValues(int plan, int current){
        this.plan = plan;
        this.current = current;
        percent = (current * 100)/plan;
        draw(new Canvas());
    }
}
