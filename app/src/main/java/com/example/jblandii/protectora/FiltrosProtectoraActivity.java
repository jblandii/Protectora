package com.example.jblandii.protectora;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.jblandii.protectora.Models.Animal;
import com.example.jblandii.protectora.Models.Comunidad;
import com.example.jblandii.protectora.Models.Protectora;
import com.example.jblandii.protectora.Models.Provincia;
import com.example.jblandii.protectora.fragments.AnimalFragment;
import com.example.jblandii.protectora.peticionesBD.JSONUtil;
import com.example.jblandii.protectora.peticionesBD.Preferencias;
import com.example.jblandii.protectora.peticionesBD.Tags;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.jblandii.protectora.Util.ValidatorUtil.ucFirst;

public class FiltrosProtectoraActivity extends AppCompatActivity {

    private ArrayList<String> lista_comunidades, lista_provincias;
    private Spinner s_comunidad, s_provincia;
    private ArrayList<Comunidad> comunidades;
    ArrayList<Provincia> provincias;
    ArrayList<Protectora> listaProtectoras;
    Button btn_aplicar_filtros;
    String mensaje = "", provincia = "", comunidad = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtros_protectora);
        cargarBotones();
    }

    /**
     * Metodo que se utiliza para cargar los elementos del layout.
     */
    private void cargarBotones() {
        listaProtectoras = new ArrayList<>();
        comunidades = new ArrayList<>();
        provincias = new ArrayList<>();
        lista_comunidades = new ArrayList<>();
        lista_provincias = new ArrayList<>();

        btn_aplicar_filtros = findViewById(R.id.btn_aplicar_filtros);
        btn_aplicar_filtros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recogerDatos();
                if (cargarProtectoras()) {
                    Bundle args = new Bundle();
                    AnimalFragment animalFragment = new AnimalFragment();
                    Intent intentFiltros = getIntent();
                    animalFragment.setArguments(args);
                    intentFiltros.putParcelableArrayListExtra("protectoras", listaProtectoras);
                    setResult(Activity.RESULT_OK, intentFiltros);
                    finish();
                } else {
                    Snackbar.make(v, mensaje, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    mensaje = "";
                }
            }
        });

        s_comunidad = findViewById(R.id.s_comunidad);
        s_comunidad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cargarProvincias(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        s_provincia = findViewById(R.id.s_provincia);
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
     * Metodo que se utiliza para recoger los datos de los spinner.
     */
    private void recogerDatos() {
        comunidad = s_comunidad.getSelectedItem().toString();
        provincia = s_provincia.getSelectedItem().toString();
        if (s_provincia.getSelectedItem().toString().equals("Todas")) {
            provincia = "";
        }
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
                    ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, lista_comunidades);
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
                    if (array.length() > 1) {
                        lista_provincias.add("Todas");
                        provincias.add(new Provincia(-1, "Todas"));
                    }
                    for (int i = 0; i < array.length(); i++) {
                        Provincia provincia = new Provincia(array.getJSONObject(i));
                        provincias.add(provincia);
                        lista_provincias.add(provincia.getProvincia());
                    }
                    ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, lista_provincias);

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

    /**
     * Metodo que se utiliza para cargar las protectoras filtradas.
     * @return
     */
    public Boolean cargarProtectoras() {
        String token = Preferencias.getToken(FiltrosProtectoraActivity.this);
        String usuario_id = Preferencias.getID(FiltrosProtectoraActivity.this);
        //Creamos el JSON que vamos a mandar al servidor
        JSONObject json = new JSONObject();
        try {
            json.put(Tags.TOKEN, token);
            json.put(Tags.USUARIO_ID, usuario_id);
            json.put(Tags.PROVINCIA, provincia);
            json.put(Tags.COMUNIDAD_AUTONOMA, comunidad);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /* Se hace petición de login al servidor. */
        json = JSONUtil.hacerPeticionServidor("protectora/cargar_protectoras/", json);

        try {
            String p = json.getString(Tags.RESULTADO);

            /* Se comprueba la conexión al servidor. */
            if (p.contains(Tags.ERRORCONEXION)) {
                mensaje = getResources().getString(R.string.error_conexion);
                return false;
            }
            /* En caso de que conecte y no haya animales para dicha busqueda. */
            else if (p.contains(Tags.OK_SIN_PROTECTORAS)) {
//                Toast.makeText(FiltrosAnimalActivity.this, ucFirst(json.getString(Tags.MENSAJE)), Toast.LENGTH_LONG).show();
                mensaje = ucFirst(json.getString(Tags.MENSAJE));
                return false;
            } else if (p.contains(Tags.OK)) {
                String res = json.getString(Tags.RESULTADO);
                JSONArray array = json.getJSONArray(Tags.LISTA_PROTECTORAS);
                if (array != null) {
                    for (int i = 0; i < array.length(); i++) {
                        Protectora protectora = new Protectora(array.getJSONObject(i));
                        listaProtectoras.add(protectora);
                    }
                }
                return true;
            }
            /* Resultado falla por otro error. */
            else if (p.contains(Tags.ERROR)) {
                mensaje = json.getString(Tags.MENSAJE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
}
