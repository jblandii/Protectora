package com.example.jblandii.protectora;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jblandii.protectora.Models.Usuario;
import com.example.jblandii.protectora.R;
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
        setContentView(R.layout.activity_login2);
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
                if (login()) {
                    Snackbar.make(view, mensaje, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    mensaje = "";
                    finish();
                } else {
                    Snackbar.make(view, mensaje, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    mensaje = "";
                }
            }
        });

        btn_registrar = findViewById(R.id.btn_registrar);
        btn_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        tv_recordar_contrasena = findViewById(R.id.tv_recordar_contrasena);
        tv_recordar_contrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentRecordarContrasena = new Intent(LoginActivity.this, RecordarContrasenaActivity.class);
                startActivity(intentRecordarContrasena);
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
        json = JSONUtil.hacerPeticionServidor("usuarios/java/login/", json); //En contacto activity, es sesiones/java/getcentros

        Log.v("login", json.toString());
        try {
            String p = json.getString(Tags.RESULTADO);

            /* Se comprueba la conexión al servidor. */
            if (p.contains(Tags.ERRORCONEXION)) {
//                Toast.makeText(getApplicationContext(), "Error de conexión", Toast.LENGTH_LONG).show(); //Si no conecta imprime un toast
                mensaje = "Error de conexión";
                return false;
            }
            /* En caso de que conecte */
            else if (p.contains(Tags.OK)) {
                /* Guarda en las preferencias el token. */
                Usuario.guardarEnPref(this, usuario, json.getString(Tags.TOKEN));
                mensaje = "Iniciando sesión";
                setResult(Tags.LOGIN_OK);
                Log.v("Entrada", "No es la 1º");
                return true;
            }

            /* Resultado falla por otro error. */
            else if (p.contains(Tags.ERROR)) {
                String msg = json.getString(Tags.MENSAJE);
//                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                mensaje = msg;
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
}
