package com.example.book.model;

import java.util.List;

public class ReviewResponse {
    private boolean success;
    private List<Review> data;

    public boolean isSuccess() {
        return success;
    }

    public List<Review> getData() {
        return data;
    }
}
