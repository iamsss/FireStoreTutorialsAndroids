package com.example.firestoretutorials;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MyDebug";
    private EditText enterTitle,enterThought;
    private TextView titleTextView,thoughtTextView;
    Button saveBtn,showBtn,updateBtn,deleteThoughtBtn,deleteAllBtn;

    public static final String KEY_TITLE = "title";
    public static final String KEY_THOUGHTS = "thought";


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference jouranlRef = db.collection("Journal")
            .document("First Thoughts");
     // private DocumentReference jouranlRef = db.document("Journal/First Thoughts");

    private CollectionReference collectionReference = db.collection("Journal");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        enterTitle = findViewById(R.id.editText);
        enterThought = findViewById(R.id.editThought);
        titleTextView = findViewById(R.id.titleTextView);
        thoughtTextView = findViewById(R.id.thoughtTextView);
        showBtn = findViewById(R.id.showBtn);
        saveBtn = findViewById(R.id.saveBtn);
        updateBtn = findViewById(R.id.updateBtn);
        deleteThoughtBtn = findViewById(R.id.deleteThoughtBtn);
        deleteAllBtn = findViewById(R.id.deleteAllBtn);

        deleteAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAll();
            }
        });
        deleteThoughtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteThought();
            }
        });
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTitle();
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String title = enterTitle.getText().toString().trim();
//                String thought = enterThought.getText().toString().trim();
//
////                Map<String,Object> data = new HashMap<>();
////                data.put(KEY_TITLE,title);
////                data.put(KEY_THOUGHTS,thought);
//
//                Journal journal = new Journal(title,thought);
//                jouranlRef.set(journal).addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Toast.makeText( MainActivity.this,"Sucess Saving Data",Toast.LENGTH_SHORT).show();
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d(TAG,"onFailure - "+ e.toString());
//                    }
//                });
                addMutiDocument();
            }
        });

        showBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jouranlRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
//                            String title = documentSnapshot.getString(KEY_TITLE);
//                            String thought = documentSnapshot.getString(KEY_THOUGHTS);
                            Journal journal = documentSnapshot.toObject(Journal.class);
                            titleTextView.setText(journal.getTitle());
                            thoughtTextView.setText(journal.getThought());
                        }else{
                            Toast.makeText( MainActivity.this,"No Data Found",Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG,"onFailure - "+ e.toString());
                    }
                });
            }
        });
    }

    private  void getThoughts(){
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot querySnapshot: queryDocumentSnapshots){
                    Journal journal = querySnapshot.toObject(Journal.class);

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }
    private void deleteAll() {
        jouranlRef.delete();
    }

    private void addMutiDocument(){
        String title = enterTitle.getText().toString().trim();
        String thought = enterThought.getText().toString().trim();
        Journal journal = new Journal(title,thought);
        collectionReference.add(journal);
    }

    private void deleteThought() {
//        Map<String,Object> data = new HashMap<>();
//        data.put(KEY_THOUGHTS, FieldValue.delete());
//        jouranlRef.update(data);
        jouranlRef.update(KEY_THOUGHTS,FieldValue.delete());
    }

    @Override
    protected void onStart() {
        super.onStart();
        jouranlRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Toast.makeText( MainActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(documentSnapshot != null && documentSnapshot.exists()){
                    Journal journal = documentSnapshot.toObject(Journal.class);
                    titleTextView.setText(journal.getTitle());
                    thoughtTextView.setText(journal.getThought());
                }else{
                    titleTextView.setText("");
                    thoughtTextView.setText("");
                }
            }
        });

    }
    private void updateTitle() {
        String title = enterTitle.getText().toString().trim();
        Map<String,Object> data = new HashMap<>();
        data.put(KEY_TITLE,title);
        jouranlRef.update(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText( MainActivity.this,"Title updated",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText( MainActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
