package com.example.pavel.moneyflow.views;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

public class RoundChart extends View {

    private int value;

    private RectF sector;
    private Rect textBounds;

    private Canvas temp;
    private Bitmap bitmap;
    private Paint paintPrimarySector;
    private Paint paintAscendSector;
    private Paint paintTransparent;
    private Paint paintText;

    private ValueAnimator valueAnimator;
    private int animationDuration = 2000;
    private int animateFromValue;
    private int animateToValue;

    private ColorPalette colorPalette;

    public RoundChart(Context context, AttributeSet attrs) {
        super(context, attrs);

        sector = new RectF();
        textBounds = new Rect();

        paintPrimarySector = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintAscendSector = new Paint(Paint.ANTI_ALIAS_FLAG);

        paintTransparent = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintTransparent.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        paintTransparent.setColor(Color.TRANSPARENT);

//        String s = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "background");
//        int i = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "background", 0);

        setBackgroundResource(0); // игнориум значения заданные в android:background

        colorPalette = ColorPalette.makeDefault();

        paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintText.setTypeface(Typeface.DEFAULT_BOLD);

        valueAnimator = new ValueAnimator();
    }

    public void setValues (int value){
        animateFromValue = this.value;
        animateToValue = value;
        this.value = value;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        /*
        widthMeasureSpec & heightMeasureSpec содержит в себе 2 значания: 1) доступные для элемента
        размеры, 2) мод, который может принимать 3 константных значения EXACTLY, AT_MOST, UNSPECIFIED -
        указывает на требования контейнера к ребенку (match_parent, wrap_content, etc)
         */

        int size;
        int defaultSize = getContext().getResources().getDisplayMetrics().widthPixels / 2;

        int measureSize = Math.min(MeasureSpec.getSize(widthMeasureSpec),
                MeasureSpec.getSize(heightMeasureSpec));

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == heightMode){
            size = widthMode == MeasureSpec.EXACTLY ? measureSize : defaultSize;
        } else if (widthMode == MeasureSpec.EXACTLY || heightMode == MeasureSpec.EXACTLY){
            size = Math.min(defaultSize, measureSize);
        } else {
            size = defaultSize;
        }

        setMeasuredDimension(size, size);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

         //при рисование и операциях, связаных с layout объекты создавать нельзя,
        // в конструкторе мы еще не знаем размеры вью, поэтому здесь

        bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(Color.TRANSPARENT);
        temp = new Canvas(bitmap);
    }

    // переопределяем методы установки фона,
    // убираем вызов метода суперкласса, чтобы запретить менять фон

    @Override
    public void setBackgroundColor(int color) {}

    @Override
    public void setBackgroundResource(int resid) {}

    @Override
    public void setBackground(Drawable background) {}

    //--------------------------------------------------------------

    @Override
    protected void onDraw(Canvas canvas) {
        int diameter = getMeasuredHeight();

        sector.set(0, 0, diameter, diameter);

        float center = diameter/2;
        String percentText = value + "%";

        paintPrimarySector.setColor(colorPalette.getPrimaryColor(value));
        paintAscendSector.setColor(colorPalette.getAscendColor(value));

        paintText.setTextSize(diameter/5);
        paintText.getTextBounds(percentText, 0, percentText.length(), textBounds);

        float angle;
        angle = value * 3.6f;
        while (angle > 360) {
            angle -= 360;
        }
        /*
        канва temp содержит битмеп как подложку, на котором мы рисуем, чтобы после нарисовать
        этот же битмеп на основной канве. делается это только потому, что если на основной канве
        внутренний круг зарисовать используя Color.TRANSPARENT и PorterDuff.Mode.CLEAR (иначе мы вообще
        не получим прозрачности), а затем поместить наш вью в ScrollView - получим серый бекграунд.
         */

        temp.drawArc(sector, 0, 360, true, paintPrimarySector);
        temp.drawArc(sector, 270, angle, true, paintAscendSector);
        temp.drawCircle(diameter/2, diameter/2, diameter/3, paintTransparent);
        temp.drawText(percentText, center - textBounds.exactCenterX(),
                center - textBounds.exactCenterY(), paintText);

        canvas.drawBitmap(bitmap, 0, 0, null);
    }

    public void beginChartAnimation() {
        valueAnimator.setValues(PropertyValuesHolder.ofInt("values", animateFromValue, animateToValue));
        valueAnimator.setDuration(animationDuration);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                value = (int) animation.getAnimatedValue("values");
                invalidate();
            }
        });
        valueAnimator.start();
    }

    public static class ColorPalette {

        private ArrayList<Integer> primaryColors;
        private ArrayList<Integer> ascendColors;
        private ArrayList<Integer> thresholds;

        private int colorAboveThreshold;
        private boolean isColorAboveSet;

        public static ColorPalette makeDefault(){
            int colorDarkGreen = Color.parseColor("#219B31");
            int colorLightGreen = Color.parseColor("#44FF5D");
            int colorDarkRed = Color.parseColor("#BB0B0E");
            int colorLightRed = Color.parseColor("#FF4450");
            ColorPalette colorPalette = new ColorPalette(colorDarkGreen, colorLightGreen, 100);
            colorPalette.addThreshold(colorDarkRed, colorLightRed, 200);
            return colorPalette;
        }

        public ColorPalette(int primaryColorAboveThreshold, int ascendColorAboveThreshold, int threshold){
            primaryColors = new ArrayList<>();
            ascendColors = new ArrayList<>();
            thresholds = new ArrayList<>();

            primaryColors.add(primaryColorAboveThreshold);
            ascendColors.add(ascendColorAboveThreshold);
            thresholds.add(threshold);

            isColorAboveSet = false;
            colorAboveThreshold = primaryColorAboveThreshold;
        }

        public void addThreshold(int primaryColorBelowThreshold, int ascendColorBelowThreshold, int threshold){
            primaryColors.add(primaryColorBelowThreshold);
            ascendColors.add(ascendColorBelowThreshold);
            thresholds.add(threshold);

            if (!isColorAboveSet) colorAboveThreshold = primaryColorBelowThreshold;
        }

        public void setColorAboveThreshold(int color) {
            this.colorAboveThreshold = color;
        }

        public int getPrimaryColor(int value){
            for (int i = 0; i < thresholds.size(); i++){
                if (value < thresholds.get(i)){
                    return primaryColors.get(i);
                }
            }
            return colorAboveThreshold;
        }

        public int getAscendColor(int value){
            for (int i = 0; i < thresholds.size(); i++){
                if (value < thresholds.get(i)){
                    return ascendColors.get(i);
                }
            }
            return colorAboveThreshold;
        }
    }
}
