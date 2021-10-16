package com.example.makan.activity.add_services;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;

import com.example.makan.R;
import com.example.makan.activity.services;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gun0912.tedbottompicker.TedBottomPicker;

public class add_pitch extends AppCompatActivity {

    Toolbar toolbar;
    Button Next;

    int StartHour;
    int StartMinute;
    String Start_amPm;

    int EndHour;
    int EndMinute;
    String End_amPm;
    TextInputEditText StartDate;
    TextInputEditText EndDate;
    TextInputLayout StartDateLayout;
    TextInputLayout EndDateLayout;
    TextInputLayout Location_l;

    TextInputEditText name;
    TextInputEditText phoneNum;
    TextInputEditText email;
    TextInputEditText description;

    TextInputEditText p_60;
    TextInputEditText p_120;

    List<String> days = new ArrayList<>();
    String daysString = "";


    AppCompatImageView coverPhoto;


    Uri photoUri;

    DatabaseReference serRef = FirebaseDatabase.getInstance().getReference("services").push();


    RadioButton radioButton;
    RadioButton radioButton2;

    MultiAutoCompleteTextView off_day;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pitch);

        toolbar = findViewById(R.id.toolbar);
        Next = findViewById(R.id.Next);
        Next.setEnabled(false);

        coverPhoto = findViewById(R.id.imageView);

        name = findViewById(R.id.name);

        phoneNum = findViewById(R.id.phone_number);

        p_60 = findViewById(R.id.price60);
        p_120 = findViewById(R.id.price120);


        email = findViewById(R.id.email);

        description = findViewById(R.id.description);


        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }
        });

        StartDate = findViewById(R.id.start_date);
        StartDateLayout = findViewById(R.id.start_date_layout);

        EndDate = findViewById(R.id.end_date);
        EndDateLayout = findViewById(R.id.end_date_layout);

        radioButton = findViewById(R.id.radioButton);
        radioButton2 = findViewById(R.id.radioButton2);


        StartDateLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetStartDate();
            }
        });

        EndDateLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetEndDate();
            }
        });


        //findViewById(R.id.off_day_l).setEnabled(false);

        AutoCompleteTextView po;
        off_day = findViewById(R.id.off_day);

        String[] item2 = {"No off days", "Saturday", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, R.layout.city_drop_menu, item2);
        off_day.setAdapter(adapter2);
        off_day.setTokenizer(new MultiAutoCompleteTextView.Tokenizer() {
            @Override
            public int findTokenStart(CharSequence charSequence, int i) {
                return 0;
            }

            @Override
            public int findTokenEnd(CharSequence charSequence, int i) {
                return 0;
            }

            @Override
            public CharSequence terminateToken(CharSequence charSequence) {
                if (days.contains(charSequence.toString())) {
                    days.remove(charSequence.toString());
                    daysString = daysString.replace(charSequence.toString() + ", ", "");
                    return daysString;
                } else {
                    days.add(charSequence.toString());
                    daysString += charSequence.toString() + ", ";
                    return daysString;
                }
            }
        });

        StartDate.setEnabled(false);
        EndDate.setEnabled(false);
        off_day.setEnabled(false);


        coverPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TedBottomPicker.OnImageSelectedListener listener = new TedBottomPicker.OnImageSelectedListener() {
                    @Override
                    public void onImageSelected(Uri uri) {
                        photoUri = uri;
                        coverPhoto.setImageURI(uri);
                        if (coverPhoto == null
                                || name.getText().toString().equals("")
                                || StartDate.getText().toString().equals("")
                                || EndDate.getText().toString().equals("")
                                || phoneNum.getText().toString().equals("")
                                || email.getText().toString().equals("")
                                || description.getText().toString().equals("")
                                || off_day.getText().toString().equals("")) {
                            Next.setEnabled(false);
                        } else {
                            Next.setEnabled(true);
                        }
                    }
                };

                TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(add_pitch.this)
                        .setOnImageSelectedListener(listener)
                        .create();
                tedBottomPicker.show(getSupportFragmentManager());

            }
        });

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (coverPhoto == null
                        || name.getText().toString().equals("")
                        || StartDate.getText().toString().equals("")
                        || EndDate.getText().toString().equals("")
                        || phoneNum.getText().toString().equals("")
                        || email.getText().toString().equals("")
                        || description.getText().toString().equals("")
                        || off_day.getText().toString().equals("")) {
                    Next.setEnabled(false);
                } else {
                    Next.setEnabled(true);
                }
            }
        });

        description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (coverPhoto == null
                        || name.getText().toString().equals("")
                        || StartDate.getText().toString().equals("")
                        || EndDate.getText().toString().equals("")
                        || phoneNum.getText().toString().equals("")
                        || email.getText().toString().equals("")
                        || description.getText().toString().equals("")
                        || off_day.getText().toString().equals("")) {
                    Next.setEnabled(false);
                } else {
                    Next.setEnabled(true);
                }
            }
        });

        off_day.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (coverPhoto == null
                        || name.getText().toString().equals("")
                        || StartDate.getText().toString().equals("")
                        || EndDate.getText().toString().equals("")
                        || phoneNum.getText().toString().equals("")
                        || email.getText().toString().equals("")
                        || description.getText().toString().equals("")
                        || off_day.getText().toString().equals("")) {
                    Next.setEnabled(false);
                } else {
                    Next.setEnabled(true);
                }

                if (name.getText().toString().equals("")) {
                    name.requestFocus();
                } else if (phoneNum.getText().toString().equals("")) {
                    phoneNum.requestFocus();
                } else if (email.getText().toString().equals("")) {
                    email.requestFocus();
                } else {
                    description.requestFocus();
                }
            }
        });

        StartDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (coverPhoto == null
                        || name.getText().toString().equals("")
                        || StartDate.getText().toString().equals("")
                        || EndDate.getText().toString().equals("")
                        || phoneNum.getText().toString().equals("")
                        || email.getText().toString().equals("")
                        || description.getText().toString().equals("")
                        || off_day.getText().toString().equals("")) {
                    Next.setEnabled(false);
                } else {
                    Next.setEnabled(true);
                }
            }
        });

        EndDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (coverPhoto == null
                        || name.getText().toString().equals("")
                        || StartDate.getText().toString().equals("")
                        || EndDate.getText().toString().equals("")
                        || phoneNum.getText().toString().equals("")
                        || email.getText().toString().equals("")
                        || description.getText().toString().equals("")
                        || off_day.getText().toString().equals("")) {
                    Next.setEnabled(false);
                } else {
                    Next.setEnabled(true);
                }
            }
        });

        phoneNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (coverPhoto == null
                        || name.getText().toString().equals("")
                        || StartDate.getText().toString().equals("")
                        || EndDate.getText().toString().equals("")
                        || phoneNum.getText().toString().equals("")
                        || email.getText().toString().equals("")
                        || description.getText().toString().equals("")
                        || off_day.getText().toString().equals("")) {
                    Next.setEnabled(false);
                } else {
                    Next.setEnabled(true);
                }
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (coverPhoto == null
                        || name.getText().toString().equals("")
                        || StartDate.getText().toString().equals("")
                        || EndDate.getText().toString().equals("")
                        || phoneNum.getText().toString().equals("")
                        || email.getText().toString().equals("")
                        || description.getText().toString().equals("")
                        || off_day.getText().toString().equals("")) {
                    Next.setEnabled(false);
                } else {
                    Next.setEnabled(true);
                }
            }
        });


        findViewById(R.id.Next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isEmail(email.getText().toString())
                        && !isPhoneNumber(phoneNum.getText().toString())
                        && description.getText().toString().length() < 50) {
                    email.setError("Enter valid email address");
                    phoneNum.setError("Enter valid phone number");
                    description.setError("must be more than 50 character");
                    return;
                } else if (!isEmail(email.getText().toString())) {
                    email.setError("Enter valid email address");
                    return;
                } else if (!isPhoneNumber(phoneNum.getText().toString())) {
                    phoneNum.setError("Enter valid phone number");
                    return;
                } else if (description.getText().toString().length() < 50) {
                    description.setError("must be more than 50 character");
                    return;
                }

                Intent intent = new Intent(add_pitch.this, SelectLocation.class);

                try {
                    intent.putExtra("owner", FirebaseAuth.getInstance().getCurrentUser().getUid());
                } catch (NullPointerException e) {
                    Toast.makeText(add_pitch.this, "No current user", Toast.LENGTH_SHORT).show();
                }

                String offDays;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    offDays = off_day.getText().toString();
                    String[] strings = offDays.split(", ");
                    intent.putExtra("off", strings);
                }

                intent.putExtra("name", name.getText().toString());

                intent.putExtra("open", StartDate.getText().toString());
                intent.putExtra("close", EndDate.getText().toString());

                intent.putExtra("60", p_60.getText().toString());
                intent.putExtra("120", p_120.getText().toString());

                intent.putExtra("phone", phoneNum.getText().toString());
                intent.putExtra("email", email.getText().toString());
                intent.putExtra("description", description.getText().toString());
                intent.putExtra("image", photoUri.toString());

                intent.putExtra("type", "pitch");
                intent.putExtra("key", serRef.getKey());
                startActivity(intent);

            }
        });

    }

    void SetStartDate() {

        Calendar calendar;

        calendar = Calendar.getInstance();
        StartHour = calendar.get(Calendar.HOUR_OF_DAY);
        StartMinute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog;

        timePickerDialog = new TimePickerDialog(add_pitch.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                String hour;
                String minute;
                if (hourOfDay < 10) {
                    hour = "0" + hourOfDay;
                } else {
                    hour = hourOfDay + "";
                }
                if (minutes < 10) {
                    minute = "0" + minutes;
                } else {
                    minute = minutes + "";
                }


                StartDate.setText("");
                StartDate.setText(StartDate.getText().toString() + hour + ":" + minute);

                if (name.getText().toString().equals("")) {
                    name.requestFocus();
                } else if (phoneNum.getText().toString().equals("")) {
                    phoneNum.requestFocus();
                } else if (email.getText().toString().equals("")) {
                    email.requestFocus();
                } else {
                    description.requestFocus();
                }
            }
        }, StartHour, StartMinute, true);

        timePickerDialog.show();


    }


    void SetEndDate() {
        Calendar calendar;

        calendar = Calendar.getInstance();
        EndHour = calendar.get(Calendar.HOUR_OF_DAY);
        EndMinute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog;

        timePickerDialog = new TimePickerDialog(add_pitch.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                String hour;
                String minute;
                if (hourOfDay < 10) {
                    hour = "0" + hourOfDay;
                } else {
                    hour = hourOfDay + "";
                }
                if (minutes < 10) {
                    minute = "0" + minutes;
                } else {
                    minute = minutes + "";
                }

                EndDate.setText("");
                EndDate.setText(EndDate.getText().toString() + hour + ":" + minute);

                if (name.getText().toString().equals("")) {
                    name.requestFocus();
                } else if (phoneNum.getText().toString().equals("")) {
                    phoneNum.requestFocus();
                } else if (email.getText().toString().equals("")) {
                    email.requestFocus();
                } else {
                    description.requestFocus();
                }
            }
        }, EndHour, EndMinute, true);

        timePickerDialog.show();
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

    public boolean isEmail(String email) {
        String emailRegex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern emailPattern = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailPattern.matcher(email);
        return matcher.find();
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(add_pitch.this, R.style.Theme_MaterialComponents_DayNight_Dialog_Alert);
        builder.setTitle("Are you sure you want to cancel adding place?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(add_pitch.this, services.class);
                startActivity(intent);
            }
        })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

    }
}
