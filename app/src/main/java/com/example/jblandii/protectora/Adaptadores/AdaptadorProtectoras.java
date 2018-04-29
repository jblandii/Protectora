package com.example.jblandii.protectora.Adaptadores;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jblandii.protectora.Models.Animal;
import com.example.jblandii.protectora.R;

import java.util.ArrayList;

/**
 * Created by jblandii on 28/04/18.
 */

public class AdaptadorProtectoras extends RecyclerView.Adapter<AdaptadorProtectoras.ProtectorasViewHolder> implements View.OnClickListener {

    ArrayList<Animal> listaProtectoras;
    private View.OnClickListener listener;

    public AdaptadorProtectoras(ArrayList<Animal> listaProtectoras) {
        this.listaProtectoras = listaProtectoras;
    }

    @Override
    public ProtectorasViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_protectoras, null, false);
        RecyclerView.LayoutParams layParams = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layParams);

        view.setOnClickListener(this);

        return new ProtectorasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProtectorasViewHolder holder, int position) {
        holder.txtNombre.setText(listaProtectoras.get(position).getColor());
        holder.txtInformacion.setText(listaProtectoras.get(position).getChip());
        holder.foto.setImageResource(R.drawable.ic_launcher_background);
    }

    @Override
    public int getItemCount() {
        return listaProtectoras.size();
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(view);
        }
    }

    public class ProtectorasViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtInformacion;
        ImageView foto;

        public ProtectorasViewHolder(View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.tv_raza_animal);
            txtInformacion = itemView.findViewById(R.id.tv_nombre_animal);
            foto = itemView.findViewById(R.id.iv_animal);
        }
    }
}