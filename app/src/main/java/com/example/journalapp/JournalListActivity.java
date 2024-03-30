package com.example.journalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.google.protobuf.DescriptorProtos;

import java.util.ArrayList;
import java.util.List;

public class JournalListActivity extends AppCompatActivity {

    //FireBaseAuth
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private FloatingActionButton fb;

    //FireBase Firestore
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private CollectionReference collectionReference=db.collection("Journal");

    //Firebase Storage
    private StorageReference storageReference;

    //list of journals
    private List<Journals> journalsList;

    //recyclerview and adapter
    private RecyclerView recyclerView;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_list);


        //Firebase Auth
        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();

        //widgets
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //posts Arraylist
        journalsList=new ArrayList<>();
        fb=findViewById(R.id.fab);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(JournalListActivity.this,AddJournalActivity.class);
                startActivity(i);
            }
        });
    }
    // Adding a Menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int itemId=item.getItemId();
        if(itemId==R.id.action_add) {

            if (user != null && firebaseAuth != null) {
                Intent i = new Intent(JournalListActivity.this, AddJournalActivity.class);
                startActivity(i);
            }
        }else if(itemId==R.id.action_signout)
        {
               if (user != null && firebaseAuth != null) {
                   firebaseAuth.signOut();
                   Intent i = new Intent(JournalListActivity.this, MainActivity.class);

               }

       }
           return super.onOptionsItemSelected(item);
          }

    @Override
    protected void onStart() {

        super.onStart();
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                //querySnapShot is an object that represents a single document received from a FireStore query
                //QueryDocumentsSnapshot--> document
                for (QueryDocumentSnapshot journal:queryDocumentSnapshots){
                    Journals journals=journal.toObject(Journals.class);
                    journalsList.add(journals);

                }

                // RecyclerView
                adapter=new MyAdapter(JournalListActivity.this,journalsList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(JournalListActivity.this, "Oops! Something went Wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
}