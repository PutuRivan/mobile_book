package com.example.book.network;

import com.example.book.model.BookResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @GET("/book")
    Call<BookResponse> getBooks();
}