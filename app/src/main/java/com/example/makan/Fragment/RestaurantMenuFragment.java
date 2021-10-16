package com.example.makan.Fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makan.Data.MenuItem;
import com.example.makan.R;
import com.example.makan.activity.menu;
import com.example.makan.adapter.adapter_show_menu;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RestaurantMenuFragment extends Fragment {

    public RestaurantMenuFragment() {
        super();
    }

    String Mame;
    adapter_show_menu adapter_show_menu;

    public RestaurantMenuFragment(String position) {
        super();
        Mame = position;
    }

    ArrayList<MenuItem> MenuItems = new ArrayList<>();
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_restaurent_menu, container, false);
        MenuItems.clear();

        recyclerView = root.findViewById(R.id.food_menu);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseDatabase.getInstance().getReference().child("services").child(menu.id).child("menu").child(Mame).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MenuItems.add(new MenuItem(dataSnapshot.getKey(), dataSnapshot.child("price").getValue().toString(), Uri.parse(dataSnapshot.child("photo").getValue().toString()), Mame));
                }
                adapter_show_menu = new adapter_show_menu(MenuItems, getContext());
                recyclerView.setAdapter(adapter_show_menu);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return root;

    }

}
