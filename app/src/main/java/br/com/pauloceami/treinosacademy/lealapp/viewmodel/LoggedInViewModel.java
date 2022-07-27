package br.com.pauloceami.treinosacademy.lealapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;

import br.com.pauloceami.treinosacademy.lealapp.model.UserAuthRepository;

public class LoggedInViewModel extends AndroidViewModel {

    private UserAuthRepository userAuthRepository;
    private MutableLiveData<FirebaseUser> userMutableLiveData;
    private MutableLiveData<Boolean> logOutMutableLiveData;

    public LoggedInViewModel(@NonNull Application application) {
        super(application);

        userAuthRepository = new UserAuthRepository(application);
        userMutableLiveData = userAuthRepository.getUserMutableLiveData();
        logOutMutableLiveData = userAuthRepository.getLogOutMutableLiveData();
    }

    public void logOut(){
        userAuthRepository.logOut();
    }

    public MutableLiveData<FirebaseUser> getUserMutableLiveData() {
        return userMutableLiveData;
    }

    public MutableLiveData<Boolean> getLogOutMutableLiveData() {
        return logOutMutableLiveData;
    }
}
