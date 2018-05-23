package com.example.jblandii.protectora.Adaptadores;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jblandii.protectora.R;

import java.util.ArrayList;

/**
 * Created by jblandii on 28/04/18.
 */

public class AdaptadorMensajes extends RecyclerView.Adapter<AdaptadorMensajes.MensajesViewHolder> {

    ArrayList<String> listaMensajes;
    Context context;

    public AdaptadorMensajes(ArrayList<String> listaMensajes, Context context) {
        this.listaMensajes = listaMensajes;
        this.context = context;
    }

    @Override
    public MensajesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_conversaciones, null, false);
        RecyclerView.LayoutParams layParams = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layParams);

        return new MensajesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MensajesViewHolder holder, final int position) {
        holder.tv_nombre_protectora_conversacion.setText(listaMensajes.get(position));
//        holder.tv_mensaje.setText(listaMensajes.get(position).getMensaje());
        holder.tv_mensaje.setText("hola");
//        holder.tv_numero_mensajes.setText(listaMensajes.get(position).get());
        holder.tv_numero_mensajes.setText("hola");
        holder.cv_mensajes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intentDetallesProtectora = new Intent(v.getContext(), DetallesProtectora.class);
//                Log.v("probandoprotectora", listaMensajes.get(position).toString());
//                intentDetallesProtectora.putExtra("mensaje", (Parcelable) listaMensajes.get(position));
//                v.getContext().startActivity(intentDetallesProtectora);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaMensajes.size();
    }

    public class MensajesViewHolder extends RecyclerView.ViewHolder {
        TextView tv_nombre_protectora_conversacion, tv_mensaje, tv_numero_mensajes;
        CardView cv_mensajes;

        public MensajesViewHolder(View itemView) {
            super(itemView);
            tv_nombre_protectora_conversacion = itemView.findViewById(R.id.tv_nombre_protectora_conversacion);
            tv_mensaje = itemView.findViewById(R.id.tv_mensaje);
            tv_numero_mensajes = itemView.findViewById(R.id.tv_numero_mensajes);
            cv_mensajes = itemView.findViewById(R.id.cv_mensajes);
        }
    }
}