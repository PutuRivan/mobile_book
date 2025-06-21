package com.example.book;

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
import com.example.book.network.ApiClient;
import com.example.book.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    private ImageView thumbnail;
    private TextView title, author, description;
    private Button btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        thumbnail = findViewById(R.id.detailThumbnail);
        title = findViewById(R.id.detailTitle);
        author = findViewById(R.id.detailAuthor);
        description = findViewById(R.id.detailDescription);
        btn_back = findViewById(R.id.btn_back);

        // Fungsi back button
        btn_back.setOnClickListener(v -> finish());

        // Ambil hanya bookId dari intent
        String bookId = getIntent().getStringExtra("bookId");

        if (bookId != null) {
            fetchBookDetail(bookId);
        } else {
            Toast.makeText(this, "Book ID tidak ditemukan!", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchBookDetail(String bookId) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<BookDetailResponse> call = apiService.getBookDetail(bookId);
        call.enqueue(new Callback<BookDetailResponse>() {
            @Override
            public void onResponse(Call<BookDetailResponse> call, Response<BookDetailResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Book book = response.body().getBook();
                    title.setText(book.getTitle());
                    author.setText(book.getAuthors().get(0)); // hanya author pertama
                    description.setText(book.getDescription());
                    Glide.with(DetailActivity.this)
                            .load(book.getThumbnail())
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



}
