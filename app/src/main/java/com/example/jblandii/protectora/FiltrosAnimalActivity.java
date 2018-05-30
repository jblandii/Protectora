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

public class FiltrosAnimalActivity extends AppCompatActivity {

    private Spinner s_color, s_tamanio, s_comunidad, s_provincia;
    private ArrayList<String> lista_comunidades, lista_provincias;
    private ArrayList<Comunidad> comunidades;
    ArrayList<Provincia> provincias;
    ArrayList<String> listaDeCodigos;
    ArrayList<Animal> listaAnimales;
    Button btn_aplicar_filtros;
    RadioGroup rg_animal, rg_pelaje, rg_sexo, rg_chip, rg_estado;
    String mensaje = "", estado = "", animal = "", tamanio = "", provincia = "", color = "", sexo = "", pelaje = "", chip = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtros_animal);
        cargarBotones();
    }

    private void cargarBotones() {
        listaDeCodigos = new ArrayList<>();
        listaAnimales = new ArrayList<>();
        comunidades = new ArrayList<>();
        provincias = new ArrayList<>();
        lista_comunidades = new ArrayList<>();
        lista_provincias = new ArrayList<>();

        rg_animal = findViewById(R.id.rg_animal);
        rg_animal.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_gato:
                        animal = "Gato";
                        break;
                    case R.id.rb_perro:
                        animal = "Perro";
                        break;
                    case R.id.rb_todos:
                        animal = "";
                        break;
                }
            }
        });

        rg_pelaje = findViewById(R.id.rg_pelaje);
        rg_pelaje.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_corto:
                        pelaje = "Corto";
                        break;
                    case R.id.rb_largo:
                        pelaje = "Largo";
                        break;
                    case R.id.rb_todos2:
                        pelaje = "";
                        break;
                }
            }
        });

        rg_sexo = findViewById(R.id.rg_sexo);
        rg_sexo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_macho:
                        sexo = "Macho";
                        break;
                    case R.id.rb_hembra:
                        sexo = "Hembra";
                        break;
                    case R.id.rb_todos3:
                        sexo = "";
                        break;
                }
            }
        });

        rg_chip = findViewById(R.id.rg_chip);
        rg_chip.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_si:
                        chip = "Si";
                        break;
                    case R.id.rb_no:
                        chip = "No";
                        break;
                    case R.id.rb_todos4:
                        chip = "";
                        break;
                }
            }
        });

        rg_estado = findViewById(R.id.rg_estado);
        rg_estado.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_acogida:
                        estado = "Acogida";
                        break;
                    case R.id.rb_adopcion:
                        estado = "Adopcion";
                        break;
                    case R.id.rb_todos5:
                        estado = "";
                        break;
                }
            }
        });

        btn_aplicar_filtros = findViewById(R.id.btn_aplicar_filtros);
        btn_aplicar_filtros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recogerDatos();
                if (cargarAnimales()) {
                    Bundle args = new Bundle();
                    AnimalFragment animalFragment = new AnimalFragment();
                    Intent intentFiltros = getIntent();
                    animalFragment.setArguments(args);
                    intentFiltros.putParcelableArrayListExtra("animales", listaAnimales);
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

        s_color = findViewById(R.id.s_color);
        ArrayAdapter spinerColoresAdapter = ArrayAdapter.createFromResource(this, R.array.colores, android.R.layout.simple_spinner_dropdown_item);
        s_color.setAdapter(spinerColoresAdapter);

        s_tamanio = findViewById(R.id.s_tamanio);
        ArrayAdapter spinerTamaniosAdapter = ArrayAdapter.createFromResource(this, R.array.tamanios, android.R.layout.simple_spinner_dropdown_item);
        s_tamanio.setAdapter(spinerTamaniosAdapter);
    }

    private void recogerDatos() {
        color = (s_color.getSelectedItem().toString().equals("Todos")) ? "" : s_color.getSelectedItem().toString();
        if (s_color.getSelectedItem().toString().equals("Marrón")) {
            color = "Marron";
        }
        tamanio = (s_tamanio.getSelectedItem().toString().equals("Todos")) ? "" : s_tamanio.getSelectedItem().toString();
        provincia = s_provincia.getSelectedItem().toString();
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

    public Boolean cargarAnimales() {
        String token = Preferencias.getToken(FiltrosAnimalActivity.this);
        String usuario_id = Preferencias.getID(FiltrosAnimalActivity.this);
        //Creamos el JSON que vamos a mandar al servidor
        JSONObject json = new JSONObject();
        try {
            json.put(Tags.TOKEN, token);
            json.put(Tags.USUARIO_ID, usuario_id);
            json.put(Tags.MASCOTA, animal);
            json.put(Tags.COLOR, color);
            json.put(Tags.PELAJE, pelaje);
            json.put(Tags.SEXO, sexo);
            json.put(Tags.TAMANO, tamanio);
            json.put(Tags.CHIP, chip);
            json.put(Tags.ESTADO, estado);
            json.put(Tags.PROVINCIA, provincia);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /* Se hace petición de login al servidor. */
        json = JSONUtil.hacerPeticionServidor("protectora/cargar_animales/", json);

        try {
            String p = json.getString(Tags.RESULTADO);

            /* Se comprueba la conexión al servidor. */
            if (p.contains(Tags.ERRORCONEXION)) {
                mensaje = getResources().getString(R.string.error_conexion);
                return false;
            }
            /* En caso de que conecte y no haya animales para dicha busqueda. */
            else if (p.contains(Tags.OK_SIN_ANIMALES)) {
//                Toast.makeText(FiltrosAnimalActivity.this, ucFirst(json.getString(Tags.MENSAJE)), Toast.LENGTH_LONG).show();
                mensaje = ucFirst(json.getString(Tags.MENSAJE));
                return false;
            } else if (p.contains(Tags.OK)) {
                String res = json.getString(Tags.RESULTADO);
                JSONArray array = json.getJSONArray(Tags.LISTA_ANIMALES);
                if (array != null) {
                    for (int i = 0; i < array.length(); i++) {
                        Animal animal = new Animal(array.getJSONObject(i));
                        Log.v("animalesjson", animal.toString());
                        listaAnimales.add(animal);
                        listaDeCodigos.add(String.valueOf(animal.getPk()));
                    }
                }
                return true;
//                Toast.makeText(FiltrosAnimalActivity.this, listaAnimales.toString(), Toast.LENGTH_LONG).show();
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
