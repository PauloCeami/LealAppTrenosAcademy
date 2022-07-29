package br.com.pauloceami.treinosacademy.lealapp.Views;

import static br.com.pauloceami.treinosacademy.lealapp.Utils.Util.TREINO_SERIALIZABLE;

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

import br.com.pauloceami.treinosacademy.lealapp.Adapters.TreinosAdapter;
import br.com.pauloceami.treinosacademy.lealapp.Model.Treino;
import br.com.pauloceami.treinosacademy.lealapp.R;
import br.com.pauloceami.treinosacademy.lealapp.Utils.RecyclerItemClickListener;
import br.com.pauloceami.treinosacademy.lealapp.ViewModels.LoggedInViewModel;
import br.com.pauloceami.treinosacademy.lealapp.ViewModels.TreinoViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment {

    private NavController navController;
    private LoggedInViewModel loggedInViewModel;
    private TreinoViewModel treinoViewModel;
    private TreinosAdapter adapter;

    @BindView(R.id.recycler_view_treinos)
    RecyclerView recycler_view_treinos;
    private List<Treino> mListTreinos;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, v);

        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_home);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(R.string.subtitle_home);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
        Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(upArrow);


        return v;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mListTreinos = new ArrayList<>();
        adapter = new TreinosAdapter();

        recycler_view_treinos.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler_view_treinos.setHasFixedSize(true);
        recycler_view_treinos.setAdapter(adapter);

        recycler_view_treinos.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getContext(),
                        recycler_view_treinos,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Treino t = mListTreinos.get(position);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable(TREINO_SERIALIZABLE, t);
                                navController.navigate(R.id.listExerciciosFragment, bundle);
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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


}
