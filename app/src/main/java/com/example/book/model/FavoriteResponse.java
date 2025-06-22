package com.example.book.model;

import java.util.List;

public class FavoriteResponse {
    private String message;
    private List<Book> data;

    public String getMessage() {
        return message;
    }
    
    public List<Book> getData() {
        return data;
    }
}
