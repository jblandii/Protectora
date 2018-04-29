package com.example.jblandii.protectora.Adaptadores;

import android.content.Context;
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

public class AdaptadorAnimales extends RecyclerView.Adapter<AdaptadorAnimales.AnimalesViewHolder> implements View.OnClickListener {

    ArrayList<Animal> listaAnimales;
    Context context;
    private View.OnClickListener listener;

    public AdaptadorAnimales(ArrayList<Animal> listaAnimales) {
        this.listaAnimales = listaAnimales;
    }

    @Override
    public AnimalesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_animales, null, false);
        RecyclerView.LayoutParams layParams = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layParams);

        view.setOnClickListener(this);

        return new AnimalesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AnimalesViewHolder holder, int position) {
        holder.tv_raza_animal.setText(listaAnimales.get(position).getRaza());
        holder.tv_nombre_animal.setText(listaAnimales.get(position).getNombre());
        holder.iv_animal.setImageResource(R.drawable.nueva_pancarta);
//        Picasso.with(get).load(R.drawable.ic_launcher_background).fit().into(holder.iv_animal);
    }

    @Override
    public int getItemCount() {
        return listaAnimales.size();
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

    public class AnimalesViewHolder extends RecyclerView.ViewHolder {
        TextView tv_raza_animal, tv_nombre_animal;
        ImageView iv_animal;

        public AnimalesViewHolder(View itemView) {
            super(itemView);
            tv_raza_animal = itemView.findViewById(R.id.tv_raza_animal);
            tv_nombre_animal = itemView.findViewById(R.id.tv_nombre_animal);
            iv_animal = itemView.findViewById(R.id.iv_animal);
        }
    }
}