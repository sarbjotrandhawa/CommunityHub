package com.example.communityhubproject;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;
import static com.example.communityhubproject.MainActivity.updateTabPosition;
import static com.google.firebase.storage.FirebaseStorage.*;


public class thirdFragment extends Fragment {


    Button btnbrowse, btnupload;
    EditText txttitle,txtdescription,txtprice ;
    ImageView imgview;
    Uri FilePathUri;

    @NonNull
    @Override
    public LayoutInflater onGetLayoutInflater(@Nullable Bundle savedInstanceState) {
        return super.onGetLayoutInflater(savedInstanceState);
    }

    StorageReference storageReference;
    DatabaseReference databaseReference;
    int Image_Request_Code = 7;
    ProgressDialog progressDialog ;
    SharedPreferences sharedpreferences;
    public static String check="true";
    public static final String Name = "nameKey";
    public static final String Email = "emailKey";
    public static final String userIds = "userIdKey";
    public static final String mypreference = "mypref";
    FirebaseAuth fAuth;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        sharedpreferences = getActivity().getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        String userId = sharedpreferences.getString(userIds, "");

        if(userId == null || userId.equals(""))
        {
            return inflater.inflate(R.layout.fragment_login_advice, container, false);
        }
        else {
        check="false";
            return inflater.inflate(R.layout.fragment_third, container, false);
        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(check.equals("false")) {
            setView(view);
        }
        else{
            viewNotLogined(view);
        }

    }

    void viewNotLogined(View view){
        Button bt= view.findViewById(R.id.login_Btn);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTabPosition();
            }
        });
    }

    void setView(View view){
        try {
            storageReference = getInstance().getReference();
            databaseReference = FirebaseDatabase.getInstance().getReference("Products");
            btnbrowse = view.findViewById(R.id.btnbrowse);
            btnupload = view.findViewById(R.id.btnupload);
            txttitle = view.findViewById(R.id.txttitle);
            txtdescription = view.findViewById(R.id.txtdescription);
            txtprice = view.findViewById(R.id.txtPrice);
            imgview = view.findViewById(R.id.image_view);
            progressDialog = new ProgressDialog(getActivity());// context name as per your project name

            btnbrowse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Image"), Image_Request_Code);

                }
            });
            btnupload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UploadImage();
                }
            });
        }
        catch (Exception e){

        }

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {
            FilePathUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), FilePathUri);
                imgview.setImageBitmap(bitmap);
            }
            catch (IOException e) {

                e.printStackTrace();
            }
        }
    }


    public String GetFileExtension(Uri uri) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;
    }


    public void UploadImage() {

        if (FilePathUri != null) {

            progressDialog.setTitle("Product info is uploading...");
            progressDialog.show();
            StorageReference sRef = storageReference.child("Products" + System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));
            sRef.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                           @Override
                                                                           public void onSuccess(Uri uri) {
                                                                              String  downloadUrl = uri.toString();
                                                                               sharedpreferences = getActivity().getSharedPreferences(mypreference,
                                                                                       Context.MODE_PRIVATE);

                                                                               String userId=null;
                                                                               if (sharedpreferences.contains(userId)) {
                                                                                   userId = sharedpreferences.getString(userIds, "");
                                                                               }
                                                                               String title = txttitle.getText().toString().trim();
                                                                               String description = txtdescription.getText().toString().trim();
                                                                               String price = txtprice.getText().toString().trim();

                                                                               progressDialog.dismiss();
                                                                               Toast.makeText(getActivity(), "Advertisement posted Successfully ", Toast.LENGTH_LONG).show();

                                                                               Product product = new Product(title,description,price, downloadUrl.toString(), userId);
                                                                               String productId = databaseReference.push().getKey();
                                                                               databaseReference.child(productId).setValue(product);
                                                                           }
                                                                       });
                            Toast.makeText(getActivity(), "Product added Sucessfully!!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getActivity(),MainActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("exe ", ""+e);
                }
            });
        }
        else {

            Toast.makeText(getActivity(), "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();

        }
    }

}