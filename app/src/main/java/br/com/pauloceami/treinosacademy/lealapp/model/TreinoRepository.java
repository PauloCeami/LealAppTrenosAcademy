package br.com.pauloceami.treinosacademy.lealapp.model;

import static android.content.ContentValues.TAG;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreinoRepository {

    public static final String COLLECTION_TREINO = "treino";
    private FirebaseFirestore db;
    private CollectionReference treinoRef;
    private onFireStoreTaskComplete onFireStoreTaskComplete;

    public TreinoRepository(onFireStoreTaskComplete onFireStoreTaskComplete) {
        this.onFireStoreTaskComplete = onFireStoreTaskComplete;
        db = FirebaseFirestore.getInstance();
        treinoRef = db.collection(COLLECTION_TREINO);
    }

    public void getTreinoData() {
        treinoRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            onFireStoreTaskComplete.treinoDataAdded(task.getResult().toObjects(Treino.class));
                        } else {
                            onFireStoreTaskComplete.onError(task.getException());
                        }
                    }
                });
    }

    public interface onFireStoreTaskComplete {
        void treinoDataAdded(List<Treino> treinos);

        void onError(Exception e);
    }

    public void save(Treino treino) {
        db.collection(COLLECTION_TREINO)
                .add(treino)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }
}
