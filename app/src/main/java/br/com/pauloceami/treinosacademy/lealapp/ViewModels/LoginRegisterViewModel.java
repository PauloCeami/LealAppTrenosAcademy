package br.com.pauloceami.treinosacademy.lealapp.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;

import br.com.pauloceami.treinosacademy.lealapp.Repository.UserAuthRepository;

public class LoginRegisterViewModel extends AndroidViewModel {

    private UserAuthRepository userAuthRepository;
    private MutableLiveData<FirebaseUser> userMutableLiveData;

    public LoginRegisterViewModel(@NonNull Application application) {
        super(application);

        userAuthRepository = new UserAuthRepository(application);
        userMutableLiveData = userAuthRepository.getUserMutableLiveData();
    }


    public void register(String email, String senha) {
        userAuthRepository.register(email, senha);
    }

    public void login(String email, String senha) {
        userAuthRepository.login(email, senha);
    }

    public MutableLiveData<FirebaseUser> getUserMutableLiveData() {
        return userMutableLiveData;
    }
}
