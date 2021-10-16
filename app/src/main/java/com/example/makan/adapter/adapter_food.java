package com.example.makan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makan.Data.MenuItem;
import com.example.makan.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class adapter_food extends RecyclerView.Adapter<adapter_food.viewholder> {
    public ArrayList<MenuItem> MenuItems;
    Context context;

    public adapter_food(ArrayList<MenuItem> MenuItem, Context context) {
        super();
        this.MenuItems = MenuItem;
        this.context = context;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu, parent, false);
        return new viewholder(view);
    }

    @NonNull
    @Override
    public void onBindViewHolder(@NonNull adapter_food.viewholder holder, int position) {
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

    class viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        TextView price;
        ImageView imageView;
        ImageView remove;


        public viewholder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.name_menu);
            price = itemView.findViewById(R.id.price);
            imageView = itemView.findViewById(R.id.image);
            remove = itemView.findViewById(R.id.remove);

            remove.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == remove.getId()) {
                PopupMenu popup = new PopupMenu(context, remove);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    public boolean onMenuItemClick(android.view.MenuItem item) {
                        if (item.getItemId() == R.id.remove) {
                            removeAt(getAdapterPosition());
                        } else {
                            return true;
                        }
                        return false;
                    }
                });
                popup.inflate(R.menu.remove);
                popup.show();

            }
        }

        void removeAt(int position) {
            MenuItems.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, MenuItems.size());
        }

    }

}
