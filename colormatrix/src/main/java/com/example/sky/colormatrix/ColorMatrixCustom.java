package com.example.sky.colormatrix;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;

public class ColorMatrixCustom extends Activity {
    private GridLayout mGridLayout;
    private ImageView mImageView;
    private Bitmap mBitmap;
    private EditText[] mEditTexts = new EditText[20];
    private float[] mColorMatrix = new float[20];
    private int mEditWidth,mEditHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_matrix_custom);
        mGridLayout = (GridLayout) findViewById(R.id.grid_layout);
        mImageView = (ImageView) findViewById(R.id.image_view);
        mGridLayout.post(new Runnable() {
            @Override
            public void run() {
                mEditWidth = mGridLayout.getWidth() / 5;
                mEditHeight = mGridLayout.getHeight() / 4;
                initialMatrix();
            }
        });
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.scenery);
        mImageView.setImageBitmap(mBitmap);
    }

    private void initialMatrix() {
        mGridLayout.removeAllViews();
        for (int i = 0; i < 20; i++) {
            EditText edt = new EditText(this);
            edt.setGravity(Gravity.CENTER);
            edt.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
            if (i % 6 == 0) {
                edt.setText(String.valueOf(1));
                mColorMatrix[i] = 1;
            } else {
                edt.setText(String.valueOf(0));
                mColorMatrix[i] = 0;
            }
            mEditTexts[i] = edt;
            mGridLayout.addView(edt, mEditWidth, mEditHeight);
        }
    }

    private float[] getColorMatrixs() {
        for (int i = 0; i < 20; i++) {
            mColorMatrix[i] = Float.parseFloat(mEditTexts[i].getText().toString());
        }
        return mColorMatrix;
    }

    public void btnChange(View v) {
        mImageView.setImageBitmap(ImageHelper.handleImageColorMatrix(mBitmap, getColorMatrixs()));
    }

    public void btnReset(View v) {
        initialMatrix();
        mImageView.setImageBitmap(ImageHelper.handleImageColorMatrix(mBitmap, getColorMatrixs()));
    }
}
