package com.example.jblandii.protectora.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.jblandii.protectora.Adaptadores.AdaptadorProtectoras;
import com.example.jblandii.protectora.Models.Animal;
import com.example.jblandii.protectora.R;

import java.util.ArrayList;

public class ProtectoraFragment extends Fragment {

    FloatingActionButton fab_filtrar_protectora;
    ArrayList<Animal> lista_protectoras;
    RecyclerView recyclerView;

    public ProtectoraFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_protectora_fragment, container, false);
        fab_filtrar_protectora = view.findViewById(R.id.fab_filtrar_protectora);
        lista_protectoras = new ArrayList<>();
        recyclerView = view.findViewById(R.id.rv_recycler_protectora);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        AdaptadorProtectoras adaptadorProtectoras = new AdaptadorProtectoras(lista_protectoras);
        recyclerView.setAdapter(adaptadorProtectoras);

        adaptadorProtectoras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Seleccion: " +
                        lista_protectoras.get(recyclerView.getChildAdapterPosition(view)).getColor(), Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }
}