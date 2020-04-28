package com.github.edngulele.udacity_newsapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.edngulele.udacity_newsapp.R;
import com.github.edngulele.udacity_newsapp.model.News;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {

    private List<News> newsData;
    Context context;

    //Inner Class for the ViewHolder
    private static class ViewHolder {
        TextView title, section, date, author;
    }

    public NewsAdapter(List<News> newsData, Context context) {
        super(context, R.layout.item_news_row, newsData);
        this.newsData = newsData;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //Get data in the given position
        News news = getItem(position);

        //Inflates the view if it doesn't exist already
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_news_row, parent, false);

            viewHolder.title = convertView.findViewById(R.id.tv_news_title);
            viewHolder.section = convertView.findViewById(R.id.tv_news_section);
            viewHolder.date = convertView.findViewById(R.id.tv_news_date);
            viewHolder.author = convertView.findViewById(R.id.tv_news_author);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.title.setText(news.getTitle());
        viewHolder.section.setText(news.getSection());
        viewHolder.date.setText(news.getWebPublicationDateAndTime());

        //checks if there are authors.
        // In case there are multiple authors they will separated by a comma
        ArrayList<String> authors = news.getAuthors();
        if (authors == null) {
            viewHolder.author.setText(context.getString(R.string.no_author));
        } else {
            StringBuilder authorString = new StringBuilder();
            for (int i = 0; i < authors.size(); i++) {
                authorString.append(authors.get(i));
                if ((i + 1) < authors.size()) {
                    authorString.append(", ");
                }
            }
            viewHolder.author.setText(authorString.toString());
            viewHolder.author.setVisibility(View.VISIBLE);
        }


        return convertView;

    }
}
