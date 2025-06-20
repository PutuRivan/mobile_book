package com.example.book;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

public class DetailActivity extends AppCompatActivity {

    private ImageView thumbnail;
    private TextView title, author, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        thumbnail = findViewById(R.id.detailThumbnail);
        title = findViewById(R.id.detailTitle);
        author = findViewById(R.id.detailAuthor);
        description = findViewById(R.id.detailDescription);

        // Ambil data dari intent
        String bookTitle = getIntent().getStringExtra("title");
        String bookAuthor = getIntent().getStringExtra("author");
        String bookThumbnail = getIntent().getStringExtra("thumbnail");
        String bookDescription = getIntent().getStringExtra("description");

        title.setText(bookTitle);
        author.setText(bookAuthor);
        description.setText(bookDescription);

        Glide.with(this)
                .load(bookThumbnail)
                .into(thumbnail);
    }
}