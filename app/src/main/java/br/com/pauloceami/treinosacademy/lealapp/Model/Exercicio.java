package br.com.pauloceami.treinosacademy.lealapp.Model;

import com.google.firebase.firestore.DocumentId;

import java.io.Serializable;

public class Exercicio implements Serializable {

    @DocumentId
    private String exercicio_id;
    private String treino_id;
    private String nome;
    private String observacao;
    private String image;

    public Exercicio() {
    }

    public Exercicio(String treino_id, String nome, String observacao, String image) {
        this.treino_id = treino_id;
        this.nome = nome;
        this.observacao = observacao;
        this.image = image;
    }

    public String getExercicio_id() {
        return exercicio_id;
    }

    public String getTreino_id() {
        return treino_id;
    }

    public String getNome() {
        return nome;
    }

    public String getObservacao() {
        return observacao;
    }

    public String getImage() {
        return image;
    }

    public void setExercicio_id(String exercicio_id) {
        this.exercicio_id = exercicio_id;
    }

    public void setTreino_id(String treino_id) {
        this.treino_id = treino_id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
