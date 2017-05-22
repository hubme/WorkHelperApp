/*
 * Copyright 2016 czy1121
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.king.applib.ui.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.king.applib.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 通告View
 * @author huoguangxu
 * @since 2017/5/22.
 */
public class NoticeView extends TextSwitcher {
    public static final int DEFAULT_INTERVAL = 3000;//ms
    public static final int DEFAULT_DURATION = 1000;//ms

    private final List<String> mDataList = new ArrayList<>();

    private int mIndex = 0;
    private int mInterval = DEFAULT_INTERVAL;
    private int mDuration = DEFAULT_DURATION;

    private Drawable mIcon;
    private int mIconTint = 0xff999999;
    private int mPaddingLeft = 0;

    private boolean mIsRunning = false;
    private final TextFactory mDefaultFactory = new TextFactory();
    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (mIsRunning) {
                show(mIndex + 1);
                postDelayed(mRunnable, mInterval);
            }
        }
    };

    public NoticeView(Context context) {
        this(context, null);
    }

    public NoticeView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initWithContext(context, attrs);
        initAnimation();
        setFactory(mDefaultFactory);

    }

    private void initWithContext(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NoticeView);
        mIcon = a.getDrawable(R.styleable.NoticeView_nvIcon);
        int mIconPadding = (int) a.getDimension(R.styleable.NoticeView_nvIconPadding, 0);

        boolean hasIconTint = a.hasValue(R.styleable.NoticeView_nvIconTint);
        if (hasIconTint) {
            mIconTint = a.getColor(R.styleable.NoticeView_nvIconTint, 0xff999999);
        }

        mInterval = a.getInteger(R.styleable.NoticeView_nvInterval, DEFAULT_INTERVAL);
        mDuration = a.getInteger(R.styleable.NoticeView_nvDuration, DEFAULT_DURATION);

        mDefaultFactory.resolve(a);
        a.recycle();

        if (mIcon != null) {
            mPaddingLeft = getPaddingLeft();
            int realPaddingLeft = mPaddingLeft + mIconPadding + mIcon.getIntrinsicWidth();
            setPadding(realPaddingLeft, getPaddingTop(), getPaddingRight(), getPaddingBottom());

            if (hasIconTint) {
                mIcon = mIcon.mutate();
                DrawableCompat.setTint(mIcon, mIconTint);
            }
        }
    }

    private void initAnimation() {
        Animation inAnim = anim(1.5f, 0);
        inAnim.setDuration(mDuration);
        setInAnimation(inAnim);

        Animation outAnim = anim(0, -1.5f);
        outAnim.setDuration(mDuration);
        setOutAnimation(outAnim);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mIcon != null) {
            int y = (getMeasuredHeight() - mIcon.getIntrinsicWidth()) / 2;
            mIcon.setBounds(mPaddingLeft, y, mPaddingLeft + mIcon.getIntrinsicWidth(), y + mIcon.getIntrinsicHeight());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mIcon != null) {
            mIcon.draw(canvas);
        }
    }

    public int getIndex() {
        return mIndex;
    }

    public void setNotices(List<String> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        if (!mDataList.isEmpty()) {
            mDataList.clear();
        }
        mDataList.addAll(list);
        startLoop();
        show(0);
    }

    public void startLoop() {
        if (mDataList.size() <= 1) {
            return;
        }
        if (!mIsRunning) {
            postDelayed(mRunnable, mInterval);
        }
        mIsRunning = true;

    }

    private void stopLoop() {
        if (mIsRunning) {
            removeCallbacks(mRunnable);
        }
        mIsRunning = false;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopLoop();
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == VISIBLE) {
            startLoop();
        } else {
            stopLoop();
        }
    }

    /*@Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                stopLoop();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                startLoop();
                break;

        }
        return super.dispatchTouchEvent(ev);
    }*/

    private void show(int index) {
        if (mDataList.isEmpty()) {
            return;
        }
        mIndex = index % mDataList.size();
        setText(mDataList.get(mIndex));
    }

    private Animation anim(float from, float to) {
        final TranslateAnimation anim = new TranslateAnimation(0, 0f, 0, 0f, Animation.RELATIVE_TO_PARENT, from, Animation.RELATIVE_TO_PARENT, to);
        anim.setDuration(mDuration);
        anim.setFillAfter(false);
        anim.setInterpolator(new LinearInterpolator());
        return anim;
    }

    private class TextFactory implements ViewFactory {
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();

        float size = dp2px(14);
        int color = 1;
        int lines = 1;
        int gravity = Gravity.LEFT;

        void resolve(TypedArray ta) {
            lines = ta.getInteger(R.styleable.NoticeView_nvTextMaxLines, lines);
            size = ta.getDimension(R.styleable.NoticeView_nvTextSize, size);
            color = ta.getColor(R.styleable.NoticeView_nvTextColor, color);
            gravity = ta.getInteger(R.styleable.NoticeView_nvTextGravity, gravity);
        }

        private int dp2px(float dp) {
            return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, dm);
        }

        @Override
        public View makeView() {
            TextView tv = new TextView(getContext());
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
            tv.setMaxLines(lines);
            if (color != 1) {
                tv.setTextColor(color);
            }
            tv.setEllipsize(TextUtils.TruncateAt.END);
            tv.setGravity(Gravity.CENTER_VERTICAL | gravity);
            tv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            return tv;
        }
    }
}