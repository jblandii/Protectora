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

public class CambiarEmail extends AppCompatActivity {
    TextInputEditText tie_email_actual, tie_email_nueva, tie_email_nueva_repetir;
    TextInputLayout til_email_actual, til_email_nueva, til_email_nueva_repetir;
    Button btn_cambiar_email;
    String mensaje = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_email);
        cargarBotones();
    }

    private void cargarBotones() {
        btn_cambiar_email = findViewById(R.id.btn_cambiar_email);
        til_email_actual = findViewById(R.id.til_email_actual);
        til_email_nueva = findViewById(R.id.til_email_nueva);
        til_email_nueva_repetir = findViewById(R.id.til_email_nueva_repetir);
        tie_email_actual = findViewById(R.id.tie_email_actual);
        tie_email_nueva = findViewById(R.id.tie_email_nueva);
        tie_email_nueva_repetir = findViewById(R.id.tie_email_nueva_repetir);

        btn_cambiar_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (tie_email_actual.getText().toString().isEmpty()) {
                    til_email_actual.setError(getResources().getString(R.string.mensaje_email_vacio));
                } else if (tie_email_nueva.getText().toString().isEmpty()) {
                    til_email_actual.setErrorEnabled(false);
                    til_email_nueva.setError(getResources().getString(R.string.mensaje_email_vacio));
                } else if (tie_email_nueva_repetir.getText().toString().isEmpty()) {
                    til_email_actual.setErrorEnabled(false);
                    til_email_nueva.setErrorEnabled(false);
                    til_email_nueva_repetir.setError(getResources().getString(R.string.mensaje_email_confirmar_vacio));
                } else if (!tie_email_nueva.getText().toString().equals(tie_email_nueva_repetir.getText().toString())) {
                    til_email_actual.setErrorEnabled(false);
                    til_email_nueva.setErrorEnabled(false);
                    til_email_nueva_repetir.setErrorEnabled(false);
                    Snackbar.make(v, getResources().getString(R.string.mensaje_email_coincidir), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                } else {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(tie_email_actual.getWindowToken(), 0);
                    inputMethodManager.hideSoftInputFromWindow(tie_email_nueva.getWindowToken(), 0);
                    inputMethodManager.hideSoftInputFromWindow(tie_email_nueva_repetir.getWindowToken(), 0);

                    AlertDialog alertDialog = new AlertDialog.Builder(CambiarEmail.this).create();
                    alertDialog.setTitle(getResources().getString(R.string.cambiar_email));
                    alertDialog.setMessage(getResources().getString(R.string.mensaje_advertencia_cambiar_email));
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getText(R.string.aceptar), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (cambiaremail()) {
                                if (!mensaje.isEmpty()) {
//                                  Snackbar.make(v, mensaje, Snackbar.LENGTH_LONG).setAction("Action", null).show();
//                                  mensaje = "";
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

    private boolean cambiaremail() {
        String email_antigua = tie_email_actual.getText().toString();
        String email_nueva = tie_email_nueva.getText().toString();

        //Creamos el JSON que vamos a mandar al servidor
        JSONObject json = new JSONObject();
        try {
            json.put(Tags.USUARIO_ID, Preferencias.getID(CambiarEmail.this));
            json.put(Tags.TOKEN, Preferencias.getToken(CambiarEmail.this));
            json.put(Tags.EMAIL_ANTIGUO, email_antigua);
            json.put(Tags.EMAIL_NUEVO, email_nueva);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /* Se hace petición de login al servidor. */
        json = JSONUtil.hacerPeticionServidor("usuarios/java/cambiar_email/", json);

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