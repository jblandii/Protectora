package com.example.jblandii.protectora;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.example.jblandii.protectora.peticionesBD.JSONUtil;
import com.example.jblandii.protectora.peticionesBD.Preferencias;
import com.example.jblandii.protectora.peticionesBD.Tags;

import org.json.JSONException;
import org.json.JSONObject;

public class CambiarContrasena extends AppCompatActivity {
    TextInputEditText tie_contrasena_actual, tie_contrasena_nueva, tie_contrasena_nueva_repetir;
    TextInputLayout til_contrasena_actual, til_contrasena_nueva, til_contrasena_nueva_repetir;
    Button btn_cambiar_contrasena;
    String mensaje = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_contrasena);
        cargarBotones();
    }

    /**
     * Metodo que se utiliza para cargar todos los elementos del layout.
     */
    private void cargarBotones() {
        btn_cambiar_contrasena = findViewById(R.id.btn_cambiar);
        til_contrasena_actual = findViewById(R.id.til_contrasena_actual);
        til_contrasena_nueva = findViewById(R.id.til_contrasena_nueva);
        til_contrasena_nueva_repetir = findViewById(R.id.til_contrasena_nueva_repetir);
        tie_contrasena_actual = findViewById(R.id.tie_contrasena_actual);
        tie_contrasena_nueva = findViewById(R.id.tie_contrasena_nueva);
        tie_contrasena_nueva_repetir = findViewById(R.id.tie_contrasena_nueva_repetir);

        btn_cambiar_contrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (tie_contrasena_actual.getText().toString().isEmpty()) {
                    til_contrasena_actual.setError(getResources().getString(R.string.mensaje_contrasena_vacia));
                } else if (tie_contrasena_nueva.getText().toString().isEmpty()) {
                    til_contrasena_actual.setErrorEnabled(false);
                    til_contrasena_nueva.setError(getResources().getString(R.string.mensaje_contrasena_vacia));
                } else if (tie_contrasena_nueva_repetir.getText().toString().isEmpty()) {
                    til_contrasena_actual.setErrorEnabled(false);
                    til_contrasena_nueva.setErrorEnabled(false);
                    til_contrasena_nueva_repetir.setError(getResources().getString(R.string.mensaje_contrasena_confirmar_vacia));
                } else if (!tie_contrasena_nueva.getText().toString().equals(tie_contrasena_nueva_repetir.getText().toString())) {
                    til_contrasena_actual.setErrorEnabled(false);
                    til_contrasena_nueva.setErrorEnabled(false);
                    til_contrasena_nueva_repetir.setErrorEnabled(false);
                    Snackbar.make(v, getResources().getString(R.string.mensaje_contrasenas_coincidir), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                } else {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(tie_contrasena_actual.getWindowToken(), 0);
                    inputMethodManager.hideSoftInputFromWindow(tie_contrasena_nueva.getWindowToken(), 0);
                    inputMethodManager.hideSoftInputFromWindow(tie_contrasena_nueva_repetir.getWindowToken(), 0);

                    AlertDialog alertDialog = new AlertDialog.Builder(CambiarContrasena.this).create();
                    alertDialog.setTitle(getResources().getString(R.string.cambiar_contrasena));
                    alertDialog.setMessage(getResources().getString(R.string.mensaje_advertencia_cambiar_contrasena));
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getText(R.string.aceptar), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (cambiarContrasena()) {
                                if (!mensaje.isEmpty()) {
                                    Intent intentmain = getIntent();
                                    setResult(Activity.RESULT_OK, intentmain);
                                    finish();
                                }
                            } else {
                                Snackbar.make(v, mensaje, Snackbar.LENGTH_LONG).setAction("Action", null).show();
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
        });
    }

    /**
     * Metodo que se utiliza para cambiar la contraseña en el servidor.
     * @return
     */
    private boolean cambiarContrasena() {
        String contrasena_antigua = tie_contrasena_actual.getText().toString();
        String contrasena_nueva = tie_contrasena_nueva.getText().toString();

        //Creamos el JSON que vamos a mandar al servidor
        JSONObject json = new JSONObject();
        try {
            json.put(Tags.USUARIO_ID, Preferencias.getID(CambiarContrasena.this));
            json.put(Tags.TOKEN, Preferencias.getToken(CambiarContrasena.this));
            json.put(Tags.PASSWORD_ANTIGUA, contrasena_antigua);
            json.put(Tags.PASSWORD_NUEVA, contrasena_nueva);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /* Se hace petición de login al servidor. */
        json = JSONUtil.hacerPeticionServidor("usuarios/java/cambiar_pass/", json);

        try {
            String p = json.getString(Tags.RESULTADO);

            /* Se comprueba la conexión al servidor. */
            if (p.contains(Tags.ERRORCONEXION)) {
                mensaje = getResources().getString(R.string.error_conexion);
                return false;
            }
            /* En caso de que conecte */
            else if (p.contains(Tags.OK)) {
                /* Guarda en las preferencias el token. */
                mensaje = json.getString(Tags.MENSAJE);
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
