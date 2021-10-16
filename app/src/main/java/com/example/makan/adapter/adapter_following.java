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

import com.example.makan.Data.event;
import com.example.makan.R;
import com.example.makan.activity.item.item_Event;
import com.example.makan.activity.item.item_pitch_gym;
import com.example.makan.activity.item.item_restaurant_stores;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class adapter_following extends RecyclerView.Adapter<adapter_following.viewholder> {
    public ArrayList<event> events;
    Context context;

    public adapter_following(ArrayList<event> events, Context context) {
        super();
        this.events = events;
        this.context = context;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_following, parent, false);
        return new viewholder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final adapter_following.viewholder holder, int position) {
        event events = this.events.get(position);

        holder.title.setText(events.title);
        Picasso.get()
                .load(events.image)
                .into(holder.imageView);

        holder.value.setText(events.pop);

    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    class viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        ImageView imageView;
        TextView value;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.Page_name);
            imageView = itemView.findViewById(R.id.image);
            value = itemView.findViewById(R.id.value);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent;
            switch (events.get(getAdapterPosition()).type) {
                case "restaurant":
                case "store":
                    intent = new Intent(context, item_restaurant_stores.class);
                    intent.putExtra("id", events.get(getAdapterPosition()).id);
                    intent.putExtra("type", events.get(getAdapterPosition()).type);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    break;
                case "event":
                    intent = new Intent(context, item_Event.class);
                    intent.putExtra("id", events.get(getAdapterPosition()).id);
                    intent.putExtra("type", events.get(getAdapterPosition()).type);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    break;
                case "pitch":
                case "gym":
                    intent = new Intent(context, item_pitch_gym.class);
                    intent.putExtra("id", events.get(getAdapterPosition()).id);
                    intent.putExtra("type", events.get(getAdapterPosition()).type);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    break;


            }
        }

    }


}
