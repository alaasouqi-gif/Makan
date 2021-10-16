package com.example.makan.activity.item;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;

import com.example.makan.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.squareup.picasso.Picasso;


public class item_my_reservation extends AppCompatActivity {
    Toolbar toolbar;

    TextView name;
    TextView start_date;
    TextView end_date;
    TextView sub_time;
    TextView payment_time;
    TextView num_ticket;
    TextView amount;
    TextView id_reservation;
    Intent intent;

    AppCompatImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_my_reservation);
        name = findViewById(R.id.name);
        start_date = findViewById(R.id.start_date);
        end_date = findViewById(R.id.end_date);
        sub_time = findViewById(R.id.sub_time);
        payment_time = findViewById(R.id.payment_time);
        num_ticket = findViewById(R.id.num_ticket);
        amount = findViewById(R.id.price);
        id_reservation = findViewById(R.id.id_reservation);


        intent = getIntent();

        imageView = findViewById(R.id.imageView);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsingToolbarLayout);
        AppBarLayout appBarLayout = findViewById(R.id.appBarLayout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
                    collapsingToolbarLayout.setTitle(intent.getStringExtra("name"));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");//careful there should a space between double quote otherwise it wont work
                    toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
                    isShow = false;
                }
            }
        });
        String im = intent.getStringExtra("image");
        Picasso.get()
                .load(intent.getStringExtra("image"))
                .into(imageView);

        if ("event".equals(intent.getStringExtra("type"))) {
            name.setText(intent.getStringExtra("name"));
            num_ticket.setVisibility(View.VISIBLE);
            num_ticket.setText("Number Of Tickets : " + intent.getStringExtra("num_ticket"));
            payment_time.setText("Check Out Date : " + intent.getStringExtra("payment_time"));
            amount.setText("Amount : " + intent.getStringExtra("amount"));
            start_date.setVisibility(View.GONE);
            end_date.setVisibility(View.GONE);
            sub_time.setText(intent.getStringExtra("sub_time"));
            id_reservation.setText("Id Reservation : " + intent.getStringExtra("reservation_id"));
        } else {
            name.setText(intent.getStringExtra("name"));
            payment_time.setText("Check Out Date : " + intent.getStringExtra("payment_time"));
            amount.setText("Amount : " + intent.getStringExtra("amount"));
            start_date.setText("Open : " + intent.getStringExtra("open"));
            end_date.setText("Close : " + intent.getStringExtra("close"));
            sub_time.setText(intent.getStringExtra("sub_time"));
            id_reservation.setText("Id Reservation : " + intent.getStringExtra("reservation_id"));
            num_ticket.setVisibility(View.GONE);
        }


    }

}
