package com.example.book.model;

public class FavoriteRequest {
    private String userId;
    private String bookId;
    private String title;
    private String authors;
    private String thumbnail;
    private String description;

    public FavoriteRequest(String userId, String bookId, String title, String authors, String thumbnail, String description) {
        this.userId = userId;
        this.bookId = bookId;
        this.title = title;
        this.authors = authors;
        this.thumbnail = thumbnail;
        this.description = description;
    }

    // Getter & Setter (opsional jika dibutuhkan)
}
