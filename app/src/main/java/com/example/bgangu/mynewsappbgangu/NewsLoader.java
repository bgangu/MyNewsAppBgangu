package com.example.bgangu.mynewsappbgangu;

import android.content.Context;
import android.support.annotation.Nullable;
import android.content.AsyncTaskLoader;


import java.util.ArrayList;

public class NewsLoader extends AsyncTaskLoader<ArrayList<News>> {

    private String mURL;

    public NewsLoader(Context context, String URL) {
        super(context);
        mURL = URL;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public ArrayList<News> loadInBackground() {
        if (mURL == null) {
            return null;
        }
        ArrayList<News> newsList = QueryUtils.fetchNews(mURL);
        return newsList;
    }
}
