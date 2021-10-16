package com.example.makan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makan.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class adapter_edit_category extends RecyclerView.Adapter<adapter_edit_category.viewholder> {

    ArrayList<String> cat;
    String key;
    Context context;

    public adapter_edit_category(ArrayList<String> cat, Context context, String key) {
        super();
        this.cat = cat;
        this.key = key;
        this.context = context;
    }

    @NonNull
    @Override
    public adapter_edit_category.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_category, parent, false);
        return new adapter_edit_category.viewholder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull adapter_edit_category.viewholder holder, int position) {
        String cat = this.cat.get(position);
        holder.catT.setText(cat);

    }

    @Override
    public int getItemCount() {
        return cat.size();
    }

    public class viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView catT;
        ImageView imageView;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            catT = itemView.findViewById(R.id.cat);
            imageView = itemView.findViewById(R.id.edit);

            imageView.setOnClickListener(this);

        }


        @Override
        public void onClick(View view) {

            if (view.getId() == imageView.getId()) {
                //Log.d("TAG", "onClick: aaaaaaaaaaaaa");
                PopupMenu popup = new PopupMenu(context.getApplicationContext(), imageView);
                popup.getMenuInflater().inflate(R.menu.remove, popup.getMenu());
                popup.show();

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

            }


        }

        void removeAt(int position) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference user_ref = database.getReference("services").child(key).child("menu");
            user_ref.child(cat.get(position)).removeValue();
            cat.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, cat.size());
        }

    }
}
