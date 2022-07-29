package br.com.pauloceami.treinosacademy.lealapp.Repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import br.com.pauloceami.treinosacademy.lealapp.Model.Exercicio;
import br.com.pauloceami.treinosacademy.lealapp.Model.Treino;

public class ExercicioRepository {

    public static final String TAG = "LealApp";
    public static final String COLLECTION_EXERCICIO = "exercicio";
    private FirebaseFirestore db;
    private CollectionReference exercicioRef;
    private onFireStoreTaskComplete onFireStoreTaskComplete;

    private MutableLiveData<Boolean> isSavedMutableLiveData;
    private MutableLiveData<Boolean> isDeleted;


    public ExercicioRepository(onFireStoreTaskComplete onFireStoreTaskComplete) {
        this.onFireStoreTaskComplete = onFireStoreTaskComplete;
        db = FirebaseFirestore.getInstance();
        exercicioRef = db.collection(COLLECTION_EXERCICIO);
        isSavedMutableLiveData = new MutableLiveData<>();
        isDeleted = new MutableLiveData<>();
    }

    public void getExercicios(String id_treino) {
        exercicioRef
                .whereEqualTo("id_treino", id_treino)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            onFireStoreTaskComplete.exercicioDataAdded(task.getResult().toObjects(Exercicio.class));
                        } else {
                            onFireStoreTaskComplete.onError(task.getException());
                        }
                    }
                });
    }

    public void save(Exercicio exercicio) {
        db.collection(COLLECTION_EXERCICIO)
                .add(exercicio)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        isSavedMutableLiveData.postValue(true);
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        isSavedMutableLiveData.postValue(false);
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }


    public MutableLiveData<Boolean> getIsSavedMutableLiveData() {
        return isSavedMutableLiveData;
    }

    public MutableLiveData<Boolean> getIsDeleted() {
        return isDeleted;
    }

    public interface onFireStoreTaskComplete {
        void exercicioDataAdded(List<Exercicio> exercicios);

        void onError(Exception e);
    }
}
