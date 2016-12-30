package com.example.lyons.demo.customerview;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Lyons on 2016/12/23.
 */

public class TabLayout extends HorizontalScrollView implements ViewPager.OnPageChangeListener {

    private static final String TAG = "TabLayout";

    /**
     *
     */
    private Context mContext;

    /**
     * 内容容器，也就是TextView的容器
     */
    private LinearLayout mContentContainer;

    /**
     * 整个TabLayout的容器
     */
    private LinearLayout mContainer;

    /**
     * Tab上方指示器
     */
    private IndicatorView mTopIndicator;

    /**
     * Tab下方指示器
     */
    private IndicatorView mBottomIndicator;

    /**
     * 标识Tab当前位置
     */
    private static int mCurrentPosition;

    /**
     * 每步的偏移量
     */
    private static int mStepOffset;

    /**
     * 联动ViewPager(可以自己去实现)
     */
    private ViewPager mViewPager;


    public TabLayout(Context context) {
        this(context, null);
    }

    public TabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TabLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * 初始化操作
     *
     * @param context
     */
    private void init(Context context) {
        this.mContext = context.getApplicationContext();
        // 容器
        this.mContainer = new LinearLayout(mContext);
        this.mContainer.setOrientation(LinearLayout.VERTICAL);
        // Tab上方指示器
        this.mTopIndicator = new IndicatorView(mContext);
        // 内容容器
        this.mContentContainer = new LinearLayout(mContext);
        this.mContentContainer.setOrientation(LinearLayout.HORIZONTAL);
        this.mContentContainer.setPadding(0, mTopIndicator.dip2px(mContext, 5), 0, mTopIndicator.dip2px(mContext, 6));
        this.mContentContainer.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        // Tab下方指示器
        this.mBottomIndicator = new IndicatorView(mContext);
        this.mContainer.addView(mTopIndicator);
        this.mContainer.addView(mContentContainer);
        this.mContainer.addView(mBottomIndicator);
        this.addView(mContainer);
    }

    /**
     * 设置标签
     *
     * @param tabs
     */
    public void setTabs(CharSequence[] tabs) {
        /**
         * 有多少个标签文字就实例出多少个TextView
         */
        int padding = mTopIndicator.dip2px(mContext, 20); // 默认距离左内边距
        for (int i = 0; i < tabs.length; i++) {
            TextView tab = new TextView(mContext);
            tab.setText(tabs[i]);
            tab.setGravity(Gravity.CENTER);
            tab.setTextColor(Color.BLACK);
            tab.setPadding(padding, 0, 0, 0);
            if (i == tabs.length - 1) {  // 最后一个左右内边距都设置一下，好看些
                tab.setPadding(padding, 0, padding, 0);
            }
            mContentContainer.addView(tab);
        }
        /**
         * 因为tab中的每个textview都是一样的，所以任意取一个就行
         * (这个地方没有考虑到tab文字个数不一样的情况)
         */
        View childView = mContentContainer.getChildAt(0);
        /**
         * 手动测量下TextView，因为要根据TextView的宽度让指示器的宽度自适应
         */
        childView.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        );
        // 计算出指示器的宽度
        mStepOffset = childView.getMeasuredWidth();
        mTopIndicator.setIndicatorWidth((float) (mStepOffset * 1.05)); // 上方指示器
        mBottomIndicator.setIndicatorWidth((float) (mStepOffset * 1.05)); // 下方指示器
        setTabItemClickListener();
    }

    /**
     * 给Tab设置监听
     */
    private void setTabItemClickListener() {
        for (int i = 0; i < mContentContainer.getChildCount(); i++) {
            final int position = i;
            mContentContainer.getChildAt(i).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    /**
                     * 向右为负，向左，负负得正
                     */
                    if (null != mViewPager) {
                        mViewPager.setCurrentItem(position);
                    }
                    scrollIndicator(position);
                }
            });
        }
    }

    private void scrollIndicator(int position) {
        int startX = mCurrentPosition * -mStepOffset;
        int offset = -mStepOffset * (position - mCurrentPosition);
        mTopIndicator.startScroll(startX, offset);
        mBottomIndicator.startScroll(startX, offset);
        mCurrentPosition = position;
    }

    public void setViewPager(ViewPager viewPager) {
        if (null != viewPager) {
            this.mViewPager = viewPager;
            this.mViewPager.setOnPageChangeListener(this);
        }
    }

    /**
     * 设置Tab背景色
     *
     * @param color
     */
    public void setBackgroundColor(int color) {
        this.mContainer.setBackgroundColor(color);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        scrollIndicator(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
