package com.example.sky.phoneintentactivity.util;

import android.os.Environment;

import java.io.File;

/**
 * Created by sky on 9/14/2016.
 */
public class FroyoAlbumStorageDirFactory implements  AlbumStorageDirFactory {
    @Override
    public File getAlbumStorageDir(String albumName) {
        return new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                albumName
        );
    }
}
