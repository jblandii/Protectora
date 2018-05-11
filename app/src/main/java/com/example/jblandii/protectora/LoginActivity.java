package com.example.jblandii.protectora;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.jblandii.protectora.Models.Usuario;
import com.example.jblandii.protectora.peticionesBD.JSONUtil;
import com.example.jblandii.protectora.peticionesBD.Tags;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    String mensaje;
    Button btn_iniciar_sesion, btn_registrar;
    TextView tv_recordar_contrasena;
    TextInputLayout til_login, til_contrasena;
    TextInputEditText tie_login, tie_contrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        cargarBotones();
    }

    /**
     * Metodo que utilizo para cargar los botones que tengo en el layout.
     */
    private void cargarBotones() {
        til_login = findViewById(R.id.til_login);
        til_contrasena = findViewById(R.id.til_contrasena);
        tie_login = findViewById(R.id.tie_login);
        tie_contrasena = findViewById(R.id.tie_contrasena);
        btn_iniciar_sesion = findViewById(R.id.btn_iniciar_sesion);
        btn_iniciar_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(tie_login.getWindowToken(), 0);
                inputMethodManager.hideSoftInputFromWindow(tie_contrasena.getWindowToken(), 0);
                if (tie_login.getText().toString().isEmpty()) {
                    til_login.setError(getResources().getString(R.string.mensaje_login_vacio));
                } else if (tie_contrasena.getText().toString().isEmpty()) {
                    til_login.setErrorEnabled(false);
                    til_contrasena.setError(getResources().getString(R.string.mensaje_contrasena_vacia));
                } else {
                    til_login.setErrorEnabled(false);
                    til_contrasena.setErrorEnabled(false);
                    if (login()) {
                        if (!mensaje.isEmpty()) {
                            Snackbar.make(view, mensaje, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                            mensaje = "";
                        }
                        Intent intentmain = getIntent();
                        setResult(Activity.RESULT_OK, intentmain);
                        finish();
                    } else {
                        Snackbar.make(view, mensaje, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        mensaje = "";
                    }
                }
            }
        });

        btn_registrar = findViewById(R.id.btn_registrar);
        btn_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentRegistrar = new Intent(LoginActivity.this, RegistrarActivity.class);
                startActivityForResult(intentRegistrar, Tags.REGISTRO);
            }
        });

        tv_recordar_contrasena = findViewById(R.id.tv_recordar_contrasena);
        tv_recordar_contrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentRecordarContrasena = new Intent(LoginActivity.this, RecordarContrasenaActivity.class);
//                startActivity(intentRecordarContrasena);
                startActivityForResult(intentRecordarContrasena, Tags.RECORDAR);
            }
        });

    }

    /**
     * Configuro que al pulsar el botón de atras se salga de la aplicación pero que siga funcionando en 2º plano.
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        moveTaskToBack(true);
        startActivity(intent);
    }

    /**
     * Logea en el servidor. Crea un JSON y confirma si la pass es correcta.
     */
    protected boolean login() {
        String usuario = tie_login.getText().toString();
        String contrasena = tie_contrasena.getText().toString();

        //Creamos el JSON que vamos a mandar al servidor
        JSONObject json = new JSONObject();
        try {
            json.put(Tags.USUARIO, usuario);
            json.put(Tags.PASSWORD, contrasena);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /* Se hace petición de login al servidor. */
        json = JSONUtil.hacerPeticionServidor("usuarios/java/login/", json);

        try {
            String p = json.getString(Tags.RESULTADO);

            /* Se comprueba la conexión al servidor. */
            if (p.contains(Tags.ERRORCONEXION)) {
                mensaje = "Error de conexión";
                return false;
            }
            /* En caso de que conecte */
            else if (p.contains(Tags.OK)) {
                /* Guarda en las preferencias el token. */
                Usuario.guardarEnPref(this, usuario, json.getString(Tags.TOKEN));
                mensaje = "";
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Tags.REGISTRO:
                    tie_login.setText(data.getStringExtra("login"));
                    break;
                case Tags.RECORDAR:
                    tie_login.setText(data.getStringExtra("login"));
                    break;
                default:
                    super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
}
