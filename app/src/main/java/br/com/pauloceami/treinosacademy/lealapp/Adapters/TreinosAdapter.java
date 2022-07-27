package br.com.pauloceami.treinosacademy.lealapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.pauloceami.treinosacademy.lealapp.R;
import br.com.pauloceami.treinosacademy.lealapp.model.Treino;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TreinosAdapter extends RecyclerView.Adapter<TreinosAdapter.MyViewHolder> {

    private List<Treino> treinoList;

    public void setTreinoList(List<Treino> treinoList) {
        this.treinoList = treinoList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemLista = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.treinos_item, viewGroup, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Treino t = treinoList.get(position);
        holder.txv_nome.setText(t.getNome());
        holder.txv_descricao.setText(t.getDescricao());
        holder.txv_data.setText(t.getData().toString());

//        Picasso.get()
//                .load(urlImage)
//                .error(R.drawable.img_notfound)
//                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if (treinoList == null) {
            return 0;
        }
        return treinoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txv_nome)
        TextView txv_nome;
        @BindView(R.id.txv_descricao)
        TextView txv_descricao;
        @BindView(R.id.txv_data)
        TextView txv_data;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
