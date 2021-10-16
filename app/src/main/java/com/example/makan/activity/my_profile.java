package com.example.makan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class my_profile extends AppCompatActivity {

    TextInputLayout textInputLayout;
    TextInputEditText textInputEditText;
    TextInputEditText email;
    TextInputEditText fName;
    TextInputEditText lName;
    TextInputEditText phone;
    TextInputEditText birthDate;

    AutoCompleteTextView city;

    MaterialButton signUp;

    Toolbar toolbar;

    private FirebaseAuth mAuth;

    TextView change;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference().child("Users").child(user.getUid());

    String[] item = {"Amman", "Irbid", "Zarqa", "Mafraq", "Ajloun", "Jerash", "Madaba", "Balqa", "Karak", "Tafileh", "Maan", "Aqaba"};
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        email = findViewById(R.id.email);

        fName = findViewById(R.id.f_name);

        lName = findViewById(R.id.l_name);

        phone = findViewById(R.id.phone_nmbr);

        change = findViewById(R.id.change);

        city = findViewById(R.id.city);


        birthDate = findViewById(R.id.birth_day_edit);

        signUp = findViewById(R.id.sign_up);

        city.setEnabled(false);


        mAuth = FirebaseAuth.getInstance();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fName.setText(dataSnapshot.child("First Name").getValue().toString());
                lName.setText(dataSnapshot.child("Last Name").getValue().toString());
                phone.setText(dataSnapshot.child("Phone Number").getValue().toString());
                city.setText(dataSnapshot.child("City").getValue().toString());
                ArrayAdapter<String> adapter = new ArrayAdapter<>(my_profile.this, R.layout.city_drop_menu, item);
                city.setAdapter(adapter);

                birthDate.setText(dataSnapshot.child("Birth Date").getValue().toString());
                email.setText(user.getEmail());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checker = true;
                final String fname = fName.getText().toString();
                final String lname = lName.getText().toString();
                final String phoneNum = phone.getText().toString();
                final String city1 = city.getText().toString();
                final String birth = birthDate.getText().toString();


                //First Name & Last Name Validation
                if (isName(fname)) {

                } else if (fName.getText().toString().equals("")) {
                    checker = false;
                    fName.setError("Please Enter First Name");

                } else {
                    checker = false;
                    fName.setError("The name must contain letters only");
                }
                if (isName(lname)) {

                } else if (lName.getText().toString().equals("")) {
                    checker = false;
                    lName.setError("Please Enter Last Name");

                } else {
                    checker = false;
                    lName.setError("The name must contain letters only");
                }


                //Phone Number Validation
                if (isPhoneNumber(phoneNum)) {

                } else if (phone.getText().toString().equals("")) {
                    checker = false;
                    phone.setError("Please Enter Phone Number");
                } else {
                    checker = false;
                    phone.setError("Invalid Phone Number");
                }

                if (checker) {
                    // emailSignUp(emailAdress,password,fname,lname,phoneNum,city1,birth);
                    ref.child("First Name").setValue(fname);
                    ref.child("Last Name").setValue(lname);
                    ref.child("Phone Number").setValue(phoneNum);
                    ref.child("City").setValue(city1);
                    ref.child("Birth Date").setValue(birth);
                    finish();

                }
            }
        });


        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(my_profile.this, change_password.class);
                startActivity(intent);
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
