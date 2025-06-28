package com.root4u.root4word;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.ViewHolder> {
    private Context context;
    private List<WordListMenu.WordListItem> items;

    public WordListAdapter(Context context, List<WordListMenu.WordListItem> items){
        this.context = context;
        this.items = items;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView nameView, countView;

        public ViewHolder(View view){
            super(view);
            nameView = view.findViewById(R.id.item_name);
            countView = view.findViewById(R.id.item_count);
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
        WordListMenu.WordListItem item = items.get(position);
        holder.nameView.setText(item.getName());
        holder.countView.setText(String.valueOf(item.getCount()));
        
        // 아이템 클릭 시 detail 가진 채로 WordListDetailActivity로 넘어감
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, WordListDetailActivity.class);
            intent.putExtra("fileName", item.getName()+".csv");
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
