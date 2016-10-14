package com.example.sky.grapgicsdemo.graphics.view;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
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
        //draw path
        onDrawPath(canvas);
        //draw text
        onDrawText(canvas);
        super.onDraw(canvas);
    }

    private void onDrawText(Canvas canvas) {
        /*
        Paint textPaint = new Paint();
        //FIXME:set normal
        textPaint.setColor(Color.RED);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setStrokeWidth(5);
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(20);
        //FIXME: set style
        // bold
        textPaint.setFakeBoldText(true);
        //under line
        textPaint.setUnderlineText(true);
        //strikeThruText
        textPaint.setStrikeThruText(true);
        //tilt effect
        textPaint.setTextSkewX(0.25f);
        //FIXME: set other
        //horizontal tensile font
        textPaint.setTextScaleX(2);*/
        Paint textPaint = new Paint();
        textPaint.setColor(Color.RED);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setStrokeWidth(5);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(80);
        canvas.drawText("Android程序员", 10, 800, textPaint);
        textPaint.setStyle(Paint.Style.STROKE);
        canvas.drawText("Android程序员", 10, 900, textPaint);
        textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawText("Android程序员", 10, 1000, textPaint);
        //style
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(40);
        textPaint.setTextSkewX(0.25f);
        textPaint.setUnderlineText(true);
        textPaint.setFakeBoldText(true);
        textPaint.setStrikeThruText(true);
        canvas.drawText("Android程序员", 10, 1050, textPaint);
        textPaint.setTextSkewX(-0.25f);
        canvas.drawText("Android程序员", 300, 1050, textPaint);
        //other
        textPaint.setTextScaleX(1);
        canvas.drawText("Android程序员", 10, 1150, textPaint);
        textPaint.setTextScaleX(2);
        canvas.drawText("Android程序员", 10, 1150, textPaint);
        //specify the text location map
        textPaint.setTextSize(40);
        canvas.drawPosText("程序",
                new float[] {
                        10, 1200,
                        10, 1240
                }, textPaint);
        //FIXME:　draw text on path
        Path circlePath = new Path();
        textPaint.setStyle(Paint.Style.STROKE);
        circlePath.addCircle(200, 1400, 100, Path.Direction.CW);
        canvas.drawPath(circlePath, textPaint);
//        circlePath.reset();
        textPaint.setColor(Color.GREEN);
        textPaint.setStyle(Paint.Style.FILL);
        canvas.drawTextOnPath("Android程序员", circlePath, 0, 0, textPaint);
        Path circlePath1 = new Path();
        textPaint.setStyle(Paint.Style.STROKE);
        circlePath1.addPath(circlePath, 300, 0);
        canvas.drawPath(circlePath1, textPaint);
        textPaint.setColor(Color.GREEN);
        textPaint.setStyle(Paint.Style.FILL);
        canvas.drawTextOnPath("Android程序员", circlePath1, 30, 40, textPaint);
        //set font
//        AssetManager am = mContext.getAssets();
//        Typeface fontTypeface = Typeface.createFromAsset(am, "fonts/jian_luobo.ttf");
//        textPaint.setTypeface(fontTypeface);
//        textPaint.setTextSize(20);
//        canvas.drawText("Android程序员", 20, 1500, textPaint);

    }

    /**
     * use path can draw any graphics
     * @param canvas
     */
    private void onDrawPath(Canvas canvas) {
        //FIXME: drawLineByPath
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        Path lp = new Path();
        //set the beginning of the point
        lp.moveTo(10, 520);
        //the relative value of a previous point
        lp.rMoveTo(40, 0);
        //move to next point
        lp.lineTo(100, 520);
        lp.rLineTo(20, 20);
        lp.lineTo(100, 560);
        //form a closed loop, ending
        lp.close();
        canvas.drawPath(lp, paint);
        //FIXME:drawArcByPath
        //reset paint
        paint.reset();
        Path arcPath = new Path();
        RectF arcRect = new RectF(10, 570, 200, 650);
        //FIXME:counterclockwise rectangle
        paint.setColor(Color.RED);
        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.STROKE);
        arcPath.addRect(arcRect, Path.Direction.CCW);
        canvas.drawPath(arcPath, paint);
        //use green draw arc
        arcPath.reset();
        paint.setColor(Color.GREEN);
        arcPath.addArc(arcRect, 0, 240);
        canvas.drawPath(arcPath, paint);
        //FIXME: draw circle by pah
        Path path = new Path();
        //clockwise draw circle
        paint.setAntiAlias(true);
        path.addCircle(400, 600, 30, Path.Direction.CW);
        canvas.drawPath(path, paint);
        //FIXME: copy path
        paint.setColor(Color.RED);
        Path circlePath = new Path();
        circlePath.addPath(path, 40, 0);
        canvas.drawPath(circlePath, paint);
        //FIXME: draw oval
        path.reset();
        RectF ovalRectF = new RectF(10, 680, 120, 750);
        //FIXME: draw round rectangle
        paint.setColor(Color.RED);
        path.addRoundRect(ovalRectF, 10, 10, Path.Direction.CCW);
        canvas.drawPath(path, paint);
        path.reset();
        paint.setColor(Color.GREEN);
        path.addOval(ovalRectF, Path.Direction.CCW);
        canvas.drawPath(path, paint);
        path.reset();
        path.addRoundRect(
                new RectF(150, 680, 260, 750),
                //specify the size of the rounded corners
                new float[] {
                        5, 10,
                        10, 15,
                        15, 20,
                        20, 25
                },
                Path.Direction.CW
        );
        paint.setColor(Color.GREEN);
        canvas.drawPath(path, paint);
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
