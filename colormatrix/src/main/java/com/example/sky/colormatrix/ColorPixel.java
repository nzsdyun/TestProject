package com.example.sky.colormatrix;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;

public class ColorPixel extends Activity implements View.OnClickListener{
    private ImageView mImageView;
    private GridLayout mControlLayout;
    private Bitmap mBitmap;
    private int mButtonWidth, mButtonHeight;
    private ImageHelper.Effect[] mControls = ImageHelper.Effect.values();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_pixel);
        mImageView = (ImageView) findViewById(R.id.image_view);
        mControlLayout = (GridLayout) findViewById(R.id.control_layout);
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.scenery);
        mImageView.setImageBitmap(mBitmap);
        mControlLayout.post(new Runnable() {
            @Override
            public void run() {
                mButtonWidth = mControlLayout.getWidth() / mControlLayout.getColumnCount();
                mButtonHeight = mControlLayout.getHeight() / mControlLayout.getRowCount();
                initialControls();
            }
        });

    }

    private void initialControls() {
        mControlLayout.removeAllViews();
        for (int i = 0; i < mControls.length; i++) {
            ImageHelper.Effect effect = mControls[i];
            Button btn = new Button(this);
            btn.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
            btn.setId(effect.getId());
            btn.setText(effect.getText());
            btn.setTag(effect);
            btn.setOnClickListener(this);
            mControlLayout.addView(btn, mButtonWidth, mButtonHeight);
        }
    }


    @Override
    public void onClick(View v) {
        if (v == null || !(v.getTag() instanceof ImageHelper.Effect))
            return;
        ImageHelper.Effect type = (ImageHelper.Effect) v.getTag();
        switch (type) {
            case NORMAL_PHOTO:
                mImageView.setImageBitmap(mBitmap);
                break;
            case OLD_PHOTO:
                mImageView.setImageBitmap(ImageHelper.handleImageEffect(mBitmap, ImageHelper.Effect.OLD_PHOTO));
                break;
            case FILM_PHOTO:
                mImageView.setImageBitmap(ImageHelper.handleImageEffect(mBitmap, ImageHelper.Effect.FILM_PHOTO));
                break;
            case RELIEF_PHOTO:
                mImageView.setImageBitmap(ImageHelper.handleImageEffect(mBitmap, ImageHelper.Effect.RELIEF_PHOTO));
                break;
        }
    }
}
