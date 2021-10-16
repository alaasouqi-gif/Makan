package com.example.makan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makan.Data.KindOfInterest;
import com.example.makan.R;
import com.example.makan.activity.choose_interest;

import java.util.ArrayList;

public class adapterKindOfInterest extends RecyclerView.Adapter<adapterKindOfInterest.viewholder> {
    public ArrayList<KindOfInterest> Interests;

    Context context;
    Button next;

    public adapterKindOfInterest(ArrayList<KindOfInterest> Interests, Context context) {
        super();
        this.Interests = Interests;
        this.context = context;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_choose_interest, parent, false);
        return new viewholder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        KindOfInterest Interests = this.Interests.get(position);

        if (Interests.select == 1) {
            holder.cardView.setVisibility(View.VISIBLE);
        }

        holder.imageView.setImageResource(Interests.Image);
        holder.Name.setText(Interests.Name);


    }

    @Override
    public int getItemCount() {
        return Interests.size();
    }

    public class viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView Name;
        ImageView imageView;
        CardView cardView;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            Name = itemView.findViewById(R.id.Name);
            imageView = itemView.findViewById(R.id.image);
            cardView = itemView.findViewById(R.id.done);

            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View view) {
            next = choose_interest.fa.findViewById(R.id.Next);
            if (((int) cardView.getVisibility()) == 0) {
                cardView.setVisibility(View.INVISIBLE);
                Interests.get(getAdapterPosition()).select = 0;

            } else {
                cardView.setVisibility(View.VISIBLE);
                Interests.get(getAdapterPosition()).select = 1;
            }
            int count = 0;
            for (int i = 0; i < Interests.size(); i++) {
                if (Interests.get(i).select == 1) {
                    count++;
                }
            }
            if (count < 3) {
                next.setVisibility(View.INVISIBLE);

            } else
                next.setVisibility(View.VISIBLE);


        }
    }
}

