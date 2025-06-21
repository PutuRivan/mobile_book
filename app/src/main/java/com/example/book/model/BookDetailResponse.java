package com.example.book.model;

public class BookDetailResponse {
    private boolean success;
    private Book book;

    public boolean isSuccess() {
        return success;
    }

    public Book getBook() {
        return book;
    }
}
