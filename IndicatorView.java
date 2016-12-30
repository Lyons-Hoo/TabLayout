package com.example.lyons.demo.customerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by Lyons on 2016/12/24.
 */

public class IndicatorView extends View {

    private static final String TAG = "IndicatorView";

    private Context mContext;

    /**
     * 指示器画笔
     */
    private Paint mLinePaint;

    /**
     * 指示器宽度
     */
    private static float mIndicatorWidth;

    /**
     * 滑动器
     */
    private Scroller mScroller;


    public IndicatorView(Context context) {
        this(context, null);
    }

    public IndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public IndicatorView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    /**
     * 初始化操作
     *
     * @param context
     */
    private void init(Context context) {
        this.mContext = context.getApplicationContext();
        this.mLinePaint = new Paint();
        setIndicatorColor(Color.DKGRAY); // 默认颜色
        setIndicatorThickness(2); // 指示器默认厚度
    }

    /**
     * 设置指示器宽度
     *
     * @param indicatorWidth
     */
    public void setIndicatorWidth(float indicatorWidth) {
        this.mIndicatorWidth = indicatorWidth;
    }

    /**
     * 设置指示器厚度
     *
     * @param indicatorThickness px值
     */
    public void setIndicatorThickness(int indicatorThickness) {
        mLinePaint.setStrokeWidth(dip2px(mContext, indicatorThickness));
    }

    /**
     * 设置指示器颜色
     *
     * @param color
     */
    public void setIndicatorColor(int color) {
        this.mLinePaint.setColor(color);
    }

    /**
     * 滑动操作
     *
     * @param startX 相对于屏幕X轴起点坐标的偏移量
     * @param offset 偏移量(正数向右，负数向左)
     */
    public void startScroll(int startX, int offset) {
        this.mScroller = new Scroller(mContext);
        mScroller.startScroll(startX, 0, offset, 0, 500);
        invalidate(); // 重绘UI
    }

    @Override
    public void computeScroll() {
        if (null != mScroller) {
            if (mScroller.computeScrollOffset()) { // 滑动没有完成
                scrollTo(mScroller.getCurrX(), 0); // 滑动
                invalidate(); // 重绘UI
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /**
         * 宽度始终保持跟父容器一直，为了适应滑动
         */
        setMeasuredDimension(
                ((ViewGroup) getParent()).getMeasuredWidth(),
                (int) mLinePaint.getStrokeWidth()
        );
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**
         * 默认是从X轴20开始画指示器
         */
        canvas.drawLine(dip2px(mContext, 15), 0, mIndicatorWidth, 0, mLinePaint);
    }

    /**
     * @param context
     * @param dipValue
     * @return
     */
    public int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
