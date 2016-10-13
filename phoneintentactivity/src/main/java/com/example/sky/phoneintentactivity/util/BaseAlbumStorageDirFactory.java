package com.example.sky.phoneintentactivity.util;

import android.os.Environment;

import java.io.File;

/**
 * Created by sky on 9/14/2016.
 */
public class BaseAlbumStorageDirFactory implements AlbumStorageDirFactory {
    // Standard storage location for digital camera files
    private static final String CAMERA_DIR = "/dcim/";

    @Override
    public File getAlbumStorageDir(String albumName) {
        return new File(
                Environment.getExternalStorageDirectory()
                        + CAMERA_DIR
                        + albumName
        );
    }
}
