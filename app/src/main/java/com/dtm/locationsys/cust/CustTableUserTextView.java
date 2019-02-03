package com.dtm.locationsys.cust;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.dtm.locationsys.R;


public class CustTableUserTextView extends AppCompatTextView {

    Paint paint = new Paint();

    public CustTableUserTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
//        int color = Color.parseColor("#80b9f2");
        // 为边框设置颜色
        paint.setColor(ContextCompat.getColor(context, R.color.gray_8f));
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 画TextView的4个边
        canvas.drawLine(0, 0, this.getWidth() - 1, 0, paint);
//        canvas.drawLine(0, 0, 0, this.getHeight() - 1, paint);
//        canvas.drawLine(this.getWidth() - 1, 0, this.getWidth() - 1, this.getHeight() - 1, paint);
//        canvas.drawLine(0, this.getHeight() - 1, this.getWidth() - 1, this.getHeight() - 1, paint);
    }

}
