package com.example.book.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.book.DetailActivity;
import com.example.book.SearchBookActivity;
import com.example.book.adapter.BookAdapter;
import com.example.book.databinding.FragmentHomeBinding;
import com.example.book.model.Book;
import com.example.book.model.BookResponse;
import com.example.book.network.ApiClient;
import com.example.book.network.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private BookAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // RecyclerView setup
        binding.recyclerViewBooks.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new BookAdapter(getContext(), book -> {
            Intent intent = new Intent(getContext(), DetailActivity.class);
            intent.putExtra("bookId", book.getId());
            startActivity(intent);
        });

        binding.recyclerViewBooks.setAdapter(adapter);

        loadBooks(); // Load popular books

        // SearchView listener
        binding.searchViewBooks.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Pindah ke SearchBookActivity bawa query
                Intent intent = new Intent(getContext(), SearchBookActivity.class);
                intent.putExtra("query", query);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false; // Tidak live search
            }
        });

        return root;
    }

    private void loadBooks() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<BookResponse> call = apiService.getBooks();

        call.enqueue(new Callback<BookResponse>() {
            @Override
            public void onResponse(Call<BookResponse> call, Response<BookResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Book> books = response.body().getBooks();
                    adapter.setBookList(books);
                }
            }

            @Override
            public void onFailure(Call<BookResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
