package br.com.pauloceami.treinosacademy.lealapp.views;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.firebase.Timestamp;

import java.util.Date;

import br.com.pauloceami.treinosacademy.lealapp.R;
import br.com.pauloceami.treinosacademy.lealapp.model.Treino;
import br.com.pauloceami.treinosacademy.lealapp.viewmodel.LoggedInViewModel;
import br.com.pauloceami.treinosacademy.lealapp.viewmodel.TreinoViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;


public class TreinoFragment extends Fragment implements View.OnClickListener {

    private LoggedInViewModel loggedInViewModel;
    private TreinoViewModel treinoViewModel;
    @BindView(R.id.btn_cadastrar)
    Button btn_cadastrar;
    @BindView(R.id.edit_nome)
    EditText edit_nome;
    @BindView(R.id.edit_descricao)
    EditText edit_descricao;

    public TreinoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loggedInViewModel = new ViewModelProvider(this).get(LoggedInViewModel.class);
        treinoViewModel = new ViewModelProvider(this).get(TreinoViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_treino, container, false);
        ButterKnife.bind(this, v);
        btn_cadastrar.setOnClickListener(this);
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Treinos Academy");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("Cadastro de Treinos - MVVM Architecture");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
        Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_custom_24dp);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(upArrow);
        return v;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_cad_treinos, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Navigation.findNavController(getView()).navigate(R.id.homeFragment);
                return true;
            case R.id.addExercicios:
                return true;
            case R.id.LogOut:
                loggedInViewModel.logOut();
                Navigation.findNavController(getView()).navigate(R.id.loginRegisterFragment);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onClick(View view) {
        String nome = edit_nome.getText().toString();
        String descr = edit_descricao.getText().toString();
        if (!TextUtils.isEmpty(nome) && !TextUtils.isEmpty(descr)) {
            Treino t = new Treino();
            t.setNome(nome);
            t.setDescricao(descr);
            t.setData(new Timestamp(new Date()));
            treinoViewModel.save(t);
            Toast.makeText(getContext(), "Treino cadastrado", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(getView()).navigate(R.id.homeFragment);
        } else {
            Toast.makeText(getContext(), "Informe dados nos campos", Toast.LENGTH_SHORT).show();
        }
    }
}