package com.example.journalapp;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Firebase;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;

public class AddJournalActivity extends AppCompatActivity {
   // Widgets
    private Button saveButton;
    private ImageView addPhotoBtn;
    private ProgressBar progressBar;
    private EditText titleEditText;
    private EditText thoughtsEt;
    private ImageView imageView;


    //Firebase Firestore
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private CollectionReference collectionReference=db.collection("Journal");
    //Firebase Storage
    private StorageReference storageReference;
    //Firebase Auth
    private String currentUserId;
    private String currentUserName;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    // Using Activity Result Launcher
    ActivityResultLauncher<String> mTakePhoto;
    Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_journal);
        progressBar=findViewById(R.id.post_progressBar);
        titleEditText=findViewById(R.id.post_title_et);
        thoughtsEt=findViewById(R.id.post_description_et);
        imageView=findViewById(R.id.post_imageView);
        saveButton=findViewById(R.id.post_save_journal_button);
        addPhotoBtn=findViewById(R.id.postCameraButton);

        progressBar.setVisibility(View.INVISIBLE);
        //FireBase storage Reference
        storageReference = FirebaseStorage.getInstance().getReference();
        // firebase Auth
        firebaseAuth =FirebaseAuth.getInstance();
        // Getting The Current User
        if(user != null){
            currentUserId=user.getUid();
            currentUserName=user.getDisplayName();

        }
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveJournal();
            }


        });

        mTakePhoto=registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        //Showing the image
                        imageView.setImageURI(result);
                        //get the image uri
                        imageUri=result;
                    }
                }
        );


        addPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Getting Image From Gallery
                mTakePhoto.launch("image/*");
            }
        });


    }
    private void saveJournal() {

        String title=titleEditText.getText().toString().trim();
        String thoughts=thoughtsEt.getText().toString().trim();

        progressBar.setVisibility(View.VISIBLE);
        if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(thoughts) && imageUri!=null){

            // the saving of the images in firebase storage
            //................../journal_images/my_images_202310071430.png
            final StorageReference filepath=storageReference.child("journal_images")
                    .child("my_image"+ Timestamp.now().getSeconds());
            //uploading the image
            filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageUri=uri.toString();
                            // creating a journal object
                            Journals journals=new Journals();
                            journals.setTitle(title);
                            journals.setThoughts(thoughts);
                            journals.setImageUrl(imageUri);
                            journals.setTimeAdded(new Timestamp(new Date()));
                            journals.setUserName(currentUserName);
                            journals.setUserId(currentUserId);

                            collectionReference.add(journals).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Intent i=new Intent(AddJournalActivity.this,JournalListActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AddJournalActivity.this, "OOops! Its Failed"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(AddJournalActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            });



        }else{
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        user=firebaseAuth.getCurrentUser();
    }
}