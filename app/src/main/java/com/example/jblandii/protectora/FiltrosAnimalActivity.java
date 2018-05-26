package com.example.jblandii.protectora;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.jblandii.protectora.Models.Comunidad;
import com.example.jblandii.protectora.Models.Provincia;
import com.example.jblandii.protectora.peticionesBD.JSONUtil;
import com.example.jblandii.protectora.peticionesBD.Tags;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FiltrosAnimalActivity extends AppCompatActivity {

    private Spinner s_color, s_tamanio, s_comunidad, s_provincia;
    private Button btn_aplicar_filtros;
    private ArrayList<String> lista_comunidades, lista_provincias;
    private ArrayList<Comunidad> comunidades;
    private ArrayList<Provincia> provincias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtros_animal);
        cargarBotones();
    }

    private void cargarBotones() {
        comunidades = new ArrayList<>();
        provincias = new ArrayList<>();
        lista_comunidades = new ArrayList<>();
        lista_provincias = new ArrayList<>();
        s_color = findViewById(R.id.s_color);
        s_tamanio = findViewById(R.id.s_tamanio);
        s_comunidad = findViewById(R.id.s_comunidad);
        s_provincia = findViewById(R.id.s_provincia);
        btn_aplicar_filtros = findViewById(R.id.btn_aplicar_filtros);
        s_comunidad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cargarProvincias(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        s_provincia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    /**
     * Metodo que permite cargar las comunidades que estan en la base de datos, recibiendolas a través de JSON
     */
    private void cargarComunidades() {
        comunidades.clear();
        JSONObject json = new JSONObject();
        try {
            json.put(Tags.TOKENFINGIDO, Tags.TOKENFINGIDOGENERADO);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        json = JSONUtil.hacerPeticionServidor("comunidad/comunidades/", json);
        try {
            String p = json.getString(Tags.RESULTADO);
            /* Se comprueba la conexión al servidor. */
            if (p.contains(Tags.ERRORCONEXION)) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_conexion), Toast.LENGTH_SHORT).show();

            }
            /* En caso de que conecte */
            else if (p.contains(Tags.OK)) {
                String res = json.getString(Tags.RESULTADO);
                JSONArray array = json.getJSONArray(Tags.COMUNIDADES);
                Log.v("comunidad", array.toString());
                if (array != null) {
                    for (int i = 0; i < array.length(); i++) {
                        Comunidad comunidad = new Comunidad(array.getJSONObject(i));
                        comunidades.add(comunidad);
                        lista_comunidades.add(comunidad.getComunidad_autonoma());
                    }
                    ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this,
                            android.R.layout.simple_spinner_item, lista_comunidades);
                    s_comunidad.setAdapter(adapter);
                }
            } else if (p.contains(Tags.ERROR)) {
                Toast.makeText(getApplicationContext(), json.getString(Tags.MENSAJE), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo que permite cargar las provincias que pertenecen a la comunidad autonoma seleccionada, que estan en la base de datos, recibiendolas a través de JSON
     *
     * @param posicion recibe la posición del array list de comunidades para pasarselo al servidor, saber de que comunidad se trata, y obtener las provincias
     *                 que pertencen a la comunidad autonoma seleccionada
     */
    private void cargarProvincias(int posicion) {
        lista_provincias.clear();
        provincias.clear();
        JSONObject json = new JSONObject();
        try {
            json.put(Tags.TOKENFINGIDO, Tags.TOKENFINGIDOGENERADO);
            json.put(Tags.ID_COMUNIDAD, comunidades.get(posicion).getPk());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        json = JSONUtil.hacerPeticionServidor("comunidad/provincias/", json);
        try {
            String p = json.getString(Tags.RESULTADO);
            /* Se comprueba la conexión al servidor. */
            if (p.contains(Tags.ERRORCONEXION)) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_conexion), Toast.LENGTH_SHORT).show();

            }
            /* En caso de que conecte */
            else if (p.contains(Tags.OK)) {
                String res = json.getString(Tags.RESULTADO);
                JSONArray array = json.getJSONArray(Tags.PROVINCIAS);
                Log.v("provincia", array.toString());
                if (array != null) {
                    for (int i = 0; i < array.length(); i++) {
                        Provincia provincia = new Provincia(array.getJSONObject(i));
                        provincias.add(provincia);
                        lista_provincias.add(provincia.getProvincia());
                    }
                    ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this,
                            android.R.layout.simple_spinner_item, lista_provincias);
                    s_provincia.setAdapter(adapter);
                }
            } else if (p.contains(Tags.ERROR)) {
                Toast.makeText(getApplicationContext(), json.getString(Tags.MENSAJE), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarComunidades();
    }
}
