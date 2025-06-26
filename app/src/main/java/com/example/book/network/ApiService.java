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
import com.example.book.model.ReviewResponse;
import com.example.book.model.SingleReviewResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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

    @DELETE("book/favorite/{userId}/{bookId}")
    Call<Void> deleteFavoriteBook(@Path("userId") String userId, @Path("bookId") String bookId);

    @GET("book/review/{bookId}")
    Call<ReviewResponse> getReviews(@Path("bookId") String bookId);

    @POST("book/review/{bookId}/{userId}")
    Call<SingleReviewResponse> addReview(
            @Path("bookId") String bookId,
            @Path("userId") String userId,
            @Body Map<String, String> body
    );

    @PUT("book/review/{bookId}/{userId}/{reviewId}")
    Call<SingleReviewResponse> updateReview(
            @Path("bookId") String bookId,
            @Path("userId") String userId,
            @Path("reviewId") String reviewId,
            @Body Map<String, String> body
    );

    @DELETE("book/review/{bookId}/{userId}/{reviewId}")
    Call<SingleReviewResponse> deleteReview(
            @Path("bookId") String bookId,
            @Path("userId") String userId,
            @Path("reviewId") String reviewId
    );

    @POST("/auth/register")
    Call<RegisterResponse> registerUser(@Body RegisterRequest request);

    @POST("/auth/login")
    Call<LoginResponse> loginUser(@Body LoginRequest request);

}