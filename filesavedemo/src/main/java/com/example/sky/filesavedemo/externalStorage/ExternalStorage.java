package com.example.sky.filesavedemo.externalStorage;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by chenkun on 9/9/2016.
 */
public class ExternalStorage {
    private static ExternalStorage sExternalStorage;
    private Context mContext;
    public ExternalStorage(Context contextw) {
        this.mContext = contextw;
    }

    public static ExternalStorage newInstance(Context context) {
        if (sExternalStorage == null) {
            synchronized (ExternalStorage.class) {
                sExternalStorage = new ExternalStorage(context);
            }
        }
        return sExternalStorage;
    }

    public boolean createPublicDir(String dirName) {
        boolean isCreate = false;
        if (isExternalStorageWriteable()) {
            File file = new File(Environment.getExternalStoragePublicDirectory(null), dirName);
            if (!file.exists()) isCreate = file.mkdirs();
        }
        return isCreate;
    }

    public boolean createPrivatecDir(String dirName, boolean isCache) {
        boolean isCreate = false;
        if (isExternalStorageWriteable()) {
            File file = new File(isCache ? mContext.getExternalCacheDir() :
                    mContext.getExternalFilesDir(null), dirName);
            if (!file.exists()) isCreate = file.mkdirs();
        }
        return isCreate;
    }

    public String read(String fileName) {
        BufferedReader bufferedReader = null;
        FileInputStream fileInputStream = null;
        StringBuffer content = new StringBuffer();
        try {
            fileInputStream = new FileInputStream(new File(fileName));
            bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            String readLine = null;
            while ((readLine = bufferedReader.readLine()) != null) {
                content.append(readLine);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return content.toString();
    }

    private boolean isExternalStorageWriteable() {
        return Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED) ? true : false;
    }
}
