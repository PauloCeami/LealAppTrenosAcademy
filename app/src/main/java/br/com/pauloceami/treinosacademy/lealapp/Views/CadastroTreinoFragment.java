package br.com.pauloceami.treinosacademy.lealapp.Views;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.Timestamp;

import java.util.Date;

import br.com.pauloceami.treinosacademy.lealapp.Model.Treino;
import br.com.pauloceami.treinosacademy.lealapp.R;
import br.com.pauloceami.treinosacademy.lealapp.ViewModels.LoggedInViewModel;
import br.com.pauloceami.treinosacademy.lealapp.ViewModels.TreinoViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;


public class CadastroTreinoFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "LealApp";
    private NavController navController;
    private LoggedInViewModel loggedInViewModel;
    private TreinoViewModel treinoViewModel;
    @BindView(R.id.btn_cadastrar)
    Button btn_cadastrar;
    @BindView(R.id.edit_nome)
    EditText edit_nome;
    @BindView(R.id.edit_descricao)
    EditText edit_descricao;
    private Treino mTreino = null;

    ProgressDialog progressDialog = null;

    public CadastroTreinoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressDialog = new ProgressDialog(getContext());
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        loggedInViewModel = new ViewModelProvider(this).get(LoggedInViewModel.class);
        treinoViewModel = new ViewModelProvider(this).get(TreinoViewModel.class);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mTreino = (Treino) bundle.getSerializable("treino");
        }

        // observer is saved ?
        treinoViewModel.getIsSavedMutableLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean is_treino_saved) {
                if (is_treino_saved) {
                    Log.i(TAG, "getIsSavedMutableLiveData SAVED");
                    Toast.makeText(getContext(), "Treino cadastrado", Toast.LENGTH_SHORT).show();
                    navController.navigate(R.id.listTreinos);
                    progressDialog.dismiss();
                } else {
                    Log.i(TAG, "getIsSavedMutableLiveData NOT SAVED");
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Não foi possível salvar o registro, tente novamente", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cadastro_treino, container, false);
        ButterKnife.bind(this, v);

        btn_cadastrar.setOnClickListener(this);
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_cadastrotreino);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(getString(R.string.subtitle_cadastrotreino));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
        Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(upArrow);

        if (mTreino != null) {
            btn_cadastrar.setText("Atualizar treino");
            edit_nome.setText(mTreino.getNome());
            edit_descricao.setText(mTreino.getDescricao());
        }

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
                navController.navigate(R.id.homeFragment);
                return true;
            case R.id.addExercicios:
                return true;
            case R.id.LogOut:
                loggedInViewModel.logOut();
                navController.navigate(R.id.loginRegisterFragment);
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

            progressDialog.setCancelable(false);
            progressDialog.setTitle(R.string.treino);

            Treino t = new Treino();
            t.setNome(nome);
            t.setDescricao(descr);

            if (mTreino != null) {
                //update
                progressDialog.setMessage(getString(R.string.atualizando_treino));
                progressDialog.show();
                t.setTreino_id(mTreino.getTreino_id());
                t.setData(mTreino.getData());
                treinoViewModel.update(t);
            } else {
                //save
                progressDialog.setMessage(getString(R.string.salvando_treino));
                progressDialog.show();
                t.setData(new Timestamp(new Date()));
                treinoViewModel.save(t);
            }
        } else {
            Toast.makeText(getContext(), "Informe dados nos campos", Toast.LENGTH_SHORT).show();
        }
    }
}