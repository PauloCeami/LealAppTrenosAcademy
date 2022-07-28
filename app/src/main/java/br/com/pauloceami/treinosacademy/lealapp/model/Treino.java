package br.com.pauloceami.treinosacademy.lealapp.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentId;

import java.util.Date;

public class Treino {

    @DocumentId
    private String treino_id;

    private String nome;
    private String descricao;

    private Timestamp data;

    public Treino() {
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
