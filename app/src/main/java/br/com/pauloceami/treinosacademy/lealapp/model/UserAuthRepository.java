package br.com.pauloceami.treinosacademy.lealapp.model;

import android.app.Application;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserAuthRepository {

    private Application aplApplication;
    private FirebaseAuth firebaseAuth;
    private MutableLiveData<FirebaseUser> userMutableLiveData;
    private MutableLiveData<Boolean> logOutMutableLiveData;

    public UserAuthRepository(Application aplApplication) {

        this.aplApplication = aplApplication;
        firebaseAuth = FirebaseAuth.getInstance();
        userMutableLiveData = new MutableLiveData<>();
        logOutMutableLiveData = new MutableLiveData<>();

        if (firebaseAuth.getCurrentUser() != null) {
            // avisando os oberserves
            userMutableLiveData.postValue(firebaseAuth.getCurrentUser());
            logOutMutableLiveData.postValue(false);
        }
    }

    public void register(String email, String senha) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            firebaseAuth.createUserWithEmailAndPassword(email, senha)
                    .addOnCompleteListener(aplApplication.getMainExecutor(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                userMutableLiveData.postValue(firebaseAuth.getCurrentUser());
                            } else {
                                Toast.makeText(aplApplication, "Falha ao registrar usuário : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(aplApplication, "Seu celular não suporta API 28 para registrar usuário :: aplApplication.getMainExecutor()", Toast.LENGTH_SHORT).show();
        }
    }

    public void login(String email, String senha) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            firebaseAuth.signInWithEmailAndPassword(email, senha)
                    .addOnCompleteListener(aplApplication.getMainExecutor(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                userMutableLiveData.postValue(firebaseAuth.getCurrentUser());
                            } else {
                                Toast.makeText(aplApplication, "Falha ao fazer login : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(aplApplication, "Seu celular não suporta API 28 para registrar usuário :: aplApplication.getMainExecutor()", Toast.LENGTH_SHORT).show();
        }
    }

    public void logOut() {
        firebaseAuth.signOut();
        logOutMutableLiveData.postValue(true);
    }

    public MutableLiveData<Boolean> getLogOutMutableLiveData() {
        return logOutMutableLiveData;
    }

    public MutableLiveData<FirebaseUser> getUserMutableLiveData() {
        return userMutableLiveData;
    }
}
