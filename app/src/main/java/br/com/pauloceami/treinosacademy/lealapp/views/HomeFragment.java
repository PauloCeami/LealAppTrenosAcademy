package br.com.pauloceami.treinosacademy.lealapp.views;

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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import br.com.pauloceami.treinosacademy.lealapp.Adapters.TreinosAdapter;
import br.com.pauloceami.treinosacademy.lealapp.R;
import br.com.pauloceami.treinosacademy.lealapp.Utils.RecyclerItemClickListener;
import br.com.pauloceami.treinosacademy.lealapp.model.Treino;
import br.com.pauloceami.treinosacademy.lealapp.viewmodel.LoggedInViewModel;
import br.com.pauloceami.treinosacademy.lealapp.viewmodel.TreinoViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment {

    private LoggedInViewModel loggedInViewModel;

    @BindView(R.id.recycler_view_treinos)
    RecyclerView recycler_view_treinos;
    private List<Treino> mListTreinos;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    private TreinoViewModel treinoViewModel;
    private TreinosAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, v);

        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Treinos Academy");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("Lista de Treinos - MVVM Architecture");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
        Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_custom_24dp);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(upArrow);

        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Navigation.findNavController(getView()).navigate(R.id.addTreinoFragment);
            }
        });


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
                                Toast.makeText(getContext(), t.getNome() + " :  ID treino ::  " + t.getTreino_id(), Toast.LENGTH_SHORT).show();
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

        treinoViewModel = new ViewModelProvider(this).get(TreinoViewModel.class);
        treinoViewModel.getListMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<Treino>>() {
            @Override
            public void onChanged(List<Treino> treinos) {
                mListTreinos = treinos;
                adapter.setTreinoList(treinos);
                adapter.notifyDataSetChanged();
            }
        });

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
                    //txvLogged.setText("Logado como " + firebaseUser.getEmail());
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addTReinos:
                Navigation.findNavController(getView()).navigate(R.id.addTreinoFragment);
                return true;
            case R.id.addExercicios:
                return true;
            case R.id.LogOut:
                loggedInViewModel.logOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
