package com.king.applib;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.TextView;

/**
 * TextView/Button倒计时帮助类,以秒为单位.<br/>
 * 当Timer生命周期比Activity的生命周期长时，会导致Activity泄露。在Activity#onDestory()方法中调用CountDownHelper#getTimer()#cancel()取消定时器<br/>
 * Created by HuoGuangxu on 2016/9/22.
 */
public class CountDownHelper {
    private CountDownTimer mCountDownTimer;
    private TextView mCountDownTV;
    private String mFinishedText;
    private int mMaxDuration;
    private int mIntervalStep;
    private onCountDownFinishedListener mFinishedListener;
    private final Context mContext;
    @StringRes
    private int mStringResId;

    private CountDownHelper(Context context) {
        mContext = context;
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
                    mCountDownTV.setText(mContext.getString(mStringResId, (millisUntilFinished + 15) / 1000));
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

    private void setStringRes(@StringRes int stringResId) {
        mStringResId = stringResId;
    }

    private void setFinishedText(String finishedText) {
        mFinishedText = finishedText;
    }

    private void setOnFinishedListener(onCountDownFinishedListener listener) {
        mFinishedListener = listener;
    }

    private void setMaxDuration(int duration) {
        mMaxDuration = duration;
    }

    private void setIntervalStep(int interval) {
        mIntervalStep = interval;
    }

    private void setTextView(TextView textView) {
        mCountDownTV = textView;
    }

    /**
     * 开始倒计时
     */
    public void start() {
        mCountDownTimer.start();
        mCountDownTV.setEnabled(false);
    }

    /**
     * 停止倒计时
     */
    public void stop() {
        mCountDownTimer.onFinish();
        mCountDownTimer.cancel();
    }

    public static class Builder {
        @StringRes
        private int mStringResId;
        private String mFinishedText;
        private int mMaxDuration;
        private int mIntervalStep;
        private onCountDownFinishedListener mListener;
        private final Context mContext;
        private final TextView mTextView;

        public Builder(Context context, TextView textView) {
            mContext = context;
            mTextView = textView;
        }

        /**
         * 倒计时结束后显示的字符串
         */
        public Builder setFinishedText(String finishedText) {
            mFinishedText = finishedText;
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
         * 设置显示的字符串
         * @param stringResId 必须包含"%1$d",用于显示秒数
         */
        public Builder setStringRes(@StringRes int stringResId) {
            mStringResId = stringResId;
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
            this.mListener = listener;
            return this;
        }

        public CountDownHelper build() {
            CountDownHelper countDownHelper = new CountDownHelper(mContext);
            countDownHelper.setTextView(mTextView);
            countDownHelper.setFinishedText(mFinishedText);
            countDownHelper.setIntervalStep(mIntervalStep);
            countDownHelper.setMaxDuration(mMaxDuration);
            countDownHelper.setOnFinishedListener(mListener);
            countDownHelper.setStringRes(mStringResId);
            countDownHelper.initTimer();
            return countDownHelper;
        }
    }

    public interface onCountDownFinishedListener {
        void onCountDownFinished();
    }
}