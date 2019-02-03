package com.dtm.locationsys.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.dtm.locationsys.R;

/**
 * Created by pq on 10/6/17.
 */

public class HistogramView extends View {
    Context mContext;

    Paint mPaint;

    Paint mBackgoundPaint;

    Paint mTextPaint;

    int mColor;

    int mCorner = 40;

    int mTextPadding = 20;

    int mMaxData;

    int mData;

    public HistogramView(Context context) {
        super(context);

        mContext = context;
    }

    public HistogramView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mContext = context;

        initPaint();
    }

    public HistogramView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;

        initPaint();
    }

    public void setMaxData(int maxData) {
        mMaxData = maxData;
    }

    public void updateData(int updateData) {
        mData = updateData;
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        mColor = mContext.getResources().getColor(R.color.colorPrimary);
        mPaint.setColor(mColor);

        mBackgoundPaint = new Paint();
        mBackgoundPaint.setColor(Color.BLACK);

        mTextPaint = new Paint(mPaint);
        mTextPaint.setColor(Color.WHITE);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        /* draw background rect */
        float textHeight = getTextHeight();

        RectF backgroundRect = createBackgroundRect(textHeight);
        canvas.drawRect(backgroundRect, mBackgoundPaint);

        /* draw column rect */
        RectF columRect = createColumnRect(textHeight, backgroundRect.height());

        canvas.drawRoundRect(columRect, DensityUtils.pxTodip(mContext, mCorner),
                DensityUtils.pxTodip(mContext, mCorner), mPaint);
        /* override round rect top */
        RectF overrideRect = new RectF(columRect);
        overrideRect.top = overrideRect.top + overrideRect.height()/2;
        canvas.drawRect(overrideRect, mPaint);

        /* draw data text */
        String drawText = mData + "";
        float textX = getWidth() * 0.5f - mPaint.measureText(drawText) * 0.5f;
        float textY = getHeight() - columRect.height() - 2 * DensityUtils.pxTodip(mContext, mTextPadding);
        canvas.drawText(drawText, textX, textY,
                mTextPaint);
    }

    private float calculateDrawTextSize() {
        String drawText = mMaxData + "";
        int drawTextLen = drawText.length();

        float drawTextSize = getWidth() / 2;
        if (drawTextLen >= 4) {
            drawTextSize = getWidth() / (drawTextLen - 1);
        }

        return drawTextSize;
    }

    private float getTextHeight() {
        float drawTextSize = calculateDrawTextSize() * 1.2f;

        mPaint.setTextSize(drawTextSize);

        mTextPaint.setTextSize(drawTextSize);

        return mPaint.ascent() + mPaint.descent();
    }

    private RectF createBackgroundRect(float textHeight) {
        float backgroundRectHeight = getHeight() - textHeight - 2 * DensityUtils.pxTodip(mContext, mTextPadding);

        RectF backgoundRect = new RectF(0, 0, getWidth(), getHeight());

        return backgoundRect;
    }

    private RectF createColumnRect(float textHeight, float maxHeight) {
        float rectHeight = maxHeight * mData / mMaxData;

        RectF columnRect = new RectF(0, getHeight() - rectHeight, getWidth(), getHeight());

        return columnRect;
    }
}
