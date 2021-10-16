package com.example.makan.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makan.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class edit_g extends RecyclerView.Adapter<edit_g.view_holder> {
    public ArrayList<String> image;
    public Context context;
    ArrayList<String> k;
    String sid;

    public edit_g(ArrayList<String> image, Context context, ArrayList<String> k, String sid) {
        super();
        this.image = image;
        this.context = context;
        this.sid = sid;
        this.k = k;
    }

    @NonNull
    @Override
    public view_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery, parent, false);
        return new view_holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull edit_g.view_holder holder, int position) {
        String Image = this.image.get(position);
        Picasso.get().load(Image).into(holder.imageView);

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
                Log.d("TAG", "onClick: aaaaaaaaaaaaa");
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
                popup.inflate(R.menu.rere);
                popup.show();

            }
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("services").child(sid).child("images");

        void removeAt(int position) {
            image.remove(position);

            notifyItemRemoved(position);
            databaseReference.child(k.get(position)).removeValue();
            notifyItemRangeChanged(position, image.size());

        }

    }

}
