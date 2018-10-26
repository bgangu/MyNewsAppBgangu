package com.example.bgangu.mynewsappbgangu;

import android.app.LoaderManager;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<News>> {

    public static final String LOG_TAG = MainActivity.class.getName();
    private static final String NEWS_REQUEST_URL = "https://content.guardianapis.com/search?from-date=2018-10-01&q=Android&api-key=test&show-tags=contributor";
    private static final int NEWS_LOADER_ID = 1;
    private NewsAdapter mAdapter;
    private TextView mEmptyTextView;
    private ListView newsListView;
    private ProgressBar mProgressView;
    private LoaderManager loaderManager = getLoaderManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEmptyTextView = (TextView) findViewById(R.id.empty_view);
        mProgressView = (ProgressBar) findViewById(R.id.progress_bar);
        newsListView = (ListView) findViewById(R.id.list);
        mAdapter = new NewsAdapter(this, new ArrayList<News>());
        ConnectivityManager mCM = (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo mNI = mCM.getActiveNetworkInfo();
        if (mNI == null || !mNI.isConnected()) {
            mEmptyTextView.setText(R.string.no_active_network);
        } else {
            mEmptyTextView.setText("");
            mProgressView.setVisibility(View.VISIBLE);
            newsListView.setAdapter(mAdapter);
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        }
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<News>> loader, ArrayList<News> news) {
        mAdapter.clear();
        mProgressView.setVisibility(View.GONE);
        if (news != null && !news.isEmpty()) {
            mAdapter.addAll(news);
        } else {
            mEmptyTextView.setText(R.string.no_news);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<News>> loader) {
        mAdapter.clear();
        mEmptyTextView.setText("");
        mProgressView.setVisibility(View.GONE);
    }

    @Override
    public Loader<ArrayList<News>> onCreateLoader(int id, Bundle args) {
        return new NewsLoader(this, NEWS_REQUEST_URL);
    }
}
