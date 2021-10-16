package com.example.makan.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makan.Data.MenuItem;
import com.example.makan.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class adapter_show_menu extends RecyclerView.Adapter<adapter_show_menu.viewholder> {
    public ArrayList<MenuItem> MenuItems;
    Context context;

    public adapter_show_menu(ArrayList<MenuItem> MenuItem, Context context) {
        super();
        this.MenuItems = MenuItem;
        this.context = context;
    }

    @NonNull
    @Override
    public adapter_show_menu.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemshowmenu, parent, false);
        return new adapter_show_menu.viewholder(view);
    }

    @NonNull
    @Override
    public void onBindViewHolder(@NonNull adapter_show_menu.viewholder holder, int position) {
        MenuItem foods = this.MenuItems.get(position);

        holder.title.setText(foods.Name);
        holder.price.setText(foods.Price);
        Picasso.get()
                .load(foods.image)
                .into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        return MenuItems.size();
    }

    class viewholder extends RecyclerView.ViewHolder {
        TextView title;
        TextView price;
        ImageView imageView;


        public viewholder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.name_menu);
            price = itemView.findViewById(R.id.price);
            imageView = itemView.findViewById(R.id.image);

        }


    }

}


