package com.example.book.model;

import java.util.List;

public class Book {
    private String id;
    private String title;
    private List<String> authors;
    private String thumbnail;
    private String description;
    private List<String> genre;
    private double rating;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getGenre() {
        return genre;
    }

    public double getRating() {
        return rating;
    }
}
