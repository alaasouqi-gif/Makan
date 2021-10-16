package com.example.makan.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makan.Data.event;
import com.example.makan.R;
import com.example.makan.activity.edit.edit_event;
import com.example.makan.activity.edit.edit_gym;
import com.example.makan.activity.edit.edit_pitch;
import com.example.makan.activity.edit.edit_restaurant;
import com.example.makan.activity.edit.edit_store;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class adapter_services extends RecyclerView.Adapter<adapter_services.viewholder> {
    public ArrayList<event> events;
    Context context;

    public adapter_services(ArrayList<event> events, Context context) {
        super();
        this.events = events;
        this.context = context;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_big, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        event events = this.events.get(position);
        holder.title.setText(events.title);
        holder.des.setText(events.des);
        holder.place.setText(events.place);
        Picasso.get()
                .load(events.image)
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;
        TextView des;
        TextView place;
        ImageView imageView;
        CardView cardView;
        Button learn_more;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.name_card);
            des = itemView.findViewById((R.id.des));
            place = itemView.findViewById(R.id.place_card);
            imageView = itemView.findViewById(R.id.image_card);
            cardView = itemView.findViewById(R.id.card);
            learn_more = itemView.findViewById(R.id.learn_more);

            learn_more.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            Intent intent;
            switch (events.get(getAdapterPosition()).type) {
                case "restaurant":
                    intent = new Intent(context, edit_restaurant.class);
                    intent.putExtra("id", events.get(getAdapterPosition()).id);
                    intent.putExtra("type", events.get(getAdapterPosition()).type);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                    break;
                case "event":
                    intent = new Intent(context, edit_event.class);
                    intent.putExtra("id", events.get(getAdapterPosition()).id);
                    intent.putExtra("type", events.get(getAdapterPosition()).type);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    break;

                case "store":
                    intent = new Intent(context, edit_store.class);
                    intent.putExtra("id", events.get(getAdapterPosition()).id);
                    intent.putExtra("type", events.get(getAdapterPosition()).type);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    break;
                case "pitch":
                    intent = new Intent(context, edit_pitch.class);
                    intent.putExtra("id", events.get(getAdapterPosition()).id);
                    intent.putExtra("type", events.get(getAdapterPosition()).type);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    break;
                case "gym":
                    intent = new Intent(context, edit_gym.class);
                    intent.putExtra("id", events.get(getAdapterPosition()).id);
                    intent.putExtra("type", events.get(getAdapterPosition()).type);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    break;

            }
        }


    }

}
