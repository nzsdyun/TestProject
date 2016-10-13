package com.example.sky.httputiltest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.sky.httputiltest.http.DefaultHttpRequest;
import com.example.sky.httputiltest.http.IHttpRequest;
import com.example.sky.httputiltest.http.ResponseListenerAdapter;

public class MainActivity extends AppCompatActivity {
    private  static final String TAG = MainActivity.class.getSimpleName();
    IHttpRequest mHttpRequest;
    TextView mTextView;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String content = (String) msg.obj;
            mTextView.setText(content);
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHttpRequest = new DefaultHttpRequest(this);
        mTextView = (TextView) findViewById(R.id.result);
    }

    public void Click(View v) {
        mTextView.setText("");
        switch (v.getId()) {
            case R.id.get:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String result = mHttpRequest.get("http://www.baidu.com");
                        mHandler.sendMessage(mHandler.obtainMessage(0, result));
                    }
                }).start();
                break;
            case R.id.async_get:
                String content = mHttpRequest.asyncGet("http://www.baidu.com", new ResponseListenerAdapter() {
                    @Override
                    public void success(String content) {
                        mTextView.setText(content);
                    }
                });
                Log.i(TAG, "content:" + content);
                break;
            case R.id.get_params:
                break;
            case R.id.async_get_params:
                break;
            case R.id.post:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String result = mHttpRequest.post("http://bbs.csdn.net/home");
                        mHandler.sendMessage(mHandler.obtainMessage(0, result));
                    }
                }).start();
                break;
            case R.id.post_params:
                break;
            case R.id.async_post:
                String postContent = mHttpRequest.asyncPost("http://bbs.csdn.net/home", new ResponseListenerAdapter() {
                    @Override
                    public void success(String content) {
                        mTextView.setText(content);
                    }
                });
                Log.i(TAG, "postContent:" + postContent);
                break;
            case R.id.async_post_params:
                break;
        }
    }
}
