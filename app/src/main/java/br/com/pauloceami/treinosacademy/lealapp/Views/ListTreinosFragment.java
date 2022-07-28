package br.com.pauloceami.treinosacademy.lealapp.Views;

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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import br.com.pauloceami.treinosacademy.lealapp.Adapters.TreinosAdapter;
import br.com.pauloceami.treinosacademy.lealapp.Model.Treino;
import br.com.pauloceami.treinosacademy.lealapp.R;
import br.com.pauloceami.treinosacademy.lealapp.Utils.RecyclerItemClickListener;
import br.com.pauloceami.treinosacademy.lealapp.ViewModels.LoggedInViewModel;
import br.com.pauloceami.treinosacademy.lealapp.ViewModels.TreinoViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ListTreinosFragment extends Fragment {

    private NavController navController;
    private LoggedInViewModel loggedInViewModel;
    private TreinoViewModel treinoViewModel;
    private TreinosAdapter adapter;

    @BindView(R.id.recycler_view_list_treinos)
    RecyclerView recycler_view_list_treinos;

    @BindView(R.id.fab_list_treinos)
    FloatingActionButton fab_list_treinos;
    private List<Treino> mListTreinos;


    //Navigation.findNavController(getView()).navigate(R.id.addTreinoFragment);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        treinoViewModel = new ViewModelProvider(this).get(TreinoViewModel.class);
        loggedInViewModel = new ViewModelProvider(this).get(LoggedInViewModel.class);


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

        // is deleted ?
        treinoViewModel.getIsDeleted().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isDeleted) {
                if (isDeleted) {
                    Toast.makeText(getContext(), "Registro excluido com sucesso", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Não foi possível excluir o registro", Toast.LENGTH_SHORT).show();
                }
            }
        });


        treinoViewModel.getListMutableLiveData().observe(this, new Observer<List<Treino>>() {
            @Override
            public void onChanged(List<Treino> treinos) {
                mListTreinos = treinos;
                adapter.setTreinoList(treinos);
                adapter.notifyDataSetChanged();
            }
        });



    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_listtreinos, container, false);
        ButterKnife.bind(this, v);

        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_listtreinos);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(getString(R.string.subtitle_listtreinos));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
        Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(upArrow);

        fab_list_treinos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.cadastroTreinoFragment);
            }
        });

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mListTreinos = new ArrayList<>();
        adapter = new TreinosAdapter();

        recycler_view_list_treinos.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler_view_list_treinos.setHasFixedSize(true);
        recycler_view_list_treinos.setAdapter(adapter);

        recycler_view_list_treinos.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getContext(),
                        recycler_view_list_treinos,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Treino t = mListTreinos.get(position);
                                final CharSequence[] dialogItem = {"Atualizar Treino : " + t.getNome(), "Excluir Treino : " + t.getNome(), "Cadastrar Treino"};
                                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                                dialog.setItems(dialogItem, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        switch (i) {
                                            case 0:
                                                Bundle bundle = new Bundle();
                                                bundle.putString("nome", t.getNome());
                                                bundle.putString("descricao", t.getDescricao());
                                                bundle.putSerializable("treino", t);
                                                navController.navigate(R.id.cadastroTreinoFragment, bundle);
                                                break;
                                            case 1:

                                                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        switch (which) {
                                                            case DialogInterface.BUTTON_POSITIVE:
                                                                treinoViewModel.delete(t.getTreino_id());
                                                                treinoViewModel.getData();
                                                                break;

                                                            case DialogInterface.BUTTON_NEGATIVE:
                                                                dialog.dismiss();
                                                                break;
                                                        }
                                                    }
                                                };
                                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                                builder.setMessage("Excluir " + t.getNome() + " ?").setPositiveButton("Sim", dialogClickListener)
                                                        .setNegativeButton("Não", dialogClickListener).show();
                                                break;

                                            case 2:
                                                navController.navigate(R.id.cadastroTreinoFragment);
                                                break;
                                            default:
                                                navController.navigate(R.id.cadastroTreinoFragment);
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
        inflater.inflate(R.menu.menu_list_treinos, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Navigation.findNavController(getView()).navigate(R.id.homeFragment);
                return true;
            case R.id.addTReinos:
                navController.navigate(R.id.cadastroTreinoFragment);
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
