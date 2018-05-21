package com.example.jblandii.protectora;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jblandii.protectora.Adaptadores.AdaptadorDetallesProtectora_ViewPager;
import com.example.jblandii.protectora.Models.Protectora;
import com.example.jblandii.protectora.Models.RedSocial;
import com.example.jblandii.protectora.peticionesBD.JSONUtil;
import com.example.jblandii.protectora.peticionesBD.Preferencias;
import com.example.jblandii.protectora.peticionesBD.Tags;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DetallesProtectora extends AppCompatActivity {

    private ViewPager vp_imagenes_protectora;
    private ArrayList<String> imagenes;
    private TextView tv_provincia_protectora, tv_direccion_protectora, tv_codigopostal_protectora, tv_descripcion_dela_protectora;
    private Protectora protectora;
    private ArrayList<RedSocial> redesSociales;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_protectora);

        Toolbar toolbar = findViewById(R.id.toolbar_protectora);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        vp_imagenes_protectora = findViewById(R.id.vp_imagenes_protectora);
        cargarBotones();
        asignarValores();


        AdaptadorDetallesProtectora_ViewPager adaptadorDetallesProtectora_viewPAger = new AdaptadorDetallesProtectora_ViewPager(this, imagenes);
        vp_imagenes_protectora.setAdapter(adaptadorDetallesProtectora_viewPAger);
    }

    private boolean recogerDatosProtectora() {
        try {
            protectora = getIntent().getExtras().getParcelable("protectora");
//            Toast.makeText(this, protectora.toString(), Toast.LENGTH_LONG).show();
            return true;
        } catch (Exception e) {
            Log.v("Exception", e.toString());
            return false;
        }
    }

    private void asignarValores() {
        if (recogerDatosProtectora()) {
            cargarImagenesProtectora();
            getSupportActionBar().setTitle(protectora.getNombre());
            tv_provincia_protectora.setText(protectora.getProvincia());
            tv_direccion_protectora.setText(protectora.getDireccion());
            tv_codigopostal_protectora.setText(protectora.getCodigo_postal());
            tv_descripcion_dela_protectora.setText(protectora.getDescripcion());
        } else {
            Intent intentmain = getIntent();
            setResult(Activity.RESULT_CANCELED, intentmain);
            Toast.makeText(DetallesProtectora.this, getResources().getString(R.string.ocurrido_error), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void cargarBotones() {
        redesSociales = new ArrayList<>();
        imagenes = new ArrayList<>();
        tv_provincia_protectora = findViewById(R.id.tv_provincia_dela_protectora);
        tv_direccion_protectora = findViewById(R.id.tv_direccion_dela_protectora);
        tv_codigopostal_protectora = findViewById(R.id.tv_codigopostal_dela_protectora);
        tv_descripcion_dela_protectora = findViewById(R.id.tv_descripcion_dela_protectora);
    }

    private void cargarImagenesProtectora() {
        //Creamos el JSON que vamos a mandar al servidor
        JSONObject json = new JSONObject();
        try {
            json.put(Tags.USUARIO_ID, Preferencias.getID(DetallesProtectora.this));
            json.put(Tags.TOKEN, Preferencias.getToken(DetallesProtectora.this));
            json.put(Tags.PROTECTORA, protectora.getPk());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /* Se hace petición de login al servidor. */
        json = JSONUtil.hacerPeticionServidor("protectora/cargar_imagenes_protectora/", json);

        try {
            String p = json.getString(Tags.RESULTADO);

            /* Se comprueba la conexión al servidor. */
            if (p.contains(Tags.ERRORCONEXION)) {
                Intent intentmain = getIntent();
                setResult(Activity.RESULT_CANCELED, intentmain);
                Toast.makeText(DetallesProtectora.this, getResources().getString(R.string.error_conexion), Toast.LENGTH_LONG).show();
                finish();
            }
            /* En caso de que conecte */
            else if (p.contains(Tags.OK)) {
                JSONArray a_redes = json.getJSONArray(Tags.REDES);
                if (a_redes != null) {
                    for (int i = 0; i < a_redes.length(); i++) {
                        RedSocial redSocial = new RedSocial(a_redes.getJSONObject(i));
                        redesSociales.add(redSocial);
                    }
                }
                JSONArray a_imagenes = json.getJSONArray(Tags.FOTO);
                if (a_imagenes != null) {
                    for (int i = 0; i < a_imagenes.length(); i++) {
                        imagenes.add(a_imagenes.get(i).toString());
                    }
                    Log.v("imagenesparadescargar", imagenes.toString());
                }
                JSONObject json_protectora = json.getJSONObject(Tags.PROTECTORA);
                protectora = new Protectora(json_protectora);
//                Toast.makeText(this, protectora.toString(), Toast.LENGTH_LONG).show();
            }

            /* Resultado falla por otro error. */
            else if (p.contains(Tags.ERROR)) {
                Intent intentmain = getIntent();
                setResult(Activity.RESULT_CANCELED, intentmain);
//                Toast.makeText(DetallesAnimal.this, json.getString(Tags.MENSAJE), Toast.LENGTH_LONG).show();
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

