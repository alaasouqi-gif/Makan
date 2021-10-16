package com.example.makan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.MultiAutoCompleteTextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.makan.R;
import com.example.makan.sending_email.CouponsGenerator;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Collections;


public class offers extends AppCompatActivity {

    MultiAutoCompleteTextView city;
    Toolbar toolbar;
    TextInputEditText title;
    TextInputEditText description;
    TextInputEditText coupon_num;
    int num;
    ArrayList<String> cities = new ArrayList<>();
    String text;
    String Sid;
    String Stype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);
        toolbar = findViewById(R.id.toolbar);
        city = findViewById(R.id.city);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        coupon_num = findViewById(R.id.coupon_num);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        Sid = intent.getStringExtra("SID");
        Stype = intent.getStringExtra("type");


        String[] item = {"Any", "Amman", "Irbid", "Zarqa", "Mafraq", "Ajloun", "Jerash", "Madaba", "Balqa", "Karak", "Tafileh", "Maan", "Aqaba"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.city_drop_menu, item);
        city.setAdapter(adapter);
        city.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());


        findViewById(R.id.Next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text = city.getText().toString();
                num = Integer.parseInt(coupon_num.getText().toString());
                Collections.addAll(cities, text.split(","));
                Log.d("TAG", "onClick: ddddd" + cities.get(0));
                CouponsGenerator couponsGenerator = new CouponsGenerator(title.getText().toString(), description.getText().toString(), Sid, Stype);
                couponsGenerator.generateCoupons(num, cities);
                finish();
            }
        });


    }
}