package com.github.edngulele.udacity_newsapp.loader;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.github.edngulele.udacity_newsapp.model.News;
import com.github.edngulele.udacity_newsapp.util.QueryUtils;

import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<News>> {

    private static final String queryRequest = "debates";


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
        if (queryRequest == null) {
            return null;
        }
        return QueryUtils.fetchNewsData(queryRequest, getContext());
    }
}