package com.example.makan.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makan.Data.monitor;
import com.example.makan.R;

import java.util.ArrayList;

public class adapter_monitor extends RecyclerView.Adapter<adapter_monitor.viewholder> {
    public ArrayList<monitor> monitors;
    Context context;

    public adapter_monitor(ArrayList<monitor> monitors, Context context) {
        super();
        this.monitors = monitors;
        this.context = context;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_monitor, parent, false);
        return new viewholder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        monitor monitors = this.monitors.get(position);
        holder.payment_time.setText(monitors.pay_time);
        holder.payment_amount.setText(monitors.amount);
        holder.Tickets_num.setText(monitors.Tickets_num);
        holder.name.setText(monitors.name);
        holder.email.setText(monitors.email);
        holder.phone.setText(monitors.phone);
        holder.id.setText(monitors.id);

    }

    @Override
    public int getItemCount() {
        return monitors.size();
    }

    public class viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView payment_time;
        TextView payment_amount;
        TextView Tickets_num;
        TextView name;
        TextView email;
        TextView phone;
        TextView id;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            payment_time = itemView.findViewById(R.id.payment_time);
            payment_amount = itemView.findViewById(R.id.payment_amount);
            Tickets_num = itemView.findViewById(R.id.Tickets_num);
            name = itemView.findViewById(R.id.name);
            email = itemView.findViewById(R.id.email);
            phone = itemView.findViewById(R.id.phone);
            id = itemView.findViewById(R.id.id);

        }

        @Override
        public void onClick(View view) {
/*
            Intent intent;
            switch (reservations.get(getAdapterPosition()).service_type){
                case "pitch":
                case "gym":
                case "restaurant":
                    intent=new Intent(context, item_my_reservation.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("name",reservations.get(getAdapterPosition()).name);
                    intent.putExtra("type",reservations.get(getAdapterPosition()).service_type);
                    intent.putExtra("sub_time",reservations.get(getAdapterPosition()).date);
                    intent.putExtra("payment_time",reservations.get(getAdapterPosition()).payment_date);
                    intent.putExtra("amount",reservations.get(getAdapterPosition()).amount);
                    intent.putExtra("image",reservations.get(getAdapterPosition()).image);
                    intent.putExtra("open",reservations.get(getAdapterPosition()).open);
                    intent.putExtra("close",reservations.get(getAdapterPosition()).close);
                    intent.putExtra("reservation_id",reservations.get(getAdapterPosition()).reservation_id);


                    context.startActivity(intent);
                    break;
                case "event":

                    intent=new Intent(context, item_my_reservation.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("name",reservations.get(getAdapterPosition()).name);
                    intent.putExtra("type",reservations.get(getAdapterPosition()).service_type);
                    intent.putExtra("sub_time",reservations.get(getAdapterPosition()).date);
                    intent.putExtra("payment_time",reservations.get(getAdapterPosition()).payment_date);
                    intent.putExtra("amount",reservations.get(getAdapterPosition()).amount);
                    intent.putExtra("image",reservations.get(getAdapterPosition()).image);
                    intent.putExtra("num_ticket",reservations.get(getAdapterPosition()).num_tic);
                    intent.putExtra("reservation_id",reservations.get(getAdapterPosition()).reservation_id);

                    context.startActivity(intent);
                    break;

            }

 */
        }


    }

}
