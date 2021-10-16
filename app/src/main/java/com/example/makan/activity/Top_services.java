package com.example.makan.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makan.Data.top_category;
import com.example.makan.R;
import com.example.makan.adapter.adapter_top;

import java.util.ArrayList;

public class Top_services extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    ArrayList<top_category> top_categories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_services);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }
        });

        top_categories.add(new top_category(R.drawable.top10, "Top 10"));
        top_categories.add(new top_category(R.drawable.toprate, "Top Rated"));
        top_categories.add(new top_category(R.drawable.visit, "Most Visited"));
        top_categories.add(new top_category(R.drawable.resturant2, "Top Restaurant"));
        top_categories.add(new top_category(R.drawable.party2, "Top Events"));
        top_categories.add(new top_category(R.drawable.stores, "Top Stores"));
        top_categories.add(new top_category(R.drawable.gym2, "Top Gyms"));
        top_categories.add(new top_category(R.drawable.pitch2, "Top Pitch"));


        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        adapter_top adapter = new adapter_top(top_categories, this);

        recyclerView.setAdapter(adapter);


    }
}