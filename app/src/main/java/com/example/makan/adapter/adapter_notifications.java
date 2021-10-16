package com.example.makan.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makan.Data.notification;
import com.example.makan.R;
import com.example.makan.activity.item.item_Event;
import com.example.makan.activity.item.item_pitch_gym;
import com.example.makan.activity.item.item_restaurant_stores;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class adapter_notifications extends RecyclerView.Adapter<adapter_notifications.viewholder> {
    private ArrayList<notification> notifications;
    private final Context context;
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

    public adapter_notifications(ArrayList<notification> notifications, Context context) {
        super();
        this.notifications = notifications;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        int limit = 10;
        return Math.min(notifications.size(), limit);

    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        notification notification = this.notifications.get(position);
        holder.title.setText(notification.title);
        holder.des.setText(notification.description);
        Picasso.get().load(notification.image).into(holder.imageView);
        if (notification.open.equals("true")) {
            holder.not.setBackgroundColor(Color.WHITE);
        }
    }

    public class viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        TextView des;
        ImageView imageView;
        RelativeLayout not;
        ImageView button;

        //bottom sheet stuff
        TextView name_bottom;
        TextView details_bottom;
        ImageView image_bottom;
        LinearLayout remove_notification;
        LinearLayout linear;


        viewholder(@NonNull View itemView) {
            super(itemView);


            title = itemView.findViewById(R.id.Page_name);
            des = itemView.findViewById(R.id.des);
            imageView = itemView.findViewById(R.id.image);
            button = itemView.findViewById(R.id.not_edit);
            not = itemView.findViewById(R.id.not);

            // linear=itemView.findViewById(R.id.linear);

            button.setOnClickListener(this);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (notifications.get(getAdapterPosition()).open.equals("false")) {

                        myRef.child("notified offers")
                                .child(notifications.get(getAdapterPosition()).id)
                                .child("open").setValue("true");
                        not.setBackgroundColor(Color.WHITE);

                    }

                    Log.d("TAG", "sssss" + notifications.get(getAdapterPosition()).service_id);
                    Intent intent;
                    switch (notifications.get(getAdapterPosition()).SType) {
                        case "restaurant":
                        case "store":
                            intent = new Intent(context, item_restaurant_stores.class);
                            intent.putExtra("id", notifications.get(getAdapterPosition()).service_id);
                            intent.putExtra("type", notifications.get(getAdapterPosition()).SType);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                            break;
                        case "event":
                            intent = new Intent(context, item_Event.class);
                            intent.putExtra("id", notifications.get(getAdapterPosition()).service_id);
                            intent.putExtra("type", notifications.get(getAdapterPosition()).SType);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                            break;
                        case "pitch":
                        case "gym":
                            intent = new Intent(context, item_pitch_gym.class);
                            intent.putExtra("id", notifications.get(getAdapterPosition()).service_id);
                            intent.putExtra("type", notifications.get(getAdapterPosition()).SType);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                            break;

                    }
                }
            });
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == button.getId()) {

                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheet);
                final View BottomSheet = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_not, linear);

                name_bottom = BottomSheet.findViewById(R.id.name_bottom);
                details_bottom = BottomSheet.findViewById(R.id.details_bottom);
                image_bottom = BottomSheet.findViewById(R.id.image_bottom);
                image_bottom = BottomSheet.findViewById(R.id.image_bottom);
                remove_notification = BottomSheet.findViewById(R.id.Remove);

                name_bottom.setText(title.getText());
                details_bottom.setText(des.getText());
                image_bottom.setImageDrawable(imageView.getDrawable());
                remove_notification.setOnClickListener(view1 -> {
                    removeAt(getAdapterPosition());
                    bottomSheetDialog.dismiss();
                });

                bottomSheetDialog.setContentView(BottomSheet);
                bottomSheetDialog.show();
            }

        }

        void removeAt(int position) {
            myRef.child("notified offers").child(notifications.get(position).id).removeValue();
            notifications.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, notifications.size());

        }

    }
}