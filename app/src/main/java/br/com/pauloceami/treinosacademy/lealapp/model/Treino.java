package br.com.pauloceami.treinosacademy.lealapp.model;

import com.google.firebase.firestore.DocumentId;

import java.util.Date;

public class Treino {

    @DocumentId
    private String treino_id;

    private String nome;
    private String descricao;

    private Date data;

    public Treino() {
    }

    public Treino(String nome, String descricao, Date data) {
        this.nome = nome;
        this.descricao = descricao;
        this.data = data;
    }

    public void setTreino_id(String treino_id) {
        this.treino_id = treino_id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getTreino_id() {
        return treino_id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public Date getData() {
        return data;
    }
}
