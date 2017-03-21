package com.example.sky.colormatrix;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

import java.util.Random;

import static android.R.attr.bitmap;

/**
 * Created by sky on 3/20/2017.
 */

public final class ImageHelper {
    /**
     * Deal with three primary colors of hue, saturation and brightness to adjust the effect of the picture
     *
     * @param src        source image resources
     * @param hue        hue
     * @param saturation saturation
     * @param brightness brightness
     * @return dest image resources
     */
    public static Bitmap handleImageEffect(Bitmap src, float hue, float saturation, float brightness) {
        Bitmap dest = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(dest);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        ColorMatrix hueMatrix = new ColorMatrix();
        hueMatrix.setRotate(0, hue);
        hueMatrix.setRotate(1, hue);
        hueMatrix.setRotate(2, hue);

        ColorMatrix saturationMatrix = new ColorMatrix();
        saturationMatrix.setSaturation(saturation);

        ColorMatrix brightnessMatrix = new ColorMatrix();
        brightnessMatrix.setScale(brightness, brightness, brightness, 1);

        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.postConcat(hueMatrix);
        colorMatrix.postConcat(saturationMatrix);
        colorMatrix.postConcat(brightnessMatrix);
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(src, 0, 0, paint);
        return dest;
    }

    /**
     * depending on color matrix array handle image resources
     *
     * @param src               source image resources
     * @param colorMatrixValues color matrix array
     * @return dest image resources
     */
    public static Bitmap handleImageColorMatrix(Bitmap src, float[] colorMatrixValues) {
        Bitmap dest = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(dest);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        ColorMatrix colorMatrix = new ColorMatrix(colorMatrixValues);
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(src, 0, 0, paint);
        return dest;
    }

    /**
     * according to the Effect to define the effects of processing images
     *
     * @param src    source image resources
     * @param effect @see Effect
     * @return dest image resources
     */
    public static Bitmap handleImageEffect(Bitmap src, Effect effect) {
        int width = src.getWidth();
        int height = src.getHeight();
        Bitmap dest = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        int color;
        int[] oldPixels = new int[width * height];
        int[] newPixels = new int[width * height];
        int r, g, b, a, r1, g1, b1;
        src.getPixels(oldPixels, 0, width, 0, 0, width, height);
        switch (effect) {
            case OLD_PHOTO:
                for (int i = 0; i < oldPixels.length; i++) {
                    color = oldPixels[i];
                    a = Color.alpha(color);
                    r = Color.red(color);
                    g = Color.green(color);
                    b = Color.blue(color);

                    r1 = (int) (0.393 * r + 0.769 * g + 0.189 * b);
                    g1 = (int) (0.349 * r + 0.686 * g + 0.168 * b);
                    b1 = (int) (0.272 * r + 0.534 * g + 0.131 * b);

                    if (r1 > 255) {
                        r1 = 255;
                    }
                    if (g1 > 255) {
                        g1 = 255;
                    }
                    if (b1 > 255) {
                        b1 = 255;
                    }
                    color = Color.argb(a, r1, g1, b1);
                    newPixels[i] = color;
                }
                dest.setPixels(newPixels, 0, width, 0, 0, width, height);
                break;
            case FILM_PHOTO:
                for (int i = 0; i < oldPixels.length; i++) {
                    color = oldPixels[i];
                    a = Color.alpha(color);
                    r = Color.red(color);
                    g = Color.green(color);
                    b = Color.blue(color);

                    r1 = 255 - r;
                    g1 = 255 - g;
                    b1 = 255 - b;

                    if (r1 > 255) {
                        r1 = 255;
                    } else if (r1 < 0) {
                        r1 = 0;
                    }
                    if (g1 > 255) {
                        g1 = 255;
                    } else if (g1 < 0) {
                        g1 = 0;
                    }
                    if (b1 > 255) {
                        b1 = 255;
                    } else if (b1 < 0) {
                        b1 = 0;
                    }
                    color = Color.argb(a, r1, g1, b1);
                    newPixels[i] = color;
                }
                dest.setPixels(newPixels, 0, width, 0, 0, width, height);
                break;
            case RELIEF_PHOTO:
                int colorBefore = 0;
                for (int i = 1; i < oldPixels.length; i++) {
                    colorBefore = oldPixels[i - 1];
                    a = Color.alpha(colorBefore);
                    r = Color.red(colorBefore);
                    g = Color.green(colorBefore);
                    b = Color.blue(colorBefore);

                    color = oldPixels[i];

                    r1 = Color.red(color);
                    g1 = Color.green(color);
                    b1 = Color.blue(color);

                    r = r - r1 + 127;
                    g = g - g1 + 127;
                    b = b - b1 + 127;

                    if (r > 255) {
                        r = 255;
                    }
                    if (g > 255) {
                        g = 255;
                    }
                    if (b > 255) {
                        b = 255;
                    }
                    color = Color.argb(a, r, g, b);
                    newPixels[i] = color;
                }
                dest.setPixels(newPixels, 0, width, 0, 0, width, height);
                break;
//            case OIL_PHOTO:
//                Random rnd = new Random();
//                int iModel = 10;
//                int i = width - iModel;
//                while (i > 1) {
//                    int j = height - iModel;
//                    while (j > 1) {
//                        int iPos = rnd.nextInt(10) % iModel;
//                        color = src.getPixel(i + iPos, j + iPos);
//                        dest.setPixel(i, j, color);
//                        j = j - 1;
//                    }
//                    i = i - 1;
//                }
//            break;
        }
        return dest;
    }

    public enum Effect {
        NORMAL_PHOTO(100, "Normal photo"),
        OLD_PHOTO(101, "Old photo"),
        FILM_PHOTO(102, "Film photo"),
//        OIL_PHOTO(103, "Oil photo"),
        RELIEF_PHOTO(104, "Relief photo");
        private int id;
        private String text;

        Effect(int id, String text) {
            this.id = id;
            this.text = text;
        }

        public int getId() {
            return id;
        }

        public String getText() {
            return text;
        }
    }
}
