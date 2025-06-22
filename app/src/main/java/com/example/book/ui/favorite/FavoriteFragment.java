package com.example.book.ui.favorite;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.book.DetailActivity;
import com.example.book.adapter.BookAdapter;
import com.example.book.databinding.FragmentFavoriteBinding;
import com.example.book.model.Book;
import com.example.book.model.FavoriteResponse;
import com.example.book.network.ApiClient;
import com.example.book.network.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteFragment extends Fragment {

    private FragmentFavoriteBinding binding;
    private BookAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentFavoriteBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.recyclerSavedBooks.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new BookAdapter(getContext(), book -> {
            Intent intent = new Intent(getContext(), DetailActivity.class);
            intent.putExtra("bookId", book.getId());
            startActivity(intent);
        });
        binding.recyclerSavedBooks.setAdapter(adapter);

        getFavoriteBooks();

        return root;
    }

    private void getFavoriteBooks() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", null);

        if (userId == null) {
            Toast.makeText(getContext(), "User ID tidak ditemukan!", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<FavoriteResponse> call = apiService.getFavoriteBooks(userId);
        call.enqueue(new Callback<FavoriteResponse>() {

            @Override
            public void onResponse(Call<FavoriteResponse> call, Response<FavoriteResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Book> favoriteBooks = response.body().getData();
                    adapter.setBookList(favoriteBooks);
                } else {
                    Toast.makeText(getContext(), "Gagal memuat data favorite!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FavoriteResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
