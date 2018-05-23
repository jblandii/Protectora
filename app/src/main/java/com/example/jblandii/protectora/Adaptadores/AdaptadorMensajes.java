package com.example.jblandii.protectora.Adaptadores;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jblandii.protectora.Models.Mensaje;
import com.example.jblandii.protectora.R;

import java.util.ArrayList;

/**
 * Created by jblandii on 28/04/18.
 */

public class AdaptadorMensajes extends RecyclerView.Adapter<AdaptadorMensajes.MensajesViewHolder> {

    ArrayList<Mensaje> listaMensajes;
    Context context;

    public AdaptadorMensajes(ArrayList<Mensaje> listaMensajes, Context context) {
        this.listaMensajes = listaMensajes;
        this.context = context;
    }

    @Override
    public MensajesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mensaje, null, false);
        RecyclerView.LayoutParams layParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layParams);

        return new MensajesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MensajesViewHolder holder, final int position) {
        if (listaMensajes.get(position).getEmisario().equals("usuario")) {
            holder.mensaje_de_usuario.setText(listaMensajes.get(position).getMensaje());
            holder.mensaje_de_usuario.setVisibility(View.VISIBLE);
        } else if (listaMensajes.get(position).getEmisario().equals("protectora")) {
            holder.mensaje_de_protectora.setText(listaMensajes.get(position).getMensaje());
            holder.mensaje_de_protectora.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return listaMensajes.size();
    }

    public class MensajesViewHolder extends RecyclerView.ViewHolder {
        TextView mensaje_de_usuario, mensaje_de_protectora;

        public MensajesViewHolder(View itemView) {
            super(itemView);
            mensaje_de_usuario = itemView.findViewById(R.id.mensaje_de_usuario);
            mensaje_de_protectora = itemView.findViewById(R.id.mensaje_de_protectora);
        }
    }
}