package com.example.bgangu.mynewsappbgangu;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    public static ArrayList<News> fetchNews(String requestUrl) {
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        ArrayList<News> news = extractNews(jsonResponse);

        return news;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    public static ArrayList<News> extractNews(String jsonResponse) {

        ArrayList<News> news = new ArrayList<>();

        try {
            JSONObject jsonResponseObject = new JSONObject(jsonResponse);
            JSONObject responseObject = jsonResponseObject.getJSONObject("response");
            JSONArray newsArray = responseObject.getJSONArray("results");

            for (int i = 0; i < newsArray.length(); i++) {
                JSONObject currentNewsObject = newsArray.getJSONObject(i);

                String newsSection;
                if (currentNewsObject.getString("sectionName") == null) {
                    newsSection = "Unknown";
                } else {
                    newsSection = currentNewsObject.getString("sectionName");
                }

                String newsTitle;
                if (currentNewsObject.getString("webTitle") == null) {
                    newsTitle = "Unknown";
                } else {
                    newsTitle = currentNewsObject.getString("webTitle");
                }

                String newsPublishedDate;
                if (currentNewsObject.getString("webPublicationDate") == null) {
                    newsPublishedDate = null;
                } else {
                    newsPublishedDate = currentNewsObject.getString("webPublicationDate");
                }

                String newsURL;
                if (currentNewsObject.getString("webUrl") == null) {
                    newsURL = "Unknown";
                } else {
                    newsURL = currentNewsObject.getString("webUrl");
                }

                String newsAuthorName = "";
                JSONArray newsTagsArray = currentNewsObject.getJSONArray("tags");
                for (int j = 0; j < newsTagsArray.length(); j++) {
                    JSONObject currentTags = newsTagsArray.getJSONObject(j);
                    String authorFirstName = currentTags.getString("firstName");
                    String authorLastName = currentTags.getString("lastName");
                    if (authorFirstName == null && authorLastName == null) {
                        newsAuthorName = "Unknown";
                    } else {
                        if (authorFirstName == null && authorLastName != null) {
                            newsAuthorName = authorFirstName;
                        } else if (authorFirstName != null && authorLastName == null) {
                            newsAuthorName = authorFirstName;
                        } else {
                            newsAuthorName = authorLastName + " " + authorFirstName;
                        }
                    }
                }

                News currentNews = new News(newsSection, newsTitle, newsAuthorName, newsPublishedDate, newsURL);
                news.add(currentNews);
            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem in parsing the JSON results", e);
        }

        return news;
    }
}

