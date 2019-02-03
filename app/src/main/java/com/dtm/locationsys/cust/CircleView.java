package com.dtm.locationsys.cust;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.jar.Attributes;

/**
 * 测向定位仪表.
 */

public class CircleView extends View {

    // 圆心X坐标
    public float CENTER_X = 300;
    // 圆心Y坐标
    public float CENTER_Y = 300;
    // 园半径
    public float RADIUS = 210;

    // 指针终端X坐标
    public float pointerX = 0;
    // 指针终端Y坐标
    public float pointerY = 0;

    private Paint mPaint;

    private Canvas mCanvas;

    private long[] powers;

    private float mAngle;

    public CircleView(Context context, AttributeSet attrs){
        super(context, attrs);
        initPaint();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        initCircle(canvas);

        drawPointer(mAngle, canvas);
    }

    public void initPaint() {
        powers = new long[4];
        mPaint = new Paint();

        // 去锯齿
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(3);

        mAngle = DirectionHelper.getPoniterAngle(powers);
    }

    private void initCircle(Canvas canvas) {
        mPaint.setColor(Color.BLACK);
        // 绘制圆形
        canvas.drawCircle(CENTER_X, CENTER_Y, RADIUS, mPaint);
        // 左方向标
        canvas.drawLine(90, 300, 100, 300, mPaint);
        // 上方向标
        canvas.drawLine(300, 90, 300, 100, mPaint);
        // 右方向标
        canvas.drawLine(510, 300, 500, 300, mPaint);
        // 下方向标
        canvas.drawLine(300, 510, 300, 500, mPaint);
    }

    public void updateData(long[] powers) {
        mAngle = DirectionHelper.getPoniterAngle(powers);
        postInvalidate();
    }

    /**
     * 根据给定角度画出指针
     * @param angle 角度
     */
    public void drawPointer(float angle, Canvas canvas) {
        // 方向指针颜色
        mPaint.setColor(Color.RED);

        pointerX = (int) (CENTER_X+ (float)(RADIUS * Math.cos(angle * Math.PI / 180)));
        pointerY = (int) (CENTER_Y+ (float)(RADIUS * Math.sin(angle * Math.PI / 180)));

        canvas.drawLine(CENTER_X, CENTER_Y, pointerX, pointerY, mPaint);
    }
}
