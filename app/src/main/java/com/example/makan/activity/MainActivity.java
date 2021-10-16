package com.example.makan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.makan.Fragment.HomeFragment;
import com.example.makan.Fragment.MapsFragment;
import com.example.makan.Fragment.MenuFragment;
import com.example.makan.Fragment.NotificationFragment;
import com.example.makan.R;
import com.example.makan.adapter.ViewPagerAdpter;
import com.example.makan.sending_email.CouponsSrevice;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    TabLayout tabLayout;
    Toolbar toolbar;
    DatabaseReference interestRef;


    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Users").child(user.getUid()).child("First Name");

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        interestRef = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        interestRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {

                try {
                    if (snapshot.child("City").getValue() == null) {
                        Intent intent = new Intent(getApplicationContext(), sign_up2.class);
                        startActivity(intent);
                    } else {
                        if (snapshot.child("interests").getChildrenCount() >= 2) {
                            setContentView(R.layout.activity_main);
                            toolbar = findViewById(R.id.toolbar);
                            setSupportActionBar(toolbar);

                            if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) < 12) {
                                toolbar.setTitle("Good Morning, " + snapshot.child("First Name").getValue().toString());
                            } else {
                                toolbar.setTitle("Good Evening, " + snapshot.child("First Name").getValue().toString());
                            }


                            tabLayout = findViewById(R.id.tabs);
                            ViewPager viewPager = findViewById(R.id.view_pager);
                            final HomeFragment homeFragment = new HomeFragment();
                            NotificationFragment NotificationFragment = new NotificationFragment();
                            MenuFragment MenuFragment = new MenuFragment();
                            final MapsFragment MapsFragment = new MapsFragment();

                            tabLayout.setupWithViewPager(viewPager);

                            ViewPagerAdpter viewPagerAdpter = new ViewPagerAdpter(getSupportFragmentManager(), 0);

                            viewPagerAdpter.add_fragment(homeFragment);
                            viewPagerAdpter.add_fragment(MapsFragment);
                            viewPagerAdpter.add_fragment(NotificationFragment);
                            viewPagerAdpter.add_fragment(MenuFragment);

                            viewPager.setAdapter(viewPagerAdpter);

                            Objects.requireNonNull(tabLayout.getTabAt(0)).setIcon(R.drawable.ic_home_black_24dp);
                            Objects.requireNonNull(tabLayout.getTabAt(2)).setIcon(R.drawable.ic_notifications_black_24dp);
                            Objects.requireNonNull(tabLayout.getTabAt(3)).setIcon(R.drawable.ic_menu_black_24dp);
                            Objects.requireNonNull(tabLayout.getTabAt(1)).setIcon(R.drawable.ic_map_black_24dp);

                            interestRef.child("notified offers").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    try {
                                        int i = 0;
                                        for (DataSnapshot d : dataSnapshot.getChildren()) {
                                            if (d.child("open").getValue().toString().equals("false")) {
                                                i++;
                                                Log.d("TAG", "onDataChange: ss" + d.child("open").getValue().toString());
                                            }
                                        }
                                        if (i == 0) {
                                            Objects.requireNonNull(tabLayout.getTabAt(2)).removeBadge();
                                        } else {
                                            final BadgeDrawable badgeDrawable = Objects.requireNonNull(tabLayout.getTabAt(2)).getOrCreateBadge();
                                            badgeDrawable.setNumber(i);
                                        }
                                    } catch (Exception ignored) {

                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                            tabLayout.getTabAt(1).view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    MapsFragment.getLocationPermissions();
                                }
                            });
                            tabLayout.getTabAt(0).view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    homeFragment.getLocationPermissions();
                                }


                            });
                            Intent intent = new Intent(MainActivity.this, CouponsSrevice.class);
                            startService(intent);

                        } else {
                            Intent to_home = new Intent(MainActivity.this, choose_interest.class);
                            startActivity(to_home);
                        }

                    }
                } catch (Exception e) {
                    Intent intent = new Intent(MainActivity.this, sign_up2.class);
                    startActivity(intent);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


    }

    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


}
