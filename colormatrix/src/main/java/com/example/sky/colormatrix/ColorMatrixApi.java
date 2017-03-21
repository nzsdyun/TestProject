package com.example.sky.colormatrix;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;

public class ColorMatrixApi extends Activity implements SeekBar.OnSeekBarChangeListener {
    private static final String TAG = "ColorMatrixApi";
    private static final int MIN_VALUE = 127;
    private static final int MAX_VALUE = 255;
    private ImageView mImageView;
    private SeekBar mBlue, mSaturation, mBrightness;
    private Bitmap mBitmap;
    private float mBlueValue = MIN_VALUE;
    private float mSaturationValue = MIN_VALUE;
    private float mBrightnessValue = MIN_VALUE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_matrix_api);
        mImageView = (ImageView) findViewById(R.id.image_view);
        mBlue = (SeekBar) findViewById(R.id.blue);
        mSaturation = (SeekBar) findViewById(R.id.saturation);
        mBrightness = (SeekBar) findViewById(R.id.brightness);
        mBlue.setMax(MAX_VALUE);
        mBlue.setProgress(MIN_VALUE);
        mSaturation.setMax(MAX_VALUE);
        mSaturation.setProgress(MIN_VALUE);
        mBrightness.setMax(MAX_VALUE);
        mBrightness.setProgress(MIN_VALUE);
        mBlue.setOnSeekBarChangeListener(this);
        mSaturation.setOnSeekBarChangeListener(this);
        mBrightness.setOnSeekBarChangeListener(this);
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.scenery);
        mImageView.setImageBitmap(mBitmap);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.blue:
                mBlueValue = (progress - MIN_VALUE) * 1.0f / MIN_VALUE * 180;
                break;
            case R.id.saturation:
                mSaturationValue = progress * 1.0f / MIN_VALUE;
                break;
            case R.id.brightness:
                mBrightnessValue = progress * 1.0f / MIN_VALUE;
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        Bitmap dest = ImageHelper.handleImageEffect(mBitmap, mBlueValue, mSaturationValue, mBrightnessValue);
        Log.i(TAG, "dest:" + dest + ", blue value:" + mBlueValue
                + ", saturation value:" + mSaturationValue + ", brightness value:" + mBrightnessValue);
        mImageView.setImageBitmap(ImageHelper.handleImageEffect(mBitmap, mBlueValue, mSaturationValue, mBrightnessValue));
    }
}
