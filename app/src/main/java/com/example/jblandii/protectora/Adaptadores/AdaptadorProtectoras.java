package com.example.jblandii.protectora.Adaptadores;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jblandii.protectora.DetallesAnimal;
import com.example.jblandii.protectora.DetallesProtectora;
import com.example.jblandii.protectora.Models.Animal;
import com.example.jblandii.protectora.Models.Protectora;
import com.example.jblandii.protectora.R;

import java.util.ArrayList;

/**
 * Created by jblandii on 28/04/18.
 */

public class AdaptadorProtectoras extends RecyclerView.Adapter<AdaptadorProtectoras.ProtectorasViewHolder> {

    ArrayList<Protectora> listaProtectoras;
    Context context;

    public AdaptadorProtectoras(ArrayList<Protectora> listaProtectoras, Context context) {
        this.listaProtectoras = listaProtectoras;
        this.context = context;
    }

    @Override
    public ProtectorasViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_protectoras, null, false);
        RecyclerView.LayoutParams layParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layParams);

        return new ProtectorasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProtectorasViewHolder holder, final int position) {
        holder.tv_nombre_protectora.setText(listaProtectoras.get(position).getNombre());
        holder.tv_nombre_provincia.setText(listaProtectoras.get(position).getProvincia());
        holder.tv_direccion.setText(listaProtectoras.get(position).getDireccion());
        holder.cv_protectoras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentDetallesProtectora = new Intent(v.getContext(), DetallesProtectora.class);
                Log.v("probandoprotectora", listaProtectoras.get(position).toString());
                intentDetallesProtectora.putExtra("protectora", (Parcelable) listaProtectoras.get(position));
                v.getContext().startActivity(intentDetallesProtectora);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaProtectoras.size();
    }

    public class ProtectorasViewHolder extends RecyclerView.ViewHolder {
        TextView tv_nombre_protectora, tv_nombre_provincia, tv_direccion;
        CardView cv_protectoras;

        public ProtectorasViewHolder(View itemView) {
            super(itemView);
            tv_nombre_protectora = itemView.findViewById(R.id.tv_nombre_protectora);
            tv_nombre_provincia = itemView.findViewById(R.id.tv_nombre_provincia);
            tv_direccion = itemView.findViewById(R.id.tv_direccion);
            cv_protectoras = itemView.findViewById(R.id.cv_protectoras);
        }
    }
}