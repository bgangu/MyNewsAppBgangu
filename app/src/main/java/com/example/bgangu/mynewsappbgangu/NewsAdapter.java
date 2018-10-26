package com.example.bgangu.mynewsappbgangu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.content.pm.PackageManager;

import java.net.URI;
import java.util.ArrayList;

public class NewsAdapter extends ArrayAdapter<News> {

    private Context mContext;
    private String mURL;
    public static final String LOG_TAG = NewsAdapter.class.getSimpleName();

    public NewsAdapter(Activity context, ArrayList<News> newsArrayList) {
        super(context, 0, newsArrayList);
        this.mContext = context;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        View listView = convertView;

        final News currentNewsDetails = getItem(position);

        if (listView == null) {
            listView = LayoutInflater.from(getContext()).inflate(R.layout.news_details, parent, false);
        }

        TextView storySectionView = (TextView) listView.findViewById(R.id.story_section_view);
        TextView storyTitleView = (TextView) listView.findViewById(R.id.story_title_view);
        TextView storyPublishedDateView = (TextView) listView.findViewById(R.id.story_date_view);
        TextView storyAuthorName = (TextView) listView.findViewById(R.id.story_author_view);

        storySectionView.setText(currentNewsDetails.getNewsSection());

        storyTitleView.setText(currentNewsDetails.getNewsTitle());

        storyTitleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mURL = currentNewsDetails.getURL();
                if (mURL == null) {
                    Toast.makeText(mContext, "URL is not available", Toast.LENGTH_LONG).show();
                } else {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                    browserIntent.setData(Uri.parse(mURL));
                    if (browserIntent.resolveActivity(mContext.getPackageManager()) != null) {
                        mContext.startActivity(browserIntent);
                    }
                    else{
                        Log.i(LOG_TAG, "Couldn't find an App that can open the link");
                    }
                }
            }
        });

        storyAuthorName.setText(currentNewsDetails.getmNewsAuthorName());

        String newsPublishedDate = currentNewsDetails.getNewsPublishedDate();
        if (newsPublishedDate == null) {
            storyPublishedDateView.setVisibility(View.GONE);
        } else {
            newsPublishedDate = returnOnlyDate(newsPublishedDate);
            storyPublishedDateView.setText(newsPublishedDate);
        }

        return listView;
    }

    private String returnOnlyDate(String fullPusblisgedDate) {
        String stringArray[] = fullPusblisgedDate.split("T");
        return stringArray[0];
    }
}
