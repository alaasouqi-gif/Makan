package com.example.makan.adapter;

import android.annotation.SuppressLint;
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

import com.example.makan.Data.reservation;
import com.example.makan.R;
import com.example.makan.activity.item.item_my_reservation;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;

public class adapter_reservation extends RecyclerView.Adapter<adapter_reservation.viewholder> implements Serializable {
    public ArrayList<reservation> reservations;
    Context context;

    public adapter_reservation(ArrayList<reservation> reservations, Context context) {
        super();
        this.reservations = reservations;
        this.context = context;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_reservation, parent, false);
        return new viewholder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        reservation reservations = this.reservations.get(position);
        holder.title.setText(reservations.name);
        holder.time.setText(reservations.date);
        holder.id_reservation.setText("Payment Id : " + reservations.reservation_id);
        Picasso.get()
                .load(reservations.image)
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }

    public class viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;
        TextView time;
        TextView id_reservation;

        ImageView imageView;
        CardView cardView;
        Button learn_more;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.name_card);
            time = itemView.findViewById((R.id.time));
            id_reservation = itemView.findViewById(R.id.id_reservation);

            imageView = itemView.findViewById(R.id.image_card);
            cardView = itemView.findViewById(R.id.card);
            learn_more = itemView.findViewById(R.id.learn_more);

            learn_more.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            Intent intent;
            switch (reservations.get(getAdapterPosition()).service_type) {
                case "pitch":
                case "gym":
                case "restaurant":
                    intent = new Intent(context, item_my_reservation.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("name", reservations.get(getAdapterPosition()).name);
                    intent.putExtra("type", reservations.get(getAdapterPosition()).service_type);
                    intent.putExtra("sub_time", reservations.get(getAdapterPosition()).date);
                    intent.putExtra("payment_time", reservations.get(getAdapterPosition()).payment_date);
                    intent.putExtra("amount", reservations.get(getAdapterPosition()).amount);
                    intent.putExtra("image", reservations.get(getAdapterPosition()).image);
                    intent.putExtra("open", reservations.get(getAdapterPosition()).open);
                    intent.putExtra("close", reservations.get(getAdapterPosition()).close);
                    intent.putExtra("reservation_id", reservations.get(getAdapterPosition()).reservation_id);


                    context.startActivity(intent);
                    break;
                case "event":

                    intent = new Intent(context, item_my_reservation.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("name", reservations.get(getAdapterPosition()).name);
                    intent.putExtra("type", reservations.get(getAdapterPosition()).service_type);
                    intent.putExtra("sub_time", reservations.get(getAdapterPosition()).date);
                    intent.putExtra("payment_time", reservations.get(getAdapterPosition()).payment_date);
                    intent.putExtra("amount", reservations.get(getAdapterPosition()).amount);
                    intent.putExtra("image", reservations.get(getAdapterPosition()).image);
                    intent.putExtra("num_ticket", reservations.get(getAdapterPosition()).num_tic);
                    intent.putExtra("reservation_id", reservations.get(getAdapterPosition()).reservation_id);

                    context.startActivity(intent);
                    break;

            }
        }


    }

}
