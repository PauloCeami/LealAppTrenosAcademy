package br.com.pauloceami.treinosacademy.lealapp.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class ListTreinosFragment extends Fragment {

    @BindView(R.id.recycler_view_treinos)
    RecyclerView recycler_view_treinos;
    private List<Treino> mListTreinos;

    private TreinoViewModel treinoViewModel;
    private TreinosAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_treinos, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
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


}
