package com.root4u.root4word;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.ViewHolder> {

    public interface OnWordDeleteListener {
        void onDelete(String word);
    }

    Context context;
    private List<WordListDetailActivity.WordItem> items;
    private OnWordDeleteListener onWordDeleteListener;

    public WordAdapter(Context context, List<WordListDetailActivity.WordItem> items, OnWordDeleteListener listener){
        this.context = context;
        this.items = items;
        this.onWordDeleteListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView wordView, meaningView;
        FloatingActionButton removeWordBtn;

        public ViewHolder(View view){
            super(view);
            wordView = view.findViewById(R.id.word_text);
            meaningView = view.findViewById(R.id.meaning_text);
            removeWordBtn = view.findViewById(R.id.removeWordBtn);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_word, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WordListDetailActivity.WordItem item = items.get(position);
        holder.wordView.setText(item.getWord());
        holder.meaningView.setText(item.getMeaning());
        holder.removeWordBtn.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("단어 삭제")
                    .setMessage("'" + item.getWord() + "'를 삭제하시겠습니까?")
                    .setPositiveButton("삭제", (dialog, which) -> {
                        if (onWordDeleteListener != null) {
                            onWordDeleteListener.onDelete(item.getWord());
                        }
                    })
                    .setNegativeButton("취소", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


}
