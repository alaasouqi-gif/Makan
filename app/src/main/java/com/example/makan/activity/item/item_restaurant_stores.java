package com.example.makan.activity.item;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makan.R;
import com.example.makan.activity.list.list_monitor;
import com.example.makan.activity.menu;
import com.example.makan.activity.offers;
import com.example.makan.activity.payment.payment_event;
import com.example.makan.activity.payment.payment_gym;
import com.example.makan.activity.payment.payment_pitch;
import com.example.makan.activity.payment.payment_restaurant;
import com.example.makan.adapter.adapter_gallary_show;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class item_restaurant_stores extends AppCompatActivity {
    Toolbar toolbar;

    String serviceId;
    String serviceType;
    Long visits;

    TextView name;
    TextView categories;
    TextView start_date;
    TextView end_date;
    TextView location;
    TextView description;
    TextView email;
    TextView phone;
    TextView rate;
    ImageView check_later;
    ImageView like;
    ImageView dis;
    Button Ticket;
    Button offer;
    Button monitor;

    LinearLayout linearLayout;

    AppCompatImageView imageView;
    RecyclerView recyclerView;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference user_ref = database.getReference("Users").child(user.getUid()).child("check later");
    DatabaseReference user_ref_like = database.getReference("Users").child(user.getUid());
    DatabaseReference current_user = database.getReference("Users").child(user.getUid());

    int flag;
    int likeflag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_resturent);

        flag = 0;
        likeflag = 0;

        toolbar = findViewById(R.id.toolbar);
        name = findViewById(R.id.name);
        categories = findViewById(R.id.categories);
        start_date = findViewById(R.id.start_date);
        end_date = findViewById(R.id.end_date);
        location = findViewById(R.id.location);
        description = findViewById(R.id.description);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        imageView = findViewById(R.id.imageView);
        rate = findViewById(R.id.rate);
        check_later = findViewById(R.id.check_later);
        like = findViewById(R.id.like);
        dis = findViewById(R.id.dis);
        Ticket = findViewById(R.id.Tickets);
        linearLayout = findViewById(R.id.add_to);
        offer = findViewById(R.id.offer);
        monitor = findViewById(R.id.monitor);

        Intent intent = getIntent();
        serviceId = intent.getStringExtra("id");
        serviceType = intent.getStringExtra("type");

        if (serviceType.equals("restaurant")) {
            toolbar.setTitle("Restaurant");

        } else {
            toolbar.setTitle("Store");
            Ticket.setVisibility(View.GONE);
        }
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        RelativeLayout relativeLayout = findViewById(R.id.menu);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(item_restaurant_stores.this, menu.class);
                intent.putExtra("id", serviceId);
                startActivity(intent);
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
                    if (serviceType.equals("restaurant")) {
                        collapsingToolbarLayout.setTitle("Restaurant");

                    } else {
                        collapsingToolbarLayout.setTitle("Store");
                        Ticket.setVisibility(View.GONE);
                    }

                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");//careful there should a space between double quote otherwise it wont work
                    toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
                    isShow = false;
                }
            }
        });

        current_user.child("services").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot s : dataSnapshot.getChildren()) {

                    if (Objects.equals(s.getValue(), serviceId)) {

                        Ticket.setVisibility(View.GONE);
                        linearLayout.setVisibility(View.GONE);
                        if (serviceType.equals("store")) {

                        } else {
                            offer.setVisibility(View.VISIBLE);
                            monitor.setVisibility(View.VISIBLE);

                        }
                        return;

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        recyclerView = findViewById(R.id.recyclerview_gallary);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        countVisits();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("services").child(serviceId);
        myRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {

                name.setText(snapshot.child("name").getValue().toString());
                Picasso.get().load(snapshot.child("cover photo").getValue().toString()).into(imageView);
                categories.setText(snapshot.child("categories").child(0 + "").getValue().toString());

                for (int i = 1; i < snapshot.child("categories").getChildrenCount(); i++) {
                    categories.setText(categories.getText().toString() + ", " + snapshot.child("categories").child(i + "").getValue());
                }

                start_date.setText("Open : " + snapshot.child("open").getValue());
                end_date.setText("Close : " + snapshot.child("close").getValue());

                description.setText(snapshot.child("des").getValue() + "");
                phone.setText("Phone : " + snapshot.child("phone").getValue());
                email.setText("Email : " + snapshot.child("email").getValue());
                location.setText(snapshot.child("city").getValue() + "");
                rate.setText(snapshot.child("rate").getValue().toString()
                        .substring(0, Math.min(4, snapshot.child("rate").getValue()
                                .toString().length())) + " based on " + (Integer.parseInt(snapshot.child("like")
                        .getValue().toString()) + Integer.parseInt(snapshot.child("dislike")
                        .getValue().toString())) + " Review");

                ArrayList<String> i = new ArrayList<>();
                for (DataSnapshot image : snapshot.child("images").getChildren()) {
                    i.add(image.getValue().toString());
                }
                adapter_gallary_show adapter = new adapter_gallary_show(i, getApplicationContext());
                recyclerView.setAdapter(adapter);

                user_ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot date_check : dataSnapshot.getChildren()) {

                            if (date_check.getValue().equals(serviceId)) {
                                flag = 1;
                                check_later.setImageResource(R.drawable.history_blue);
                                return;

                            } else {
                                flag = 0;
                                check_later.setImageResource(R.drawable.history_32);

                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                user_ref_like.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        likeflag = 0;
                        for (DataSnapshot date_check : dataSnapshot.child("Liked").getChildren()) {

                            if (date_check.getValue().equals(serviceId)) {
                                likeflag = 1;
                                like.setImageResource(R.drawable.thumb_up_blue);
                                return;

                            }

                        }

                        for (DataSnapshot date_check : dataSnapshot.child("Disliked").getChildren()) {

                            if (date_check.getValue().equals(serviceId)) {
                                likeflag = 2;
                                dis.setImageResource(R.drawable.thumb_down_blue);
                                return;
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                switch (serviceType) {
                    case "restaurant":
                        intent = new Intent(item_restaurant_stores.this, payment_restaurant.class);
                        intent.putExtra("SID", serviceId);
                        startActivity(intent);
                        break;
                    case "event":
                        intent = new Intent(item_restaurant_stores.this, payment_event.class);
                        intent.putExtra("SID", serviceId);
                        startActivity(intent);
                        break;

                    case "pitch":
                        intent = new Intent(item_restaurant_stores.this, payment_pitch.class);
                        intent.putExtra("SID", serviceId);
                        startActivity(intent);
                        break;

                    case "gym":
                        intent = new Intent(item_restaurant_stores.this, payment_gym.class);
                        intent.putExtra("SID", serviceId);
                        startActivity(intent);
                        break;

                    default:
                }

            }
        });

        check_later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag == 0) {
                    check_later.setImageResource(R.drawable.history_blue);
                    flag = 1;
                    user_ref.child(serviceId).setValue(serviceId);
                } else {
                    flag = 0;
                    check_later.setImageResource(R.drawable.history_32);
                    user_ref.child(serviceId).removeValue();

                }
            }
        });

        offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent;
                intent = new Intent(item_restaurant_stores.this, offers.class);
                intent.putExtra("SID", serviceId);
                intent.putExtra("type", serviceType);
                startActivity(intent);

            }
        });

        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int like_count = Integer.parseInt(dataSnapshot.child("like").getValue().toString());
                        int dislike_count = Integer.parseInt(dataSnapshot.child("dislike").getValue().toString());

                        if (likeflag == 0) {
                            like.setImageResource(R.drawable.thumb_up_blue);
                            likeflag = 1;
                            user_ref_like.child("Liked").child(serviceId).setValue(serviceId);
                            like_count++;


                        } else if (likeflag == 1) {
                            user_ref_like.child("Liked").child(serviceId).removeValue();
                            likeflag = 0;
                            like.setImageResource(R.drawable.thumb_up);
                            like_count--;

                        } else if (likeflag == 2) {
                            user_ref_like.child("Disliked").child(serviceId).removeValue();

                            likeflag = 1;
                            dis.setImageResource(R.drawable.thumb_down);
                            like.setImageResource(R.drawable.thumb_up_blue);
                            user_ref_like.child("Liked").child(serviceId).setValue(serviceId);
                            like_count++;
                            dislike_count--;


                        }
                        myRef.child("like").setValue(like_count);
                        myRef.child("dislike").setValue(dislike_count);

                        if ((like_count + dislike_count) == 0)
                            myRef.child("rate").setValue(0);
                        else
                            myRef.child("rate").setValue((like_count * 1.0 / (like_count + dislike_count)) * 5);


                    }
                });
                dis.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int like_count = Integer.parseInt(dataSnapshot.child("like").getValue().toString());
                        int dislike_count = Integer.parseInt(dataSnapshot.child("dislike").getValue().toString());

                        if (likeflag == 0) {

                            likeflag = 2;
                            dis.setImageResource(R.drawable.thumb_down_blue);
                            user_ref_like.child("Disliked").child(serviceId).setValue(serviceId);
                            dislike_count++;

                        } else if (likeflag == 1) {
                            user_ref_like.child("Liked").child(serviceId).removeValue();

                            likeflag = 2;
                            dislike_count++;
                            like_count--;


                            like.setImageResource(R.drawable.thumb_up);
                            dis.setImageResource(R.drawable.thumb_down_blue);

                            user_ref_like.child("Disliked").child(serviceId).setValue(serviceId);


                        } else if (likeflag == 2) {
                            user_ref_like.child("Disliked").child(serviceId).removeValue();
                            likeflag = 0;
                            dis.setImageResource(R.drawable.thumb_down);
                            dislike_count--;

                        }
                        myRef.child("like").setValue(like_count);
                        myRef.child("dislike").setValue(dislike_count);
                        if ((like_count + dislike_count) == 0)
                            myRef.child("rate").setValue(0);
                        else
                            myRef.child("rate").setValue((like_count * 1.0 / (like_count + dislike_count)) * 5);

                    }

                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
/*
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), edit_menu.class);
                intent.putExtra("sid",serviceId);
                startActivity(intent);
            }
        });*/

        monitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(item_restaurant_stores.this, list_monitor.class);
                intent.putExtra("sid", serviceId);
                intent.putExtra("type", serviceType);
                startActivity(intent);
            }
        });
    }

    public void countVisits() {
        final DatabaseReference service = FirebaseDatabase.getInstance().getReference("services").child(serviceId);
        final DatabaseReference visitors = FirebaseDatabase.getInstance().getReference("services").child(serviceId).child("visitors");
        try {
            visitors.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (dataSnapshot.getValue().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            return;
                        }
                    }
                    visitors.push().setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    service.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            visits = (Long) snapshot.child("visits").getValue();
                            visits++;
                            service.child("visits").setValue(visits);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        } catch (NullPointerException e) {
            visitors.push().setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
            service.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    visits = (Long) snapshot.child("visits").getValue();
                    visits++;
                    service.child("visits").setValue(visits);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }


    }

}



