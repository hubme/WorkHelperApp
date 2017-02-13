package com.king.app.workhelper.ui.customview;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.FloatRange;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.king.app.workhelper.R;
import com.king.applib.log.Logger;

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
    private static final float DEFAULT_RING_WIDTH = 40;//默认环形宽度

    /**
     * 显示色块图标数量
     **/
    private static final int MAX_IMAGE_COUNT = 11;
    /**
     * 最多显示数据数量
     **/
    private static final int MAX_DATA_COUNT = 12;
    private int mDefaultColor;

    private int mImageSize;
    private int mImageTextSize;
    private int mImageSecondTextSize;
    private int mImageTextColor;
    private int mImagePadding;
    private float mChartSizePercent;
    private int mChartStrokeWidth;
    private float mChartStrokeHerPercent;
    private float mChartProgress = 0;

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    // 第一个块起始角度
    private float mStartAngle = -75;
    private RectF mRectF = new RectF();

    private ValueAnimator mAnimator;

    /**
     * 文字的一半对角线距离
     **/
    private float defTextHL;

    private ArrayList<PieData> mPieItems = new ArrayList<>();

    public FundFormPieView(Context context) {
        super(context);
        init(context, null);
    }

    public FundFormPieView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FundFormPieView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(21)
    public FundFormPieView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FundFormPieView);
        mImageSize = a.getDimensionPixelSize(R.styleable.FundFormPieView_imageSize, 50);
        mImageTextSize = a.getDimensionPixelSize(R.styleable.FundFormPieView_imageTextSize, 20);
        mImageSecondTextSize = getResources().getDimensionPixelSize(R.dimen.dp_11);
        mImagePadding = a.getDimensionPixelOffset(R.styleable.FundFormPieView_imagePadding, 10);
        mImageTextColor = a.getColor(R.styleable.FundFormPieView_imageTextColor, Color.BLACK);

        mChartSizePercent = a.getFloat(R.styleable.FundFormPieView_chartPercent, 120f / 610);
        mChartStrokeWidth = a.getDimensionPixelSize(R.styleable.FundFormPieView_chartStrokeWidth, 40);
        mChartStrokeHerPercent = 115f / 610;
        mChartProgress = a.getFloat(R.styleable.FundFormPieView_chartProgress, 100);
        a.recycle();

        mDefaultColor = ContextCompat.getColor(context, R.color.blue);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(mImageTextSize);

        float hw = mPaint.measureText("12%") / 2;
        float hh = mImageTextSize / 2;
        defTextHL = (float) Math.sqrt(hw * hw + hh * hh);
    }


    @SuppressWarnings("unCheck")
    public void updateData(final List<IFormStatisticsData> formData, boolean anim) {
        if (formData == null || formData.size() == 0) {
            mPieItems.clear();
            postInvalidate();
            return;
        }
        readPieDataFromFormData(anim, formData.toArray(new IFormStatisticsData[formData.size()]));
    }

    /**
     * 转换数据，从传入参数转换为画图数据
     **/
    private void readPieDataFromFormData(final boolean anim, final IFormStatisticsData... formPieData) {
        //TODO:因为只有两条数据，所以做了简单处理，如果其中一个比例小于0.01则修改为0.01,后面有时间做进一步处理
        if (formPieData.length >= 2) {
            if (formPieData[0].money < 0.01f) {
                formPieData[0].money = 0.01f;
                formPieData[1].money = 1 - 0.01f;
            } else if (formPieData[1].money < 0.01f) {
                formPieData[1].money = 0.01f;
                formPieData[0].money = 1 - 0.01f;
            }
        }

        ArrayList<PieData> results = new ArrayList<>(Math.min(MAX_DATA_COUNT, formPieData.length));

        double totalMoney = 0;
        for (IFormStatisticsData f : formPieData) {
            totalMoney += f.money;
        }

        double currentCount = 0;
        for (int i = formPieData.length - 1; i >= 0; i--) {
            IFormStatisticsData fpd = formPieData[i];

            int pdCount = results.size();
            float percent = (float) (fpd.money / totalMoney);
            if (percent == 0) {
                continue;
            } else {
                int color = fpd.colorInt;
                color = color == Color.BLACK ? mDefaultColor : color;
                currentCount += fpd.money;
                //PieData pd = new PieData(color, percent, drawable);
                PieData pd = new PieData(fpd.primaryText, fpd.secondText, color, percent);
                results.add(pd);
            }
        }

        mPieItems.clear();
        mPieItems.addAll(results);
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

        int maxValue = Math.min(getWidth(), getHeight());
        float strokeSize = mChartStrokeWidth;
        float strokeHerSize = maxValue * mChartStrokeHerPercent;
        float chartR = maxValue * mChartSizePercent - strokeSize / 2;
        Logger.i("maxValue: " + maxValue + ";strokeSize: " + strokeSize + ";strokeHerSize: " + strokeHerSize + ";chartR: " + chartR);

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        if (mChartProgress <= 0) {
            return;
        }

        mRectF.set(centerX - chartR, centerY - chartR, centerX + chartR, centerY + chartR);

        float angle = mStartAngle;
        final float fullAngle = Math.min(360, mChartProgress);

        for (int size = mPieItems.size(), i = 0; i < size; i++) {
            PieData pd = mPieItems.get(i);
            mPaint.setColor(pd.color);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(strokeSize);
            float ang = fullAngle * pd.percent;

            canvas.drawArc(mRectF, angle, ang + 1, false, mPaint); // 此处不+1会出现间隙
            angle += ang;

            if (fullAngle >= 360 && !TextUtils.isEmpty(pd.primaryText)) {
                if (pd.p1 == null) {
                    findImagePlace(mStartAngle, mRectF.centerX(), mRectF.centerY(), mRectF.width() / 2 + strokeSize / 2, strokeHerSize);
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
        if (pieData.p1 == null) {
            return;
        }

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(pieData.color);
        mPaint.setStrokeWidth(2);

        if (progress <= 0.5) { // 画连线进度
            progress = progress * 2;
            if (progress <= 0.5) {
                progress = progress * 2;
                float x = pieData.p1.x + (pieData.p2.x - pieData.p1.x) * progress;
                float y = pieData.p1.y + (pieData.p2.y - pieData.p1.y) * progress;
                canvas.drawLine(pieData.p1.x, pieData.p1.y, x, y, mPaint);
            } else {
                progress = progress - 0.5f;
                progress = progress * 2f;
                float y = pieData.p2.y;
                float x = pieData.p2.x + (pieData.p3.x - pieData.p2.x) * progress;
                canvas.drawLine(pieData.p1.x, pieData.p1.y, x, y, mPaint);
            }
            return;
        }
        progress = progress * 2 - 1; // 剩余图片和文字动画进度

        // 连线
        canvas.drawLine(pieData.p1.x, pieData.p1.y, pieData.p2.x, pieData.p2.y, mPaint);
        canvas.drawLine(pieData.p2.x, pieData.p2.y, pieData.p3.x, pieData.p3.y, mPaint);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(pieData.p3.x, pieData.p3.y, 6, mPaint);

        // 百分比文字
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mImageTextColor);
        mPaint.setAlpha((int) (255 * progress));
        mPaint.setTextSize(mImageTextSize);
        canvas.drawText(pieData.primaryText, pieData.textPrimaryCenter.x, pieData.textPrimaryCenter.y, mPaint);
        mPaint.setColor(pieData.color);
        mPaint.setTextSize(mImageSecondTextSize);
        canvas.drawText(pieData.secondText, pieData.textSecondCenter.x, pieData.textSecondCenter.y, mPaint);
        mPaint.setColor(mImageTextColor);
        mPaint.setTextSize(mImageTextSize);
        //mPaint.setAlpha(255);
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
        final int imageW = mImageSize;
        final int halfSize = imageW / 2;
        final int count = mPieItems.size();

        final float or = r + halfSize * 3; // 图片所在圆圈半径
        final float olr = r + imageW * 1.2f; // 图片连线底部所在圆圈半径 折线第一段的长度
        final float otr = or + halfSize + defTextHL; // 文字中心所在圆圈半径

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

                pieData.p1 = new Point((int) (centerX + r * Math.cos(ca)), (int) (centerY + r * Math.sin(ca)));
                pieData.p2 = new Point((int) (centerX + olr * Math.cos(sar)), (int) (centerY + olr * Math.sin(sar)));
                pieData.p3 = new Point((int) (centerX + olr * Math.cos(sar) + (Math.cos(sar) >= 0 ? herStroke : -herStroke)), (int) (centerY + olr * Math.sin(sar)));
                boolean isRight = (Math.cos(sar) >= 0);

                int p2cx = (int) (centerX + or * Math.cos(sar));
                int p2cy = (int) (centerY + or * Math.sin(sar));
                //pieData.imageRect = new Rect(p2cx - halfSize, p2cy - halfSize, p2cx + halfSize, p2cy + halfSize);

                mPaint.setTextSize(mImageTextSize);
                float hpw = mPaint.measureText(pieData.primaryText) / 2;
                float hph = mImageTextSize / 2;
                mPaint.setTextSize(mImageSecondTextSize);
                float hsw = mPaint.measureText(pieData.secondText) / 2;
                float hsh = mImageSecondTextSize / 2;
                pieData.textPrimaryCenter = new Point((int) (isRight ? (pieData.p3.x - hpw - 2) : (pieData.p3.x + hpw + 2)), (int) (pieData.p3.y - hph + 5));
                pieData.textSecondCenter = new Point((int) (isRight ? (pieData.p3.x - hsw - 2) : (pieData.p3.x + hsw + 2)), (int) (pieData.p3.y + hsh + hsh));
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

            pieData.p1 = new Point((int) (centerX + r * Math.cos(ra)), (int) (centerY + r * Math.sin(ra)));
            pieData.p2 = new Point((int) (centerX + olr * Math.cos(ra)), (int) (centerY + olr * Math.sin(ra)));
            pieData.p3 = new Point((int) (centerX + olr * Math.cos(ra) + (Math.cos(ra) >= 0 ? herStroke : -herStroke)), (int) (centerY + olr * Math.sin(ra)));
            boolean isRight = (Math.cos(ra) >= 0);
            //pieData.imageRect = new Rect(p2cx - halfSize, p2cy - halfSize, p2cx + halfSize, p2cy + halfSize);

            mPaint.setTextSize(mImageTextSize);
            float hpw = mPaint.measureText(pieData.primaryText) / 2;
            float hph = mImageTextSize / 2;
            mPaint.setTextSize(mImageSecondTextSize);
            float hsw = mPaint.measureText(pieData.secondText) / 2;
            float hsh = mImageSecondTextSize / 2;
            pieData.textPrimaryCenter = new Point((int) (isRight ? (pieData.p3.x - hpw - 2) : (pieData.p3.x + hpw + 2)), (int) (pieData.p3.y - hph + 5));
            pieData.textSecondCenter = new Point((int) (isRight ? (pieData.p3.x - hsw - 2) : (pieData.p3.x + hsw + 2)), (int) (pieData.p3.y + hsh + hsh));
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
     * 圆饼图数据
     */
    private static class PieData {
        final int color;
        final float percent;
        String primaryText;
        String secondText;

        // 文字中心点1
        private Point textPrimaryCenter;
        // 文字中心点2
        private Point textSecondCenter;
        // 连线起点
        private Point p1;
        // 连线转折点
        private Point p2;
        // 连线终点
        private Point p3;

//        public PieData(int color, float percent, Drawable image) {
//            this.color = color;
//            this.percent = percent;
//            this.image = image;
//        }

        public PieData(String primaryText, String secondText, int color, float percent) {
            this.primaryText = primaryText;
            this.secondText = secondText;
            this.color = color;
            this.percent = percent;
        }
    }

    public static class IFormStatisticsData implements Comparable<IFormStatisticsData> {

        public String primaryText;
        public String secondText;
        public float money;
        public int colorInt;

        public IFormStatisticsData(String primaryText, String secondText, float money, int colorInt) {
            this.primaryText = primaryText;
            this.secondText = secondText;
            this.money = money;
            this.colorInt = colorInt;
        }

        @Override
        public int compareTo(IFormStatisticsData data) {
            float cf = this.money - data.money;
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
