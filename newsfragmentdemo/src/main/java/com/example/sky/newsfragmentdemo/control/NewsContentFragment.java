package com.example.sky.newsfragmentdemo.control;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sky.newsfragmentdemo.R;
import com.example.sky.newsfragmentdemo.entity.News;

/**
 * Created by sky on 9/7/2016.
 */
public class NewsContentFragment extends Fragment {
    private TextView mTitle;
    private TextView mContent;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_content_frag, container, false);
        mTitle = (TextView) view.findViewById(R.id.title);
        mContent = (TextView) view.findViewById(R.id.content);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void refreshData(News news) {
        if (news == null)
            return;
        mTitle.setText(news.getTitle());
        mContent.setText(news.getContent());
    }

    public void refreshData(String title, String content) {
        mTitle.setText(title);
        mContent.setText(content);
    }
}
