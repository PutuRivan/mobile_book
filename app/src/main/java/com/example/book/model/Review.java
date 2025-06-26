package com.example.book.model;

public class Review {
    private String id;
    private String comment;
    private User user;
    private String bookId; // Tambahkan ini

    public String getId() {
        return id;
    }

    public String getComment() {
        return comment;
    }

    public User getUser() {
        return user;
    }

    public String getBookId() {
        return bookId;
    } // Getter
}
