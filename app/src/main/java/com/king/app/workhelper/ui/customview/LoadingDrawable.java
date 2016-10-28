package com.king.app.workhelper.ui.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.PathInterpolatorCompat;
import android.support.v4.widget.ScrollerCompat;
import android.view.animation.Interpolator;

import com.king.app.workhelper.R;

/**
 * 小人进度条
 * <p>
 * 注：
 * 1.drawable 根据高度等比例拉伸
 * 2.由于小人图片太多，为了节约内存，裁剪为上半身和下半身
 * @author CJL
 * @since 2016-10-27
 */
public class LoadingDrawable extends Drawable implements Animatable {
    private Context mContext;
    private int mProgress = 0; // 由于scroller只支持int类型，因此进度设置为 0->MAX_PROGRESS
    private boolean mIsRunning = false;
    private boolean mIsTimeout = true;
    private OnFinishAnimListener mListener;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private ScrollerCompat mDefScroller;
    private ScrollerCompat mPgScroller;

    private DrawableInfo ldInfo;
    private Rect mRect = new Rect();
    private RectF mRectF = new RectF();

    private static final int MAX_PROGRESS_TIME = 30 * 1000; // 默认最大动画时间
    private static final int TO_PROGRESS_DURA = 300; // 结束动画平滑滚动到指定位置时间
    private static final int MAX_PROGRESS = 10000; // 最大进度值
    private static final int MAX_STEP_CYCLE = 150; // 跑完进度条双腿总迈圈数
//    private static final float MAX_TIME_OUT_PG = 0.85f; // 默认直到超时进度条最大进度

    public LoadingDrawable(Context ctx) {
        this.mContext = ctx.getApplicationContext();

        // 默认直到超时Interpolator，可考虑换成LinearInterpolator
        Interpolator defInt = PathInterpolatorCompat.create(0, 1.1f, 2, 1.1f);
        mDefScroller = ScrollerCompat.create(mContext, defInt);
        mPgScroller = ScrollerCompat.create(mContext);
    }

    @Override
    public void start() {
        this.mProgress = 0;
        mIsRunning = true;
        mIsTimeout = true;

        mPgScroller.abortAnimation();
        mDefScroller.startScroll(0, 0, MAX_PROGRESS, 0, MAX_PROGRESS_TIME);
        invalidateSelf();
    }

    @Override
    public void stop() {
        mIsTimeout = false;
        setProgress(1);
    }

    @Override
    public boolean isRunning() {
        return mIsRunning;
    }

    public void setAnimEndListener(OnFinishAnimListener listener) {
        this.mListener = listener;
    }

    /**
     * 指定当前进度值
     * <p>
     * 注意：设置之后将会平滑走到改进度值，并会停止默认进度动作！
     * @param progress 进度百分比值
     */
    public void setProgress(@FloatRange(from = 0, to = 1) float progress) {
        final int toPg = (int) (progress * MAX_PROGRESS);
        if (mProgress != toPg) {
            mDefScroller.abortAnimation();
            mPgScroller.startScroll(mProgress, 0, toPg - mProgress, 0, TO_PROGRESS_DURA);
            invalidateSelf();
        }
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        if (ldInfo != null) {
            ldInfo.onBoundsChange(bounds);
        }
        super.onBoundsChange(bounds);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (mPgScroller.computeScrollOffset()) {
            mProgress = mPgScroller.getCurrX();
        } else if (mDefScroller.computeScrollOffset()) {
            mProgress = mDefScroller.getCurrX();
        }

        drawProgress(canvas);

        if (mIsRunning && (mPgScroller.computeScrollOffset() || mDefScroller.computeScrollOffset())) {
            invalidateSelf();
        } else {
            if (mIsRunning) {
                mIsRunning = false;
                ldInfo = null;
                if (mListener != null) {
                    mListener.onFinishAnim(mIsTimeout);
                }
                mIsRunning = false;
            }
        }
    }

    /**
     * 画当前进度值
     */
    private void drawProgress(Canvas canvas) {
        Rect rect = getBounds();
        if (rect.width() <= 0) {
            return;
        }
        final float progress = (float) mProgress / MAX_PROGRESS;

        if (ldInfo == null) {
            ldInfo = new DrawableInfo(mContext);
            ldInfo.onBoundsChange(rect);
        }

        mPaint.setColor(0xff88b9fb);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        // 画外层进度条
        float pgInnerPadding = (ldInfo.pgOuterStroke - ldInfo.pgInnerStroke) * 0.5f;
        float halfStroke = ldInfo.pgOuterStroke * 0.5f;
        mRectF.set(ldInfo.pgStart - pgInnerPadding, rect.bottom - ldInfo.pgOuterStroke,
                ldInfo.pgEnd + pgInnerPadding, rect.bottom);
        canvas.drawRoundRect(mRectF, halfStroke, halfStroke, mPaint);

        // 画内层进度条
        mPaint.setColor(0xfff1e378);
        halfStroke = ldInfo.pgInnerStroke * 0.5f;
        float pgTop = rect.bottom - ldInfo.pgOuterStroke + ldInfo.pgInnerStroke / 2;
        mRectF.set(ldInfo.pgStart, pgTop, ldInfo.pgEnd, pgTop + ldInfo.pgInnerStroke);
        canvas.drawRoundRect(mRectF, halfStroke, halfStroke, mPaint);

        // 画房子
        ldInfo.house.draw(canvas);

        final int pgX = (int) ((ldInfo.pgEnd - ldInfo.pgStart) * progress);

        // 画人上半身
        mRect.set(ldInfo.headRect);
        mRect.offset(pgX, 0);
        ldInfo.head.setBounds(mRect);
        ldInfo.head.draw(canvas);

        // 画人下半身
        mRect.set(ldInfo.legRect);
        mRect.offset(pgX, 0);
        int legIdx = (pgX * MAX_STEP_CYCLE * ldInfo.legs.length / MAX_PROGRESS) % ldInfo.legs.length;
        ldInfo.legs[legIdx].setBounds(mRect);
        ldInfo.legs[legIdx].draw(canvas);

        // 画球
        mRect.set(ldInfo.ballRect);
        mRect.offset(pgX, 0);
        int rotateAngle = (pgX % ldInfo.balll) * 360 / ldInfo.balll;
        canvas.rotate(rotateAngle, mRect.centerX(), mRect.centerY());
        ldInfo.ball.setBounds(mRect);
        ldInfo.ball.draw(canvas);
    }

    public static void main(String[] args) {

        for (int i = 0; i < MAX_PROGRESS; i++) {
            int legIdx = (i * MAX_STEP_CYCLE * 9 / MAX_PROGRESS) % 9;
            System.out.println("pos=" + i + "; legIdx=" + legIdx);
        }

    }


    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }


    private final static class DrawableInfo {
        Drawable ball;
        Drawable head;
        Drawable house;
        Drawable[] legs;

        int pgInnerStroke;
        int pgOuterStroke;
        int ballr;
        int balll;

        Rect ballRect = new Rect();
        Rect headRect = new Rect();
        Rect legRect = new Rect();

        int pgStart, pgEnd;

        DrawableInfo(Context context) {
            ball = ContextCompat.getDrawable(context, R.mipmap.ball);
            head = ContextCompat.getDrawable(context, R.mipmap.head);
            house = ContextCompat.getDrawable(context, R.mipmap.house);

            int[] legIds = new int[]{
                    R.mipmap.leg_01, R.mipmap.leg_02, R.mipmap.leg_03,
                    R.mipmap.leg_04, R.mipmap.leg_05, R.mipmap.leg_06,
                    R.mipmap.leg_07, R.mipmap.leg_08, R.mipmap.leg_09
            };

            legs = new Drawable[legIds.length];
            for (int i = 0; i < legIds.length; i++) {
                legs[i] = ContextCompat.getDrawable(context, legIds[i]);
            }
        }

        /**
         * 更新各个图片位置与大小
         * @param bounds 整个drawable边框
         */
        void onBoundsChange(Rect bounds) {
            final float scale = bounds.height() / 260f;
            ballr = (int) (72 * scale);
            final float imgScale = ballr * 2f / ball.getIntrinsicWidth();

            pgInnerStroke = (int) (26 * scale);
            pgOuterStroke = (int) (52 * scale);

            float ballBottomH = (pgOuterStroke - pgInnerStroke) * 0.5f;

            int headW = (int) (head.getIntrinsicWidth() * imgScale);
            int headH = (int) (head.getIntrinsicHeight() * imgScale);
            headRect.set(bounds.left, bounds.top, bounds.left + headW, bounds.top + headH);
            legRect.set(headRect.left, headRect.bottom, headRect.right,
                    (int) (headRect.bottom + legs[0].getIntrinsicHeight() * imgScale));

            int ballT = (int) (bounds.bottom - ballBottomH - ballr * 2);
            ballRect.set(headW - ballr, ballT, headW + ballr, (int) (bounds.bottom - ballBottomH));

            int houseW = (int) (house.getIntrinsicWidth() * imgScale);
            int houseH = (int) (house.getIntrinsicHeight() * imgScale);
            int hoursR = bounds.right - ballr;
            int houseB = bounds.bottom - pgOuterStroke;
            house.setBounds(hoursR - houseW, houseB - houseH, hoursR, houseB);

            pgStart = bounds.left + headW;
            pgEnd = bounds.right - ballr;

            balll = (int) (Math.PI * ballr * 2);
        }
    }

    public interface OnFinishAnimListener {
        void onFinishAnim(boolean isTimeout);
    }
}
