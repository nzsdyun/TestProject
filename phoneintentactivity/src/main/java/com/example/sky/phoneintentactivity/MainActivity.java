package com.example.sky.phoneintentactivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.VideoView;

import com.example.sky.phoneintentactivity.util.AlbumStorageDirFactory;
import com.example.sky.phoneintentactivity.util.BaseAlbumStorageDirFactory;
import com.example.sky.phoneintentactivity.util.FroyoAlbumStorageDirFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_VIDEO_CAPTURE = 2;
    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";
    private static final String VIDEO_STORAGE_KEY = "viewvideo";
    private static final String VIDEOVIEW_VISIBILITY_STORAGE_KEY = "videoviewvisibility";

    private ImageView mPicture;
    private CheckBox mCheckBox;
    private VideoView mVideoView;
    private AlbumStorageDirFactory mAlbumStorageDirFactory;
    private String mCurrentPhotoPath = null;
    private Uri mVideoUri = null;
    private boolean mIsSave = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPicture = (ImageView) findViewById(R.id.picture);
        mCheckBox = (CheckBox) findViewById(R.id.checkbox);
        mVideoView = (VideoView) findViewById(R.id.video_view);
        mCheckBox.setOnCheckedChangeListener(mCheckedChangeListener);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            mAlbumStorageDirFactory = new FroyoAlbumStorageDirFactory();
        } else {
            mAlbumStorageDirFactory = new BaseAlbumStorageDirFactory();
        }
    }

    private final CompoundButton.OnCheckedChangeListener mCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            mIsSave = isChecked;
        }
    };


    public void Click(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tack_picture:
                tackPicture(mIsSave);
                break;
            case R.id.tack_video:
                tackVideo();
                break;
        }
    }

    private void tackVideo() {
        if (!isSupportCamera(this)) return;
        Intent tackVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (tackVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(tackVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    private void tackPicture(boolean isSave) {
        if (!isSupportCamera(this)) return;
        Intent tackPictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (isSave) {
            File photoFil = setUpPhotoFile();
            mCurrentPhotoPath = photoFil.getAbsolutePath();
            Log.d(TAG, "current photo path:" + mCurrentPhotoPath);
            tackPictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFil));
        }
        if (tackPictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(tackPictureIntent, REQUEST_IMAGE_CAPTURE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "request code:" + requestCode + ", result code:" + requestCode + ", data:" + data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                if (data != null) {
                    Uri dataUri = data.getData();
                    if (dataUri != null) {
                        String path = null;
                        Cursor cursor = null;
                        try {
                            String[] projection = {MediaStore.Images.Media.DATA };
                            cursor = getContentResolver().query(dataUri, projection, null, null, null);
                            if (cursor != null && cursor.getCount() > 0) {
                                cursor.moveToFirst();
                                path = cursor.getString(cursor.getColumnIndex(projection[0]));
                            }
                        } finally {
                            if (cursor != null)
                                cursor.close();
                        }
                        try {
                            Log.i(TAG, "path:" + path);
                            InputStream inputStream =  getContentResolver().openInputStream(Uri.fromFile(new File(path)));
                            showPicture(BitmapFactory.decodeStream(inputStream));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Bundle bundle = data.getExtras();
                        Log.i(TAG, "bundle:" + bundle.toString());
                        showPicture((Bitmap) bundle.get("data"));
                    }
                } else {
                    if (mCurrentPhotoPath != null) {
                        //direct use of the path into a series of pictures
                        showPicture();
                    }
                }
                break;
            case REQUEST_VIDEO_CAPTURE:
                Uri videoUri = data.getData();
                showVideo(videoUri);
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showVideo(Uri videoUri) {
        if (videoUri == null) return;
        mVideoUri = videoUri;
        mPicture.setVisibility(View.INVISIBLE);
        mVideoView.setVisibility(View.VISIBLE);
        mVideoView.setVideoURI(mVideoUri);
        if (!mVideoView.isPlaying()) {
            mVideoView.start();
        }
    }

    private void showPicture() {
        if (mCurrentPhotoPath != null) {
            setPic();
            galleryAddPic();
            mCurrentPhotoPath = null;
        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent  = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }

    private void setPic() {
        int targetW = mPicture.getWidth();
        int targetH = mPicture.getHeight();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, options);
        int photoW = options.outWidth;
        int photoH = options.outHeight;
        int scaleFactor = 1;
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW/targetW, photoH/targetH);
        }
        options.inJustDecodeBounds = false;
        options.inSampleSize = scaleFactor;
        options.inPurgeable = true;
        showPicture(BitmapFactory.decodeFile(mCurrentPhotoPath, options));
    }

    private void showPicture(Bitmap bitmap) {
        if (mVideoView.isPlaying()) {
            mVideoView.suspend();
        }
        mVideoUri = null;
        mVideoView.setVisibility(View.INVISIBLE);
        mPicture.setVisibility(View.VISIBLE);
        mPicture.setImageBitmap(bitmap);
    }

    private boolean isSupportCamera(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    public File setUpPhotoFile() {
        File tempFile = createTempFile();
        Log.d(TAG, "tempFile:" + tempFile.getAbsolutePath());
        return tempFile;
    }

    private File createTempFile() {
        String tempStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + tempStamp + "_";
        File albumDir = getAlbumDir();
        Log.d(TAG, "album dir:" + albumDir.getAbsolutePath());
        File tempFile = null;
        try {
            tempFile = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tempFile;
    }

    public File getAlbumDir() {
        File albumDir = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            albumDir = mAlbumStorageDirFactory.getAlbumStorageDir("take_picture");
            if (albumDir != null) {
                if (!albumDir.mkdirs()) {
                    if (!albumDir.exists()) {
                        Log.d("CameraSample", "failed to create directory");
                        return null;
                    }
                }
            }
        } else {
            Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
        }
        return albumDir;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(VIDEO_STORAGE_KEY, mVideoUri);
        outState.putBoolean(VIDEOVIEW_VISIBILITY_STORAGE_KEY, (mVideoUri != null));
        if (mVideoView.isPlaying()) {
            mVideoView.suspend();
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mVideoUri = savedInstanceState.getParcelable(VIDEO_STORAGE_KEY);
        mVideoView.setVideoURI(mVideoUri);
        mVideoView.setVisibility(savedInstanceState.getBoolean(VIDEOVIEW_VISIBILITY_STORAGE_KEY)
                ? View.VISIBLE
                : View.INVISIBLE);
        if (!mVideoView.isPlaying() && savedInstanceState.getBoolean(VIDEOVIEW_VISIBILITY_STORAGE_KEY)) {
            mVideoView.start();
        }
    }
}
