package br.com.pauloceami.treinosacademy.lealapp.Views;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import br.com.pauloceami.treinosacademy.lealapp.Adapters.SpinnerAdapter;
import br.com.pauloceami.treinosacademy.lealapp.Model.Exercicio;
import br.com.pauloceami.treinosacademy.lealapp.Model.Treino;
import br.com.pauloceami.treinosacademy.lealapp.R;
import br.com.pauloceami.treinosacademy.lealapp.ViewModels.ExercicioViewModel;
import br.com.pauloceami.treinosacademy.lealapp.ViewModels.LoggedInViewModel;
import br.com.pauloceami.treinosacademy.lealapp.ViewModels.TreinoViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;


public class CadastroExercicioFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "LealApp";
    private NavController navController;
    private LoggedInViewModel loggedInViewModel;
    private ExercicioViewModel exercicioViewModel;

    @BindView(R.id.edit_nome_exercicio)
    EditText edit_nome_exercicio;

    @BindView(R.id.edit_observacao_exercicio)
    EditText edit_observacao_exercicio;

    @BindView(R.id.imv_exercicio)
    ImageView imv_exercicio;

    @BindView(R.id.btn_cadastrar_exercicio)
    Button btn_cadastrar_exercicio;
    private TreinoViewModel treinoViewModel;

    private SpinnerAdapter spinnerAdapter;
    private String TREINO_SELECTED_ID = "";
    ProgressDialog progressDialog = null;

    private List<String> mListadeFotosInterna = new ArrayList<>();
    private List<String> mListadeFotosFirebase = new ArrayList<>();
    private FirebaseAuth mFirebaseAuth;
    private StorageReference mFirebaseStorage;
    private UploadTask mUpload;
    private List<Uri> mFotoUriProfileUserChangeList = new ArrayList<>();
    private Uri mUriFotoFirebase;


    private int mEnviandoCadastro = 0; // 0=nada foi feito  1=iniciou o envio    3=enviada

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        progressDialog = new ProgressDialog(getContext());
        mFirebaseStorage = FirebaseStorage.getInstance().getReference();
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        exercicioViewModel = new ViewModelProvider(
                this,
                new ExercicioViewModel("NENHUM_ARGUMENTO_AQUI"))
                .get(ExercicioViewModel.class);


        loggedInViewModel = new ViewModelProvider(this).get(LoggedInViewModel.class);
        treinoViewModel = new ViewModelProvider(this).get(TreinoViewModel.class);
        treinoViewModel.getListMutableLiveData().observe(this, new Observer<List<Treino>>() {
            @Override
            public void onChanged(List<Treino> treinos) {
                populateSpinnerData(treinos);
            }
        });


        // observer para mutações em register and login
        loggedInViewModel.getUserMutableLiveData().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null) {
                    //txv.setText("Logado como " + firebaseUser.getEmail());
                }
            }
        });

        // observer is logged
        loggedInViewModel.getLogOutMutableLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLogout) {
                if (isLogout) {
                    navController.navigate(R.id.loginRegisterFragment);
                }
            }
        });

        // observer is saved ?
        exercicioViewModel.getIsSavedMutableLiveData().observe(this, new Observer<Boolean>() {
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cadastro_exercicio, container, false);
        ButterKnife.bind(this, v);

        imv_exercicio.setOnClickListener(this);
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_cadastro_exercicio);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(getString(R.string.subtitle_cadastro_exercicio));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
        Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(upArrow);

        btn_cadastrar_exercicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isValid()) {
                    return;
                }

                saveFotoFirebaseStorage();
            }
        });


        return v;
    }

    private boolean isValid() {

        String nome = edit_nome_exercicio.getText().toString();
        String observacao = edit_observacao_exercicio.getText().toString();

        if (TextUtils.isEmpty(nome) || TextUtils.isEmpty(observacao) || TextUtils.isEmpty(TREINO_SELECTED_ID)) {
            return false;
        }

        if (mListadeFotosInterna.isEmpty() || mListadeFotosInterna == null || mListadeFotosInterna.size() <= 0) {
            Toast.makeText(getContext(), "selecione a foto", Toast.LENGTH_LONG).show();
            return false;
        }


        if (mListadeFotosInterna.size() < 1) {
            Toast.makeText(getContext(), "Selecione a foto", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    public void escolherImagem(int requestcode) {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, requestcode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri imagemSelecionada = data.getData();
            String pathImageDevice = imagemSelecionada.toString();

            if (requestCode == 1) {
                imv_exercicio.setImageURI(imagemSelecionada);
            }
            mListadeFotosInterna.add(pathImageDevice);
        }
    }

    private void saveFotoFirebaseStorage() {

        progressDialog.setCancelable(false);
        progressDialog.setTitle(R.string.aguarde);
        progressDialog.show();

        int mTamanhoListaFotos = mListadeFotosInterna.size();
        for (int i = 0; i < mListadeFotosInterna.size(); i++) {
            String urlImagem = mListadeFotosInterna.get(i);
            salvarFotoStorage(urlImagem, mTamanhoListaFotos, i);
        }
    }

    private void salvarFotoStorage(String urlImagemString, int mTamanhoListaFotos, int i) {
        String[] nome = "imagem_exercicios".split(" ");
        Random rand = new Random();
        Integer id = rand.nextInt(1000000000);
        String nomes = nome[0].replace(" ", "") + id.toString();
        StorageReference mFotoUsuario = mFirebaseStorage.child("imagens")
                .child(nomes.toLowerCase() + "_" + i);
        mUpload = mFotoUsuario.putFile(Uri.parse(urlImagemString));
        mUpload.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                progressDialog.dismiss();
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return mFotoUsuario.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    mUriFotoFirebase = task.getResult();
                    Exercicio e = new Exercicio();
                    e.setNome(edit_nome_exercicio.getText().toString());
                    e.setObservacao(edit_observacao_exercicio.getText().toString());
                    e.setTreino_id(TREINO_SELECTED_ID);
                    e.setImage(mUriFotoFirebase.toString());
                    exercicioViewModel.save(e);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void populateSpinnerData(List<Treino> mListTreinos) {
        Spinner sp_treinos = (Spinner) getActivity().findViewById(R.id.sp_treinos);
        Treino[] treino = new Treino[mListTreinos.size()];
        for (int i = 0; i < mListTreinos.size(); i++) {
            treino[i] = new Treino();
            treino[i].setTreino_id(mListTreinos.get(i).getTreino_id());
            treino[i].setNome(mListTreinos.get(i).getNome());
            treino[i].setDescricao(mListTreinos.get(i).getDescricao());
        }
        spinnerAdapter = new SpinnerAdapter(getContext(),
                android.R.layout.simple_spinner_item,
                treino);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_treinos.setAdapter(spinnerAdapter);
        sp_treinos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Treino tr = spinnerAdapter.getItem(position);
                TREINO_SELECTED_ID = tr.getTreino_id();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                navController.navigate(R.id.homeFragment);
                return true;
            case R.id.addTReinos:
                navController.navigate(R.id.listTreinos);
                return true;
            case R.id.addExercicios:
                navController.navigate(R.id.cadastroExercicio);
                return true;
            case R.id.LogOut:
                loggedInViewModel.logOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imv_exercicio:
                escolherImagem(1);
                break;
        }
    }
}
