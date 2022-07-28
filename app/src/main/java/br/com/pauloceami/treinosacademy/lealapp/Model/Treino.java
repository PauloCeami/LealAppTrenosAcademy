package br.com.pauloceami.treinosacademy.lealapp.Model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentId;

import java.io.Serializable;

public class Treino implements Serializable {

    @DocumentId
    private String treino_id;
    private String nome;
    private String descricao;
    private Timestamp data;

    public Treino() {
    }

    public Treino(String nome, String descricao, Timestamp data) {
        this.nome = nome;
        this.descricao = descricao;
        this.data = data;
    }

    public void setTreino_id(String treino_id) {
        this.treino_id = treino_id;
    }

    public String getTreino_id() {
        return treino_id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setData(Timestamp data) {
        this.data = data;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public Timestamp getData() {
        return data;
    }
}
