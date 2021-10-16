package com.example.makan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.makan.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class sign_up2 extends AppCompatActivity {

    TextInputLayout textInputLayout;
    TextInputEditText textInputEditText;
    TextInputEditText phone;
    TextInputEditText birthDate;
    AutoCompleteTextView city;

    MaterialButton signUp;

    Toolbar toolbar;

    private FirebaseAuth mAuth;


    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference user_ref = database.getReference("Users").child(user.getUid());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);


        phone = findViewById(R.id.phone_nmbr);


        city = findViewById(R.id.city);

        birthDate = findViewById(R.id.birth_day_edit);

        signUp = findViewById(R.id.sign_up);

        city.setEnabled(false);
        String[] item = {"Amman", "Irbid", "Zarqa", "Mafraq", "Ajloun", "Jerash", "Madaba", "Balqa", "Karak", "Tafileh", "Maan", "Aqaba"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.city_drop_menu, item);
        city.setAdapter(adapter);

        mAuth = FirebaseAuth.getInstance();


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checker = true;

                final String phoneNum = phone.getText().toString();
                final String city1 = city.getText().toString();
                final String birth = birthDate.getText().toString();


                if (isPhoneNumber(phoneNum)) {

                } else if (phone.getText().toString().equals("")) {
                    checker = false;
                    phone.setError("Please Enter Phone Number");
                } else {
                    checker = false;
                    phone.setError("Invalid Phone Number");
                }

                if (checker) {
                    //Toast.makeText(sign_up2.this, "sdvjhferij", Toast.LENGTH_SHORT).show();
                    user_ref.child("Birth Date").setValue(birth);
                    user_ref.child("City").setValue(city1);
                    user_ref.child("Phone Number").setValue(phoneNum);
                    Intent intent = new Intent(sign_up2.this, MainActivity.class);
                    startActivity(intent);

                }

            }
        });


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
    }

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    public boolean isName(String name) {
        return Pattern.matches("[a-zA-Z]+", name);
    }

    public boolean isEmail(String email) {
        String emailRegex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern emailPattern = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailPattern.matcher(email);
        return matcher.find();
    }

    public boolean isPhoneNumber(String phone) {
        if (phone.length() == 10)
            return phone.charAt(0) == '0' && phone.charAt(1) == '7'
                    && (phone.charAt(2) == '7' || phone.charAt(2) == '8' || phone.charAt(2) == '9')
                    && phone.matches("[0-9]+");


        else {
            return false;
        }
    }


}
