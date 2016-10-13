package com.example.sky.newsfragmentdemo.control;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Window;

import com.example.sky.newsfragmentdemo.R;

/**
 * Created by sky on 9/7/2016.
 */
public class NewsContentActivity extends FragmentActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.news_content);
        Intent intent = getIntent();
        if (intent != null) {
            String title = intent.getStringExtra("title");
            String content = intent.getStringExtra("content");
            FragmentManager fragmentManager = getSupportFragmentManager();
            NewsContentFragment contentFragment = (NewsContentFragment) fragmentManager.findFragmentById(R.id.content);
            contentFragment.refreshData(title, content);
        }
    }

    public static void startActivity(Context context, String title, String content) {
        Intent intent = new Intent(context, NewsContentActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("content", content);
        context.startActivity(intent);
    }
}
