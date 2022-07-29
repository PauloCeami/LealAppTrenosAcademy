package br.com.pauloceami.treinosacademy.lealapp.Views;

import android.app.Application;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import br.com.pauloceami.treinosacademy.lealapp.Adapters.ExerciciosAdapter;
import br.com.pauloceami.treinosacademy.lealapp.Model.Exercicio;
import br.com.pauloceami.treinosacademy.lealapp.Model.Treino;
import br.com.pauloceami.treinosacademy.lealapp.R;
import br.com.pauloceami.treinosacademy.lealapp.Utils.RecyclerItemClickListener;
import br.com.pauloceami.treinosacademy.lealapp.ViewModels.ExercicioViewModel;
import br.com.pauloceami.treinosacademy.lealapp.ViewModels.LoggedInViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ListExerciciosFragment extends Fragment {

    private NavController navController;
    private LoggedInViewModel loggedInViewModel;
    private ExercicioViewModel exercicioViewModel;
    private ExerciciosAdapter adapter;
    @BindView(R.id.recycler_view_list_exercicios)
    RecyclerView recycler_view_list_exercicios;
    private Treino mTreino = null;
    private List<Exercicio> exercicioList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mTreino = (Treino) bundle.getSerializable("treino");
        }

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        exercicioViewModel = new ViewModelProvider(
                this,
                new ExercicioViewModel(mTreino.getTreino_id()))
                .get(ExercicioViewModel.class);


        loggedInViewModel = new ViewModelProvider(this).get(LoggedInViewModel.class);

        // observer para mutações em register and login
        loggedInViewModel.getUserMutableLiveData().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null) {
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

        // is deleted ?
        exercicioViewModel.getIsDeleted().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isDeleted) {
                if (isDeleted) {
                    Toast.makeText(getContext(), "Registro excluido com sucesso", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Não foi possível excluir o registro", Toast.LENGTH_SHORT).show();
                }
            }
        });

        exercicioViewModel.getListMutableLiveData().observe(this, new Observer<List<Exercicio>>() {
            @Override
            public void onChanged(List<Exercicio> exercicios) {
                exercicioList = exercicios;
                adapter.setTreinoList(exercicios);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_listexercicios, container, false);
        ButterKnife.bind(this, v);

        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Exercícios para : " + mTreino.getNome().toString().toUpperCase());
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(getString(R.string.subtitle_listexercicio));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
        Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(upArrow);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        exercicioList = new ArrayList<>();
        adapter = new ExerciciosAdapter();

        recycler_view_list_exercicios.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler_view_list_exercicios.setHasFixedSize(true);
        recycler_view_list_exercicios.setAdapter(adapter);

        recycler_view_list_exercicios.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getContext(),
                        recycler_view_list_exercicios,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Exercicio e = exercicioList.get(position);
                                final CharSequence[] dialogItem = {"Atualizar Exercício : " + e.getNome(), "Excluir Exercício : " + e.getNome(), "Cadastrar Exercício"};
                                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                                dialog.setItems(dialogItem, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        switch (i) {
                                            case 0:
                                                Bundle bundle = new Bundle();
                                                bundle.putSerializable("exercicio", e);
                                                navController.navigate(R.id.cadastroTreinoFragment, bundle);
                                                break;
                                            case 1:

                                                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        switch (which) {
                                                            case DialogInterface.BUTTON_POSITIVE:
                                                                //treinoViewModel.delete(t.getTreino_id());
                                                                //treinoViewModel.getData();
                                                                break;

                                                            case DialogInterface.BUTTON_NEGATIVE:
                                                                dialog.dismiss();
                                                                break;
                                                        }
                                                    }
                                                };
                                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                                builder.setMessage("Excluir " + e.getNome() + " ?").setPositiveButton("Sim", dialogClickListener)
                                                        .setNegativeButton("Não", dialogClickListener).show();
                                                break;

                                            case 2:
                                                navController.navigate(R.id.cadastroExercicio);
                                                break;
                                            default:
                                                Toast.makeText(getContext(), "cadastrar exercicio", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                dialog.show();

                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_list_exercicios, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Navigation.findNavController(getView()).navigate(R.id.homeFragment);
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
}
