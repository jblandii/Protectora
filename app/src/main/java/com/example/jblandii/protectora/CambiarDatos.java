package com.example.jblandii.protectora;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
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
import com.example.jblandii.protectora.Models.Usuario;
import com.example.jblandii.protectora.peticionesBD.JSONUtil;
import com.example.jblandii.protectora.peticionesBD.Preferencias;
import com.example.jblandii.protectora.peticionesBD.Tags;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CambiarDatos extends AppCompatActivity {
    private ArrayList<Comunidad> comunidades;
    private ArrayList<Provincia> provincias;
    ArrayList<String> lista_comunidades, lista_provincias;
    Usuario usuario;
    Button btn_actualizar_datos;
    TextInputLayout til_nombre, til_apellidos, til_direccion, til_codigo_postal, til_telefono;
    TextInputEditText tie_nombre, tie_apellidos, tie_direccion, tie_codigo_postal, tie_telefono;
    Spinner s_provincias, s_comunidades;
    Boolean primera_carga = true;
    int comunidad_usuario, provincia_usuario;
    String mensaje = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_datos);
        comunidades = new ArrayList<>();
        provincias = new ArrayList<>();
        lista_comunidades = new ArrayList<>();
        lista_provincias = new ArrayList<>();
        cargarBotones();
        asignarValores();
    }

    private boolean recogerDatosUsuario() {
        try {
            usuario = getIntent().getExtras().getParcelable("usuario");
            return true;
        } catch (Exception e) {
            Log.v("Exception", e.toString());
            return false;
        }
    }

    private void cargarBotones() {
        s_provincias = findViewById(R.id.s_provincias);
        s_comunidades = findViewById(R.id.s_comunidades);
        til_nombre = findViewById(R.id.til_nombre);
        til_apellidos = findViewById(R.id.til_apellidos);
        til_direccion = findViewById(R.id.til_direccion);
        til_codigo_postal = findViewById(R.id.til_codigo_postal);
        til_telefono = findViewById(R.id.til_telefono);
        tie_apellidos = findViewById(R.id.tie_apellidos);
        tie_nombre = findViewById(R.id.tie_nombre);
        tie_direccion = findViewById(R.id.tie_direccion);
        tie_codigo_postal = findViewById(R.id.tie_codigo_postal);
        tie_telefono = findViewById(R.id.tie_telefono);
        btn_actualizar_datos = findViewById(R.id.btn_actualizar_datos);
        btn_actualizar_datos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tie_nombre.getText().toString().isEmpty()) {
                    til_nombre.setError(getResources().getString(R.string.mensaje_nombre_vacio));
                } else if (tie_apellidos.getText().toString().isEmpty()) {
                    til_nombre.setErrorEnabled(false);
                    til_apellidos.setError(getResources().getString(R.string.mensaje_apellidos_vacio));
                } else if (tie_direccion.getText().toString().isEmpty()) {
                    til_nombre.setErrorEnabled(false);
                    til_apellidos.setErrorEnabled(false);
                    til_direccion.setError(getResources().getString(R.string.mensaje_direccion_vacio));
                } else if (tie_codigo_postal.getText().toString().isEmpty()) {
                    til_nombre.setErrorEnabled(false);
                    til_apellidos.setErrorEnabled(false);
                    til_direccion.setErrorEnabled(false);
                    til_codigo_postal.setError(getResources().getString(R.string.mensaje_cp_vacio));
                } else if (tie_telefono.getText().toString().isEmpty()) {
                    til_nombre.setErrorEnabled(false);
                    til_apellidos.setErrorEnabled(false);
                    til_direccion.setErrorEnabled(false);
                    til_codigo_postal.setErrorEnabled(false);
                    til_telefono.setError(getResources().getString(R.string.mensaje_tlfn_vacio));
                } else {
                    if (actualizarDatos()) {
                        Intent intentmain = getIntent();
                        usuario.setNombre(tie_nombre.getText().toString());
                        usuario.setApellidos(tie_apellidos.getText().toString());
                        usuario.setDireccion(tie_direccion.getText().toString());
                        usuario.setDireccion(tie_codigo_postal.getText().toString());
                        usuario.setTelefono(tie_telefono.getText().toString());
                        usuario.setProvincia(s_provincias.getSelectedItem().toString());
                        intentmain.putExtra("usuario", usuario);
                        setResult(Activity.RESULT_OK, intentmain);
                        finish();
                    } else {
                        Snackbar.make(v, mensaje, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        mensaje = "";
                    }
                }
            }
        });

        s_comunidades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (primera_carga) {
                    for (int i = 0; i < comunidades.size(); i++) {
                        if (comunidades.get(i).getPk() == comunidad_usuario) {
                            s_comunidades.setSelection(i);
                            cargarProvincias(i);
                            Log.v("este", i + comunidades.get(i).getComunidad_autonoma());
                        }
                    }
                } else {
                    cargarProvincias(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                if (primera_carga) {
                    for (int i = 0; i < comunidades.size(); i++) {
                        if (comunidades.get(i).getPk() == comunidad_usuario) {
                            s_comunidades.setSelection(i);
                            cargarProvincias(i);
                            Log.v("este", i + comunidades.get(i).getComunidad_autonoma());
                        }
                    }
                }
            }
        });

        s_provincias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (primera_carga) {
                    for (int i = 0; i < provincias.size(); i++) {
                        if (provincias.get(i).getPk() == provincia_usuario) {
                            s_provincias.setSelection(i);
                        }
                    }
                    primera_carga = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void asignarValores() {
        if (recogerDatosUsuario()) {
            tie_nombre.setText(usuario.getNombre());
            tie_apellidos.setText(usuario.getApellidos());
            tie_telefono.setText(usuario.getTelefono());
            tie_direccion.setText(usuario.getDireccion());
            tie_codigo_postal.setText(usuario.getCodigo_postal());
        } else {
            Intent intentmain = getIntent();
            setResult(Activity.RESULT_CANCELED, intentmain);
            Toast.makeText(CambiarDatos.this, getResources().getString(R.string.ocurrido_error), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarComunidades();
    }

    /**
     * Metodo que permite cargar las comunidades que estan en la base de datos, recibiendolas a través de JSON
     */
    private void cargarComunidades() {
        comunidades.clear();
        JSONObject json = new JSONObject();
        try {
            json.put(Tags.TOKENFINGIDO, Tags.TOKENFINGIDOGENERADO);
            json.put(Tags.USUARIO_ID, Preferencias.getID(CambiarDatos.this));
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
                    s_comunidades.setAdapter(adapter);
                    comunidad_usuario = json.getInt(Tags.COMUNIDAD_USUARIO);
                    Log.v("comunidad_usuario", comunidad_usuario + "");
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
            json.put(Tags.USUARIO_ID, Preferencias.getID(CambiarDatos.this));
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
                JSONArray array = json.getJSONArray(Tags.PROVINCIAS);
                Log.v("provincia", array.toString());
                if (array != null) {
                    for (int i = 0; i < array.length(); i++) {
                        Provincia provincia = new Provincia(array.getJSONObject(i));
                        provincias.add(provincia);
                        lista_provincias.add(provincia.getProvincia());
                        Log.v("esteprovincia", provincia.getProvincia() + provincia.getPk());
                    }
                    ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this,
                            android.R.layout.simple_spinner_dropdown_item, lista_provincias);
                    s_provincias.setAdapter(adapter);
                    provincia_usuario = json.getInt(Tags.PROVINCIA_USUARIO);
                    Log.v("esteprovincia_usuario", provincia_usuario + "");
                }
            } else if (p.contains(Tags.ERROR)) {
                Toast.makeText(getApplicationContext(), json.getString(Tags.MENSAJE), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean actualizarDatos() {
        //Creamos el JSON que vamos a mandar al servidor
        JSONObject json = new JSONObject();
        try {
            json.put(Tags.NOMBRE, tie_nombre.getText().toString());
            json.put(Tags.APELLIDOS, tie_apellidos.getText().toString());
            json.put(Tags.DIRECCION, tie_direccion.getText().toString());
            json.put(Tags.PROVINCIA, s_provincias.getSelectedItem().toString());
            json.put(Tags.TELEFONO, tie_telefono.getText().toString());
            json.put(Tags.COD_POSTAL, tie_codigo_postal.getText().toString());
            json.put(Tags.TOKEN, Preferencias.getToken(CambiarDatos.this));
            json.put(Tags.USUARIO_ID, Preferencias.getID(CambiarDatos.this));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /* Se hace petición de login al servidor. */
        json = JSONUtil.hacerPeticionServidor("usuarios/java/cambiar_datos/", json);

        try {
            String p = json.getString(Tags.RESULTADO);

            /* Se comprueba la conexión al servidor. */
            if (p.contains(Tags.ERRORCONEXION)) {
                mensaje = getResources().getString(R.string.error_conexion);
                return false;
            }
            /* En caso de que conecte */
            else if (p.contains(Tags.OK)) {
                return true;
            }

            /* Resultado falla por otro error. */
            else if (p.contains(Tags.ERROR)) {
                String msg = json.getString(Tags.MENSAJE);
                mensaje = msg;
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
}
