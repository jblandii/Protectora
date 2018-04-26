package com.example.jblandii.protectora.fragments;


import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jblandii.protectora.LoginActivity;
import com.example.jblandii.protectora.Models.Animal;
import com.example.jblandii.protectora.Models.Usuario;
import com.example.jblandii.protectora.R;
import com.example.jblandii.protectora.RecordarContrasenaActivity;
import com.example.jblandii.protectora.peticionesBD.JSONUtil;
import com.example.jblandii.protectora.peticionesBD.Preferencias;
import com.example.jblandii.protectora.peticionesBD.Tags;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.jblandii.protectora.peticionesBD.Preferencias.getToken;

public class AnimalFragment extends Fragment {

    FloatingActionButton fab_filtrar_protectora;

    public AnimalFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cargarAnimales();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_animal_fragment, container, false);
        fab_filtrar_protectora = view.findViewById(R.id.fab_filtrar_protectora);
//        fab_filtrar_protectora.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intentRecordarContrasena = new Intent(getActivity(), RecordarContrasenaActivity.class);
//                startActivity(intentRecordarContrasena);
//            }
//        });
        return view;
    }

    /**
     * Metodo que permite cargar los animales que pertenecen a la misma comunidad que el usuario.
     */
    public void cargarAnimales() {
        String token = Preferencias.getToken(getActivity());
        String usuario_id = Preferencias.getID(getActivity());
        Log.v("token", token);
        Log.v("token", usuario_id);
        //Creamos el JSON que vamos a mandar al servidor
        JSONObject json = new JSONObject();
        try {
            json.put(Tags.TOKEN, token);
            json.put(Tags.USUARIO_ID, usuario_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /* Se hace petición de login al servidor. */
        json = JSONUtil.hacerPeticionServidor("protectora/cargar_animales_provincia_usuario/", json);

        try {
            String p = json.getString(Tags.RESULTADO);

            /* Se comprueba la conexión al servidor. */
            if (p.contains(Tags.ERRORCONEXION)) {
//                mensaje = "Error de conexión";
            }
            /* En caso de que conecte */
            else if (p.contains(Tags.OK)) {
                /* Guarda en las preferencias el token. */
//                Usuario.guardarEnPref(this, usuario, json.getString(Tags.TOKEN));
//                mensaje = "";
                Log.v("Entrada", "No es la 1º");
            }

            /* Resultado falla por otro error. */
            else if (p.contains(Tags.ERROR)) {
                String msg = json.getString(Tags.MENSAJE);
//                mensaje = msg;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}