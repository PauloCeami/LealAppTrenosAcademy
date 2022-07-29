package br.com.pauloceami.treinosacademy.lealapp.ViewModels;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import br.com.pauloceami.treinosacademy.lealapp.Model.Exercicio;
import br.com.pauloceami.treinosacademy.lealapp.Repository.ExercicioRepository;

public class ExercicioViewModel extends ViewModel implements ExercicioRepository.onFireStoreTaskComplete, ViewModelProvider.Factory {

    MutableLiveData<List<Exercicio>> listLiveData = new MutableLiveData<>();
    ExercicioRepository exercicioRepository = new ExercicioRepository(this);
    private MutableLiveData<Boolean> isSavedMutableLiveData;
    private MutableLiveData<Boolean> isDeleted;
    private String id_treino;

    public ExercicioViewModel(String id_treino) {
        exercicioRepository.getExercicios(id_treino);
        this.id_treino = id_treino;
        isSavedMutableLiveData = exercicioRepository.getIsSavedMutableLiveData();
        isDeleted = exercicioRepository.getIsDeleted();
    }

    public void save(Exercicio exercicio) {
        exercicioRepository.save(exercicio);
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new ExercicioViewModel(id_treino);
    }

    @Override
    public void exercicioDataAdded(List<Exercicio> exercicios) {
        listLiveData.postValue(exercicios);
    }

    @Override
    public void onError(Exception e) {

    }

    public LiveData<List<Exercicio>> getListMutableLiveData() {
        return listLiveData;
    }

    public MutableLiveData<Boolean> getIsSavedMutableLiveData() {
        return isSavedMutableLiveData;
    }

    public MutableLiveData<Boolean> getIsDeleted() {
        return isDeleted;
    }
}
