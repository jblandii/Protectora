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
import com.example.jblandii.protectora.Models.Protectora;
import com.example.jblandii.protectora.R;
import com.example.jblandii.protectora.peticionesBD.JSONUtil;
import com.example.jblandii.protectora.peticionesBD.Preferencias;
import com.example.jblandii.protectora.peticionesBD.Tags;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.jblandii.protectora.Util.ValidatorUtil.ucFirst;

public class ProtectoraFragment extends Fragment {

    FloatingActionButton fab_filtrar_protectora;
    ArrayList<Protectora> lista_protectoras;
    RecyclerView recyclerView;
    String direccion = "", provincia = "", codigo_postal = "";

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

        cargarProtectoras();

        AdaptadorProtectoras adaptadorProtectoras = new AdaptadorProtectoras(lista_protectoras);
        recyclerView.setAdapter(adaptadorProtectoras);

        adaptadorProtectoras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Seleccion: " +
                        lista_protectoras.get(recyclerView.getChildAdapterPosition(view)).getNombre(), Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }

    /**
     * Metodo que permite cargar los animales que pertenecen a la misma comunidad que el usuario.
     */
    public void cargarProtectoras() {
        String token = Preferencias.getToken(getActivity());
        String usuario_id = Preferencias.getID(getActivity());
        //Creamos el JSON que vamos a mandar al servidor
        JSONObject json = new JSONObject();
        try {
            json.put(Tags.TOKEN, token);
            json.put(Tags.USUARIO_ID, usuario_id);
            json.put(Tags.DIRECCION, direccion);
            json.put(Tags.COD_POSTAL, codigo_postal);
            json.put(Tags.PROVINCIA, provincia);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /* Se hace petición de login al servidor. */
        json = JSONUtil.hacerPeticionServidor("protectora/cargar_protectoras/", json);

        try {
            String p = json.getString(Tags.RESULTADO);


            /* Se comprueba la conexión al servidor. */
            if (p.contains(Tags.ERRORCONEXION)) {
//                mensaje = "Error de conexión";
            }
            /* En caso de que conecte y no haya animales para dicha busqueda. */
            else if (p.contains(Tags.OK_SIN_PROTECTORAS)) {
                Toast.makeText(getContext(), ucFirst(json.getString(Tags.MENSAJE)), Toast.LENGTH_LONG).show();
            } else if (p.contains(Tags.OK)) {
                String res = json.getString(Tags.RESULTADO);
                JSONArray array = json.getJSONArray(Tags.LISTA_PROTECTORAS);
                if (array != null) {
                    for (int i = 0; i < array.length(); i++) {
                        Protectora protectora = new Protectora(array.getJSONObject(i));
                        lista_protectoras.add(protectora);
                    }
                }
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