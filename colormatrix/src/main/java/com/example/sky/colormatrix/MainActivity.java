package com.example.sky.colormatrix;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btnColorMatrixApi(View view) {
        startActivity(new Intent(this, ColorMatrixApi.class));
    }

    public void btnColorMatrixCustom(View view) {
        startActivity(new Intent(this, ColorMatrixCustom.class));
    }

    public void btnColorPixel(View view) {
        startActivity(new Intent(this, ColorPixel.class));
    }
}
