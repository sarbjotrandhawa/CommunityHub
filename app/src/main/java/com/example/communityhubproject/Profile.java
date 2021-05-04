package com.example.communityhubproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Profile extends Fragment {

    FirebaseAuth fAuth;
    FirebaseUser user;
    TextView name, email, phone;
    Button bt;
    DatabaseReference databaseReference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        name = view.findViewById(R.id.textName);
        email = view.findViewById(R.id.textEmail);
        phone = view.findViewById(R.id.textPhone);
        bt = view.findViewById(R.id.buttonOut);
        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child(user.getUid());
        
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

                   }
               });
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
}