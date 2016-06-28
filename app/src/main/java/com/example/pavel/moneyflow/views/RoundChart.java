package com.example.pavel.moneyflow.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import com.example.pavel.moneyflow.R;

public class RoundChart extends View {

    //DRAWING OBJECT
    private RectF sector;
    private Rect textBounds;

    private Paint paintPrimarySector;
    private Paint paintAscendSector;
    private Paint paintInnerCircle;
    private Paint paintText;

    int plan = 1;
    int current = 1;
    int percent = 0;
    int diameter;

    float angleValue;

    public RoundChart(Context context, AttributeSet attrs) {
        super(context, attrs);

        textBounds = new Rect();
        sector = new RectF();

        paintPrimarySector = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintAscendSector = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintInnerCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int colorPrimary;
        int colorAscend;

        float angle = angleValue;

        if (angle < 360){
            colorPrimary = getResources().getColor(R.color.darkGreen);
            colorAscend = getResources().getColor(R.color.lightGreen);
        } else {
            angle -= 360;
            colorPrimary = getResources().getColor(R.color.darkRed);
            colorAscend = getResources().getColor(R.color.lightRed);
        }

        paintPrimarySector.setColor(colorPrimary);
        paintAscendSector.setColor(colorAscend);

        sector.set(0, 0, diameter, diameter);

        canvas.drawArc(sector, 0, 360, true, paintPrimarySector);
        canvas.drawArc(sector, 270, angle, true, paintAscendSector);

        paintInnerCircle.setColor(Color.WHITE);
        canvas.drawCircle(diameter/2, diameter/2, diameter/3, paintInnerCircle);

        //----------------------TEXT-----------------------

        String percentText = percent + "%";

        paintText.setColor(Color.BLACK);
        paintText.setTextSize(diameter/5);
        paintText.setTypeface(Typeface.DEFAULT_BOLD);
        paintText.getTextBounds(percentText, 0, percentText.length(), textBounds);

        float center = diameter/2;

        canvas.drawText(percentText, center - textBounds.exactCenterX(),
                center - textBounds.exactCenterY(), paintText);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        diameter = ((int) getMeasuredHeight()/2);
        setMeasuredDimension(diameter, diameter);
    }

    public void setValues(int plan, int current){
        this.plan = plan;
        this.current = current;
        setValues((current * 100)/plan);
        draw(new Canvas());
    }

    public void setValues(int percent) {
        this.percent = percent;
        angleValue = percent * 3.6f;
    }
}
