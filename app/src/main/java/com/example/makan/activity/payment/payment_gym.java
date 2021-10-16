package com.example.makan.activity.payment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.makan.R;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class payment_gym extends AppCompatActivity {

    Toolbar toolbar;

    ArrayList<Integer> period = new ArrayList<>();
    ArrayList<Integer> price = new ArrayList<>();

    Button Next;
    RadioGroup radioGroup;
    TextInputEditText textInputEditText;
    TextInputLayout textInputLayout;

    ArrayList<String> offDays = new ArrayList<>();
    TextView open;
    TextView name;
    TextView off_day;
    String SID;
    String open_time;
    String close_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_gym);

        toolbar = findViewById(R.id.toolbar);
        name = findViewById(R.id.name);
        open = findViewById(R.id.open);
        off_day = findViewById(R.id.off_day);
        radioGroup = findViewById(R.id.radio);

        Intent intent = getIntent();
        SID = intent.getStringExtra("SID");

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("services").child(SID);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Next = findViewById(R.id.Next);
        myRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                price.clear();
                period.clear();
                radioGroup.removeAllViews();
                name.setText(dataSnapshot.child("name").getValue().toString());
                open_time = String.valueOf(dataSnapshot.child("open").getValue());
                close_time = String.valueOf(dataSnapshot.child("close").getValue());
                open.setText(open_time + " to " + close_time);

                for (DataSnapshot s : dataSnapshot.child("subscription type").getChildren()) {
                    period.add(Integer.parseInt(String.valueOf(s.getKey())));
                    price.add(Integer.parseInt(String.valueOf(s.getValue())));
                }

                for (int i = 0; i < period.size(); i++) {

                    RadioButton rb_flash = new RadioButton(getApplicationContext());
                    rb_flash.setText("The period : " + period.get(i) + " months\t\t\tPrice : " + price.get(i) + " JD");
                    RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0, 15, 15, 0);
                    rb_flash.setLayoutParams(params);
                    rb_flash.setTextColor(Color.BLACK);
                    rb_flash.setTextSize(17);
                    rb_flash.setPadding(0, 8, 0, 8);
                    radioGroup.addView(rb_flash);
                }

                try {
                    offDays.add(dataSnapshot.child("off days").child(0 + "").getValue().toString());
                    off_day.setText("Off Days : " + offDays.get(0));

                    for (int i = 1; i < dataSnapshot.child("off days").getChildrenCount(); i++) {
                        offDays.add(dataSnapshot.child("off days").child(i + "").getValue().toString());
                        off_day.setText(off_day.getText().toString() + ", " + offDays.get(i));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SimpleDateFormat df;
                df = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
                Date c = Calendar.getInstance().getTime();
                df.format(c);

                String[] parts21 = textInputEditText.getText().toString().split(" ");
                Date date2;
                Calendar reservation = Calendar.getInstance();

                try {
                    date2 = new SimpleDateFormat("MMM", Locale.getDefault()).parse(parts21[1]);
                    String reservationString = textInputEditText.getText().toString();
                    String[] parts = reservationString.split(" ");
                    reservation.set(Calendar.DAY_OF_MONTH, Integer.parseInt(parts[0]));
                    reservation.set(Calendar.MONTH, date2.getMonth());
                    reservation.set(Calendar.YEAR, Integer.parseInt(parts[2]));

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if ((reservation.getTime().getMonth() < c.getMonth() && reservation.getTime().getYear() <= c.getYear())
                        || (reservation.getTime().getYear() == c.getYear() && reservation.getTime().getDate() < c.getDate())) {
                    Toast.makeText(payment_gym.this, ""
                            + "Cant reserve before today!!!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!isDayValid(offDays)) {
                    Toast.makeText(payment_gym.this, "off Day", Toast.LENGTH_SHORT).show();
                    return;
                }
                int radioButtonID = radioGroup.getCheckedRadioButtonId();
                View radioButton = radioGroup.findViewById(radioButtonID);
                int idx = radioGroup.indexOfChild(radioButton);

                Intent intent = new Intent(payment_gym.this, CreditCard.class);
                int id = radioGroup.getCheckedRadioButtonId();
                intent.putExtra("subscription", textInputEditText.getText().toString());
                intent.putExtra("period", period.get(id - 1));
                intent.putExtra("amount", price.get(id - 1).doubleValue());
                intent.putExtra("type", "gym");
                intent.putExtra("SID", SID);

                startActivity(intent);


            }
        });

        textInputEditText = findViewById(R.id.birth_day_edit);

        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();

        final MaterialDatePicker<Long> materialDatePicker = builder.build();

        textInputLayout = findViewById(R.id.birth_day);

        textInputEditText.setEnabled(false);

        textInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDatePicker.show(getSupportFragmentManager(), "DATA_PIKER");
            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                textInputEditText.setText(materialDatePicker.getHeaderText());

            }
        });


    }

    public boolean isDayValid(List<String> offDays) {
        SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy");
        String finalDay = "";
        try {
            Date date = format.parse(textInputEditText.getText().toString());
            DateFormat format1 = new SimpleDateFormat("EEEE");
            finalDay = format1.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (offDays.contains(finalDay)) {
            return false;
        } else
            return true;
    }
}