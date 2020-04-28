package com.github.edngulele.udacity_newsapp.model;


import java.util.ArrayList;

public class News {
    private String title;
    private String section;
    private String webPublicationDateAndTime;
    private String webUrl;
    private ArrayList<String> authors;

    public News(String title, String section, String webPublicationDateAndTime, String webUrl, ArrayList<String> authors) {
        this.title = title;
        this.section = section;
        this.webPublicationDateAndTime = webPublicationDateAndTime;
        this.webUrl = webUrl;
        this.authors = authors;
    }

    public String getTitle() {
        return title;
    }

    public String getSection() {
        return section;
    }

    public String getWebPublicationDateAndTime() {
        return webPublicationDateAndTime;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public ArrayList<String> getAuthors() {
        return authors;
    }
}
