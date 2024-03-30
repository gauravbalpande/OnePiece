package com.example.journalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpActivity extends AppCompatActivity {
    //widgets
    EditText password_create, username_create, email_create;
    Button createBtn;
    // FireBase Auth
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;
    // FireBase Connections
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        password_create = findViewById(R.id.password_create);
        username_create = findViewById(R.id.username_create_ET);
        email_create = findViewById(R.id.email_create);
        createBtn = findViewById(R.id.acc_signUp_btn);

        // Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();
        //Listening for changes in the authentication
        //state and responds accordingly when the state changes
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();

                // check the users logged in or not
                if (currentUser != null) {
                    // user already logged in

                } else {
                    // the user signed out


                }
            }
        };

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(email_create.getText().toString())
                && !TextUtils.isEmpty(username_create.getText().toString())
                        && !TextUtils.isEmpty(password_create.getText().toString())
                ){
                    String email=email_create.getText().toString().trim();
                    String pass=password_create.getText().toString().trim();
                    String username=username_create.getText().toString().trim();
                    CreateUserEmailAccount(email,pass,username);
                }
                else{
                    Toast.makeText(SignUpActivity.this, "No Fields are allowed to be Empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void CreateUserEmailAccount(String email, String pass, String username) {
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(username)) {
            firebaseAuth.createUserWithEmailAndPassword(
                    email, pass
            ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                       if(task.isSuccessful()){
                           Toast.makeText(SignUpActivity.this, "Account created Successfully", Toast.LENGTH_SHORT).show();
                       }
                }
            });
        }
    }
}
