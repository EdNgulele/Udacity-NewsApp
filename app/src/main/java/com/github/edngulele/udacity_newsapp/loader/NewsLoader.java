package com.github.edngulele.udacity_newsapp.loader;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.github.edngulele.udacity_newsapp.model.News;
import com.github.edngulele.udacity_newsapp.util.QueryUtils;

import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<News>> {

    private static final String request_url = "https://content.guardianapis.com/search?show-tags=contributor&api-key=37ad3e66-4685-4142-8d5b-42c17e231b73";

    public NewsLoader(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public List<News> loadInBackground() {
        if (request_url == null) {
            return null;
        }
        return QueryUtils.fetchNewsData(request_url, getContext());
    }
}