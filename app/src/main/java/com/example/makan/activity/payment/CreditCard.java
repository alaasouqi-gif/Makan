package com.example.makan.activity.payment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.craftman.cardform.Card;
import com.craftman.cardform.CardForm;
import com.craftman.cardform.OnPayBtnClickListner;
import com.example.makan.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CreditCard extends AppCompatActivity {
    String type;
    String date;
    double amount;
    int period;
    String time;
    String SID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card);

        date = getIntent().getStringExtra("subscription");

        type = getIntent().getStringExtra("type");
        amount = getIntent().getDoubleExtra("amount", 1);
        period = getIntent().getIntExtra("period", 3);
        time = getIntent().getStringExtra("time");

        Intent intent = getIntent();

        SID = intent.getStringExtra("SID");

        CardForm cardForm = findViewById(R.id.card_form);

        TextView textView = findViewById(R.id.payment_amount);
        Button button = findViewById(R.id.btn_pay);

        textView.setText(amount + " JOD");

        button.setText("SUBMIT");

        cardForm.setPayBtnClickListner(new OnPayBtnClickListner() {
            @Override
            public void onClick(Card card) {
                addOnFirebase();
                finish();
            }
        });
    }

    String paymentID = FirebaseDatabase.getInstance().getReference().push().getKey();
    Date c;
    SimpleDateFormat df;

    private void addOnFirebase() {
        c = Calendar.getInstance().getTime();
        df = new SimpleDateFormat("dd MM yyyy HH:mm:ss", Locale.getDefault());

        switch (type) {

            case "event":
                FirebaseDatabase.getInstance().getReference("services").child(SID).child("tickets").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Long tickets = (Long) snapshot.getValue();
                        tickets += getIntent().getIntExtra("tickets", 1);
                        FirebaseDatabase.getInstance().getReference("services").child(SID).child("tickets").setValue(tickets);

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("services").child(SID).child("subscriptions")
                                .child(df.format(c));
                        reference.child("user").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        reference.child("tickets number").setValue(getIntent().getIntExtra("tickets", 1));
                        reference.child("amount").setValue(amount + " JOD");
                        reference.child("id").setValue(paymentID);

                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("reservations").child(df.format(c));
                        reference2.child("amount").setValue(amount + " JOD");
                        reference2.child("service").setValue(SID);
                        reference2.child("id").setValue(paymentID);
                        reference2.child("tickets number").setValue(getIntent().getIntExtra("tickets", 1));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                break;
            case "gym":
                FirebaseDatabase.getInstance().getReference("services").child(SID).child("books").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Long books = (Long) snapshot.getValue();
                        books++;
                        FirebaseDatabase.getInstance().getReference("services").child(SID).child("books").setValue(books);
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("services").child(SID).child("subscriptions")
                                .child(df.format(c));
                        reference.child("user").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        reference.child("period").setValue(getIntent().getIntExtra("period", 1));
                        reference.child("amount").setValue(amount + " JOD");
                        reference.child("date").setValue(date);
                        reference.child("id").setValue(paymentID);

                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("reservations").child(df.format(c));
                        reference2.child("amount").setValue(amount + " JOD");
                        reference2.child("service").setValue(SID);
                        reference2.child("id").setValue(paymentID);
                        reference2.child("period").setValue(getIntent().getIntExtra("period", 1));
                        reference2.child("date").setValue(date);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                break;
            case "pitch":
                date = getIntent().getStringExtra("reservation");
                FirebaseDatabase.getInstance().getReference("services").child(SID).child("books").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Long books = (Long) snapshot.getValue();
                        books++;
                        FirebaseDatabase.getInstance().getReference("services").child(SID).child("books").setValue(books);
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("services").child(SID).child("subscriptions")
                                .child(df.format(c));
                        reference.child("user").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        reference.child("period").setValue(period);
                        reference.child("time").setValue(time);
                        reference.child("amount").setValue(amount + " JOD");
                        reference.child("date").setValue(date);
                        reference.child("id").setValue(paymentID);

                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("reservations").child(df.format(c));
                        reference2.child("amount").setValue(amount + " JOD");
                        reference2.child("service").setValue(SID);
                        reference2.child("id").setValue(paymentID);
                        reference2.child("period").setValue(period);
                        reference2.child("date").setValue(date);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                break;
            case "restaurant":
                date = getIntent().getStringExtra("reservation");
                FirebaseDatabase.getInstance().getReference("services").child(SID).child("books").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Long books = (Long) snapshot.getValue();
                        books++;
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("services").child(SID).child("subscriptions")
                                .child(df.format(c));
                        reference.child("user").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        reference.child("time").setValue(time);
                        reference.child("amount").setValue(amount + " JOD");
                        reference.child("date").setValue(date);
                        reference.child("id").setValue(paymentID);

                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("reservations").child(df.format(c));
                        reference2.child("amount").setValue(amount + " JOD");
                        reference2.child("service").setValue(SID);
                        reference2.child("id").setValue(paymentID);
                        reference2.child("date").setValue(date);
                        reference2.child("time").setValue(time);
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        }


    }
}