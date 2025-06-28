package com.root4u.root4word;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.ViewHolder> {
    private List<WordListDetailActivity.WordItem> items;

    public WordAdapter(List<WordListDetailActivity.WordItem> items){
        this.items = items;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView wordView, meaningView;

        public ViewHolder(View view){
            super(view);
            wordView = view.findViewById(R.id.word_text);
            meaningView = view.findViewById(R.id.meaning_text);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_word_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WordListDetailActivity.WordItem item = items.get(position);
        holder.wordView.setText(item.getWord());
        holder.meaningView.setText(item.getMeaning());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


}
