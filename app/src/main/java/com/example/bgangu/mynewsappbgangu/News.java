package com.example.bgangu.mynewsappbgangu;

import java.net.URL;
import java.util.ArrayList;

public class News extends ArrayList<News> {

    private String mNewsSection;
    private String mNewsTitle;
    private String mNewsAuthorName;
    private String mNewsPublishedDate;
    private String mURL;

    public News(String newsSection, String newsTitle, String newsAuthorName,String newsPublishedDate, String URL) {
        mNewsSection = newsSection;
        mNewsTitle = newsTitle;
        mNewsAuthorName = newsAuthorName;
        mNewsPublishedDate = newsPublishedDate;
        mURL = URL;
    }

    public String getNewsSection() {
        return mNewsSection;
    }

    public String getNewsTitle() {
        return mNewsTitle;
    }

    public String getmNewsAuthorName(){
        return mNewsAuthorName;
    }

    public String getNewsPublishedDate() {
        return mNewsPublishedDate;
    }

    public String getURL() {
        return mURL;
    }

}
