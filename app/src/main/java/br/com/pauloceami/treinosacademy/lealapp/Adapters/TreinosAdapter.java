package br.com.pauloceami.treinosacademy.lealapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import br.com.pauloceami.treinosacademy.lealapp.R;
import br.com.pauloceami.treinosacademy.lealapp.Model.Treino;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TreinosAdapter extends RecyclerView.Adapter<TreinosAdapter.MyViewHolder> {

    private List<Treino> treinoList;

    private Dialog dialog;

    public interface Dialog {
        void onClick(int pos);
    }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }

    public void setTreinoList(List<Treino> treinoList) {
        this.treinoList = treinoList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemLista = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_treinoitem, viewGroup, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Treino t = treinoList.get(position);
        holder.txv_nome.setText(t.getNome());
        holder.txv_descricao.setText(t.getDescricao());

        Long d = t.getData().getSeconds();
        Date de = new Date(d * 1000);
        holder.txv_data.setText(de.toString());

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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (dialog != null) {
                        dialog.onClick(getLayoutPosition());
                    }
                }
            });
        }

    }
}
