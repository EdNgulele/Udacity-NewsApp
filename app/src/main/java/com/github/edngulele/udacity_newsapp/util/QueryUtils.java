package com.github.edngulele.udacity_newsapp.util;

import android.content.Context;
import android.net.Uri;

import com.github.edngulele.udacity_newsapp.R;
import com.github.edngulele.udacity_newsapp.model.News;

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
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {
    static int readTimeout = 10000; //milliseconds
    static int connectTimeout = 15000; //milliseconds

    private static final String BASE_URL = "https://content.guardianapis.com/search?";
    private static final String QUERY_PARAM = "q";
    private static final String SHOW_TAGS = "show-tags";
    private static final String API_KEY = "api-key";
    private static final String SECTION = "section";

    private QueryUtils() {
    }

    public static List<News> fetchNewsData(String queryUrl, Context context) {
        URL url = createUrl(queryUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url, context);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return extractNews(jsonResponse, context);
    }

    private static URL createUrl(String queryUrl) {

        URL url = null;
        try {
            Uri buildString = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(QUERY_PARAM, queryUrl)
                    .appendQueryParameter(SHOW_TAGS, "contributor")
                    .appendQueryParameter(API_KEY, "test")
                    .appendQueryParameter(SECTION, "politics")
                    .build();

            url = new URL(buildString.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    private static String makeHttpRequest(URL url, Context context) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(readTimeout);
            urlConnection.setConnectTimeout(connectTimeout);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream, context);
            } else {
            }
        } catch (IOException e) {
            e.printStackTrace();
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

    private static String readFromStream(InputStream inputStream, Context context) throws IOException {
        StringBuilder outputString = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader isr = new InputStreamReader(inputStream, context.getString(R.string.utf_8));
            BufferedReader br = new BufferedReader(isr);
            String currentLine = br.readLine();
            while (currentLine != null) {
                outputString.append(currentLine);
                currentLine = br.readLine();
            }
        }
        return outputString.toString();
    }

    private static List<News> extractNews(String jsonResponse, Context context) {
        List<News> news = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONObject responseJSONObject = jsonObject.getJSONObject(context.getString(R.string.response));
            JSONArray resultsJSONArray = responseJSONObject.getJSONArray(context.getString(R.string.results));

            for (int i = 0; i < resultsJSONArray.length(); i++) {
                JSONObject currentJSONObject = resultsJSONArray.getJSONObject(i);
                String title = currentJSONObject.getString(context.getString(R.string.webTitle));

                String section = currentJSONObject.getString(context.getString(R.string.section_name));

                String publicationDateTime = currentJSONObject.getString(context.getString(R.string.publication_date_and_time));

                String webUrl = currentJSONObject.getString(context.getString(R.string.web_url));

                //Authors Array
                ArrayList<String> authors = new ArrayList<>();
                if (currentJSONObject.has(context.getString(R.string.tags_array))) {
                    JSONArray currentTagsArray = currentJSONObject.getJSONArray(context.getString(R.string.tags_array));
                    if (currentTagsArray == null || currentTagsArray.length() == 0) {
                        authors = null;
                    } else {
                        for (int j = 0; j < currentTagsArray.length(); j++) {
                            JSONObject currentObjectInTags = currentTagsArray.getJSONObject(j);
                            authors.add(currentObjectInTags.getString(context.getString(R.string.web_title)));
                        }
                    }
                } else {
                    authors = null;
                }
                news.add(new News(title, section, publicationDateTime, webUrl, authors));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return news;
    }
}