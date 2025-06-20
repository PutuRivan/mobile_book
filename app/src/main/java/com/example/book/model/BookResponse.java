package com.example.book.model;

import java.util.List;

public class BookResponse {
    private boolean success;
    private List<Book> books;

    public boolean isSuccess() {
        return success;
    }

    public List<Book> getBooks() {
        return books;
    }
}
