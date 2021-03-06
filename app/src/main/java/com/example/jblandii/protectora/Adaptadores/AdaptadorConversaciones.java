package com.example.jblandii.protectora.Adaptadores;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jblandii.protectora.Conversacion_Mensajes;
import com.example.jblandii.protectora.Models.Conversacion;
import com.example.jblandii.protectora.R;

import java.util.ArrayList;

/**
 * Created by jblandii on 28/04/18.
 */

public class AdaptadorConversaciones extends RecyclerView.Adapter<AdaptadorConversaciones.ConversacionesViewHolder> {

    private ArrayList<Conversacion> listaConversaciones;
    private Context context;

    public AdaptadorConversaciones(ArrayList<Conversacion> listaConversaciones, Context context) {
        this.listaConversaciones = listaConversaciones;
        this.context = context;
    }

    @Override
    public ConversacionesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_conversaciones, null, false);
        RecyclerView.LayoutParams layParams = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layParams);

        return new ConversacionesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ConversacionesViewHolder holder, final int position) {
        holder.tv_nombre_protectora_conversacion.setText(listaConversaciones.get(position).getProtectora().getNombre());
        holder.cv_mensajes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentDetallesProtectora = new Intent(v.getContext(), Conversacion_Mensajes.class);
                intentDetallesProtectora.putExtra("conversacion", (Parcelable) listaConversaciones.get(position).getProtectora());
                v.getContext().startActivity(intentDetallesProtectora);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaConversaciones.size();
    }

    public class ConversacionesViewHolder extends RecyclerView.ViewHolder {
        TextView tv_nombre_protectora_conversacion, tv_mensaje, tv_numero_mensajes;
        CardView cv_mensajes;

        public ConversacionesViewHolder(View itemView) {
            super(itemView);
            tv_nombre_protectora_conversacion = itemView.findViewById(R.id.tv_nombre_protectora_conversacion);
            tv_mensaje = itemView.findViewById(R.id.tv_mensaje);
            tv_numero_mensajes = itemView.findViewById(R.id.tv_numero_mensajes);
            cv_mensajes = itemView.findViewById(R.id.cv_mensajes);
        }
    }
}