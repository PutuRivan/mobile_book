package com.example.book;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.book.adapter.ReviewAdapter;
import com.example.book.adapter.ReviewListener;
import com.example.book.model.Book;
import com.example.book.model.BookDetailResponse;
import com.example.book.model.FavoriteRequest;
import com.example.book.model.FavoriteResponse;
import com.example.book.model.Review;
import com.example.book.model.ReviewResponse;
import com.example.book.model.SingleReviewResponse;
import com.example.book.network.ApiClient;
import com.example.book.network.ApiService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity implements ReviewListener {
    private ImageView thumbnail;
    private TextView title, author, description;
    private Button btn_back, btn_favorite, btn_submit_review;
    private EditText et_review;
    private RecyclerView reviewRecyclerView;
    private ReviewAdapter reviewAdapter;
    private Book currentBook;
    private String userId;
    private List<Review> reviewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", null);

        thumbnail = findViewById(R.id.detailThumbnail);
        title = findViewById(R.id.detailTitle);
        author = findViewById(R.id.detailAuthor);
        description = findViewById(R.id.detailDescription);
        btn_back = findViewById(R.id.btn_back);
        btn_favorite = findViewById(R.id.btn_favorite);
        btn_submit_review = findViewById(R.id.btn_submit_review);
        et_review = findViewById(R.id.et_review);
        reviewRecyclerView = findViewById(R.id.reviewRecyclerView);

        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        btn_back.setOnClickListener(v -> finish());

        String bookId = getIntent().getStringExtra("bookId");

        if (bookId != null) {
            fetchBookDetail(bookId);
            fetchReviews(bookId);
        } else {
            Toast.makeText(this, "Book ID tidak ditemukan!", Toast.LENGTH_SHORT).show();
        }

        if (userId == null) {
            Toast.makeText(this, "User ID tidak ditemukan, silakan login ulang!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        btn_favorite.setOnClickListener(v -> {
            if (currentBook != null) {
                addToFavorite(currentBook);
            } else {
                Toast.makeText(this, "Data buku belum tersedia!", Toast.LENGTH_SHORT).show();
            }
        });

        btn_submit_review.setOnClickListener(v -> {
            String reviewText = et_review.getText().toString().trim();
            if (!reviewText.isEmpty() && currentBook != null) {
                addReview(currentBook.getId(), userId, reviewText);
            }
        });
    }

    private void fetchBookDetail(String bookId) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<BookDetailResponse> call = apiService.getBookDetail(bookId);
        call.enqueue(new Callback<BookDetailResponse>() {
            @Override
            public void onResponse(Call<BookDetailResponse> call, Response<BookDetailResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentBook = response.body().getBook();
                    title.setText(currentBook.getTitle());
                    author.setText(currentBook.getAuthors());
                    description.setText(currentBook.getDescription());
                    Glide.with(DetailActivity.this)
                            .load(currentBook.getThumbnail())
                            .into(thumbnail);
                }
            }

            @Override
            public void onFailure(Call<BookDetailResponse> call, Throwable t) {
                Toast.makeText(DetailActivity.this, "Gagal mengambil data buku", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchReviews(String bookId) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ReviewResponse> call = apiService.getReviews(bookId);
        call.enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    reviewList = response.body().getData();
                    reviewAdapter = new ReviewAdapter(reviewList, userId, DetailActivity.this);
                    reviewRecyclerView.setAdapter(reviewAdapter);
                }
            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
                Toast.makeText(DetailActivity.this, "Gagal mengambil review", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addReview(String bookId, String userId, String comment) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Map<String, String> body = new HashMap<>();
        body.put("comment", comment);

        Call<SingleReviewResponse> call = apiService.addReview(bookId, userId, body);
        call.enqueue(new Callback<SingleReviewResponse>() { // FIXED: tipe disesuaikan
            @Override
            public void onResponse(Call<SingleReviewResponse> call, Response<SingleReviewResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(DetailActivity.this, "Review ditambahkan", Toast.LENGTH_SHORT).show();
                    fetchReviews(bookId);
                    et_review.setText("");
                }
            }

            @Override
            public void onFailure(Call<SingleReviewResponse> call, Throwable t) {
                Toast.makeText(DetailActivity.this, "Gagal menambahkan review", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void showUpdateDialog(String bookId, String reviewId, String oldComment) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Review");

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_edit_review, null);
        final EditText input = viewInflated.findViewById(R.id.etEditReview);
        input.setText(oldComment);

        builder.setView(viewInflated);

        builder.setPositiveButton("Update", (dialog, which) -> {
            String newComment = input.getText().toString();
            updateReview(bookId, userId, reviewId, newComment);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void updateReview(String bookId, String userId, String reviewId, String comment) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Map<String, String> body = new HashMap<>();
        body.put("comment", comment);

        Call<SingleReviewResponse> call = apiService.updateReview(bookId, userId, reviewId, body);
        call.enqueue(new Callback<SingleReviewResponse>() {
            @Override
            public void onResponse(Call<SingleReviewResponse> call, Response<SingleReviewResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(DetailActivity.this, "Review diupdate", Toast.LENGTH_SHORT).show();
                    fetchReviews(bookId);
                }
            }

            @Override
            public void onFailure(Call<SingleReviewResponse> call, Throwable t) {
                Toast.makeText(DetailActivity.this, "Gagal update review", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deleteReview(String bookId, String reviewId) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<SingleReviewResponse> call = apiService.deleteReview(bookId, userId, reviewId);
        call.enqueue(new Callback<SingleReviewResponse>() {
            @Override
            public void onResponse(Call<SingleReviewResponse> call, Response<SingleReviewResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(DetailActivity.this, "Review dihapus", Toast.LENGTH_SHORT).show();
                    fetchReviews(bookId);
                }
            }

            @Override
            public void onFailure(Call<SingleReviewResponse> call, Throwable t) {
                Toast.makeText(DetailActivity.this, "Gagal hapus review", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addToFavorite(Book book) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        FavoriteRequest request = new FavoriteRequest(
                userId,
                book.getId(),
                book.getTitle(),
                book.getAuthors(),
                book.getThumbnail(),
                book.getDescription()
        );

        Call<FavoriteResponse> call = apiService.addToFavorite(request);
        call.enqueue(new Callback<FavoriteResponse>() {
            @Override
            public void onResponse(Call<FavoriteResponse> call, Response<FavoriteResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(DetailActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DetailActivity.this, "Gagal menambahkan ke favorit!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FavoriteResponse> call, Throwable t) {
                Toast.makeText(DetailActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
