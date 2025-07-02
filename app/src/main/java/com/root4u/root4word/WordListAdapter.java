package com.root4u.root4word;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.ViewHolder> {

    public interface OnWordListDeleteListener{
        void onDelete(String wordList);
    }

    private Context context;
    private List<WordListMenuActivity.WordListItem> items;
    private OnWordListDeleteListener onWordListDeleteListener;

    public WordListAdapter(Context context, List<WordListMenuActivity.WordListItem> items, OnWordListDeleteListener listener){
        this.context = context;
        this.items = items;
        this.onWordListDeleteListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView nameView, countView;
        FloatingActionButton removeWordListBtn;

        public ViewHolder(View view){
            super(view);
            nameView = view.findViewById(R.id.item_name);
            countView = view.findViewById(R.id.item_count);
            removeWordListBtn = view.findViewById(R.id.removeWordListBtn);
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
        WordListMenuActivity.WordListItem item = items.get(position);
        holder.nameView.setText(item.getName());
        holder.countView.setText(String.valueOf(item.getCount()));
        
        // 아이템 클릭 시 detail 가진 채로 WordListDetailActivity로 넘어감
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, WordListDetailActivity.class);
            intent.putExtra("fileName", item.getName()+".csv");
            context.startActivity(intent);
        });

        holder.removeWordListBtn.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("단어 리스트 삭제")
                    .setMessage("'" + item.getName() + "'를 삭제하시겠습니까?")
                    .setPositiveButton("삭제", (dialog, which) -> {
                        if (onWordListDeleteListener != null){
                            onWordListDeleteListener.onDelete(item.getName());
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
