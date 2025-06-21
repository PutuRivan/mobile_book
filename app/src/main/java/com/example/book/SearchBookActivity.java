package com.example.book;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book.adapter.BookAdapter;
import com.example.book.model.Book;
import com.example.book.model.BookResponse;
import com.example.book.network.ApiClient;
import com.example.book.network.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchBookActivity extends AppCompatActivity {

    private SearchView searchView;
    private RecyclerView recyclerView;
    private BookAdapter bookAdapter;
    private List<Book> bookList = new ArrayList<>();
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_book);

        searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.recyclerViewSearch);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bookAdapter = new BookAdapter(this, book -> {
            Intent intent = new Intent(SearchBookActivity.this, DetailActivity.class);
            intent.putExtra("bookId", book.getId());
            startActivity(intent);
        });

        recyclerView.setAdapter(bookAdapter);

        apiService = ApiClient.getClient().create(ApiService.class);

        // Cek apakah ada query dari HomeFragment
        String initialQuery = getIntent().getStringExtra("query");
        if (initialQuery != null && !initialQuery.isEmpty()) {
            searchView.setQuery(initialQuery, false); // Menampilkan query di SearchView
            searchBook(initialQuery); // Langsung melakukan search
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchBook(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }


    private void searchBook(String query) {
        Call<BookResponse> call = apiService.searchBooks(query);
        call.enqueue(new Callback<BookResponse>() {
            @Override
            public void onResponse(Call<BookResponse> call, Response<BookResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    bookList = response.body().getBooks();
                    bookAdapter.setBookList(bookList);
                } else {
                    Toast.makeText(SearchBookActivity.this, "Buku tidak ditemukan!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BookResponse> call, Throwable t) {
                Toast.makeText(SearchBookActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
