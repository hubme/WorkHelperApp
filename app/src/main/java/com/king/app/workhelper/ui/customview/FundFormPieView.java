package com.king.app.workhelper.ui.customview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.king.app.workhelper.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 资金表单饼图
 * todo:圆环宽度
 *
 * @author CJL
 * @since 2016-01-13
 */
public class FundFormPieView extends View {
    /**
     * 最多显示数据数量
     **/
    private static final int MAX_DATA_COUNT = 12;

    private int mImageSize = 50;
    private int mImageTextSize;
    private int mImageSecondTextSize;
    private int mImageTextColor;
    private float mChartSizePercent;
    private int mChartStrokeWidth;
    private float mChartStrokeHerPercent = 90f / 610;//文字距离水平方向外边距的比例
    private float mChartProgress = 0;

    private Paint mPaint;
    // 第一个块起始角度
    private float mStartAngle = -75;
    private RectF mRectF = new RectF();

    private ValueAnimator mAnimator;

    /**
     * 文字的一半对角线距离
     **/
    private float defTextHL;
    private boolean isPrint;

    private ArrayList<PieData> mPieItems = new ArrayList<>();

    public FundFormPieView(Context context) {
        this(context, null);
    }

    public FundFormPieView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FundFormPieView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FundFormPieView);
//        mImageSize = a.getDimensionPixelSize(R.styleable.FundFormPieView_imageSize, 50);
        mImageTextSize = a.getDimensionPixelSize(R.styleable.FundFormPieView_imageTextSize, 24);
        mImageSecondTextSize = mImageTextSize;
        mImageTextColor = a.getColor(R.styleable.FundFormPieView_imageTextColor, Color.BLACK);
        mChartSizePercent = a.getFloat(R.styleable.FundFormPieView_chartPercent, 120f / 610);
        mChartStrokeWidth = a.getDimensionPixelSize(R.styleable.FundFormPieView_strokeWidth, 40);
        mChartProgress = a.getFloat(R.styleable.FundFormPieView_progress, 100);
        a.recycle();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setDither(true);//防抖动
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(mImageTextSize);

        float hw = mPaint.measureText("12%") / 2;
        float hh = mImageTextSize / 2;
        defTextHL = (float) Math.sqrt(hw * hw + hh * hh);
    }

    public void updateData(final List<PieItem> pieItems, boolean anim) {
        if (pieItems != null && !pieItems.isEmpty()) {
            readPieDataFromFormData(pieItems, anim);
        }
    }

    /**
     * 转换数据，从传入参数转换为画图数据
     **/
    private void readPieDataFromFormData(final List<PieItem> pieItems, final boolean anim) {
        //TODO:因为只有两条数据，所以做了简单处理，如果其中一个比例小于0.01则修改为0.01,后面有时间做进一步处理
        if (pieItems.size() >= 2) {
            if (pieItems.get(0).value < 0.01f) {
                pieItems.get(0).value = 0.01f;
                pieItems.get(1).value = 1 - 0.01f;
            } else if (pieItems.get(1).value < 0.01f) {
                pieItems.get(1).value = 0.01f;
                pieItems.get(0).value = 1 - 0.01f;
            }
        }

        float totalValue = 0;
        for (PieItem p : pieItems) {
            if (p != null) {
                totalValue += p.value;
            }
        }

        mPieItems.clear();
        for (PieItem item : pieItems) {
            if (item == null) {
                continue;
            }
            PieData pd = new PieData(item.primaryText, item.secondText, item.color, item.value / totalValue);
            mPieItems.add(0, pd);
        }

        if (anim) {
            startAnim();
        } else {
            stopAnim();
            invalidate();
        }
    }

    @Override
    public void draw(final Canvas canvas) {
        super.draw(canvas);
        if (mChartProgress <= 0) {
            return;
        }

        final int width = getWidth();
        final int height = getHeight();
        final int centerX = width / 2;
        final int centerY = height / 2;
        int maxValue = Math.min(width, height);

        float chartRadius = (maxValue * mChartSizePercent - mChartStrokeWidth) / 2;
        if (!isPrint) {
//            Logger.i("maxValue: " + maxValue + ";strokeSize: " + mChartStrokeWidth + ";chartRadius: " + chartRadius);
            isPrint = true;
        }

        mRectF.set(centerX - chartRadius, centerY - chartRadius, centerX + chartRadius, centerY + chartRadius);

        float angle = mStartAngle;
        final float fullAngle = Math.min(360, mChartProgress);
//        Logger.i("mChartProgress: " + mChartProgress + ";fullAngle: " + fullAngle);

        for (PieData pd : mPieItems) {
            if (pd == null) {
                continue;
            }
            mPaint.setColor(pd.color);
            // FIXME: 2017/2/21 以下两句放在循环外面图标变形
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(mChartStrokeWidth);
            float ang = fullAngle * pd.percent;

            canvas.drawArc(mRectF, angle, ang + 1, false, mPaint); // 此处不+1会出现间隙
            angle += ang;

            if (fullAngle >= 360 && !TextUtils.isEmpty(pd.primaryText)) {
                if (pd.startPoint == null) {
                    findImagePlace(mStartAngle, mRectF.centerX(), mRectF.centerY(), chartRadius, maxValue * mChartStrokeHerPercent);
                }
                drawImageAndText(canvas, mPaint, pd, (mChartProgress - 360) / 360);
            }
        }
    }

    public void startAnim() {
        mChartProgress = 0;
        if (mAnimator == null) {
            mAnimator = ValueAnimator.ofFloat(0, 360, 720).setDuration(700);
            mAnimator.setInterpolator(new LinearInterpolator());
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mChartProgress = (float) animation.getAnimatedValue();
                    postInvalidate();
                }
            });
        } else {
            mAnimator.end();
        }
        mAnimator.start();
        postInvalidate();
    }

    public void stopAnim() {
        if (mAnimator != null && mAnimator.isRunning()) {
            mAnimator.end();
        }
        mChartProgress = 720;
    }

    public void setAnimProgress(@FloatRange(from = 0, to = 720) float progress) {
        this.mChartProgress = progress;
        postInvalidate();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAnimator != null && mAnimator.isRunning()) {
            mAnimator.cancel();
        }
    }

    /**
     * 画类别图标和对应的百分比文字
     */
    private void drawImageAndText(Canvas canvas, Paint mPaint, PieData pieData, float progress) {
        if (pieData.startPoint == null) {
            return;
        }

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(pieData.color);
        mPaint.setStrokeWidth(2);

        if (progress <= 0.5) { // 画连线进度
            progress = progress * 2;
            if (progress <= 0.5) {
                progress = progress * 2;
                float x = pieData.startPoint.x + (pieData.turnPoint.x - pieData.startPoint.x) * progress;
                float y = pieData.startPoint.y + (pieData.turnPoint.y - pieData.startPoint.y) * progress;
                canvas.drawLine(pieData.startPoint.x, pieData.startPoint.y, x, y, mPaint);
            } else {
                progress = progress - 0.5f;
                progress = progress * 2f;
                float y = pieData.turnPoint.y;
                float x = pieData.turnPoint.x + (pieData.endPoint.x - pieData.turnPoint.x) * progress;
                canvas.drawLine(pieData.startPoint.x, pieData.startPoint.y, x, y, mPaint);
            }
            return;
        }
        progress = progress * 2 - 1; // 剩余图片和文字动画进度

        // 连线
        canvas.drawLine(pieData.startPoint.x, pieData.startPoint.y, pieData.turnPoint.x, pieData.turnPoint.y, mPaint);
        canvas.drawLine(pieData.turnPoint.x, pieData.turnPoint.y, pieData.endPoint.x, pieData.endPoint.y, mPaint);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(pieData.endPoint.x, pieData.endPoint.y, 6, mPaint);

        // 百分比文字
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mImageTextColor);
        mPaint.setAlpha((int) (255 * progress));
        mPaint.setTextSize(mImageTextSize);
        canvas.drawText(pieData.primaryText, pieData.textPrimaryPoint.x, pieData.textPrimaryPoint.y, mPaint);

        mPaint.setColor(pieData.color);
        mPaint.setTextSize(mImageSecondTextSize);
        canvas.drawText(pieData.secondText, pieData.textSecondPoint.x, pieData.textSecondPoint.y, mPaint);
        mPaint.setColor(mImageTextColor);
        mPaint.setTextSize(mImageTextSize);
    }

    /**
     * 按中心缩放方形边框
     *
     * @param rect  Rect
     * @param to    缩放后保存位置
     * @param scale 缩放比例
     * @return to
     */
    private Rect scaleRectByCenter(Rect rect, Rect to, float scale) {
        final int cx = rect.centerX();
        final int cy = rect.centerY();
        final int hw = (int) (rect.width() * scale) >> 1;
        final int hh = (int) (rect.height() * scale) >> 1;
        to.set(cx - hw, cy - hh, cx + hw, cy + hh);
        return to;
    }

    private void findImagePlace2(float startAngle, float centerX, float centerY, float r, float herStroke) {
        
    }

    /**
     * 给数据寻找图片和文字的合适空间
     * FIXME
     * 由于最后一位可能是其它，它的值很可能大于倒数第二个的值而且角度大于图片角度
     * 导致判断偏移时候直接断定不用偏移图片了（倒数第二个角度还是小于图片角度，可能会造成后面几个图片重叠）
     * 简单重现：流水值：一个200元 和 12个8元，每一笔的收支类别都不同。由于最多显示11个正常图片和一个其他，
     * 这里面其它占比大概5.4%，且超过图片角度比例，后面的8元图标都重叠了
     * 这个改起来麻烦，懒得整了。。。
     *
     * @param startAngle 起始角度
     * @param centerX    圆圈中心点X
     * @param centerY    圆圈中心点Y
     * @param r          圆圈半径
     * @param herStroke  水平线长度
     */
    private void findImagePlace(float startAngle, float centerX, float centerY, float r, float herStroke) {
        int mImageSize = 50;

        final int halfSize = mImageSize / 2;
        final int count = mPieItems.size();

        final float or = r + halfSize * 3; // 图片所在圆圈半径
        final float olr = r + mImageSize * 1.2f; // 图片连线底部所在圆圈半径 折线第一段的长度

        // 每个图片占用角度
        final double imageAngle = Math.toDegrees(Math.atan2(halfSize, or)) * 2;
        final float imageAnglePercent = (float) (imageAngle / 360); // 每个图片占用角度百分比

        int ePos = count; // 偏移截至位置
        float ePercent = 0;
        for (int i = count - 1; i >= 0; i--) {
            PieData pd = mPieItems.get(i);
            ePercent += pd.percent;
            if (ePercent >= (count - i) * imageAnglePercent) {
                ePos = i;
                break;
            }
        }

        float itemSa = startAngle;
        if (ePos != count) {
            float efShift = (imageAnglePercent * (count - ePos - 1) - ePercent + mPieItems.get(ePos).percent) * 0.5f;
            if (efShift < (mPieItems.get(ePos).percent - imageAnglePercent) * 0.5f) { // 第一个不需要偏移
                ePos++;
            }
            float sa = mStartAngle + 360 * efShift;

            // 偏移的图片，它们每个都相邻
            for (int i = count - 1; i >= ePos; i--) {
                PieData pieData = mPieItems.get(i);

                double ca = Math.toRadians(itemSa - 360 * pieData.percent / 2);
                double sar = Math.toRadians(sa);

                pieData.startPoint = new Point((int) (centerX + r * Math.cos(ca)), (int) (centerY + r * Math.sin(ca)));
                pieData.turnPoint = new Point((int) (centerX + olr * Math.cos(sar)), (int) (centerY + olr * Math.sin(sar)));
                pieData.endPoint = new Point((int) (centerX + olr * Math.cos(sar) + (Math.cos(sar) >= 0 ? herStroke : -herStroke)), (int) (centerY + olr * Math.sin(sar)));
                boolean isRight = (Math.cos(sar) >= 0);

                mPaint.setTextSize(mImageTextSize);
                float hpw = mPaint.measureText(pieData.primaryText) / 2;
                float hph = mImageTextSize / 2;
                mPaint.setTextSize(mImageSecondTextSize);
                float hsw = mPaint.measureText(pieData.secondText) / 2;
                float hsh = mImageSecondTextSize / 2;
                pieData.textPrimaryPoint = new Point((int) (isRight ? (pieData.endPoint.x - hpw - 2) : (pieData.endPoint.x + hpw + 2)), (int) (pieData.endPoint.y - hph + 5));
                pieData.textSecondPoint = new Point((int) (isRight ? (pieData.endPoint.x - hsw - 2) : (pieData.endPoint.x + hsw + 2)), (int) (pieData.endPoint.y + hsh + hsh));
                mPaint.setTextSize(mImageTextSize);

                itemSa -= 360 * pieData.percent;
                sa -= imageAngle;
            }
        }

        itemSa = startAngle;
        // 不偏移的图片！
        for (int i = 0; i < ePos; i++) {
            PieData pieData = mPieItems.get(i);
            double ra = Math.toRadians(itemSa + 360 * pieData.percent / 2);
            int p2cx = (int) (centerX + or * Math.cos(ra));
            int p2cy = (int) (centerY + or * Math.sin(ra));

            pieData.startPoint = new Point((int) (centerX + r * Math.cos(ra)), (int) (centerY + r * Math.sin(ra)));
            pieData.turnPoint = new Point((int) (centerX + olr * Math.cos(ra)), (int) (centerY + olr * Math.sin(ra)));
            pieData.endPoint = new Point((int) (centerX + olr * Math.cos(ra) + (Math.cos(ra) >= 0 ? herStroke : -herStroke)), (int) (centerY + olr * Math.sin(ra)));
            boolean isRight = (Math.cos(ra) >= 0);
            //pieData.imageRect = new Rect(p2cx - halfSize, p2cy - halfSize, p2cx + halfSize, p2cy + halfSize);

            mPaint.setTextSize(mImageTextSize);
            float hpw = mPaint.measureText(pieData.primaryText) / 2;
            float hph = mImageTextSize / 2;
            mPaint.setTextSize(mImageSecondTextSize);
            float hsw = mPaint.measureText(pieData.secondText) / 2;
            float hsh = mImageSecondTextSize / 2;
            pieData.textPrimaryPoint = new Point((int) (isRight ? (pieData.endPoint.x - hpw - 2) : (pieData.endPoint.x + hpw + 2)), (int) (pieData.endPoint.y - hph + 5));
            pieData.textSecondPoint = new Point((int) (isRight ? (pieData.endPoint.x - hsw - 2) : (pieData.endPoint.x + hsw + 2)), (int) (pieData.endPoint.y + hsh + hsh));
            mPaint.setTextSize(mImageTextSize);

            itemSa += 360 * pieData.percent;
        }
    }

    /**
     * 判断2个圆是否相交
     **/
    private boolean isIntersect(int x, int y, Rect r) {
        final int x2 = r.centerX();
        final int y2 = r.centerY();
        final int w = r.width();
        return (x - x2) * (x - x2) + (y - y2) * (y - y2) < w * w;
    }

    /**
     * 饼图元数据
     */
    private static class PieData {
        private int color;
        private float percent;
        private String primaryText;
        private String secondText;

        // 文字中心点1
        private Point textPrimaryPoint;
        // 文字中心点2
        private Point textSecondPoint;
        private Point startPoint;
        private Point turnPoint;
        private Point endPoint;

        public PieData(String primaryText, String secondText, int color, float percent) {
            this.primaryText = primaryText;
            this.secondText = secondText;
            this.color = color;
            this.percent = percent;
        }
    }

    //client端饼图元数据
    public static class PieItem implements Comparable<PieItem> {

        public String primaryText;
        public String secondText;
        public float value;
        @ColorInt
        public int color = Color.BLACK;

        public PieItem(String primaryText, String secondText, float value, @ColorInt int color) {
            this.primaryText = primaryText;
            this.secondText = secondText;
            this.value = value;
            this.color = color;
        }

        @Override
        public int compareTo(PieItem data) {
            float cf = this.value - data.value;
            if (cf > 0) {
                return 1;
            } else if (cf == 0) {
                return 0;
            } else {
                return -1;
            }
        }
    }
}
