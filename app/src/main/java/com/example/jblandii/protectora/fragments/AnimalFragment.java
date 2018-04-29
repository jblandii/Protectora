package com.example.jblandii.protectora.fragments;


import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.jblandii.protectora.Adaptadores.AdaptadorAnimales;
import com.example.jblandii.protectora.Models.Animal;
import com.example.jblandii.protectora.R;
import com.example.jblandii.protectora.peticionesBD.JSONUtil;
import com.example.jblandii.protectora.peticionesBD.Preferencias;
import com.example.jblandii.protectora.peticionesBD.Tags;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.jblandii.protectora.Util.ValidatorUtil.ucFirst;

public class AnimalFragment extends Fragment {

    FloatingActionButton fab_filtrar_protectora;
    ArrayList<Animal> listaAnimales;
    RecyclerView recyclerView;

    public AnimalFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_animal_fragment, container, false);
        fab_filtrar_protectora = view.findViewById(R.id.fab_filtrar_protectora);
        listaAnimales = new ArrayList<>();
        recyclerView = view.findViewById(R.id.rv_recycleranimales);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        cargarAnimales();

        AdaptadorAnimales adaptadorAnimales = new AdaptadorAnimales(listaAnimales);
        recyclerView.setAdapter(adaptadorAnimales);

        adaptadorAnimales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Seleccion: " +
                        listaAnimales.get(recyclerView.getChildAdapterPosition(view)).getColor(), Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }

    private void llenarListaAnimales() {
        listaAnimales.add(new Animal(1, "Perro", "Bichon Maltes", "Blanco", "2", "Largo", "Hembra", "Pequeño", "2kg",
                "No tiene", "Todas", "Sí", "Adopción", "1", "MG", "Mu bonica", "12-12-12"));
    }

    /**
     * Metodo que permite cargar los animales que pertenecen a la misma comunidad que el usuario.
     */
    public void cargarAnimales() {
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
        json = JSONUtil.hacerPeticionServidor("protectora/cargar_animales/", json);

        try {
            String p = json.getString(Tags.RESULTADO);

            /* Se comprueba la conexión al servidor. */
            if (p.contains(Tags.ERRORCONEXION)) {
//                mensaje = "Error de conexión";
            }
            /* En caso de que conecte y no haya animales para dicha busqueda. */
            else if (p.contains(Tags.OK_SIN_ANIMALES)) {
                Toast.makeText(getContext(), ucFirst(json.getString(Tags.MENSAJE)), Toast.LENGTH_LONG).show();
            } else if (p.contains(Tags.OK)) {
                //Aqui rellena el ararylist del json
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

    @Override
    public void onResume() {
        super.onResume();
    }


//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if(context instanceof Activity){
//            this.activity = (Activity) context;
//            interfaceComunicaFragments = (IComunicaFragments) this.activity;
//        }
//    }
}