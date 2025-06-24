package com.example.book.adapter;

public interface ReviewListener {
    void showUpdateDialog(String bookId, String reviewId, String oldComment);
    void deleteReview(String bookId, String reviewId);
}

