package com.example.firestoretutorials;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MyDebug";
    private EditText enterTitle;
    private EditText enterThought;
    Button saveBtn;

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
        saveBtn = findViewById(R.id.saveBtn);
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
    }
}
