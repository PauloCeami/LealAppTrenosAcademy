package br.com.pauloceami.treinosacademy.lealapp.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseUser;

import br.com.pauloceami.treinosacademy.lealapp.R;
import br.com.pauloceami.treinosacademy.lealapp.viewmodel.LoggedInViewModel;
import br.com.pauloceami.treinosacademy.lealapp.viewmodel.LoginRegisterViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LoggedFragment extends Fragment {

    @BindView(R.id.txvLogged)
    TextView txvLogged;
    @BindView(R.id.btn_logout)
    Button btn_logout;

    private LoggedInViewModel loggedInViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_logged, container, false);
        ButterKnife.bind(this, v);

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loggedInViewModel.logOut();
            }
        });


        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // observer para mutações em register and login
        loggedInViewModel = new ViewModelProvider(this).get(LoggedInViewModel.class);
        loggedInViewModel.getUserMutableLiveData().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null) {
                    txvLogged.setText("Logado como " + firebaseUser.getEmail());
                }
            }
        });


        // observer para ver se está logado
        loggedInViewModel.getLogOutMutableLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLogged) {
                if (isLogged) {
                    Navigation.findNavController(getView()).navigate(R.id.loginRegisterFragment);
                }
            }
        });

    }
}
