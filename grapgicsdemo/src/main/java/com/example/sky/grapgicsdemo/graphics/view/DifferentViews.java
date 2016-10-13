package com.example.sky.grapgicsdemo.graphics.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.example.sky.grapgicsdemo.R;

/**
 * draw all kinds of simple graphics, does not support wrap_content and match_content
 * @author sky
 */
public class DifferentViews extends View {
    private Paint mPaint;
    private Context mContext;
    public DifferentViews(Context context) {
        super(context);
        initPaint();
        mContext = context;
    }

    public DifferentViews(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        mContext = context;
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(6);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //draw canvas background
        canvas.drawColor(Color.BLUE);
        //draw arc
        onDrawArc(canvas);
        //draw bitmap
        onDrawBitmap(canvas);
        //draw line
        onDrawLine(canvas);
        //draw oval
        onDrawOval(canvas);
        //draw point
        onDrawPoint(canvas);
        //draw rect
        onDrawRect(canvas);
        super.onDraw(canvas);
    }

    private void onDrawRect(Canvas canvas) {
        Paint rectPaint = new Paint();
        rectPaint.setColor(Color.RED);
        rectPaint.setStyle(Paint.Style.FILL);
        //draw square
        canvas.drawRect(new RectF(10, 450, 60, 500), rectPaint);
        rectPaint.setColor(Color.BLACK);
        rectPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        rectPaint.setStrokeWidth(5);
        //draw rectangle
        canvas.drawRect(new RectF(70, 450, 150, 500), rectPaint);
        //draw rounded rectangle
        rectPaint.setAntiAlias(true);
        rectPaint.setColor(Color.WHITE);
        rectPaint.setStrokeWidth(4);
        rectPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRoundRect(new RectF(180, 450, 240, 500), 10, 10, rectPaint);

    }

    private void onDrawPoint(Canvas canvas) {
        Paint point = new Paint();
        point.setColor(Color.RED);
        point.setStyle(Paint.Style.FILL);
        point.setStrokeWidth(10);
        //use paint's cap
        point.setStrokeCap(Paint.Cap.ROUND);
        //draw single point
        canvas.drawPoint(50, 390, point);
        point.setColor(Color.GREEN);
        point.setStyle(Paint.Style.STROKE);
        point.setStrokeWidth(20);
        //use paint's join
        point.setStrokeJoin(Paint.Join.ROUND);
        canvas.drawPoint(100, 390, point);
        //draw multiple points
        point.setColor(Color.BLACK);
        point.setStrokeWidth(10);
        point.setStyle(Paint.Style.FILL);
        float[] pts = new float[] {
                120, 390,
                150, 400,
                130, 410,
                160, 420,
                180, 395,
                175, 405
        };
        canvas.drawPoints(pts, point);
        point.setColor(Color.RED);
        //in fact only drawn (150, 400), (130, 410), (160, 420), (180, 395) practice of the point
        canvas.drawPoints(pts, 2, 8, point);

    }

    private void onDrawOval(Canvas canvas) {
//        canvas.drawOval(10, 250, 60, 300, mPaint);
        Paint ovalPaint = new Paint();
        ovalPaint.setColor(Color.RED);
        ovalPaint.setStyle(Paint.Style.STROKE);
        ovalPaint.setStrokeWidth(4);
        RectF ovalRectF = new RectF(10, 280, 60, 330);
        canvas.drawRect(ovalRectF, ovalPaint);
        ovalPaint.setColor(Color.GREEN);
        //draw round,radius is (60 - 10)/2 = 25
        canvas.drawOval(ovalRectF, ovalPaint);
        ovalRectF = new RectF(100, 280, 250, 330);
        ovalPaint.setColor(Color.RED);
        canvas.drawRect(ovalRectF, ovalPaint);
        ovalPaint.setColor(Color.GREEN);
        //draw oval
        canvas.drawOval(ovalRectF, ovalPaint);
    }

    private void onDrawLine(Canvas canvas) {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLACK);
        //draw single line
        canvas.drawLine(10, 150, 100, 250, mPaint);
        //draw multiple lines
        mPaint.setColor(Color.RED);
        float[] pts = new float[] {
                50, 150,
                150, 250,
                80, 150,
                100, 250
        };
        canvas.drawLines(pts, mPaint);
        //draw multiple lines
        float[] ptsOffset = new float[] {
                50, 150,
                150, 250,
                110, 150,
                180, 250,
                80, 150,
                100, 250

        };
        //in fact only drawn (110, 150), (180, 250) practice of the line
        canvas.drawLines(ptsOffset, 4, 4, mPaint);
    }

    private void onDrawBitmap(Canvas canvas) {
//        Drawable drawable = mContext.getResources().getDrawable(R.drawable.ic_launcher);
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_launcher);
        canvas.drawBitmap(bitmap, 300, 10, null);
        //cut out bitmap,src is cut area and det is show area
        canvas.drawBitmap(bitmap, new Rect(10, 10, 80, 80), new RectF(500, 10, 580, 100), mPaint);
    }

    private void onDrawArc(Canvas canvas) {
        canvas.drawColor(Color.BLUE);
        //use point specify the location of the arc rectangular outside
//        canvas.drawArc(10, 10, 100, 100, 0, 90, true, mPaint);
        RectF arcRectF = new RectF(10, 10, 100, 100);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.RED);
        //use red color draw Rect
        canvas.drawRect(arcRectF, mPaint);
        mPaint.setColor(Color.GREEN);
        canvas.drawArc(arcRectF, 0, 90, true, mPaint);
        arcRectF = new RectF(110, 10, 210, 100);
        mPaint.setColor(Color.RED);
        //use red color draw Rect
        canvas.drawRect(arcRectF, mPaint);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.GREEN);
        //use RectF specify the location of the arc, on the right side of the first arc 100 location
        canvas.drawArc(arcRectF, 0, -120, false, mPaint);
    }
}
