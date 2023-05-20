package com.example.task81;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.task81.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;
    FirebaseAuth mAuth;

    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("users");
    Uri imageUri;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Creating binding for using items from view
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();


        binding.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, password, confirmPassword, fullName, phoneNumber;

                //get input email and password
                email = binding.emailEditText.getText().toString().trim();
                password = binding.passwordEditText.getText().toString();
                confirmPassword = binding.confirmPasswordEditText.getText().toString();
                fullName = binding.fullNameEditText.getText().toString().trim();

                //check all details have been input
                if(TextUtils.isEmpty(email)) {
                    Toast.makeText(SignUpActivity.this, "Please Enter an Email Address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)) {
                    Toast.makeText(SignUpActivity.this, "Please Enter a Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!password.equals(confirmPassword)) {
                    Toast.makeText(SignUpActivity.this, "Check Passwords Match", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(fullName)) {
                    Toast.makeText(SignUpActivity.this, "Please Enter Your Full Name", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //if creation of user is successful
                                if (task.isSuccessful()) {
                                    Toast.makeText(SignUpActivity.this, "Account Created.",
                                            Toast.LENGTH_SHORT).show();

                                    //add image to storage database
                                    //this will be stored as a folder named the user id
                                    //within this folder is profileImage.(extension)
                                    String id = mAuth.getCurrentUser().getUid();
                                    UserModel userModel = new UserModel(fullName);
                                    myRef.child(id).setValue(userModel);


                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });

        binding.returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

}