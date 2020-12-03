package com.example.introfirestoreandroid;

import androidx.annotation.NonNull;
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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private EditText enterTitle, enterDescription;
    private Button saveBtn, showBtn;
    private TextView showTitle, showDescription;

    //Keys
    public static final String KEY_TITLE = "Title";
    public static final String KEY_DESCRIPTION = "Description";


    //connection to fireStore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    //Firestore document reference
    private DocumentReference journalRef = db.collection("Journal").document("Description Book");
   // private DocumentReference journalRef = db.document("Journal/Description Book");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enterTitle = findViewById(R.id.edit_text_title);
        enterDescription = findViewById(R.id.edit_description_title);

        showTitle = findViewById(R.id.show_title);
        showDescription = findViewById(R.id.show_description);

        saveBtn = findViewById(R.id.save_button);
        showBtn = findViewById(R.id.show_button);

//        Bundle bundle = new Bundle();
//        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
//        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
//        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
//        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = enterTitle.getText().toString().trim();
                String description = enterDescription.getText().toString().trim();

                Map<String, Object> data = new HashMap<>();

                data.put(KEY_TITLE, title);
                data.put(KEY_DESCRIPTION, description);

                journalRef.set(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();

                                //Log.d("success", "onSuccess: ");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //Log.d("failures", "onFailure: " + e.toString());
                            }
                        });
            }
        });

        showBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                journalRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if(documentSnapshot.exists()){
                            String title = documentSnapshot.getString(KEY_TITLE);
                            String description = documentSnapshot.getString(KEY_DESCRIPTION);

                            showTitle.setText(title);
                            showDescription.setText(description);

                        }else {
                            Toast.makeText(MainActivity.this, "No Data Exits", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: "+ e.toString());
                    }
                });
            }
        });

    }
}