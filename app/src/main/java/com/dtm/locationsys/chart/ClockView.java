package com.dtm.locationsys.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.dtm.locationsys.R;

/**
 * Created by pq on 10/7/17.
 */

public class ClockView extends View {
    float mPointerX;

    float mPointerY;

    float mPlateRadius;

    float[] mShortAxisEndPoint;

    float[] mLongAxisEndPoint;

    Paint mPaint;

    Paint mPointerPaint;

    Context mContext;

    private final static int TEXT_PADDING = 20;

    private final float ZERO_PRECISION = 0.001f;

    public ClockView(Context context) {
        super(context);

        mContext = context;

        initializePaint();

        initializeAxisPoint();
    }

    public ClockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mContext = context;

        initializePaint();

        initializeAxisPoint();
    }

    public ClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;

        initializePaint();

        initializeAxisPoint();
    }

    public void updatePointerPosition(double posX, double posY) {
        mPointerX = (float)posX;
        mPointerY = (float)posY;

        postInvalidate();
    }

    private void initializePaint() {
        mPaint = new Paint();

        mPaint.setColor(mContext.getResources().getColor(R.color.colorMainGray));

        mPaint.setAntiAlias(true);

        mPaint.setStyle(Paint.Style.STROKE);

        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(6);

        mPointerPaint = new Paint(mPaint);
        mPointerPaint.setStyle(Paint.Style.FILL);
        mPointerPaint.setColor(mContext.getResources().getColor(R.color.colorPrimary));
    }

    private void initializeAxisPoint() {
        mShortAxisEndPoint = new float[2];

        mLongAxisEndPoint = new float[2];
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        /* draw dial plate */
        drawDialPlate(canvas);

        /* draw graduation line */
        drawGraduation(canvas);

        /* draw pointer */
        drawPointer(canvas);
    }

    private void drawDialPlate(Canvas canvas) {
        canvas.translate(getWidth()/2, getHeight()/2);

        mPlateRadius = getDialPlateRadius();
        canvas.drawCircle(0, 0, mPlateRadius, mPaint);
    }

    private void drawGraduation(Canvas canvas) {
        Paint graduationPaint = new Paint(mPaint);
        graduationPaint.setStrokeWidth(3);
        graduationPaint.setTextSize(DensityUtils.dipTopx(mContext, 16));

        float y = getDialPlateRadius();
        int count = 12; //total graduations

        for(int i=0 ; i <count ; i++){
            canvas.drawLine(0f, y, 0, y+12f, mPaint);
            canvas.rotate(360/count,0f,0f); //rotate canvas
        }
    }

    private void drawPointer(Canvas canvas) {
        Float slope = caculateDirectionSlope(mPointerX, mPointerY);

        caculateShortAxisEndPoint(mPointerX, slope);

        caculateLongAxisEndPoint(mPointerX, slope);

        canvas.drawLine(mShortAxisEndPoint[0], mShortAxisEndPoint[1], mLongAxisEndPoint[0],
                mLongAxisEndPoint[1], mPointerPaint);

        Paint pointerPaint = new Paint(mPaint);
        pointerPaint.setStyle(Paint.Style.FILL);

        canvas.drawCircle(0, 0, getWidth()/20, pointerPaint);
    }

    private void caculateShortAxisEndPoint(float directionPosX, Float slope) {
        float shortAxisLength = getHeight()/10;

        float posX = 0.0f;
        float posY = -shortAxisLength;
        if (null != slope) {
            posX = shortAxisLength / (float)Math.sqrt(Math.pow(slope, 2) + 1);
            if (directionPosX > 0) {
                posX = -posX;
            }

            posY = slope * posX;
        }

        mShortAxisEndPoint[0] = posX;
        mShortAxisEndPoint[1] = -posY;
    }

    private void caculateLongAxisEndPoint(float directionPosX, Float slope) {
        float longAxisLength = getHeight() / 3;

        float posX = 0.0f;
        float posY = longAxisLength;

        if (null != slope) {
            posX = longAxisLength / (float)Math.sqrt(Math.pow(slope, 2) + 1);
            if ( directionPosX < 0) {
                posX = -posX;
            }

            posY = slope * posX;
        }

        mLongAxisEndPoint[0] = posX;
        mLongAxisEndPoint[1] = -posY;
    }

    private float getDialPlateRadius() {
        int width = getWidth();
        int height = getHeight();

        int radius = ((width < height) ? width : height)/2;

        radius -= DensityUtils.pxTodip(mContext, TEXT_PADDING);

        return radius;
    }

    private Float caculateDirectionSlope(float directionPosX, float directionPosY) {
        Float slope = null;
        if (Math.abs(directionPosX - 0f) > ZERO_PRECISION) {
            slope = new Float(directionPosY / directionPosX);
        }

        return slope;
    }
}
