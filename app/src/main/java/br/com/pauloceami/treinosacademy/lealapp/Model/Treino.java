package br.com.pauloceami.treinosacademy.lealapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentId;

import java.io.Serializable;

public class Treino implements Serializable, Parcelable {

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

    protected Treino(Parcel in) {
        treino_id = in.readString();
        nome = in.readString();
        descricao = in.readString();
        data = in.readParcelable(Timestamp.class.getClassLoader());
    }

    public static final Creator<Treino> CREATOR = new Creator<Treino>() {
        @Override
        public Treino createFromParcel(Parcel in) {
            return new Treino(in);
        }

        @Override
        public Treino[] newArray(int size) {
            return new Treino[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(treino_id);
        parcel.writeString(nome);
        parcel.writeString(descricao);
        parcel.writeParcelable(data, i);
    }
}
