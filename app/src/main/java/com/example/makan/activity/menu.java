package com.example.makan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.makan.Fragment.RestaurantMenuFragment;
import com.example.makan.R;
import com.example.makan.adapter.ViewPagerAdpter;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class menu extends AppCompatActivity {

    TabLayout tabLayout;
    Toolbar toolbar;
    ViewPager viewPager;
    ViewPagerAdpter viewPagerAdpter;
    public static String id;
    ArrayList<RestaurantMenuFragment> restaurantMenuFragments = new ArrayList<>();

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("services");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.view_pager);

        viewPagerAdpter = new ViewPagerAdpter(getSupportFragmentManager(), 0);
        tabLayout.setupWithViewPager(viewPager);

        reference.child(id).child("menu").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    restaurantMenuFragments.add(new RestaurantMenuFragment(dataSnapshot.getKey()));

                    viewPagerAdpter.add_fragment(restaurantMenuFragments.get(i++));


                }
                viewPager.setAdapter(viewPagerAdpter);

                i = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    tabLayout.getTabAt(i++).setText(dataSnapshot.getKey());
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}
