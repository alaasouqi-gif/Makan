package com.example.makan.activity.payment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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

public class payment_restaurant extends AppCompatActivity {

    Toolbar toolbar;
    TextInputEditText textInputEditText;
    TextInputLayout textInputLayout;
    String SID;

    Long resCount;
    boolean chk;
    int f = 2;

    AutoCompleteTextView hour;
    AutoCompleteTextView minute;
    AutoCompleteTextView am;

    TextView open;
    TextView name;
    TextView off_day;

    TextView policies;

    TextView price;


    String resHour = "";
    String open_time;
    String close_time;
    final ArrayList<String> off_days = new ArrayList<>();
    double amount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_restaurent);

        Intent intent = getIntent();
        SID = intent.getStringExtra("SID");
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("services").child(SID).child("reservations");
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("services").child(SID);


        toolbar = findViewById(R.id.toolbar);

        name = findViewById(R.id.name);
        open = findViewById(R.id.open);
        off_day = findViewById(R.id.off_day);
        price = findViewById(R.id.price);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
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

        hour = findViewById(R.id.hour);

        myRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name.setText(dataSnapshot.child("name").getValue().toString());
                open_time = String.valueOf(dataSnapshot.child("open").getValue());
                close_time = String.valueOf(dataSnapshot.child("close").getValue());
                open.setText(open_time + " to " + close_time);
                amount = Double.parseDouble(dataSnapshot.child("price").getValue().toString());
                price.setText("Price : " + amount);
                try {
                    off_days.add(dataSnapshot.child("off days").child(0 + "").getValue().toString());
                    off_day.setText("Off Days : " + off_days.get(0));

                    for (int i = 1; i < dataSnapshot.child("off days").getChildrenCount(); i++) {
                        off_days.add(dataSnapshot.child("off days").child(i + "").getValue().toString());
                        off_day.setText(off_day.getText().toString() + ", " + off_days.get(i));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        minute = findViewById(R.id.minutes);

        String[] item =
                {"12", "1", "2", "3",
                        "4", "5", "6", "7",
                        "8", "9", "10", "11"};
        String[] minutes =
                {"00", "10", "20", "30",
                        "40", "50"};
        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(this, R.layout.city_drop_menu, minutes);
        minute.setAdapter(mAdapter);

        ArrayAdapter<String> hAdapter = new ArrayAdapter<>(this, R.layout.city_drop_menu, item);
        hour.setAdapter(hAdapter);

        am = findViewById(R.id.AM);

        String[] item2 =
                {"AM", "PM"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, R.layout.city_drop_menu, item2);
        am.setAdapter(adapter2);


        findViewById(R.id.Next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (am.getText().toString()) {
                    case "AM":
                        if (hour.getText().toString().equals("12")) {
                            resHour = "00:" + minute.getText().toString();
                        } else {
                            resHour = hour.getText().toString() + ":" + minute.getText().toString();
                        }
                        break;
                    case "PM":
                        if (hour.getText().toString().equals("12")) {
                            resHour = hour.getText().toString() + ":" + minute.getText().toString();
                        } else {
                            int d = Integer.parseInt(hour.getText().toString());
                            d = d + 12;
                            resHour = d + ":" + minute.getText().toString();
                        }
                        break;
                }
                chk = true;
                if (isDayValid(off_days)) {
                    if (isHourValid(resHour, open_time, close_time)) {
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


                        String reservationTString = resHour;
                        String[] parts1 = reservationTString.split(":");
                        Calendar reservationT = Calendar.getInstance();
                        reservationT.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts1[0]));
                        reservationT.set(Calendar.MINUTE, Integer.parseInt(parts1[1]));
                        reservationT.set(Calendar.SECOND, 0);


                        if ((reservation.getTime().getMonth() < c.getMonth() && reservation.getTime().getYear() <= c.getYear())
                                || (reservation.getTime().getYear() == c.getYear() && reservation.getTime().getDate() < c.getDate())) {
                            Toast.makeText(payment_restaurant.this, ""
                                    + "Cant reserve before today!!!", Toast.LENGTH_SHORT).show();
                            return;
                        } else if ((reservationT.getTime().getHours() < c.getHours() || reservationT.getTime().getHours() == c.getHours() && reservationT.getTime().getMinutes() < c.getMinutes())
                                && reservation.getTime().getYear() == c.getYear()
                                && reservation.getTime().getMonth() == c.getMonth() && reservation.getTime().getDate() == c.getDate()) {
                            Toast.makeText(payment_restaurant.this, ""
                                    + "Cant reserve before now!!!", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (reservation.getTime().getYear() >= c.getYear()
                                && reservation.getTime().getMonth() >= c.getMonth() && reservation.getTime().getDate() >= c.getDate()) {
                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    resCount = snapshot.getChildrenCount();
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        if (dataSnapshot.getKey().equals(textInputEditText.getText().toString())) {
                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                if (dataSnapshot1.child("time").getValue().toString().equals(resHour)) {
                                                    Toast.makeText(payment_restaurant.this, "this time is reserved", Toast.LENGTH_SHORT).show();
                                                    chk = false;
                                                    break;
                                                } else if (Integer.parseInt(dataSnapshot1.child("time").getValue().toString()
                                                        .split(":")[0])
                                                        - Integer.parseInt(resHour.split(":")[0])
                                                        == 1
                                                        && Integer.parseInt(dataSnapshot1.child("time").getValue().toString()
                                                        .split(":")[1])
                                                        - Integer.parseInt(resHour.split(":")[1]) == 0) {
                                                    Toast.makeText(payment_restaurant.this, "there is reservation after one hour."
                                                            , Toast.LENGTH_SHORT).show();
                                                    chk = false;
                                                    break;
                                                } else if (Integer.parseInt(dataSnapshot1.child("time").getValue().toString()
                                                        .split(":")[0])
                                                        == Integer.parseInt(resHour.split(":")[0])
                                                        && Integer.parseInt(dataSnapshot1.child("time").getValue().toString()
                                                        .split(":")[1])
                                                        - Integer.parseInt(resHour.split(":")[1]) > 0) {
                                                    Toast.makeText(payment_restaurant.this, "there is reservation after "
                                                                    + (Integer.parseInt(dataSnapshot1.child("time").getValue().toString()
                                                                    .split(":")[1])
                                                                    - Integer.parseInt(resHour.split(":")[1]))
                                                                    + " minutes"
                                                            , Toast.LENGTH_SHORT).show();
                                                    chk = false;
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                    if (chk) {
                                        Intent intent = new Intent(payment_restaurant.this, CreditCard.class);
                                        intent.putExtra("reservation", textInputEditText.getText().toString());
                                        intent.putExtra("amount", amount);
                                        intent.putExtra("time", resHour);
                                        intent.putExtra("SID", SID);

                                        chk = false;
                                        intent.putExtra("type", "restaurant");
                                        startActivity(intent);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    } else {
                        Toast.makeText(payment_restaurant.this, "close hour", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //Toast.makeText(payment_restaurant.this, "off Day", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public boolean isHourValid(String time, String openString, String closeString) {
        String reservationString = time;
        String[] parts = reservationString.split(":");
        Calendar reservation = Calendar.getInstance();
        reservation.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[0]));
        reservation.set(Calendar.MINUTE, Integer.parseInt(parts[1]));
        reservation.set(Calendar.SECOND, 0);

        String[] parts2 = openString.split(":");
        Calendar open = Calendar.getInstance();
        open.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts2[0]));
        open.set(Calendar.MINUTE, Integer.parseInt(parts2[1]));
        open.set(Calendar.SECOND, 0);


        String[] parts3 = closeString.split(":");
        Calendar close = Calendar.getInstance();
        close.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts3[0]));
        close.set(Calendar.MINUTE, Integer.parseInt(parts3[1]));
        close.set(Calendar.SECOND, 0);

        Calendar midNight = Calendar.getInstance();
        midNight.set(Calendar.HOUR_OF_DAY, 0);
        midNight.set(Calendar.MINUTE, 0);
        midNight.set(Calendar.SECOND, 0);

        if ((reservation.after(open) || reservation.equals(open)) && close.before(open) && reservation.after(close)) {
            if (isTimeEnough(close, reservation, 1, 1)) {
                return true;
            } else {
                return false;
            }
        } else if (reservation.before(close) && reservation.before(open) && close.before(open)) {
            if (isTimeEnough(close, reservation, 2, 1)) {
                return true;
            } else {
                return false;
            }
        } else if ((reservation.after(open) || reservation.equals(open)) && reservation.before(close)) {
            if (isTimeEnough(close, reservation, 2, 1)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean isDayValid(List<String> off_days) {
        SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy");
        String finalDay = "";
        try {
            Date date = format.parse(textInputEditText.getText().toString());
            DateFormat format1 = new SimpleDateFormat("EEEE");
            finalDay = format1.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (off_days.contains(finalDay)) {
            return false;
        } else
            return true;
    }

    private boolean isTimeEnough(Calendar close, Calendar reservation, int timesCase, int period) {
        int diff = 0;
        switch (timesCase) {
            case 1:
                diff = Math.abs(24 - (reservation.getTime().getHours() - close.getTime().getHours()));
                break;
            case 2:
                diff = Math.abs(reservation.getTime().getHours() - close.getTime().getHours());
                break;
        }
        if (diff >= period) {
            return true;
        } else {
            return false;
        }
    }

}