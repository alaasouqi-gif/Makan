package com.example.makan.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makan.R;
import com.example.makan.adapter.adapter_search_h;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class Search extends AppCompatActivity {

    Toolbar toolbar;
    TextInputEditText textInputEditText;
    ArrayList<String> word = new ArrayList<>();
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        word.clear();
        word.add("events");
        word.add("football");

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textInputEditText = findViewById(R.id.search_bar);


        ImageView imageView = findViewById(R.id.back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.search_history);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final RecyclerView.Adapter adapter = new adapter_search_h(word, this);

        recyclerView.setAdapter(adapter);
        textInputEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    word.add(0, String.valueOf(textInputEditText.getText()));
                    adapter.notifyDataSetChanged();
                    return true;
                }
                return false;
            }
        });

    }
}
