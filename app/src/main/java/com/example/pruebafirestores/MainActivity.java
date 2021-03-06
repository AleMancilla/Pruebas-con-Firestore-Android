package com.example.pruebafirestores;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final String KEY_TITLE="title";
    private static final String KEY_DESCRIPTION="description";

    private EditText editTextTitle;
    private EditText editTextDescription;
    private TextView textViewMostrar;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("Notebook");
    private DocumentReference noteRef = db.document("Notebook/My first note");
    private ListenerRegistration noteListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextTitle = (EditText) findViewById(R.id.edit_text_titulo);
        editTextDescription = (EditText) findViewById(R.id.edit_text_descripcion);
        textViewMostrar = (TextView) findViewById(R.id.textViewmostrar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        notebookRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                if (e != null)
                {
                    return;
                }

                String data ="";
                for (QueryDocumentSnapshot documentSnapshots : queryDocumentSnapshots)
                {
                    Note note = documentSnapshots.toObject(Note.class);
                    note.setdocumentID(documentSnapshots.getId());

                    String documentID = note.getdocumentID();
                    String title = note.getTitle();
                    String description = note.getDescription();

                    data += "ID "+documentID+"\nTitle = " + title + "\nDescripcion = "+ description + "\n\n";
                }
                textViewMostrar.setText(data);
            }
        });
/*
        noteRef.addSnapshotListener(this,new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if(e != null)
                {
                    Toast.makeText(MainActivity.this, "error on start", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, e.toString());
                    return;
                }
                if(documentSnapshot.exists())
                {
                    ///String title = documentSnapshot.getString(KEY_TITLE);
                    ///String description = documentSnapshot.getString(KEY_DESCRIPTION);

                    ///textViewMostrar.setText("titulo "+ title+"\nDescripcion "+description);
                    Note note = documentSnapshot.toObject(Note.class);
                    String title = note.getTitle();
                    String description = note.getDescription();

                    textViewMostrar.setText("titulo "+ title+"\nDescripcion "+description);
                }
                else
                {
                    textViewMostrar.setText("");
                }
            }
        });

        */
    }

    public void saveNotes(View v)
    {
        String title = editTextTitle.getText().toString();
        String descripcion = editTextDescription.getText().toString();

        //Map<String, Object> note = new HashMap<>();
        //note.put(KEY_TITLE,title);
        //note.put(KEY_DESCRIPTION,descripcion);

        Note note = new Note(title,descripcion);

        noteRef.set(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Datos salvados correctamente", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });
    }

    public void loadNote(View v)
    {
        noteRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        //verificamos si existe
                        if(documentSnapshot.exists())
                        {
                            ///String title = documentSnapshot.getString(KEY_TITLE);
                            ///String description = documentSnapshot.getString(KEY_DESCRIPTION);

                            //Map<String, Object> note = documentSnapshot.getData();

                            Note note = documentSnapshot.toObject(Note.class);
                            String title = note.getTitle();
                            String description = note.getDescription();

                            textViewMostrar.setText("titulo "+ title+"\nDescripcion "+description);
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "Documento No existe", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });
    }

    public void updateDescription(View v)
    {
        String descripcion = editTextDescription.getText().toString();
        Map<String, Object> note = new HashMap<>();

        note.put(KEY_DESCRIPTION,descripcion);
        noteRef.set(note, SetOptions.merge());
    }

    public void deleteNote(View v)
    {
        noteRef.delete();
    }

    public void deleteDescription(View v)
    {
        Map<String,Object> note = new HashMap<>();
        note.put(KEY_DESCRIPTION, FieldValue.delete());

        noteRef.update(note);
    }
    public void addNotes(View v)
    {
        String title = editTextTitle.getText().toString();
        String descripcion = editTextDescription.getText().toString();

        Note note = new Note(title,descripcion);
        notebookRef.add(note);
    }

    public void LeerTodo(View v)
    {
        notebookRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        String dato ="";
                        for (QueryDocumentSnapshot documentSnapshots : queryDocumentSnapshots)
                        {

                            Note note = documentSnapshots.toObject(Note.class);

                            note.setdocumentID(documentSnapshots.getId());
                            String documentID = note.getdocumentID();

                            String title = note.getTitle();
                            String description = note.getDescription();

                            dato += "ID "+documentID+"\nTitle = " + title + "\nDescripcion = "+ description + "\n\n";
                        }
                        textViewMostrar.setText(dato);
                    }
                });
    }
}
