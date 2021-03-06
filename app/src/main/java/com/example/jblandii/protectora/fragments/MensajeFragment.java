package com.example.jblandii.protectora.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.jblandii.protectora.Adaptadores.AdaptadorConversaciones;
import com.example.jblandii.protectora.Models.Conversacion;
import com.example.jblandii.protectora.R;
import com.example.jblandii.protectora.peticionesBD.JSONUtil;
import com.example.jblandii.protectora.peticionesBD.Preferencias;
import com.example.jblandii.protectora.peticionesBD.Tags;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MensajeFragment extends Fragment {

    private ArrayList<Conversacion> listaConversaciones;
    RecyclerView recyclerView;
    private Conversacion conversaciones;

    public MensajeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_mensaje_fragment, container, false);
        listaConversaciones = new ArrayList<>();
        recyclerView = view.findViewById(R.id.rv_recycler_conversaciones);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), 1));

        cargarConversaciones();

        AdaptadorConversaciones adaptadorConversaciones = new AdaptadorConversaciones(listaConversaciones, getContext());
        recyclerView.setAdapter(adaptadorConversaciones);

        return view;
    }

    /**
     * Metodo que utilizamos para cargar todas las conversaciones.
     */
    public void cargarConversaciones() {
        String token = Preferencias.getToken(getActivity());
        String usuario_id = Preferencias.getID(getActivity());
        //Creamos el JSON que vamos a mandar al servidor
        JSONObject json = new JSONObject();
        try {
            json.put(Tags.TOKEN, token);
            json.put(Tags.USUARIO_ID, usuario_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /* Se hace petición de login al servidor. */
        json = JSONUtil.hacerPeticionServidor("conversacion/cargar_conversaciones/", json);
        try {
            String p = json.getString(Tags.RESULTADO);

            /* Se comprueba la conexión al servidor. */
            if (p.contains(Tags.ERRORCONEXION)) {
//                mensaje = "Error de conexión";
            } else if (p.contains(Tags.OK)) {

                JSONArray array = json.getJSONArray(Tags.LISTA_CONVERSACIONES);
                if (array.length() > 0) {
                    for (int i = 0; i < array.length(); i++) {
                        Conversacion conversacion = new Conversacion(array.getJSONObject(i));
                        listaConversaciones.add(conversacion);
                    }
                }
            }
            /* Resultado falla por otro error. */
            else if (p.contains(Tags.ERROR)) {
                Toast.makeText(getContext(), json.getString(Tags.MENSAJE), Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}