package com.example.book.network;

import com.example.book.model.Book;
import com.example.book.model.BookDetailResponse;
import com.example.book.model.BookResponse;
import com.example.book.model.FavoriteRequest;
import com.example.book.model.FavoriteResponse;
import com.example.book.model.LoginRequest;
import com.example.book.model.LoginResponse;
import com.example.book.model.RegisterRequest;
import com.example.book.model.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("/book")
    Call<BookResponse> getBooks();

    @GET("book/{id}")
    Call<BookDetailResponse> getBookDetail(@Path("id") String bookId);

    @GET("book/search")
    Call<BookResponse> searchBooks(@Query("q") String query);

    @POST("book/favorite")
    Call<FavoriteResponse> addToFavorite(@Body FavoriteRequest request);

    @GET("/book/favorite/{userId}")
    Call<FavoriteResponse> getFavoriteBooks(@Path("userId") String userId);

    @POST("/auth/register")
    Call<RegisterResponse> registerUser(@Body RegisterRequest request);

    @POST("/auth/login")
    Call<LoginResponse> loginUser(@Body LoginRequest request);
}