package br.com.pauloceami.treinosacademy.lealapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.pauloceami.treinosacademy.lealapp.Model.Exercicio;
import br.com.pauloceami.treinosacademy.lealapp.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ExerciciosAdapter extends RecyclerView.Adapter<ExerciciosAdapter.MyViewHolder> {

    private List<Exercicio> exercicioList;


    public void setTreinoList(List<Exercicio> exercicioList) {
        this.exercicioList = exercicioList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemLista = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_exercicioitem, viewGroup, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Exercicio e = exercicioList.get(position);

        holder.txv_nome_exercicio.setText(e.getNome());
        holder.txv_observacao_exercicio.setText(e.getObservacao());
        Picasso.get()
                .load(e.getImage())
                .error(R.drawable.img_exercicio)
                .into(holder.imv_imagem);
    }

    @Override
    public int getItemCount() {
        if (exercicioList == null) {
            return 0;
        }
        return exercicioList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txv_nome_exercicio)
        TextView txv_nome_exercicio;
        @BindView(R.id.txv_observacao_exercicio)
        TextView txv_observacao_exercicio;
        @BindView(R.id.imv_imagem)
        CircleImageView imv_imagem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
