package com.example.jblandii.protectora.fragments;


import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jblandii.protectora.LoginActivity;
import com.example.jblandii.protectora.Models.Animal;
import com.example.jblandii.protectora.R;
import com.example.jblandii.protectora.RecordarContrasenaActivity;

public class AnimalFragment extends Fragment {

    FloatingActionButton fab_filtrar_protectora;

    public AnimalFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_animal_fragment, container, false);
        fab_filtrar_protectora = view.findViewById(R.id.fab);
        fab_filtrar_protectora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intentRecordarContrasena = new Intent(getActivity(), RecordarContrasenaActivity.class);
//                startActivity(intentRecordarContrasena);
            }
        });
        return view;
    }

    /**
     * Metodo que permite cargar los animales que pertenecen a la misma comunidad que el usuario.
     */
    public void cargarAnimales() {

    }
}