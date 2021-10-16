package com.example.makan.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makan.Data.top_category;
import com.example.makan.R;
import com.example.makan.activity.top10;

import java.util.ArrayList;

public class adapter_top extends RecyclerView.Adapter<adapter_top.view_holder> {

    ArrayList<top_category> list;
    Context context;

    public adapter_top(ArrayList<top_category> list, Context context) {

        super();
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public view_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_top, parent, false);
        return new view_holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull view_holder holder, int position) {

        top_category current = this.list.get(position);

        holder.category.setText(current.category);
        holder.imageView.setImageResource(current.image);

    }

    @Override
    public int getItemCount() {
        return Math.min(list.size(), 10);
    }

    public class view_holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView category;
        ImageView imageView;

        public view_holder(@NonNull View itemView) {

            super(itemView);
            category = itemView.findViewById(R.id.category_name);
            imageView = itemView.findViewById(R.id.image_card);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, top10.class);
            intent.putExtra("TopCategory", list.get(getAdapterPosition()).category);
            context.startActivity(intent);
        }
    }
}
