package com.example.book.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.book.DetailActivity;
import com.example.book.R;
import com.example.book.model.Book;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {
    private Context context;
    private List<Book> favoriteList;
    private OnDeleteClickListener deleteClickListener;

    public interface OnDeleteClickListener {
        void onDeleteClick(Book book);
    }

    public FavoriteAdapter(Context context, List<Book> favoriteList, OnDeleteClickListener deleteClickListener) {
        this.context = context;
        this.favoriteList = favoriteList;
        this.deleteClickListener = deleteClickListener;
    }

    public static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView bookTitle, bookAuthor;
        ImageButton btnDelete;

        public FavoriteViewHolder(View view) {
            super(view);
            thumbnail = view.findViewById(R.id.thumbnail);
            bookTitle = view.findViewById(R.id.bookTitle);
            bookAuthor = view.findViewById(R.id.bookAuthor);
            btnDelete = view.findViewById(R.id.btnDelete);
        }
    }

    @Override
    public FavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorite, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoriteViewHolder holder, int position) {
        Book book = favoriteList.get(position);
        holder.bookTitle.setText(book.getTitle());
        holder.bookAuthor.setText(book.getAuthors());
        Glide.with(context)
                .load(book.getThumbnail())
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.thumbnail);

        holder.btnDelete.setOnClickListener(v -> {
            if (deleteClickListener != null) {
                deleteClickListener.onDeleteClick(book);
            }
        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("bookId", book.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return (favoriteList == null) ? 0 : favoriteList.size();
    }
}
