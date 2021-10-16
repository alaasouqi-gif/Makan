package com.example.makan.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makan.Data.event;
import com.example.makan.R;
import com.example.makan.adapter.adapter_following;

import java.util.ArrayList;

public class following extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    private ArrayList<event> events = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        /*
        events.add(new event("restaurant pizza hut","Amman","Pizza hut","this is events is to play football",1,R.drawable.music));
        events.add(new event("Android Course","Amman","Courses","this is events is to play football",2,R.drawable.restaurant));
        events.add(new event("Pizza hut event","Amman","Pizza hut","this is events is to play football",2,R.drawable.party));
        events.add(new event("Pizza hut event","Amman","Pizza hut","this is events is to play football",2,R.drawable.sport));
        events.add(new event("Pizza hut event","Amman","Pizza hut","this is events is to play football",2,R.drawable.android));
        events.add(new event("Pizza hut event","Amman","Pizza hut","this is events is to play football",2,R.drawable.android));
        events.add(new event("Pizza hut event","Amman","Pizza hut","this is events is to play football",2,R.drawable.android));
        events.add(new event("Pizza hut event","Amman","Pizza hut","this is events is to play football",1,R.drawable.android));
        events.add(new event("Pizza hut event","Amman","Pizza hut","this is events is to play football",2,R.drawable.android));
        events.add(new event("Pizza hut event","Amman","Pizza hut","this is events is to play football",1,R.drawable.android));
        events.add(new event("Pizza hut event","Amman","Pizza hut","this is events is to play football",3,R.drawable.android));
        events.add(new event("Pizza hut event","Amman","Pizza hut","this is events is to play football",3,R.drawable.android));
*/

        recyclerView = findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView.Adapter adapter = new adapter_following(events, this);

        recyclerView.setAdapter(adapter);

    }
}
