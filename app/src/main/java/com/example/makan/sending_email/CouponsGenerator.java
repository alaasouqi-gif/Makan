package com.example.makan.sending_email;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CouponsGenerator {

    String title;
    String des;
    String key;
    String name;
    String city;
    String phone;
    String email;
    String stype;

    DatabaseReference offersReference;
    DatabaseReference databaseReference;

    public CouponsGenerator(String title, String des, String key, String stype) {
        offersReference = FirebaseDatabase.getInstance().getReference("services").child(key).child("offers").push();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        this.title = title;
        this.des = des;
        this.key = key;
        this.name = getServiceName();
        this.city = getServiceCity();
        this.phone = getServicePhone();
        this.email = getServiceEmail();
        this.stype = stype;

    }


    int index = 0;
    List<String> users = new ArrayList<>();


    public void generateCoupons(int num, final List<String> cities) {
        final List<Integer> integers = randomNumbers(num);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (cities.contains("Any") || cities.contains(dataSnapshot.child("City").getValue().toString())) {
                        users.add(dataSnapshot.getKey());
                    }
                }

                if (!users.isEmpty()) {

                    offersReference.child("title").setValue(title);
                    offersReference.child("description").setValue(des);

                    if (integers.size() >= users.size()) {
                        for (String s : users) {
                            addOffer(s);
                            sendEmail(snapshot.child(s).child("Email").getValue().toString(), snapshot.child(s).child("First Name").getValue().toString());
                        }
                    } else {
                        for (String s : users) {
                            if (integers.contains(index)) {
                                addOffer(s);
                                sendEmail(snapshot.child(s).child("Email").getValue().toString(), snapshot.child(s).child("First Name").getValue().toString());
                            }
                            index++;
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void addOffer(String key) {
        offersReference.child("users").push().setValue(key);
        DatabaseReference reference = databaseReference.child(key).child("notified offers").child(offersReference.getKey());
        reference.child("service").setValue(this.key);
        reference.child("read").setValue("false");
        reference.child("title").setValue(title);
        reference.child("description").setValue(des);
        reference.child("type").setValue("offers");
        reference.child("service type").setValue(stype);
        reference.child("open").setValue("false");

    }

    private List<Integer> randomNumbers(int num) {
        List<Integer> integers = new ArrayList<>();
        List<Integer> selected = new ArrayList<>();

        int number;
        Random random = new Random();
        for (int i = 0; i < num; i++) {
            for (int j = 0; ; j++) {
                number = random.nextInt(num);
                if (selected.isEmpty() || !selected.contains(number)) {
                    integers.add(number);
                    break;
                }
            }

        }
        return integers;
    }

    private void sendEmail(final String recipients, final String fName) {

        Thread sender = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GMailSender sender = new GMailSender("alaa3souqi@gmail.com", "xbuurfwjbbjiiwzg");
                    sender.sendMail("Makan App",
                            "Congratulations " + fName + ".." + "\n\n" + "You got " + title + " coupon. From " + name + ", " + city + "."
                                    + "\n" + des + "\n"
                                    + "\n" + "Please show us this email when you visit us." + "\n\n"
                                    + "For more information please contact us on:" + "\n\n"
                                    + phone + "\n\n"
                                    + email,
                            "obadou8114@gmail.com",
                            recipients);
                } catch (Exception e) {
                    Log.e("mylog", "Error: " + e.getMessage());
                }
            }
        });
        sender.start();
    }

    private String getServiceName() {
        FirebaseDatabase.getInstance().getReference("services").child(key).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name = snapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return name;
    }

    private String getServiceCity() {
        FirebaseDatabase.getInstance().getReference("services").child(key).child("city").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                city = snapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return city;
    }

    private String getServicePhone() {
        FirebaseDatabase.getInstance().getReference("services").child(key).child("phone").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                phone = snapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return phone;
    }

    private String getServiceEmail() {
        FirebaseDatabase.getInstance().getReference("services").child(key).child("email").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                email = snapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return email;
    }
}
