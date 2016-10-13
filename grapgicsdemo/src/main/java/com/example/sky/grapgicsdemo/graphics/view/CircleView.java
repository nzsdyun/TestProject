package com.example.sky.grapgicsdemo.graphics.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * draw circle,does not support wrap_content and match_content
 * @author sky
 */
public class CircleView  extends View {
    private Paint mPaint;
    public CircleView(Context context) {
        super(context);
        initPaint();
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(4);
        mPaint.setShadowLayer(10, 0, 0, Color.GREEN);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLUE);
        canvas.drawCircle(50, 50, 50, mPaint);
        super.onDraw(canvas);
    }
}
