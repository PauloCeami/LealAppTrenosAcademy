package br.com.pauloceami.treinosacademy.lealapp.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import br.com.pauloceami.treinosacademy.lealapp.Model.Treino;
import br.com.pauloceami.treinosacademy.lealapp.Repository.TreinoRepository;

public class TreinoViewModel extends ViewModel implements TreinoRepository.onFireStoreTaskComplete {

    MutableLiveData<List<Treino>> listLiveData = new MutableLiveData<>();

    private MutableLiveData<Boolean> isSavedMutableLiveData;
    private MutableLiveData<Boolean> isDeleted;

    TreinoRepository treinoRepository = new TreinoRepository(this);


    public TreinoViewModel() {
        treinoRepository.getTreinoData();
        isSavedMutableLiveData = treinoRepository.getIsSavedMutableLiveData();
        isDeleted = treinoRepository.getIsDeleted();
    }

    public void save(Treino t) {
        treinoRepository.save(t);
    }

    public void getData(){
        treinoRepository.getTreinoData();
    }

    public void update(Treino t) {
        treinoRepository.update(t);
    }

    public void delete(String id) {
        treinoRepository.delete(id);
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

    public MutableLiveData<Boolean> getIsSavedMutableLiveData() {
        return isSavedMutableLiveData;
    }

    public MutableLiveData<Boolean> getIsDeleted() {
        return isDeleted;
    }
}
