package com.example.jblandii.protectora;

import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.jblandii.protectora.Models.Usuario;
import com.example.jblandii.protectora.peticionesBD.JSONUtil;
import com.example.jblandii.protectora.peticionesBD.Tags;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.jblandii.protectora.Util.ValidatorUtil.validateEmail;

public class RegistrarActivity extends AppCompatActivity {

    TextInputLayout til_login, til_contrasena, til_contrasena_confirmar, til_email, til_email_confirmar;
    TextInputEditText tie_login, tie_contrasena, tie_contrasena_confirmar, tie_email, tie_email_confirmar;
    Button btn_enviar;
    String mensaje = "";

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
    }

    private void cargarBotones() {
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

        btn_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                        if (registrar()) {
                            if (!mensaje.isEmpty()) {
                                Snackbar.make(view, mensaje, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                mensaje = "";
                            }
                        } else {
                            Snackbar.make(view, mensaje, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                            mensaje = "";
                        }
                    }
                }
            }
        });
    }

    /**
     * Registra en el servidor. Crea un JSON..
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
                String msg = json.getString(Tags.MENSAJE);
                mensaje = msg;
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

    private void cargarComunidades() {
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
                JSONArray array = json.getJSONArray(Tags.COMUNIDAD);
                Log.v("comunidad", array.toString());
            } else if (p.contains(Tags.ERROR)) {
                Toast.makeText(getApplicationContext(), json.getString(Tags.MENSAJE), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
