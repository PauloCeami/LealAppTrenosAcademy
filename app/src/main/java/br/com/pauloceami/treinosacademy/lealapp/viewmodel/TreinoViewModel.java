package br.com.pauloceami.treinosacademy.lealapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import br.com.pauloceami.treinosacademy.lealapp.model.Treino;
import br.com.pauloceami.treinosacademy.lealapp.model.TreinoRepository;

public class TreinoViewModel extends ViewModel implements TreinoRepository.onFireStoreTaskComplete {

    MutableLiveData<List<Treino>> listLiveData = new MutableLiveData<>();

    TreinoRepository treinoRepository = new TreinoRepository(this);


    public TreinoViewModel() {
        treinoRepository.getTreinoData();
    }

    public void save(Treino t) {
        treinoRepository.save(t);
    }


    @Override
    public void treinoDataAdded(List<Treino> treinos) {
        listLiveData.postValue(treinos);
    }

    @Override
    public void onError(Exception e) {

    }

    public LiveData<List<Treino>> getListMutableLiveData() {
        return listLiveData;
    }
}
