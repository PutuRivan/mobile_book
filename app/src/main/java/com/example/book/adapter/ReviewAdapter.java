package com.example.book.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book.R;
import com.example.book.model.Review;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private List<Review> reviewList;
    private String userId;
    private ReviewListener listener;

    public ReviewAdapter(List<Review> reviewList, String userId, ReviewListener listener) {
        this.reviewList = reviewList;
        this.userId = userId;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ViewHolder holder, int position) {
        Review review = reviewList.get(position);
        holder.tvUsername.setText(review.getUser().getUsername());
        holder.tvComment.setText(review.getComment());

        // Jika review dari user yg login (userId == review.user.id), tampilkan tombol edit & delete
        if (review.getUser() != null && review.getUser().getId().equals(userId)) {
            holder.btnEdit.setVisibility(View.VISIBLE);
            holder.btnDelete.setVisibility(View.VISIBLE);
        } else {
            holder.btnEdit.setVisibility(View.GONE);
            holder.btnDelete.setVisibility(View.GONE);
        }

        holder.btnEdit.setOnClickListener(v -> {
            listener.showUpdateDialog(
                    review.getBookId(), // kamu perlu pastikan field ini ada di Review model
                    review.getId(),
                    review.getComment()
            );
        });

        holder.btnDelete.setOnClickListener(v -> {
            listener.deleteReview(
                    review.getBookId(), // kamu perlu pastikan field ini ada di Review model
                    review.getId()
            );
        });
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername, tvComment;
        Button btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvComment = itemView.findViewById(R.id.tvComment);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
