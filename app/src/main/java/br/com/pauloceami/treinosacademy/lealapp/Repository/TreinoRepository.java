package br.com.pauloceami.treinosacademy.lealapp.Repository;

import static br.com.pauloceami.treinosacademy.lealapp.Utils.Util.TAG_LEALAPPS;

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

import br.com.pauloceami.treinosacademy.lealapp.Model.Treino;

public class TreinoRepository {

    public static final String TAG = "LealApp";
    public static final String COLLECTION_TREINO = "treino";
    private FirebaseFirestore db;
    private CollectionReference treinoRef;
    private onFireStoreTaskComplete onFireStoreTaskComplete;
    private MutableLiveData<Boolean> isSavedMutableLiveData;
    private MutableLiveData<Boolean> isDeleted;

    public TreinoRepository(onFireStoreTaskComplete onFireStoreTaskComplete) {
        this.onFireStoreTaskComplete = onFireStoreTaskComplete;
        db = FirebaseFirestore.getInstance();
        treinoRef = db.collection(COLLECTION_TREINO);
        isSavedMutableLiveData = new MutableLiveData<>();
        isDeleted = new MutableLiveData<>();
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


    public void save(Treino treino) {
        db.collection(COLLECTION_TREINO)
                .add(treino)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        isSavedMutableLiveData.postValue(true);
                        Log.i(TAG_LEALAPPS, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        isSavedMutableLiveData.postValue(false);
                        Log.i(TAG_LEALAPPS, "Error adding document", e);
                    }
                });
    }

    public void update(Treino treino) {
        db.collection(COLLECTION_TREINO).document(treino.getTreino_id())
                .set(treino)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            isSavedMutableLiveData.postValue(true);
                        } else {
                            isSavedMutableLiveData.postValue(false);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        isSavedMutableLiveData.postValue(false);
                        Log.i(TAG_LEALAPPS, "Error adding document", e);
                    }
                });
    }

    public void delete(String idDocument) {
        db.collection(COLLECTION_TREINO).document(idDocument)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            isDeleted.postValue(true);
                        } else {
                            isDeleted.postValue(false);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        isDeleted.postValue(false);
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
        void treinoDataAdded(List<Treino> treinos);

        void onError(Exception e);
    }

}
