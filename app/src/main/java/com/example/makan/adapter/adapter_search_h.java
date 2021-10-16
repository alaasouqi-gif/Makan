package com.example.makan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makan.R;

import java.util.ArrayList;

public class adapter_search_h extends RecyclerView.Adapter<adapter_search_h.viewholder> {
    public ArrayList<String> word;
    Context context;

    public adapter_search_h(ArrayList<String> word, Context context) {
        super();
        this.word = word;
        this.context = context;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        String current = this.word.get(position);
        holder.textView.setText(current);
    }

    @Override
    public int getItemCount() {
        return Math.min(word.size(), 10);

    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView textView;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.search_history);
        }
    }
}
