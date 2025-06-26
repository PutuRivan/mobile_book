package com.example.book.ui.favorite;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.book.adapter.FavoriteAdapter;
import com.example.book.databinding.FragmentFavoriteBinding;
import com.example.book.model.Book;
import com.example.book.model.FavoriteResponse;
import com.example.book.network.ApiClient;
import com.example.book.network.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteFragment extends Fragment {

    private FragmentFavoriteBinding binding;
    private FavoriteAdapter adapter;
    private final List<Book> favoriteList = new ArrayList<>();
    private String userId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentFavoriteBinding.inflate(inflater, container, false);

        setupRecyclerView();
        loadUserId();

        if (userId != null) {
            getFavoriteBooks();
        }

        return binding.getRoot();
    }

    private void setupRecyclerView() {
        binding.recyclerSavedBooks.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new FavoriteAdapter(getContext(), favoriteList, book -> {
            if (userId != null) {
                deleteFavoriteFromServer(userId, book.getId());
            }
        });
        binding.recyclerSavedBooks.setAdapter(adapter);
    }

    private void loadUserId() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", null);

        if (userId == null) {
            Toast.makeText(getContext(), "User ID tidak ditemukan!", Toast.LENGTH_SHORT).show();
        }
    }

    private void getFavoriteBooks() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getFavoriteBooks(userId).enqueue(new Callback<FavoriteResponse>() {
            @Override
            public void onResponse(Call<FavoriteResponse> call, Response<FavoriteResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    favoriteList.clear();
                    favoriteList.addAll(response.body().getData());
                    adapter.notifyDataSetChanged();
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

    private void deleteFavoriteFromServer(String userId, String bookId) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.deleteFavoriteBook(userId, bookId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Hapus dari list lokal juga
                    favoriteList.removeIf(book -> book.getId().equals(bookId));
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), "Berhasil dihapus dari favorit", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Gagal menghapus dari server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
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
