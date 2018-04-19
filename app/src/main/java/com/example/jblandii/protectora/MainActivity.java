package com.example.jblandii.protectora;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.example.jblandii.protectora.Models.Usuario;
import com.example.jblandii.protectora.R;
import com.example.jblandii.protectora.peticionesBD.JSONUtil;
import com.example.jblandii.protectora.peticionesBD.Tags;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Boolean inicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inicio = true;
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
        /*Hacer que el teclado se superponga y no se mueva*/
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (inicio) {
            lanzarLogin();
        }
    }

    /**
     * Metodo que se encarga de lanzar el login.
     */
    public void lanzarLogin() {
        if (hayInternet()) {
            String token = Usuario.getToken(getApplicationContext());
            if (token == null || token == "" || !JSONUtil.compruebaToken(token)) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), LoginActivity.class);
                startActivityForResult(intent, Tags.LOGIN);
            } else {
                inicializar();
            }
        }
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

    private void inicializar() {
//        contador++;
        comprobarFotoEmail();
    }

    public void comprobarFotoEmail() {
        //Creamos el JSON que vamos a mandar al servidor
        JSONObject jsonConsulta = new JSONObject();
        try {
            jsonConsulta.put(Tags.USUARIO_ID, Usuario.getID(MainActivity.this));
            jsonConsulta.put(Tags.TOKEN, Usuario.getToken(MainActivity.this));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Hacemos petición de lista de centros al servidor
        jsonConsulta = JSONUtil.hacerPeticionServidor("usuarios/java/get_perfil/", jsonConsulta);
        Log.i("DATOS", jsonConsulta.toString());


        try {
            String p = jsonConsulta.getString(Tags.RESULTADO);
            Log.i("DATOS", p);
            if (p.contains(Tags.OK)) {

                Usuario user = new Usuario(jsonConsulta);
                Log.i("DATOS", "correo" + user.getCorreo() + "9999");
                Log.i("DATOS", "foto " + user.getImagenURL());
                if (user.getImagenURL().equals("") || user.getImagenURL().contains("noavatar.png") || user.getCorreo().equals("")) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setCancelable(false);
                    builder.setMessage("Te falta configurar una foto o el correo");
                    builder.setNegativeButton("Ahora no", new DialogInterface.OnClickListener() {
                        //Al pulsar el boton llamar, activa la agenda con el numero para llamar
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
                    builder.setPositiveButton("Configurar", new DialogInterface.OnClickListener() {
                        //Al pulsar el boton llamar, activa la agenda con el numero para llamar
                        public void onClick(DialogInterface dialog, int id) {
//                            Intent i = new Intent(MainActivity.this, UserActivity.class);
//                            startActivity(i);
                        }
                    }).show();

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo que se encarga de comprobar si hay conexión a internet.
     *
     * @return true en caso de haber conexión o false en el caso contrario.
     */
    public boolean hayInternet() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }
}
