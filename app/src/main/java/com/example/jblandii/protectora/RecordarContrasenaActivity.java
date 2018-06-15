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
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.jblandii.protectora.peticionesBD.JSONUtil;
import com.example.jblandii.protectora.peticionesBD.Tags;

import org.json.JSONException;
import org.json.JSONObject;

public class RecordarContrasenaActivity extends AppCompatActivity {
    Button btn_recordar_contrasena;
    TextInputLayout til_login;
    TextInputEditText tie_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordar_contrasena);
        cargarBotones();
    }

    /**
     * Metodo que utilizo para cargar los elementos que tengo en el layout.
     */
    private void cargarBotones() {
        til_login = findViewById(R.id.til_login);
        tie_login = findViewById(R.id.tie_login);
        btn_recordar_contrasena = findViewById(R.id.btn_recordar_contrasena);
        btn_recordar_contrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (tie_login.getText().toString().isEmpty()) {
                    til_login.setError(getResources().getText(R.string.mensaje_recuperar_login));
                } else if (tie_login.getText().toString().length() < 5) {
                    til_login.setError(getResources().getText(R.string.mensaje_recuperar_login_error));
                } else {
                    String usuario = tie_login.getText().toString();

                    /* Creo el JSON que vamos a mandar al servidor. */
                    JSONObject json = new JSONObject();
                    try {
                        json.put(Tags.USUARIO, usuario);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(tie_login.getWindowToken(), 0);

                    AlertDialog alertDialog = new AlertDialog.Builder(RecordarContrasenaActivity.this).create();
                    alertDialog.setTitle(getResources().getString(R.string.recordar_contrasena));
                    alertDialog.setMessage(getResources().getString(R.string.mensaje_advertencia_recordar_contrasena));
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getText(R.string.aceptar), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            JSONObject json = new JSONObject();
                            try {
                                json.put(Tags.USUARIO, tie_login.getText().toString());
                                json = JSONUtil.hacerPeticionServidor("usuarios/java/recuperar_contrasena/", json);


                                String p = json.getString(Tags.RESULTADO);
                                Log.v("json2", p);
                                /* Compruebo la conexión con el servidor. */
                                if (p.contains(Tags.ERRORCONEXION)) {
                                    /* Si no conecta imprime un snackbar con un mensaje de error de conexión. */
                                    Snackbar.make(view, getResources().getText(R.string.error_conexion), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                } else if (p.contains(Tags.OK)) {
                                    Intent intentSecond = getIntent();
                                    intentSecond.putExtra("login", tie_login.getText().toString());
                                    setResult(Activity.RESULT_OK, intentSecond);
                                    finish();
                                }
                                /* Fallo por otro error. */
                                else if (p.contains(Tags.ERROR)) {
                                    String msg = json.getString(Tags.MENSAJE);
                                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
//                                return false;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
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
}
