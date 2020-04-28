package com.github.edngulele.udacity_newsapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.Loader;

import com.github.edngulele.udacity_newsapp.adapter.NewsAdapter;
import com.github.edngulele.udacity_newsapp.loader.NewsLoader;
import com.github.edngulele.udacity_newsapp.model.News;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements androidx.loader.app.LoaderManager.LoaderCallbacks<List<News>> {

    private NewsAdapter newsAdapter;
    private ListView listView;
    private TextView emptyText;
    private View progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emptyText = findViewById(R.id.tv_empty);
        progressBar = findViewById(R.id.pb_loading);
        listView = findViewById(R.id.lv_news);

        //Setting the adapter
        newsAdapter = new NewsAdapter(new ArrayList<News>(), this);
        listView.setAdapter(newsAdapter);

        //Check internet connectivity
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (cm != null) {
            networkInfo = cm.getActiveNetworkInfo();
        }
        if (networkInfo != null && networkInfo.isConnected()) {
            androidx.loader.app.LoaderManager.getInstance(this).initLoader(1, null, this);

        } else {
            progressBar.setVisibility(View.GONE);
            emptyText.setText(this.getString(R.string.no_internet));
            emptyText.setVisibility(View.VISIBLE);
        }

        //Setting the click listener to direct the user to the browser
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                News actualNews = newsAdapter.getItem(position);
                Uri newsUri = Uri.parse(actualNews.getWebUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);
                startActivity(websiteIntent);
            }
        });
    }

    @NonNull
    @Override
    public Loader<List<News>> onCreateLoader(int id, @Nullable Bundle args) {
        return new NewsLoader(this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<News>> loader, List<News> data) {
        progressBar.setVisibility(View.GONE);
        emptyText.setText(getApplicationContext().getString(R.string.no_news));
        listView.setEmptyView(emptyText);
        newsAdapter.clear();

        if (data != null && !data.isEmpty()) {
            newsAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<News>> loader) {
        newsAdapter.clear();

    }

}
