package com.example.sky.filesavedemo.internalStorage;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by sky on 9/9/2016.
 */
public class InternalStorage {
    private static InternalStorage sInternalStorage;
    private Context mContext;

    private InternalStorage(Context context) {
        this.mContext = context;
    }

    public static InternalStorage newInstance(Context context) {
        if (sInternalStorage == null) {
            synchronized (InternalStorage.class) {
                sInternalStorage = new InternalStorage(context);
            }
        }
        return sInternalStorage;
    }

    public boolean createFilesDir(String dirName) {
        boolean isCreate = false;
        File filesDir = new File(mContext.getFilesDir(), dirName);
        if (!filesDir.exists()) {
            isCreate = filesDir.mkdirs();
        } else {
            isCreate = true;
        }
        return isCreate;
    }

    public boolean createCacheDir(String dirName) {
        boolean isCreate = false;
        File cacheDir = new File(mContext.getCacheDir(), dirName);
        if (!cacheDir.exists()) {
            isCreate = cacheDir.mkdirs();
        } else {
            isCreate = true;
        }
        return isCreate;
    }

    public void write(String fileName, String content, int mode, boolean isCache) {
        BufferedWriter bufferedWriter = null;
        FileOutputStream outputStream = null;
        try {
            outputStream = isCache ? new FileOutputStream(new File(mContext.getCacheDir(), fileName))
                    : mContext.openFileOutput(fileName, mode);
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
            bufferedWriter.write(content);
            bufferedWriter.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String read(String fileName, boolean isCache) {
        return readFileCache(fileName, isCache);
    }

    private String readFileCache(String fileName, boolean isCache) {
        BufferedReader bufferedReader = null;
        FileInputStream inputStream = null;
        StringBuilder result = new StringBuilder();
        try {
            inputStream = isCache ? new FileInputStream(new File(mContext.getCacheDir(), fileName)) : mContext.openFileInput(fileName);
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String readLine = null;
            while ((readLine = bufferedReader.readLine()) != null) {
                result.append(readLine);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

}
