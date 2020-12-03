package com.example.introfirestoreandroid;

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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private EditText enterTitle, enterDescription;
    private Button saveBtn, showBtn, updateBtn, deleteBtn;
    private TextView showTitle, showDescription;

    //Keys
    public static final String KEY_TITLE = "Title";
    public static final String KEY_DESCRIPTION = "Description";


    //connection to fireStore
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    //Firestore document reference
    private final DocumentReference journalRef = db.collection("Journal").document("Description Book");
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
        updateBtn = findViewById(R.id.update_button);
        deleteBtn = findViewById(R.id.delete_button);

//        Bundle bundle = new Bundle();
//        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
//        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
//        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
//        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);


        saveBtn.setOnClickListener(this);

        showBtn.setOnClickListener(this);

        updateBtn.setOnClickListener(this);

        deleteBtn.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.update_button:
                updateTitleFunc();
                break;
            case R.id.show_button:
                showFunc();
                break;
            case R.id.save_button:
                saveFunc();
                break;
            case R.id.delete_button:
                deleteFunc();
                break;
            default:
                break;

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        journalRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }

                if(value != null && value.exists()){
                    showTitle.setText(value.getString(KEY_TITLE));
                    showDescription.setText(value.getString(KEY_DESCRIPTION));

                }else {
                    showTitle.setText("");
                    showDescription.setText("");

                }

            }
        });
    }

    private void saveFunc() {
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

    private void showFunc() {
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

    private void updateTitleFunc() {
        String title = enterTitle.getText().toString().trim();
        String description = enterDescription.getText().toString().trim();

        Map<String, Object> data = new HashMap<>();

        data.put(KEY_TITLE, title);
        data.put(KEY_DESCRIPTION, description);

        journalRef.update(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(MainActivity.this, "updated", Toast.LENGTH_SHORT).show();

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

    private void deleteFunc() {

        //journalRef.delete();

        Map<String, Object> data = new HashMap<>();

        data.put(KEY_TITLE, FieldValue.delete());
        data.put(KEY_DESCRIPTION, FieldValue.delete());

        journalRef.update(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Toast.makeText(MainActivity.this, "deleted", Toast.LENGTH_SHORT).show();

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

    /*@Override
    protected void onStop() {
        super.onStop();

    }*/

}