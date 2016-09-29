package com.king.applib;

import android.text.TextUtils;
import android.widget.TextView;

import java.util.Locale;

/**
 * TextView/Button倒计时帮助类,以秒为单位.<br/>
 * 当Timer生命周期比Activity的生命周期长时，会导致Activity泄露。在Activity#onDestory()方法中调用CountDownHelper#getTimer()#cancel()取消定时器<br/>
 * Created by HuoGuangxu on 2016/9/22.
 */
public class CountDownHelper {
    private CountDownTimer mCountDownTimer;
    private TextView mCountDownTV;
    private String mFinishedText;
    private String mPrefixText;
    private int mMaxDuration;
    private int mIntervalStep;
    private onCountDownFinishedListener mFinishedListener;

    private CountDownHelper(Builder builder) {
        mCountDownTV = builder.mCountDownTV;
        mFinishedText = builder.mFinishedText;
        mPrefixText = builder.mPrefixText;
        mMaxDuration = builder.mMaxDuration;
        mIntervalStep = builder.mIntervalStep;
        mFinishedListener = builder.listener;

        initTimer();
    }

    public CountDownTimer getTimer() {
        return mCountDownTimer;
    }

    private void initTimer() {
        /*
        由于CountDownTimer并不是准确计时，在onTick方法调用的时候，time会有1-10ms左右的误差，这会导致最后一秒不会调用onTick()
		/因此，设置间隔的时候，默认减去了10ms，从而减去误差。
		经过以上的微调，最后一秒的显示时间会由于10ms延迟的积累，导致显示时间比1s长max*10ms的时间，其他时间的显示正常,总时间正常
         */
        mCountDownTimer = new CountDownTimer(mMaxDuration * 1000, mIntervalStep * 1000 - 10) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (mCountDownTV != null) {
                    mCountDownTV.setText(String.format(Locale.US, "%s%d%s", mPrefixText == null ? "" : mPrefixText, (millisUntilFinished + 15) / 1000, "秒"));
                }
            }

            @Override
            public void onFinish() {
                if (mCountDownTV != null) {
                    mCountDownTV.setEnabled(true);
                    mCountDownTV.setText(mFinishedText);
                }
                if (mFinishedListener != null) {
                    mFinishedListener.onCountDownFinished();
                }
            }
        };
    }


    /**
     * 开始倒计时
     */
    public void start() {
        mCountDownTimer.start();
        mCountDownTV.setEnabled(false);
    }

    public static class Builder {
        private TextView mCountDownTV;
        private String mFinishedText;
        private String mPrefixText;
        private int mMaxDuration;
        private int mIntervalStep;
        private onCountDownFinishedListener listener;

        public Builder(TextView textView) {
            mCountDownTV = textView;
        }

        /**
         * 倒计时结束后显示的字符串
         */
        public Builder setFinishedText(String finishedText) {
            mFinishedText = finishedText;
            return this;
        }

        /**
         * 数字前面的字符串
         */
        public Builder setPrefixText(String prefixText) {
            if (prefixText == null || TextUtils.isEmpty(prefixText.trim())) {
                prefixText = "";
            }
            mPrefixText = prefixText;
            return this;
        }

        /**
         * 总时长,以秒为单位
         */
        public Builder setMaxDuration(int duration) {
            if (duration < 0) {
                mMaxDuration = 0;
            } else {
                mMaxDuration = duration;
            }
            return this;
        }

        /**
         * 每几秒更新一次UI
         */
        public Builder setIntervalStep(int interval) {
            if (interval < 0) {
                mIntervalStep = 0;
            } else if (interval > mIntervalStep) {
                mIntervalStep = 1;
            } else {
                mIntervalStep = interval;
            }
            return this;
        }

        /**
         * 倒计时回调结束监听
         */
        public Builder setOnFinishedListener(onCountDownFinishedListener listener) {
            this.listener = listener;
            return this;
        }

        public CountDownHelper build() {
            return new CountDownHelper(this);
        }
    }

    public interface onCountDownFinishedListener {
        void onCountDownFinished();
    }
}
