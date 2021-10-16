package com.example.makan.activity.add_services;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makan.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class add_gym_reservation extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    TextView add;
    TextInputEditText period_text;
    TextInputEditText price_text;

    ArrayList<Integer> period = new ArrayList<>();
    ArrayList<Integer> price = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gym_reservation);

        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerview);
        add = findViewById(R.id.add);
        period_text = findViewById(R.id.period);
        price_text = findViewById(R.id.price);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final RecyclerView.Adapter adapter = new adapterGym(period, price, this);

        recyclerView.setAdapter(adapter);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (period_text.getText().toString().equals("")
                        && price_text.getText().toString().equals("")) {
                    period_text.setError("enter the period");
                    price_text.setError("enter the price");
                    return;
                } else if (period_text.getText().toString().equals("")) {
                    period_text.setError("enter the period");
                    return;
                } else if (price_text.getText().toString().equals("")) {
                    price_text.setError("enter the price");
                    return;
                }

                period.add(Integer.parseInt(period_text.getText().toString()));
                price.add(Integer.parseInt(price_text.getText().toString()));
                adapter.notifyDataSetChanged();
            }
        });

        findViewById(R.id.Next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(add_gym_reservation.this, SelectLocation.class);

                if (period.isEmpty()) {
                    period_text.setError("Please add subscription");
                    return;
                }

                intent.putIntegerArrayListExtra("periods", period);
                intent.putIntegerArrayListExtra("prices", price);


                intent.putExtra("owner", getIntent().getStringExtra("owner_g"));

                intent.putExtra("name", getIntent().getStringExtra("name_g"));

                intent.putExtra("open", getIntent().getStringExtra("open_g"));
                intent.putExtra("close", getIntent().getStringExtra("close_g"));

                intent.putExtra("off", getIntent().getStringArrayExtra("off_g"));

                intent.putExtra("phone", getIntent().getStringExtra("phone_g"));
                intent.putExtra("email", getIntent().getStringExtra("email_g"));
                intent.putExtra("description", getIntent().getStringExtra("description_g"));
                intent.putExtra("image", getIntent().getStringExtra("image_g"));

                intent.putExtra("key", getIntent().getStringExtra("key_g"));

                intent.putExtra("type", getIntent().getStringExtra("type_g"));
                startActivity(intent);
                finish();

            }
        });

    }

}

class adapterGym extends RecyclerView.Adapter<adapterGym.viewholder> {
    public ArrayList<Integer> period;
    public ArrayList<Integer> price;

    Context context;

    public adapterGym(ArrayList<Integer> period, ArrayList<Integer> price, Context context) {
        super();
        this.period = period;
        this.price = price;
        this.context = context;
    }

    @NonNull
    @Override
    public adapterGym.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gym_reservation, parent, false);
        return new adapterGym.viewholder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull adapterGym.viewholder holder, int position) {
        int period = this.period.get(position);
        int price = this.price.get(position);

        String string_period = "The period : " + period + " month";
        String string_price = "price : " + price;

        holder.periodT.setText(string_period);
        holder.priceT.setText(string_price);

    }

    @Override
    public int getItemCount() {
        return period.size();
    }

    public class viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView priceT;
        TextView periodT;
        ImageView imageView;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            periodT = itemView.findViewById(R.id.period);
            priceT = itemView.findViewById(R.id.price);
            imageView = itemView.findViewById(R.id.edit);

            imageView.setOnClickListener(this);

        }


        @Override
        public void onClick(View view) {

            if (view.getId() == imageView.getId()) {
                PopupMenu popup = new PopupMenu(context, imageView);
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
            period.remove(position);
            price.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, period.size());
        }

    }
}

