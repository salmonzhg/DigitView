package com.salmonzhg.digitview.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.salmonzhg.digitview.R;
import com.salmonzhg.digitview.utils.DisplayUtils;


/**
 * Created by Salmon on 2016/6/23 0023.
 */
public class DigitalGroupView extends LinearLayout {
    private static final int DEFAULT_COLOR = Color.BLACK;
    private static final int DEFAULT_TEXT_SIZE = 16;
    private static final int DEFAULT_FIGURE_COUNT = 1;
    private static final int DEFAULT_INTERVAL = 2;
    private int mFigureCount = DEFAULT_FIGURE_COUNT;
    private int mColor = DEFAULT_COLOR;
    private int mTextSize = DEFAULT_TEXT_SIZE;
    private int mTextInterval = DEFAULT_INTERVAL;
    private int mDigits = 0;
    private int[] mParsedDigits;

    public DigitalGroupView(Context context) {
        super(context);

        init(context, null);
    }

    public DigitalGroupView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setOrientation(HORIZONTAL);

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DigitalGroupView);

            mFigureCount = a.getInteger(R.styleable.DigitalGroupView_digiGroupFigureCounts, DEFAULT_FIGURE_COUNT);
            mColor = a.getColor(R.styleable.DigitalGroupView_digiGroupColor, Color.BLACK);
            mTextSize = a.getDimensionPixelSize(R.styleable.DigitalGroupView_digiGroupTextSize, DEFAULT_TEXT_SIZE);
            mTextInterval = a.getDimensionPixelOffset(R.styleable.DigitalGroupView_digiGroupInterval, DEFAULT_INTERVAL);
        }

        resetChildren();
    }

    public void setFigureCount(int count) {
        if (count < 1)
            return;
        mFigureCount = count;
        resetChildren();

        requestLayout();
        invalidate();
    }

    public void setInterval(int interval) {
        if (interval < 0) {
            return;
        }
        mTextInterval = dp2px(interval);

        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            DigitalView v = (DigitalView) getChildAt(i);
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            if (i > 0)
                params.leftMargin = dp2px(mTextInterval);
            v.setLayoutParams(params);
        }

        requestLayout();
    }

    public void setColor(int color) {
        mColor = color;
        for (DigitalView v : getChildren()) {
            v.setTextColor(mColor);
        }
        invalidate();
    }

    public void setTextSize(int size) {
        mTextSize = size;
        for (DigitalView v : getChildren()) {
            v.setTextSize(mTextSize);
        }
        invalidate();
    }

    private int dp2px(int pxValue) {
        return DisplayUtils.dip2px(getContext(), pxValue);
    }

    public void setDigits(int digits) {
        digits = Math.abs(digits);
        mDigits = digits;

        parseDigits();
        play();
    }

    private void play() {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            DigitalView v = (DigitalView) getChildAt(i);
            v.startAnim(mParsedDigits[i]);
        }
    }

    /**
     * 截取数字，规则如下：
     * | 1 | 5 | 6 | 7 |  <-- 输入
     *       ↓   ↓   ↓
     *     | 0 | 0 | 0 |  <-- view中的长度
     *           ↑   ↑
     *         | 5 | 9 |  <-- 输入
     */
    private void parseDigits() {
        String s = String.valueOf(mDigits);
        int[] newParsed = new int[mFigureCount];
        int shorterOne = s.length() < newParsed.length ? s.length() : newParsed.length;

        int index = newParsed.length - 1;
        for (int i = s.length() - 1; i >= s.length() - shorterOne; i--) {
            newParsed[index] = Integer.parseInt(s.substring(i, i + 1));
            index--;
        }
        mParsedDigits = newParsed;
    }

    private void resetChildren() {
        mParsedDigits = new int[mFigureCount];

        removeAllViews();
        for (int i = 0; i < mFigureCount; i++) {
            DigitalView v = new DigitalView(getContext());
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            if (i > 0)
                params.leftMargin = dp2px(mTextInterval);
            v.setLayoutParams(params);
            v.setTextColor(mColor);
            v.setTextSize(mTextSize);
            addView(v);
        }
    }

    private DigitalView[] getChildren() {
        DigitalView[] result = new DigitalView[getChildCount()];
        for (int i = 0; i < result.length; i++) {
            result[i] = (DigitalView) getChildAt(i);
        }
        return result;
    }
}
