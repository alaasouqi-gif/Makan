package com.example.makan.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makan.Data.KindOfInterest;
import com.example.makan.R;
import com.example.makan.adapter.adapterKindOfInterest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class choose_interest extends AppCompatActivity {

    ArrayList<KindOfInterest> kindOfInterests = new ArrayList<>();
    public static Activity fa;
    RecyclerView recyclerView;
    Button Next;
    ArrayList<String> category = new ArrayList<>();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Users").child(user.getUid());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_intrest);

        fa = this;

        Next = findViewById(R.id.Next);

        kindOfInterests.add(new KindOfInterest(R.drawable.party, "Party"));
        kindOfInterests.add(new KindOfInterest(R.drawable.music, "Music"));
        kindOfInterests.add(new KindOfInterest(R.drawable.workshop, "Workshops"));
        kindOfInterests.add(new KindOfInterest(R.drawable.seminars, "Seminars"));
        kindOfInterests.add(new KindOfInterest(R.drawable.business, "Business"));
        kindOfInterests.add(new KindOfInterest(R.drawable.oriental_food, "Oriental Food"));
        kindOfInterests.add(new KindOfInterest(R.drawable.sea_food, "SeaFood"));
        kindOfInterests.add(new KindOfInterest(R.drawable.italianfood, "Italian Food"));
        kindOfInterests.add(new KindOfInterest(R.drawable.fastfood, "FastFood"));
        kindOfInterests.add(new KindOfInterest(R.drawable.groceries, "Groceries"));
        kindOfInterests.add(new KindOfInterest(R.drawable.apparel, "Apparel"));
        kindOfInterests.add(new KindOfInterest(R.drawable.furniture, "Furniture"));
        kindOfInterests.add(new KindOfInterest(R.drawable.sport, "Football"));
        kindOfInterests.add(new KindOfInterest(R.drawable.gym, "Gym"));
        kindOfInterests.add(new KindOfInterest(R.drawable.swimming, "Swimming"));
        kindOfInterests.add(new KindOfInterest(R.drawable.basketball, "Basketball"));
        kindOfInterests.add(new KindOfInterest(R.drawable.tennis, "Tennis"));

        myRef.child("interests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    category.add(dataSnapshot.getValue().toString());
                }

                for (int j = 0; j < kindOfInterests.size(); j++) {

                    for (int i = 0; i < category.size(); i++) {

                        if (kindOfInterests.get(j).Name.equals(category.get(i))) {
                            kindOfInterests.get(j).select = 1;
                        }
                    }
                }

                int count = 0;
                for (int i = 0; i < kindOfInterests.size(); i++) {
                    if (kindOfInterests.get(i).select == 1) {
                        count++;
                    }
                }
                if (count < 3) {
                    Next.setVisibility(View.INVISIBLE);

                } else
                    Next.setVisibility(View.VISIBLE);


                recyclerView = findViewById(R.id.Interests_list);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));

                adapterKindOfInterest adapter = new adapterKindOfInterest(kindOfInterests, getApplicationContext());

                recyclerView.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                assert user != null;
                myRef.child("interests").removeValue();

                for (int i = 0; i < kindOfInterests.size(); i++) {
                    if (kindOfInterests.get(i).select != 0) {
                        myRef.child("interests").push().setValue(kindOfInterests.get(i).Name);
                    }
                }
                Intent next = new Intent(choose_interest.this, MainActivity.class);
                startActivity(next);
            }
        });
    }
}
