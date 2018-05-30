package com.example.jblandii.protectora.fragments;


import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.jblandii.protectora.Adaptadores.AdaptadorAnimales;
import com.example.jblandii.protectora.FiltrosAnimalActivity;
import com.example.jblandii.protectora.Models.Animal;
import com.example.jblandii.protectora.R;
import com.example.jblandii.protectora.peticionesBD.JSONUtil;
import com.example.jblandii.protectora.peticionesBD.Preferencias;
import com.example.jblandii.protectora.peticionesBD.Tags;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static com.example.jblandii.protectora.Util.ValidatorUtil.ucFirst;

public class AnimalFragment extends Fragment {

    FloatingActionButton fab_filtrar_protectora;
    private ArrayList<Animal> listaAnimales;
    private ArrayList<Animal> listaAnimalesNueva;
    ArrayList<String> listaDeCodigos;
    RecyclerView recyclerView;
    private String mascota = "", raza = "", color = "", edad = "", pelaje = "", sexo = "", tamano = "",
            peso = "", chip = "", id_protectora = "", fecha = "", estado = "";

    public AnimalFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_animal_fragment, container, false);
        fab_filtrar_protectora = view.findViewById(R.id.fab_filtrar_animal);
        fab_filtrar_protectora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentFilter = new Intent(getContext(), FiltrosAnimalActivity.class);
                startActivityForResult(intentFilter, Tags.FILTRO_ANIMAL);
            }
        });
        listaAnimales = new ArrayList<>();
        listaAnimalesNueva = new ArrayList<>();
        listaDeCodigos = new ArrayList<>();
        recyclerView = view.findViewById(R.id.rv_recycler_animales);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        cargarAnimales();

        AdaptadorAnimales adaptadorAnimales = new AdaptadorAnimales(listaAnimales, getContext());
        recyclerView.setAdapter(adaptadorAnimales);

        return view;
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
            json.put(Tags.MASCOTA, mascota);
            json.put(Tags.RAZA, raza);
            json.put(Tags.COLOR, color);
            json.put(Tags.EDAD, edad);
            json.put(Tags.PELAJE, pelaje);
            json.put(Tags.SEXO, sexo);
            json.put(Tags.TAMANO, tamano);
            json.put(Tags.PESO, peso);
            json.put(Tags.CHIP, chip);
            json.put(Tags.ID_PROTECTORA, id_protectora);
            json.put(Tags.FECHA, fecha);
            json.put(Tags.ESTADO, estado);
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
                String res = json.getString(Tags.RESULTADO);
                JSONArray array = json.getJSONArray(Tags.LISTA_ANIMALES);
                Log.v("animalesjson", array.toString());
                if (array != null) {
                    for (int i = 0; i < array.length(); i++) {
                        Animal animal = new Animal(array.getJSONObject(i));
                        Log.v("animalesbucle", animal.toString());
                        listaAnimales.add(animal);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("FragmentA.java", "onActivityResult called");
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Tags.FILTRO_ANIMAL:
                    if (data != null) {
                        listaAnimalesNueva.clear();
                        Log.v("estecreateview", data.getExtras().toString());
                        listaDeCodigos = data.getStringArrayListExtra("codigos");
                        for (int i = 0; i < listaDeCodigos.size(); i++) {
                            for (int j = 0; j < listaAnimales.size(); j++) {
                                if (listaDeCodigos.get(i).equals(String.valueOf(listaAnimales.get(j).getPk()))) {
                                    listaAnimalesNueva.add(listaAnimales.get(j));
                                }
                            }
                        }
                        AdaptadorAnimales adaptadorAnimales = new AdaptadorAnimales(listaAnimalesNueva, getContext());
                        recyclerView.setAdapter(adaptadorAnimales);
                        Log.v("estecreateview", listaDeCodigos + " - " + color + " - " + pelaje + " - " + sexo + " - " + sexo + " - " + tamano + " - " + chip + " - " + fecha + " - " + estado);
                    }

                    Log.v("onActivityResult", "onActivityResult");

                    break;
                default:
                    super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
}