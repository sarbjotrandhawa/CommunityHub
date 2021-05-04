package com.example.communityhubproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class secondFragment extends Fragment {

    EditText mEmail,mPassword;
    Button mLoginBtn, profile;
    TextView mCreateBtn,forgotTextLink;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    SharedPreferences sharedPreferences;
    public static final String mypreference = "mypref";
    public static final String Name = "nameKey";
    public static final String Email = "emailKey";
    public static final String userId = "userIdKey";
    public static String check="true";

    public static final String userIds = "userIdKey";

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        mEmail = view.findViewById(R.id.Email);
        mPassword = view.findViewById(R.id.password);
        progressBar = view.findViewById(R.id.progressBar);
        fAuth = FirebaseAuth.getInstance();
        mLoginBtn = view.findViewById(R.id.loginBtn);
        mCreateBtn = view.findViewById(R.id.createText);
        profile = view.findViewById(R.id.profilebtn);

        //forgotTextLink = view.findViewById(R.id.forgotPassword);

        String userId = sharedPreferences.getString(userIds, "");

        if(userId == null || userId.equals(""))
        {
            profile.setVisibility(View.GONE);
        }
        else{
            mEmail.setVisibility(View.GONE);
            mPassword.setVisibility(View.GONE);
            mLoginBtn.setVisibility(View.GONE);
            mCreateBtn.setVisibility(View.GONE);
            view.findViewById(R.id.textView2).setVisibility(View.GONE);
        }

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required.");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password is Required.");
                    return;
                }

                if(password.length() < 6){
                    mPassword.setError("Password Must be >= 6 Characters");
                    return;
                }

               // progressBar.setVisibility(View.VISIBLE);


                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("userIdKey", task.getResult().getUser().getUid());
                            editor.commit();
                      //      Toast.makeText(getActivity(), "Logged in Successfully", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getActivity(),MainActivity.class);
                            Toast.makeText(getActivity(), userId+ " Logged in Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                            getActivity().finish();
                        }else {
                            Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                      //      progressBar.setVisibility(View.GONE);
                        }
                    }
               });
            }
        });

        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity() ,Register.class));
              //  startActivity(new Intent(getActivity() ,UserProfile.class));
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity() ,UserProfile.class));
                //  startActivity(new Intent(getActivity() ,UserProfile.class));
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_second2, container, false);
    }
}