package com.example.makan.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makan.R;

import java.util.ArrayList;

public class adapter_image extends RecyclerView.Adapter<adapter_image.view_holder> {
    public ArrayList<Uri> image;
    public Context context;
    String key;

    public adapter_image(ArrayList<Uri> image, Context context) {
        super();
        this.image = image;
        this.context = context;
    }

    @NonNull
    @Override
    public view_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery, parent, false);
        return new view_holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adapter_image.view_holder holder, int position) {
        Uri Image = this.image.get(position);
        holder.imageView.setImageURI(Image);
        //Picasso.get().load(Image).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return image.size();
    }

    class view_holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        ImageView remove;

        public view_holder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            remove = itemView.findViewById(R.id.remove);

            remove.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == remove.getId()) {
                PopupMenu popup = new PopupMenu(context, remove);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    public boolean onMenuItemClick(MenuItem item) {
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
            image.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, image.size());

        }

    }

}
