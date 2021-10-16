package com.example.makan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makan.Data.event;
import com.example.makan.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class adapter_list extends RecyclerView.Adapter<adapter_list.viewholder> {
    public ArrayList<event> events;
    Context context;

    public adapter_list(ArrayList<event> events, Context context) {
        super();
        this.events = events;
        this.context = context;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adapter_list.viewholder holder, int position) {
        event events = this.events.get(position);

        holder.title.setText(events.title);
        holder.des.setText(events.des);
        Picasso.get()
                .load(events.image)
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    static class viewholder extends RecyclerView.ViewHolder {
        TextView title;
        TextView des;
        ImageView imageView;
        ImageView button;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.Page_name);
            des = itemView.findViewById(R.id.des);
            imageView = itemView.findViewById(R.id.image);
            button = itemView.findViewById(R.id.not_edit);
            button.setVisibility(View.GONE);
        }
    }
}
