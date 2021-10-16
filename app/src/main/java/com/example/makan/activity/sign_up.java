package com.example.makan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.makan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class sign_up extends AppCompatActivity {

    TextInputLayout textInputLayout;
    TextInputEditText textInputEditText;
    TextInputEditText email;
    TextInputEditText pass;
    TextInputEditText pass2;
    TextInputEditText fName;
    TextInputEditText lName;
    TextInputEditText phone;
    TextInputEditText birthDate;

    AutoCompleteTextView city;

    MaterialButton signUp;

    Toolbar toolbar;

    private FirebaseAuth mAuth;

    usersData usersData = new usersData();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        email = findViewById(R.id.email);

        fName = findViewById(R.id.f_name);

        lName = findViewById(R.id.l_name);

        phone = findViewById(R.id.phone_nmbr);

        pass = findViewById(R.id.password);

        city = findViewById(R.id.city);

        pass2 = findViewById(R.id.c_password);

        birthDate = findViewById(R.id.birth_day_edit);

        signUp = findViewById(R.id.sign_up);

        city.setEnabled(false);
        String[] item = {"Amman", "Irbid", "Zarqa", "Mafraq", "Ajloun", "Jerash", "Madaba", "Balqa", "Karak", "Tafileh", "Maan", "Aqaba"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.city_drop_menu, item);
        city.setAdapter(adapter);

        mAuth = FirebaseAuth.getInstance();


        signUp.setOnClickListener(v -> {
            boolean checker = true;
            final String emailAdress = email.getText().toString();
            final String password = pass.getText().toString();
            final String password2 = pass2.getText().toString();
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


            //Email Validation
            if (isEmail(emailAdress)) {

            } else if (email.getText().toString().equals("")) {
                checker = false;
                email.setError("Please Enter Email Address");
            } else {
                checker = false;
                email.setError("Invalid Email Address");
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

            //Password Validation
            if (!password.equals("")) {
                if (password.equals(password2)) {
                    if (password.contains(fname) || password.contains(lname)) {
                        Toast.makeText(sign_up.this, "Please do not use your first name or last name in password", Toast.LENGTH_SHORT).show();
                        pass.setError("Please do not use your first name or last name in password");
                        pass.findFocus();
                        checker = false;
                    }
                    if (password.equals(phoneNum)) {
                        Toast.makeText(sign_up.this, "Please do not use your phone number as a password", Toast.LENGTH_SHORT).show();
                        pass.setError("Please do not use your phone number as a password");
                        pass.findFocus();
                        checker = false;
                    }
                    if (password.length() < 6) {
                        Toast.makeText(sign_up.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                        pass.setError("Password must be at least 6 characters");
                        pass.findFocus();
                        checker = false;
                    }

                } else if (pass2.getText().toString().equals("")) {
                    checker = false;
                    pass2.setError("Please Enter Confirm Password");
                } else {
                    checker = false;
                    pass2.setError("Passwords Do Not Match");
                }

                if (checker == true) {
                    emailSignUp(emailAdress, password, fname, lname, phoneNum, city1, birth);
                }
            } else {
                checker = false;
                pass.setError("Please Enter Password");
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

    public void emailSignUp(String emailAdress, String password, final String fName, final String lName, final String phone, final String city, final String birthDate) {
        mAuth.createUserWithEmailAndPassword(emailAdress, password)
                .addOnCompleteListener(sign_up.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            usersData.addUserToDB_EMAIL(user.getUid(), user.getEmail(), fName, lName, phone, city, birthDate);

                            //Toast.makeText(sign_up.this, "createUserWithEmail:success", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(sign_up.this, choose_interest.class);
                            startActivity(intent);


                        } else {
                            // If sign in fails, display a message to the user.
                            try {
                                String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                                switch (errorCode) {
                                    case "ERROR_INVALID_CREDENTIAL":
                                        Toast.makeText(sign_up.this, "The supplied auth credential is malformed or has expired.", Toast.LENGTH_LONG).show();
                                        break;


                                    case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                                        Toast.makeText(sign_up.this, "An account already exists with the same email address but different sign-in credentials. Sign in using a provider associated with this email address.", Toast.LENGTH_LONG).show();
                                        break;

                                    case "ERROR_EMAIL_ALREADY_IN_USE":
                                        //Toast.makeText(sign_up.this, "The email address is already in use by another account.   ", Toast.LENGTH_LONG).show();
                                        email.setError("The email address is already in use by another account.");
                                        email.requestFocus();
                                        break;

                                    case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                                        Toast.makeText(sign_up.this, "This credential is already associated with a different user account.", Toast.LENGTH_LONG).show();
                                        break;

                                    default:
                                        Toast.makeText(sign_up.this, errorCode, Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                Toast.makeText(sign_up.this, "Something Wrong!! Wait a second and try again.", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });

    }

}
