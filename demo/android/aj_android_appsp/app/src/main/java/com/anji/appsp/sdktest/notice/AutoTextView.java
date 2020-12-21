package com.anji.appsp.sdktest.notice;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class AutoTextView extends AppCompatTextView {

    public static final String TAG = "AutoTextView";

    /**
     * 字幕滚动的速度 快，普通，慢
     */
    public static final int SCROLL_SLOW = 0;
    public static final int SCROLL_NORM = 1;
    public static final int SCROLL_FAST = 2;

    /**
     * 字幕内容
     */
    private String mText;

    /**
     * 字幕字体颜色
     */
    private int mTextColor;

    /**
     * 字幕字体大小
     */
    private float mTextSize;

    private float offX = 0f;

    private float mStep = 0.5f;

    private Rect mRect = new Rect();

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    boolean overScroll = false;

    public AutoTextView(Context context) {
        super(context);
        setSingleLine(true);
    }

    public AutoTextView(Context context, AttributeSet attr) {
        super(context, attr);
        setSingleLine(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        requestTextChange();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    public void draw(Canvas canvas) {
        float x, y;
        y = getMeasuredHeight() / 2 + (-(mPaint.getFontMetrics().ascent + mPaint.getFontMetrics().descent) / 2);
        if (mText == null) {
            super.draw(canvas);
            return;
        }
//        if (!overScroll) {
//            x = getMeasuredWidth() / 2 - mRect.width() / 2;
//
//            canvas.drawText(mText, x, y, mPaint);
//            return;
//        }
        x = offX;
        canvas.drawText(mText, x, y, mPaint);
        offX -= mStep;
        if (offX <= -mRect.width()) {
            offX = getMeasuredWidth();
        }
        invalidate();
    }

    public void requestTextChange() {
        mText = getText().toString();
        mTextColor = getCurrentTextColor();
        mTextSize = getTextSize();
        mPaint.setColor(mTextColor);
        mPaint.setTextSize(mTextSize);
        mPaint.getTextBounds(mText, 0, mText.length(), mRect);
        overScroll = getMeasuredWidth() < mRect.width();
        invalidate();
    }

    /**
     * 设置字幕滚动的速度
     */
    public void setScrollMode(int scrollMod) {
        if (scrollMod == SCROLL_SLOW) {
            mStep = 0.5f;
        } else if (scrollMod == SCROLL_NORM) {
            mStep = 1f;
        } else {
            mStep = 2.4f;
        }
    }

}
