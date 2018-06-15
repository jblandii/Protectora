package com.example.jblandii.protectora;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.jblandii.protectora.Adaptadores.AdaptadorMensajes;
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
    ImageButton ib_enviar;
    private ArrayList<Mensaje> listaMensajes;
    private RecyclerView recyclerView;
    private AdaptadorMensajes adaptadorMensajes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversacion_mensajes);
        recyclerView = findViewById(R.id.rv_recycler_mensajes);
        recyclerView.setLayoutManager(new LinearLayoutManager(Conversacion_Mensajes.this));
        cargarBotones();
        asignarValores();
        cargarMensajes();

        adaptadorMensajes = new AdaptadorMensajes(listaMensajes, Conversacion_Mensajes.this);
        recyclerView.setAdapter(adaptadorMensajes);
        recyclerView.scrollToPosition(listaMensajes.size() - 1);
    }

    /**
     * Metodo que se utiliza para cargar los elementos del layout.
     */
    private void cargarBotones() {
        listaMensajes = new ArrayList<>();

        et_mensaje_a_enviar = findViewById(R.id.et_mensaje_a_enviar);
        ib_enviar = findViewById(R.id.ib_enviar);
        ib_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!et_mensaje_a_enviar.getText().toString().isEmpty()) {
                    mandarMensajeDeUsuario(et_mensaje_a_enviar.getText().toString());
                    et_mensaje_a_enviar.setText("");
                }
            }
        });

        if (et_mensaje_a_enviar.getShowSoftInputOnFocus()) {
            recyclerView.scrollToPosition(listaMensajes.size() - 1);
        }
    }

    /**
     * Metodo que se utiliza para pone en la toolbar el nombre de la protectora o volver a la pantalla de las conversaciones dependiendo del resultado que reciba
     * del metodo recogerDatosConversacion.
     */
    private void asignarValores() {
        if (recogerDatosConversacion()) {
            getSupportActionBar().setTitle(protectora.getNombre());
        } else {
            Intent intentmain = getIntent();
            setResult(Activity.RESULT_CANCELED, intentmain);
            Toast.makeText(Conversacion_Mensajes.this, getResources().getString(R.string.ocurrido_error), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    /**
     * Metodo que se utiliza para recoger los datos que se reciben de la conversacion.
     *
     * @return devuelve si se recoge true o false dependiendo de si se recogen correctamtente los datos.
     */
    private boolean recogerDatosConversacion() {
        try {
            protectora = getIntent().getExtras().getParcelable("conversacion");
            return true;
        } catch (Exception e) {
            Log.v("Exception", e.toString());
            return false;
        }
    }

    /**
     * Metodo para cargar todos los mensajes del usuario.
     */
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
        try {
            String p = json.getString(Tags.RESULTADO);

            /* Se comprueba la conexión al servidor. */
            if (p.contains(Tags.ERRORCONEXION)) {
//                mensaje = "Error de conexión";
            } else if (p.contains(Tags.OK)) {

                JSONArray array = json.getJSONArray(Tags.LISTA_CONVERSACIONES);
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

    /**
     * Metodo para enviar al servidor el nuevo mensaje que ha escrito el usuario.
     *
     * @param mensaje
     */
    private void mandarMensajeDeUsuario(String mensaje) {
        //Creamos el JSON que vamos a mandar al servidor
        JSONObject json = new JSONObject();
        try {
            json.put(Tags.USUARIO_ID, Preferencias.getID(Conversacion_Mensajes.this));
            json.put(Tags.TOKEN, Preferencias.getToken(Conversacion_Mensajes.this));
            json.put(Tags.MENSAJE, mensaje);
            json.put(Tags.PROTECTORA, protectora.getPk());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /* Se hace petición de login al servidor. */
        json = JSONUtil.hacerPeticionServidor("conversacion/enviar_mensaje_protectora_de_usuario/", json);

        try {
            String p = json.getString(Tags.RESULTADO);

            /* Se comprueba la conexión al servidor. */
            if (p.contains(Tags.ERRORCONEXION)) {
                mensaje = getResources().getString(R.string.error_conexion);
            }
            /* En caso de que conecte */
            else if (p.contains(Tags.OK)) {
                /* Guarda en las preferencias el token. */
                JSONArray array = json.getJSONArray(Tags.LISTA_CONVERSACIONES);
                if (array.length() > 0) {
                    for (int i = 0; i < array.length(); i++) {
                        Mensaje mensaje_enviado = new Mensaje(array.getJSONObject(i));
                        listaMensajes.add(mensaje_enviado);
                        Log.v("Mensajebuclecon", mensaje.toString());
                        adaptadorMensajes.notifyItemInserted(listaMensajes.size() - 1);
                        adaptadorMensajes.notifyItemInserted(listaMensajes.size());
                        recyclerView.scrollToPosition(listaMensajes.size() - 1);
                    }
                }
            }

            /* Resultado falla por otro error. */
            else if (p.contains(Tags.ERROR)) {
                mensaje = json.getString(Tags.MENSAJE);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
