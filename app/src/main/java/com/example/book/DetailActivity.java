package com.example.book;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.book.model.Book;
import com.example.book.model.BookDetailResponse;
import com.example.book.model.FavoriteRequest;
import com.example.book.model.FavoriteResponse;
import com.example.book.network.ApiClient;
import com.example.book.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    private ImageView thumbnail;
    private TextView title, author, description;
    private Button btn_back, btn_favorite;
    private Book currentBook;
    private String userId;

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

        btn_back.setOnClickListener(v -> finish());

        String bookId = getIntent().getStringExtra("bookId");

        if (bookId != null) {
            fetchBookDetail(bookId);
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
                    author.setText(currentBook.getAuthors().get(0));
                    description.setText(currentBook.getDescription());
                    Glide.with(DetailActivity.this)
                            .load(currentBook.getThumbnail())
                            .into(thumbnail);
                }
            }

            @Override
            public void onFailure(Call<BookDetailResponse> call, Throwable t) {
                Log.e("DetailActivity", "Error: " + t.getMessage());
                Toast.makeText(DetailActivity.this, "Gagal mengambil data buku", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addToFavorite(Book book) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        FavoriteRequest request = new FavoriteRequest(
                userId,
                book.getId(),
                book.getTitle(),
                book.getAuthors().get(0),
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
