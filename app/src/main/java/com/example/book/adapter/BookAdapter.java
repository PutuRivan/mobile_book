package com.example.book.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.book.DetailActivity;
import com.example.book.R;
import com.bumptech.glide.Glide;
import com.example.book.model.Book;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private Context context;
    private List<Book> bookList;

    public BookAdapter(Context context) {
        this.context = context;
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
        notifyDataSetChanged();
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView bookTitle, bookAuthor;

        public BookViewHolder(View view) {
            super(view);
            thumbnail = view.findViewById(R.id.thumbnail);
            bookTitle = view.findViewById(R.id.bookTitle);
            bookAuthor = view.findViewById(R.id.bookAuthor);
        }
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        Book book = bookList.get(position);
        holder.bookTitle.setText(book.getTitle());
        holder.bookAuthor.setText(book.getAuthors().get(0)); // Ambil author pertama
        Glide.with(context)
                .load(book.getThumbnail())
                .placeholder(R.drawable.ic_launcher_background) // Gambar placeholder opsional
                .into(holder.thumbnail);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("title", book.getTitle());
            intent.putExtra("author", book.getAuthors().get(0)); // ambil author pertama
            intent.putExtra("thumbnail", book.getThumbnail());
            intent.putExtra("description", book.getDescription());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return (bookList == null) ? 0 : bookList.size();
    }
}
