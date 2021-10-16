package com.example.makan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makan.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class adapter_gallary_show extends RecyclerView.Adapter<adapter_gallary_show.view_holder> {
    public ArrayList<String> image;
    public Context context;

    public adapter_gallary_show(ArrayList<String> image, Context context) {
        super();
        this.image = image;
        this.context = context;
    }

    @NonNull
    @Override
    public view_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallary_show, parent, false);
        return new view_holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adapter_gallary_show.view_holder holder, int position) {

        String Image = this.image.get(position);
        Picasso.get().load(Image).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return image.size();
    }

    class view_holder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public view_holder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);

        }


    }

}
