package br.com.pauloceami.treinosacademy.lealapp.model;

import com.google.firebase.firestore.DocumentId;

import java.util.Date;

public class Treino {

    @DocumentId
    private String treino_id;

    private String nome;

    private Date data;

    public Treino() {
    }

    public Treino(String nome, Date data) {
        this.nome = nome;
        this.data = data;
    }

    public String getId() {
        return treino_id;
    }

    public void setId(String id) {
        this.treino_id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
}
