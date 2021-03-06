package com.example.jblandii.protectora;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

import static com.example.jblandii.protectora.Util.ValidatorUtil.validateEmail;

public class RegistrarActivity extends AppCompatActivity {
    private ArrayList<Comunidad> comunidades;
    private ArrayList<Provincia> provincias;
    ArrayList<String> lista_comunidades, lista_provincias;
    TextInputLayout til_login, til_contrasena, til_contrasena_confirmar, til_email, til_email_confirmar;
    TextInputEditText tie_login, tie_contrasena, tie_contrasena_confirmar, tie_email, tie_email_confirmar;
    Button btn_enviar;
    String mensaje = "";
    Spinner s_comunidades, s_provincias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        cargarBotones();
        comunidades = new ArrayList<>();
        provincias = new ArrayList<>();
        lista_comunidades = new ArrayList<>();
        lista_provincias = new ArrayList<>();
    }

    /**
     * Metodo que utilizo para cargar los elementos que tengo en el layout.
     */
    private void cargarBotones() {
        s_comunidades = findViewById(R.id.s_comunidades);
        s_provincias = findViewById(R.id.s_provincias);
        btn_enviar = findViewById(R.id.btn_enviar);
        til_login = findViewById(R.id.til_login);
        til_contrasena = findViewById(R.id.til_contrasena);
        til_contrasena_confirmar = findViewById(R.id.til_contrasena_confirmar);
        til_email = findViewById(R.id.til_email);
        til_email_confirmar = findViewById(R.id.til_email_confirmar);
        tie_login = findViewById(R.id.tie_login);
        tie_contrasena = findViewById(R.id.tie_contrasena);
        tie_contrasena_confirmar = findViewById(R.id.tie_contrasena_confirmar);
        tie_email = findViewById(R.id.tie_email);
        tie_email_confirmar = findViewById(R.id.tie_email_confirmar);

        s_comunidades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cargarProvincias(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        s_provincias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (tie_login.getText().toString().isEmpty()) {
                    til_login.setError(getResources().getString(R.string.mensaje_login_vacio));
                } else if (tie_email.getText().toString().isEmpty()) {
                    til_login.setErrorEnabled(false);
                    til_email.setError(getResources().getString(R.string.mensaje_email_vacio));
                } else if (!validateEmail(tie_email.getText().toString())) {
                    til_login.setErrorEnabled(false);
                    til_email.setError(getResources().getString(R.string.mensaje_email_no_formato));
                } else if (tie_email_confirmar.getText().toString().isEmpty()) {
                    til_login.setErrorEnabled(false);
                    til_email.setErrorEnabled(false);
                    til_email_confirmar.setError(getResources().getString(R.string.mensaje_email_confirmar_vacio));
                } else if (!validateEmail(tie_email_confirmar.getText().toString())) {
                    til_login.setErrorEnabled(false);
                    til_email.setErrorEnabled(false);
                    til_email_confirmar.setError(getResources().getString(R.string.mensaje_email_no_formato));
                } else if (tie_contrasena.getText().toString().isEmpty()) {
                    til_login.setErrorEnabled(false);
                    til_email.setErrorEnabled(false);
                    til_email_confirmar.setErrorEnabled(false);
                    til_contrasena.setError(getResources().getString(R.string.mensaje_contrasena_vacia));
                } else if (tie_contrasena_confirmar.getText().toString().isEmpty()) {
                    til_login.setErrorEnabled(false);
                    til_email.setErrorEnabled(false);
                    til_email_confirmar.setErrorEnabled(false);
                    til_contrasena.setErrorEnabled(false);
                    til_contrasena_confirmar.setError(getResources().getString(R.string.mensaje_contrasena_confirmar_vacia));
                } else {
                    til_login.setErrorEnabled(false);
                    til_email.setErrorEnabled(false);
                    til_email_confirmar.setErrorEnabled(false);
                    til_contrasena.setErrorEnabled(false);
                    til_contrasena_confirmar.setErrorEnabled(false);
                    if (!tie_email.getText().toString().equals(tie_email_confirmar.getText().toString())) {
                        Snackbar.make(view, getResources().getString(R.string.mensaje_email_coincidir), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    } else if (!tie_contrasena.getText().toString().equals(tie_contrasena_confirmar.getText().toString())) {
                        Snackbar.make(view, getResources().getString(R.string.mensaje_contrasenas_coincidir), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    } else {
                        AlertDialog alertDialog = new AlertDialog.Builder(RegistrarActivity.this).create();
                        alertDialog.setTitle(getResources().getString(R.string.registrar));
                        alertDialog.setMessage(getResources().getString(R.string.mensaje_advertencia_crear_cuenta));
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getText(R.string.aceptar), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (registrar()) {
                                    if (!mensaje.isEmpty()) {
                                        Snackbar.make(view, mensaje, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                        mensaje = "";
                                    }
                                    Intent intentSecond = getIntent();
                                    intentSecond.putExtra("login", tie_login.getText().toString());
                                    setResult(Activity.RESULT_OK, intentSecond);
                                    finish();
                                } else {
                                    Snackbar.make(view, mensaje, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                    mensaje = "";
                                }
                                dialog.dismiss();
                            }
                        });

                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getText(R.string.cancelar), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alertDialog.show();
                    }
                }
            }
        });
    }

    /**
     * Registra en el servidor un usuario que se lo manda a traves de JSON.
     */
    protected boolean registrar() {
        String usuario = tie_login.getText().toString();
        String contrasena = tie_contrasena.getText().toString();
        String email = tie_email.getText().toString();

        /* Creo el JSON que voy a mandar al servidor. */
        JSONObject json = new JSONObject();
        try {
            json.put(Tags.USUARIO, usuario);
            json.put(Tags.PASSWORD, contrasena);
            json.put(Tags.EMAIL, email);
            Log.v("provincia escogida", s_provincias.getSelectedItem().toString());
            json.put(Tags.PROVINCIA, s_provincias.getSelectedItem().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        json = JSONUtil.hacerPeticionServidor("usuarios/java/registrar_usuario/", json);

        try {
            String p = json.getString(Tags.RESULTADO);

            /* Se comprueba la conexión al servidor. */
            if (p.contains(Tags.ERRORCONEXION)) {
                mensaje = "Error de conexión";
                return false;
            }
            /* En caso de que conecte */
            else if (p.contains(Tags.OK)) {
                mensaje = "";
//                mensaje = "Iniciando sesión";
                setResult(Tags.REGISTRO_OK);
                return true;
            }

            /* Resultado falla por otro error. */
            else if (p.contains(Tags.ERROR)) {
                mensaje = json.getString(Tags.MENSAJE);
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
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
                            android.R.layout.simple_spinner_dropdown_item, lista_comunidades);
                    s_comunidades.setAdapter(adapter);
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
                    s_provincias.setAdapter(adapter);
                }
            } else if (p.contains(Tags.ERROR)) {
                Toast.makeText(getApplicationContext(), json.getString(Tags.MENSAJE), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
