package com.example.sky.newsfragmentdemo.entity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.sky.newsfragmentdemo.R;

import java.util.List;

/**
 * Created by sky on 9/7/2016.
 */
public class NewsAdapter extends ArrayAdapter<News> {
    private int resourceId;
    public NewsAdapter(Context context, int resource, List<News> objects) {
        super(context, resource, objects);
        this.resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.news_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(getItem(position).getTitle());
        return convertView;
    }

    static class ViewHolder {
        public TextView textView;
    }
}
