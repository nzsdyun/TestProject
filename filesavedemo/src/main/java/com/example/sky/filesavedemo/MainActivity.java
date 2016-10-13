package com.example.sky.filesavedemo;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.sky.filesavedemo.internalStorage.InternalStorage;

public class MainActivity extends AppCompatActivity implements
        CompoundButton.OnCheckedChangeListener, View.OnClickListener{
    private static final String TAG = MainActivity.class.getSimpleName();
    private InternalStorage mInternalStorage;
    private CheckBox mIsExternal;
    private CheckBox mIsPrivateCache;
    private Button mCreateFPDir;
    private Button mCreateCPDir;
    private Button mRead;
    private Button mWrite;
    private boolean isInternal = false;
    private boolean isPrivateCache = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testInternalStorage();
        initViews();
        initListener();
    }

    private void initListener() {
        mIsExternal.setOnCheckedChangeListener(this);
        mIsPrivateCache.setOnCheckedChangeListener(this);

        mCreateFPDir.setOnClickListener(this);
        mCreateCPDir.setOnClickListener(this);
        mRead.setOnClickListener(this);
        mWrite.setOnClickListener(this);
    }

    private void initViews() {
        mIsExternal = (CheckBox) findViewById(R.id.internal_external);
        mIsPrivateCache = (CheckBox) findViewById(R.id.private_cache);
        mCreateFPDir = (Button) findViewById(R.id.create_fileDir_or_publicExternal);
        mCreateCPDir = (Button) findViewById(R.id.create_cacheDir_or_privateExternal);
        mRead = (Button) findViewById(R.id.read);
        mWrite = (Button) findViewById(R.id.write);
    }

    public void testInternalStorage() {
        mInternalStorage = InternalStorage.newInstance(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id = buttonView.getId();
        if (id == R.id.internal_external) {
            isInternal = isChecked;
        } else if (id == R.id.private_cache) {
            isPrivateCache = isChecked;
        }
    }

    @Override
    public void onClick(View v) {
        if (isInternal) {
            String fileDir = "name";
            String fileName = "secret";
            switch (v.getId()) {
                case  R.id.create_fileDir_or_publicExternal:
                    boolean isSuccess = mInternalStorage.createFilesDir(fileDir);
                    showToast(isSuccess);
                    break;
                case R.id.create_cacheDir_or_privateExternal:
                    boolean isSuccessful = mInternalStorage.createCacheDir(fileDir);
                    showToast(isSuccessful);
                    break;
                case R.id.read:
                    String content =  mInternalStorage.read(fileName, isPrivateCache);
                    Toast.makeText(this, content, Toast.LENGTH_LONG).show();
                    break;
                case R.id.write:
                    mInternalStorage.write(fileName, "wlm and sky is wife", Context.MODE_APPEND, isPrivateCache);
                    break;
            }
        }
    }



    private void showToast(boolean isSuccess) {
        if (isSuccess) {
            Log.i(TAG,  "create dir successful");
            Toast.makeText(this, "create dir successful", Toast.LENGTH_LONG).show();
        } else {
            Log.i(TAG,  "create dir failed");
            Toast.makeText(this, "create dir failed", Toast.LENGTH_LONG).show();
        }
    }
}
