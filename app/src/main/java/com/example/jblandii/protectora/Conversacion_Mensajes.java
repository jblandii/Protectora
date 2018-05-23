package com.example.jblandii.protectora;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.jblandii.protectora.Adaptadores.AdaptadorConversaciones;
import com.example.jblandii.protectora.Adaptadores.AdaptadorMensajes;
import com.example.jblandii.protectora.Models.Conversacion;
import com.example.jblandii.protectora.Models.Mensaje;
import com.example.jblandii.protectora.Models.Protectora;
import com.example.jblandii.protectora.peticionesBD.JSONUtil;
import com.example.jblandii.protectora.peticionesBD.Preferencias;
import com.example.jblandii.protectora.peticionesBD.Tags;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Conversacion_Mensajes extends AppCompatActivity {

    private Protectora protectora;
    private EditText et_mensaje_a_enviar;
    private ImageButton ib_enviar;
    private ArrayList<Mensaje> listaMensajes;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversacion_mensajes);
        recyclerView = findViewById(R.id.rv_recycler_mensajes);
        recyclerView.setLayoutManager(new LinearLayoutManager(Conversacion_Mensajes.this));
        cargarBotones();
        asignarValores();
        cargarMensajes();
        Log.v("Mensajebuclecon", listaMensajes.toString());

        AdaptadorMensajes adaptadorMensajes = new AdaptadorMensajes(listaMensajes, Conversacion_Mensajes.this);
        recyclerView.setAdapter(adaptadorMensajes);
    }

    private void cargarBotones() {
        listaMensajes = new ArrayList<>();

        et_mensaje_a_enviar = findViewById(R.id.et_mensaje_a_enviar);
        ib_enviar = findViewById(R.id.ib_enviar);
    }

    private void asignarValores() {
        if (recogerDatosConversacion()) {

        } else {
            Intent intentmain = getIntent();
            setResult(Activity.RESULT_CANCELED, intentmain);
            Toast.makeText(Conversacion_Mensajes.this, getResources().getString(R.string.ocurrido_error), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private boolean recogerDatosConversacion() {
        try {
            protectora = getIntent().getExtras().getParcelable("conversacion");
//            Toast.makeText(this, protectora.toString(), Toast.LENGTH_LONG).show();
            return true;
        } catch (Exception e) {
            Log.v("Exception", e.toString());
            return false;
        }
    }

    public void cargarMensajes() {
        String token = Preferencias.getToken(Conversacion_Mensajes.this);
        String usuario_id = Preferencias.getID(Conversacion_Mensajes.this);
        //Creamos el JSON que vamos a mandar al servidor
        JSONObject json = new JSONObject();
        try {
            json.put(Tags.TOKEN, token);
            json.put(Tags.USUARIO_ID, usuario_id);
            json.put(Tags.ID_PROTECTORA, protectora.getPk());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /* Se hace petición de login al servidor. */
        json = JSONUtil.hacerPeticionServidor("conversacion/cargar_mensajes/", json);
        Log.v("mensajesmostrar", json.toString());
        try {
            String p = json.getString(Tags.RESULTADO);

            /* Se comprueba la conexión al servidor. */
            if (p.contains(Tags.ERRORCONEXION)) {
//                mensaje = "Error de conexión";
            } else if (p.contains(Tags.OK)) {

                JSONArray array = json.getJSONArray(Tags.LISTA_CONVERSACIONES);
                Log.v("Mensajebuclesin", array.toString());
                if (array.length() > 0) {
                    for (int i = 0; i < array.length(); i++) {
                        Mensaje mensaje = new Mensaje(array.getJSONObject(i));
                        listaMensajes.add(mensaje);
                        Log.v("Mensajebuclecon", mensaje.toString());
                    }
                }
            }
            /* Resultado falla por otro error. */
            else if (p.contains(Tags.ERROR)) {
                Toast.makeText(Conversacion_Mensajes.this, json.getString(Tags.MENSAJE), Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
