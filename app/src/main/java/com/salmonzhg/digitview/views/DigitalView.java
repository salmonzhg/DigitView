package com.salmonzhg.digitview.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.salmonzhg.digitview.R;
import com.salmonzhg.digitview.utils.DisplayUtils;


/**
 * Created by Salmon on 2016/5/25 0025.
 */
public class DigitalView extends View {
    private static final int DEFAULT_COLOR = Color.BLACK;
    private static final int DEFAULT_TEXT_SIZE = 16;
    private int mColor = DEFAULT_COLOR;
    private int mTextSize = DEFAULT_TEXT_SIZE;
    private int mOffset = 0;
    private int mWid = 0;
    private int mHei = 0;
    private Paint mPaint;
    private int mRequiredWid = 0;
    private int mRequiredHei = 0;
    private float mBaseLine;
    private int mCurrentNum = 0;
    private boolean isPlaying = false;

    public DigitalView(Context context) {
        super(context);

        init(context, null);
    }

    public DigitalView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DigitalView);
            if (ta != null) {
                mColor = ta.getColor(R.styleable.DigitalView_digiColor, Color.BLACK);
                mTextSize = ta.getDimensionPixelSize(R.styleable.DigitalView_digiTextSize, DEFAULT_TEXT_SIZE);

                ta.recycle();
            }
        }

        setTextSize(mTextSize);
        setTextColor(mColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < 10; i++) {
            canvas.drawText(String.valueOf(i), (mWid - mRequiredWid) / 2,
                    mHei * i + (mHei - mBaseLine) - mOffset, mPaint);
        }
    }

    private void calRequireSize() {
        Paint.FontMetrics fm = mPaint.getFontMetrics();

        mRequiredWid = (int) mPaint.measureText("0");
        mRequiredHei = (int) (fm.bottom - fm.top);

        //  canvas.drawText()其中一个参数为baseLine,如果设置为试图的高度，则会截掉部分
        //  如小写字母p中的下吧吗部分，就属于baseLine以下的部分
        //  故需要计算baseLine的高度，公式参考如下
        //  http://www.sjsjw.com/kf_mobile/article/9_31376_30207.asp
        mBaseLine = (fm.bottom - fm.top) / 2 - fm.descent;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWid = w;
        mHei = h;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measure(widthMeasureSpec, true), measure(heightMeasureSpec, false));
    }

    private int measure(int measureSpec, boolean isWidSpec) {
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        int requireSize = isWidSpec ? mRequiredWid : mRequiredHei;

        if (!(mode == MeasureSpec.EXACTLY && size > requireSize))
            size = requireSize;

        return size;
    }

    public void startAnim(int num) {
        if (isPlaying)
            return;
        if (num < 0) {
            num = 0;
        } else if (num > 9) {
            num = 9;
        }
        mCurrentNum = num;
        ValueAnimator anim = ValueAnimator.ofInt(mOffset, num * mHei);
        anim.setDuration(1000);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mOffset = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isPlaying = false;
            }
        });
        anim.start();
        isPlaying = true;
    }

    public void setTextColor(int color) {
        mColor = color;
        mPaint.setColor(mColor);

        invalidate();
    }

    public void setTextSize(int size) {
        if (size < 0) {
            return;
        }
        mTextSize = size;
        int pxSize = DisplayUtils.dip2px(getContext(), mTextSize);
        mPaint.setTextSize(pxSize);

        calRequireSize();
        requestLayout();
        mOffset = mCurrentNum * mHei;
        invalidate();
    }
}
