package com.example.sky.newsfragmentdemo.control;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.sky.newsfragmentdemo.R;
import com.example.sky.newsfragmentdemo.entity.News;
import com.example.sky.newsfragmentdemo.entity.NewsAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sky on 9/7/2016.
 */
public class NewsTitleFragment extends Fragment implements AdapterView.OnItemClickListener{
    private List<News> mNewsList;
    private NewsAdapter mNewsAdapter;
    private ListView mListView;
    private boolean  mTwoScreen;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mNewsList = getNews();
        mNewsAdapter = new NewsAdapter(context, R.layout.news_title_item, mNewsList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_title, container, false);
        mListView = (ListView) view.findViewById(R.id.title_list_view);
        mListView.setAdapter(mNewsAdapter);
        mListView.setOnItemClickListener(this);
        mListView.setSelection(0);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity().findViewById(R.id.content_fragment) != null) {
            mTwoScreen = true;
            FragmentManager fragmentManager = getFragmentManager();
            NewsContentFragment newsContentFragment = (NewsContentFragment) fragmentManager.findFragmentById(R.id.content_fragment);
            newsContentFragment.refreshData(mNewsList.get(0));
        } else {
            mTwoScreen = false;
        }
    }

    public List<News> getNews() {
        List<News> news = new ArrayList<>();
        News new1 = new News();
        new1.setTitle("New1");
        new1.setContent("asdasdhjaskhdjkashdkjashdkjasdgbasjkbdksabdksab kashcsahdkjas" +
                "sadsadsaddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                "asdasvasvasvasadasdwqasdasdasdsadsa");
        news.add(new1);
        News new2 = new News();
        new2.setTitle("New2");
        new2.setContent("hjsajksahdjkhsakdjb dhbvjhcxbczxhbchzxbchjzxbckzhxhbkjzxhxjkzh" +
                "azxjkszbkjbaskjcbaskjxcbsakjdbnaksjhdjkashdxkjasnuhwuiqyeqgdaushdoiahsd" +
                "sadhjkashdashdjksahdjkashndhasiodhsaiodjdoisajdklnxjkbcjkshkasjhdkajhgahshnk" +
                "sadjhgaskjdhaksa");
        news.add(new2);
        return news;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        News news = mNewsList.get(position);
        if (mTwoScreen) {
            FragmentManager fragmentManager = getFragmentManager();
            NewsContentFragment newsContentFragment = (NewsContentFragment) fragmentManager.findFragmentById(R.id.content_fragment);
            newsContentFragment.refreshData(news);
        } else {
            NewsContentActivity.startActivity(getContext(), news.getTitle(), news.getContent());
        }

    }
}
