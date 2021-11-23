package com.king.applib.ui.customview;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.LayoutDirection;
import android.util.Size;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.text.TextUtilsCompat;

import java.util.Locale;

/**
 * 支持 drawableStart 和 drawableEnd 响应点击事件的 TextView。<br>
 * 不支持 drawableTop 和 drawableBottom，且不支持 paddingTop 和 paddingBottom。
 *
 * @author VanceKing
 * @since 2021/11/10
 */
public class DrawableClickableTextView extends androidx.appcompat.widget.AppCompatTextView {
    private static final int DRAWABLE_NONE = -1;
    private static final int DRAWABLE_START = 0;
    private static final int DRAWABLE_TOP = 1;
    private static final int DRAWABLE_END = 2;
    private static final int DRAWABLE_BOTTOM = 3;

    private int mClickedDrawable = DRAWABLE_NONE;

    private View.OnClickListener mStartListener;
    private View.OnClickListener mTopListener;
    private View.OnClickListener mEndListener;
    private View.OnClickListener mBottomListener;

    private Size mStartArea;
    private Size mTopArea;
    private Size mEndArea;
    private Size mBottomArea;

    private final Drawable[] mDrawables;

    private final Rect mTempRect = new Rect();

    public DrawableClickableTextView(@NonNull Context context) {
        this(context, null);
    }

    public DrawableClickableTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawableClickableTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mDrawables = getCompoundDrawablesRelative();
        //setCompoundDrawablesRelative(mDrawables[DRAWABLE_START], null, mDrawables[DRAWABLE_END], null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_CANCEL:
                mClickedDrawable = DRAWABLE_NONE;
                break;
            case MotionEvent.ACTION_DOWN:
                mClickedDrawable = DRAWABLE_NONE;
                //如果点击了 Drawable，就不会调用 performClick()
                return isDrawableStartClick(event) || isDrawableEndClick(event) || performClick();
            case MotionEvent.ACTION_UP:
                switch (mClickedDrawable) {
                    case DRAWABLE_START:
                        if (mStartListener != null) {
                            mStartListener.onClick(this);
                        }
                        return true;
                    case DRAWABLE_TOP:
                        if (mTopListener != null) {
                            mTopListener.onClick(this);
                        }
                        return true;
                    case DRAWABLE_END:
                        if (mEndListener != null) {
                            mEndListener.onClick(this);
                        }
                        return true;
                    case DRAWABLE_BOTTOM:
                        if (mBottomListener != null) {
                            mBottomListener.onClick(this);
                        }
                        return true;
                    default:
                        break;
                }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    private boolean isDrawableStartClick(MotionEvent event) {
        final Drawable drawable = mDrawables[DRAWABLE_START];
        if (drawable == null || mStartListener == null) {
            return false;
        }
        int widthExtra = 0;
        int heightExtra = 0;
        if (mStartArea != null) {
            widthExtra = mStartArea.getWidth();
            heightExtra = mStartArea.getHeight();
        }
        final Rect bounds = drawable.getBounds();

        if (LayoutDirection.LTR == TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault())) {
            mTempRect.set(getPaddingStart() - widthExtra,
                    getHeight() / 2 - bounds.height() / 2 - heightExtra,
                    getPaddingStart() + bounds.width() + widthExtra,
                    getHeight() / 2 + bounds.height() / 2 + heightExtra);
        } else {
            mTempRect.set(getWidth() - getPaddingStart() - bounds.width() - widthExtra,
                    getHeight() / 2 - bounds.height() / 2 - heightExtra,
                    getWidth() - getPaddingStart() + widthExtra,
                    getHeight() / 2 + bounds.height() / 2 + heightExtra);
        }

        boolean contains = mTempRect.contains((int) event.getX(), (int) event.getY());
        if (contains) {
            mClickedDrawable = DRAWABLE_START;
        }
        return contains;
    }

    private boolean isDrawableTopClick(MotionEvent event) {
        Drawable drawable = mDrawables[DRAWABLE_TOP];
        if (drawable == null || mTopListener == null) {
            return false;
        }

        boolean contains = mTempRect.contains((int) event.getX(), (int) event.getY());
        if (contains) {
            mClickedDrawable = DRAWABLE_TOP;
        }
        return contains;
    }

    private boolean isDrawableEndClick(MotionEvent event) {
        final Drawable drawable = mDrawables[DRAWABLE_END];
        if (drawable == null || mEndListener == null) {
            return false;
        }

        int widthExtra = 0;
        int heightExtra = 0;
        if (mEndArea != null) {
            widthExtra = mEndArea.getWidth();
            heightExtra = mEndArea.getHeight();
        }
        final Rect bounds = drawable.getBounds();

        if (LayoutDirection.LTR == TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault())) {
            mTempRect.set(getWidth() - getPaddingEnd() - bounds.width() - widthExtra,
                    getHeight() / 2 - bounds.height() / 2 - heightExtra,
                    getWidth() - getPaddingEnd() + widthExtra,
                    getHeight() / 2 + bounds.height() / 2 + heightExtra);
        } else {
            mTempRect.set(getPaddingEnd() - widthExtra,
                    getHeight() / 2 - bounds.height() / 2 - heightExtra,
                    getPaddingEnd() + bounds.width() + widthExtra,
                    getHeight() / 2 + bounds.height() / 2 + heightExtra);
        }

        boolean contains = mTempRect.contains((int) event.getX(), (int) event.getY());
        if (contains) {
            mClickedDrawable = DRAWABLE_END;
        }
        return contains;
    }

    private boolean isDrawableBottomClick(MotionEvent event) {
        Drawable drawable = mDrawables[DRAWABLE_BOTTOM];
        if (drawable == null || mBottomListener == null) {
            return false;
        }

        boolean contains = mTempRect.contains((int) event.getX(), (int) event.getY());
        if (contains) {
            mClickedDrawable = DRAWABLE_BOTTOM;
        }
        return contains;
    }

    private int getMarginStart() {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) layoutParams;
            return params.getMarginStart();
        }
        return 0;
    }

    public void setDrawableStartClick(View.OnClickListener listener, Size extraArea) {
        mStartListener = listener;
        mStartArea = extraArea;
    }

    private void setDrawableTopClick(View.OnClickListener listener, Size extraArea) {
        mTopListener = listener;
        mTopArea = extraArea;
    }

    public void setDrawableEndClick(View.OnClickListener listener, Size extraArea) {
        mEndListener = listener;
        mEndArea = extraArea;
    }

    private void setDrawableBottomClick(View.OnClickListener listener, Size extraArea) {
        mBottomListener = listener;
        mBottomArea = extraArea;
    }
}
