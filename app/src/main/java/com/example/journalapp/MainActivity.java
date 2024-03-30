package com.example.journalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
   Button loginBtn,createAccountBtn;
   private EditText emailEt,passEt;
   //Firebase auth
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;
    private Animation btn_animation,text_animation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginBtn=findViewById(R.id.email_signin);
        createAccountBtn=findViewById(R.id.creat_account);
        emailEt=findViewById(R.id.email);
        passEt=findViewById(R.id.password);

        // Animation
        btn_animation= AnimationUtils.loadAnimation(this,
                R.anim.animate_btn);
        text_animation=AnimationUtils.loadAnimation(this,
                R.anim.animate_text);

        loginBtn.setAnimation(btn_animation);
        createAccountBtn.setAnimation(btn_animation);
        emailEt.setAnimation(text_animation);
        passEt.setAnimation(text_animation);






            createAccountBtn.setOnClickListener(v ->  {
            //onClick()
            Intent i=new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(i);
            });
            //Firebase Authentication
            firebaseAuth=FirebaseAuth.getInstance();
            loginBtn.setOnClickListener(v1 -> {
                loginEmailPassUser(
                        emailEt.getText().toString().trim(),
                        passEt.getText().toString().trim()
                );


                    });
    }
    private void loginEmailPassUser(String email,String pwd){
        //checking for empty text
        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pwd)) {
            firebaseAuth.signInWithEmailAndPassword(email,pwd)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            FirebaseUser user=firebaseAuth.getCurrentUser();
                            Intent i=new Intent(MainActivity.this, JournalListActivity.class);
                            startActivity(i);
                        }

                    });
        }

    }
}