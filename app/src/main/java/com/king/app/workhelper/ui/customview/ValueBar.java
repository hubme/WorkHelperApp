package com.king.app.workhelper.ui.customview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;

import com.king.app.workhelper.R;

/**
 * View that displays a colored bar as a ratio of an integer value to a maximum value.  There is also a circle
 * indicator and text that displays at the position of the current value, as well as a label (positioned above the bar).
 * The maximum value is displayed to the right of the bar.
 * <p>See {@link R.styleable#ValueBar ValueBar Attributes}</p>
 */
public class ValueBar extends View {

    private int maxValue = 100;
    private int currentValue = 0;

    private boolean animated;
    private float valueToDraw; //for use during an animation
    private long animationDuration = 4000L; //4 second default.  this is the time it takes to traverse the entire bar
    private ValueAnimator animation;

    //instance variables for storing xml attributes
    private int barHeight;
    private int circleRadius;
    private int spaceAfterBar;
    private int circleTextSize;
    private int maxValueTextSize;
    private int labelTextSize;
    private int labelTextColor;
    private int currentValueTextColor;
    private int circleTextColor;
    private int baseColor;
    private int fillColor;

    private String labelText;

    //objects used for drawing
    private Paint labelPaint;
    private Paint maxValuePaint;
    private Paint barBasePaint;
    private Paint barFillPaint;
    private Paint circlePaint;
    private Paint currentValuePaint;


    public ValueBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setSaveEnabled(true);


        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ValueBar, 0, 0);
        barHeight = ta.getDimensionPixelSize(R.styleable.ValueBar_barHeight, 0);
        circleRadius = ta.getDimensionPixelSize(R.styleable.ValueBar_circleRadius, 0);
        spaceAfterBar = ta.getDimensionPixelSize(R.styleable.ValueBar_spaceAfterBar, 0);
        circleTextSize = ta.getDimensionPixelSize(R.styleable.ValueBar_circleTextSize, 0);
        maxValueTextSize = ta.getDimensionPixelSize(R.styleable.ValueBar_maxValueTextSize, 0);
        labelTextSize = ta.getDimensionPixelSize(R.styleable.ValueBar_labelTextSize, 0);
        labelTextColor = ta.getColor(R.styleable.ValueBar_labelTextColor, Color.BLACK);
        currentValueTextColor = ta.getColor(R.styleable.ValueBar_maxValueTextColor, Color.BLACK);
        circleTextColor = ta.getColor(R.styleable.ValueBar_circleTextColor, Color.BLACK);
        baseColor = ta.getColor(R.styleable.ValueBar_baseColor, Color.BLACK);
        fillColor = ta.getColor(R.styleable.ValueBar_fillColor, Color.BLACK);
        labelText = ta.getString(R.styleable.ValueBar_labelText);
        ta.recycle();

        labelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        labelPaint.setTextSize(labelTextSize);
        labelPaint.setColor(labelTextColor);
        labelPaint.setTextAlign(Paint.Align.LEFT);
        labelPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        maxValuePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        maxValuePaint.setTextSize(maxValueTextSize);
        maxValuePaint.setColor(currentValueTextColor);
        maxValuePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        maxValuePaint.setTextAlign(Paint.Align.RIGHT);

        barBasePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        barBasePaint.setColor(baseColor);

        barFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        barFillPaint.setColor(fillColor);

        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(fillColor);

        currentValuePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        currentValuePaint.setTextSize(circleTextSize);
        currentValuePaint.setColor(circleTextColor);
        currentValuePaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawLabel(canvas);
        drawBar(canvas);
        drawMaxValue(canvas);
    }

    private int measureWidth(int measureSpec) {
        int size = getPaddingLeft() + getPaddingRight();
        Rect bounds = new Rect();
        labelPaint.getTextBounds(labelText, 0, labelText.length(), bounds);
        size += bounds.width();

        bounds = new Rect();
        String maxValueText = String.valueOf(maxValue);
        maxValuePaint.getTextBounds(maxValueText, 0, maxValueText.length(), bounds);
        size += bounds.width();
        return resolveSizeAndState(size, measureSpec, 0);
    }

    private int measureHeight(int measureSpec) {
        int size = getPaddingTop() + getPaddingBottom() + (int) labelPaint.getFontSpacing();
        float maxValueTextSpacing = maxValuePaint.getFontSpacing();
        size += Math.max(maxValueTextSpacing, Math.max(barHeight, circleRadius * 2));
        return resolveSizeAndState(size, measureSpec, 0);
    }

    private void drawLabel(Canvas canvas) {
        float x = getPaddingLeft();
        //the y coordinate marks the bottom of the text, so we need to factor in the height
        Rect bounds = new Rect();
        labelPaint.getTextBounds(labelText, 0, labelText.length(), bounds);
        float y = getPaddingTop() + bounds.height();
        canvas.drawText(labelText, x, y, labelPaint);
    }

    private void drawBar(Canvas canvas) {
        String maxValueString = String.valueOf(maxValue);
        Rect maxValueRect = new Rect();
        maxValuePaint.getTextBounds(maxValueString, 0, maxValueString.length(), maxValueRect);
        float barLength = getWidth() - getPaddingRight() - getPaddingLeft() - circleRadius - maxValueRect.width() - spaceAfterBar;

        float barCenter = getBarCenter();

        float halfBarHeight = barHeight / 2;
        float top = barCenter - halfBarHeight;
        float bottom = barCenter + halfBarHeight;
        float left = getPaddingLeft();
        float right = getPaddingLeft() + barLength;
        RectF rect = new RectF(left, top, right, bottom);
        canvas.drawRoundRect(rect, halfBarHeight, halfBarHeight, barBasePaint);


        float percentFilled = valueToDraw / (float) maxValue;
        float fillLength = barLength * percentFilled;
        float fillPosition = left + fillLength;
        RectF fillRect = new RectF(left, top, fillPosition, bottom);
        canvas.drawRoundRect(fillRect, halfBarHeight, halfBarHeight, barFillPaint);

        canvas.drawCircle(fillPosition, barCenter, circleRadius, circlePaint);

        Rect bounds = new Rect();
        String valueString = String.valueOf(Math.round(valueToDraw));
        currentValuePaint.getTextBounds(valueString, 0, valueString.length(), bounds);
        float y = barCenter + (bounds.height() / 2);
        canvas.drawText(valueString, fillPosition, y, currentValuePaint);
    }

    private void drawMaxValue(Canvas canvas) {
        String maxValue = String.valueOf(this.maxValue);
        Rect maxValueRect = new Rect();
        maxValuePaint.getTextBounds(maxValue, 0, maxValue.length(), maxValueRect);

        float xPos = getWidth() - getPaddingRight();
        float yPos = getBarCenter() + maxValueRect.height() / 2;
        canvas.drawText(maxValue, xPos, yPos, maxValuePaint);
    }


    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    /**
     * Sets the value of the bar.  If the passed in value exceeds the maximum, the value
     * will be set to the maximum.
     */
    public void setValue(int newValue) {
        int previousValue = currentValue;
        if (newValue < 0) {
            currentValue = 0;
        } else if (newValue > maxValue) {
            currentValue = maxValue;
        } else {
            currentValue = newValue;
        }

        if (animated) {
            runAnim(previousValue);
        } else {
            valueToDraw = currentValue;
        }

        invalidate();
    }

    private void runAnim(int previousValue) {
        if (animation == null) {
            animation = ValueAnimator.ofFloat();
        } else {
            animation.cancel();
        }
        animation.setFloatValues(previousValue, currentValue);
        int changeInValue = Math.abs(currentValue - previousValue);
        long durationToUse = (long) (animationDuration * ((float) changeInValue / (float) maxValue));
        animation.setDuration(durationToUse);
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                valueToDraw = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });

        animation.start();
    }

    public int getValue() {
        return currentValue;
    }

    private float getBarCenter() {
        //position the bar slightly below the middle of the drawable area
        float barCenter = (getHeight() - getPaddingTop() - getPaddingBottom()) / 2; //this is the center
        barCenter += getPaddingTop() + 0.1f * getHeight(); //move it down a bit
        return barCenter;
    }

    /**
     * Indicate whether or not the graph should use animation when the value is changed.  If true, the
     * indicator will "slide" to the new value.
     * <p>See {@link #setAnimationDuration(long)}</p>
     *
     * @param animated whether or not the graph should use animation
     */
    public void setAnimated(boolean animated) {
        this.animated = animated;
    }

    /**
     * Set the time (in milliseconds) that the animation should use to traverse the entire graph.  The actual
     * value used will depend on how much tha value changes.
     *
     * @param animationDuration duration of the animation
     */
    public void setAnimationDuration(long animationDuration) {
        this.animationDuration = animationDuration;
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.value = currentValue;
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        currentValue = ss.value;
        valueToDraw = currentValue; //set valueToDraw directly to prevent re-animation
    }

    private static class SavedState extends BaseSavedState {
        int value;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            value = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(value);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
