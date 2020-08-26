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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MyDebug";
    private EditText enterTitle,enterThought;
    private TextView titleTextView,thoughtTextView;
    Button saveBtn,showBtn,updateBtn;

    public static final String KEY_TITLE = "title";
    public static final String KEY_THOUGHTS = "thought";


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference jouranlRef = db.collection("Journal")
            .document("First Thoughts");
     // private DocumentReference jouranlRef = db.document("Journal/First Thoughts");


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
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTitle();
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = enterTitle.getText().toString().trim();
                String thought = enterThought.getText().toString().trim();

                Map<String,Object> data = new HashMap<>();
                data.put(KEY_TITLE,title);
                data.put(KEY_THOUGHTS,thought);

                jouranlRef.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText( MainActivity.this,"Sucess Saving Data",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG,"onFailure - "+ e.toString());
                    }
                });
            }
        });

        showBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jouranlRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            String title = documentSnapshot.getString(KEY_TITLE);
                            String thought = documentSnapshot.getString(KEY_THOUGHTS);
                            titleTextView.setText(title);
                            thoughtTextView.setText(thought);
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
                    String title = documentSnapshot.getString(KEY_TITLE);
                    String thought = documentSnapshot.getString(KEY_THOUGHTS);
                    titleTextView.setText(title);
                    thoughtTextView.setText(thought);
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
