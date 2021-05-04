package com.example.communityhubproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfile extends AppCompatActivity {
    FirebaseAuth fAuth;
    TextView name, email, phone;
    Button bt;
    DatabaseReference databaseReference;
    FirebaseUser user;
    public static final String mypreference = "mypref";
    public static final String userIds = "userIdKey";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        name = findViewById(R.id.textName);
        email = findViewById(R.id.textEmail);
        phone = findViewById(R.id.textPhone);
        bt = findViewById(R.id.buttonOut);
        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
   //     databaseReference = FirebaseDatabase.getInstance().getReference().child(fAuth.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String named = snapshot.child("name").getValue().toString();
                String emaild = snapshot.child("email").getValue().toString();
                String phoned = snapshot.child("phone").getValue().toString();

                name.setText(named);
                email.setText(emaild);
                phone.setText(phoned);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Data Error", Toast.LENGTH_LONG).show();

            }
        });

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fAuth.getInstance().signOut();
                SharedPreferences settings = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
                settings.edit().clear().commit();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.conMain, new secondFragment()).commit();

            }
        });
    }
}