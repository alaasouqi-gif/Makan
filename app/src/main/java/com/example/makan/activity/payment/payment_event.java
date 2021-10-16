package com.example.makan.activity.payment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.example.makan.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class payment_event extends AppCompatActivity {


    TextView num_tickets;
    TextView total;
    TextView start_date;
    TextView end_date;
    TextView price;
    int tmax = 100000;
    TextView title;
    Long count;
    int num;

    double price_num;
    double num_total;

    String SID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_event);
        Toolbar toolbar;

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();

        SID = intent.getStringExtra("SID");

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("services").child(SID);


        CardView subtraction = findViewById(R.id.subtraction);
        CardView add = findViewById(R.id.plus);
        num_tickets = findViewById(R.id.num_tickets);
        total = findViewById(R.id.total);
        start_date = findViewById(R.id.start_date);
        end_date = findViewById(R.id.end_date);
        price = findViewById(R.id.price);
        title = findViewById(R.id.title);

        num = Integer.parseInt(num_tickets.getText().toString());


        myRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                price_num = Double.parseDouble(dataSnapshot.child("price").getValue().toString());
                start_date.setText(dataSnapshot.child("open").getValue().toString());
                end_date.setText(dataSnapshot.child("close").getValue().toString());
                total.setText("Total : " + price_num + " JD");
                price.setText(price_num + " JD");
                title.setText(dataSnapshot.child("name").getValue().toString());
                count = (Long) dataSnapshot.child("tickets").getValue();
                try {
                    tmax = Integer.parseInt(dataSnapshot.child("tickets num").getValue().toString());


                } catch (Exception e) {
                }
                num_total = price_num;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        subtraction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (num != 1) {
                    num_tickets.setText(--num + "");
                    num_total = num * price_num;
                    total.setText("Total : " + num_total + " JD");
                }
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                num_tickets.setText(++num + "");
                num_total = num * price_num;
                total.setText("Total : " + num_total + " JD");

            }
        });

        findViewById(R.id.Next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (count + num > tmax) {
                    Toast.makeText(payment_event.this, "there is not Enough Tickets", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(payment_event.this, CreditCard.class);
                    intent.putExtra("amount", num_total);
                    intent.putExtra("tickets", num);
                    intent.putExtra("type", "event");
                    intent.putExtra("SID", SID);
                    startActivity(intent);
                }

            }
        });

    }
}