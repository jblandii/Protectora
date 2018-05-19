package com.example.jblandii.protectora;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.jblandii.protectora.Adaptadores.AdaptadorDetallesAnimal_ViewPager;
import com.example.jblandii.protectora.Models.Animal;
import com.example.jblandii.protectora.peticionesBD.JSONUtil;
import com.example.jblandii.protectora.peticionesBD.Preferencias;
import com.example.jblandii.protectora.peticionesBD.Tags;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DetallesAnimal extends AppCompatActivity {

    private ViewPager vp_imagenes_animal;
    private Animal animal;
    private ImageView ib_megusta_detalle_animal;
    private ArrayList<String> imagenes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_animal);
        vp_imagenes_animal = findViewById(R.id.vp_imagenes_animal);
        cargarBotones();
        asignarValores();
        cargarImagenesAnimal();


        AdaptadorDetallesAnimal_ViewPager adaptadorDetallesAnimal_viewPager = new AdaptadorDetallesAnimal_ViewPager(this, imagenes);
        vp_imagenes_animal.setAdapter(adaptadorDetallesAnimal_viewPager);
    }

    private boolean recogerDatosAnimal() {
        try {
            animal = getIntent().getExtras().getParcelable("animal");
            Toast.makeText(this, animal.toString(), Toast.LENGTH_LONG).show();
            return true;
        } catch (Exception e) {
            Log.v("Exception", e.toString());
            return false;
        }
    }

    private void asignarValores() {
        if (recogerDatosAnimal()) {
            Toast.makeText(DetallesAnimal.this, "hola", Toast.LENGTH_LONG).show();
        } else {
            Intent intentmain = getIntent();
            setResult(Activity.RESULT_CANCELED, intentmain);
            Toast.makeText(DetallesAnimal.this, getResources().getString(R.string.ocurrido_error), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void cargarBotones() {
        imagenes = new ArrayList<>();
        ib_megusta_detalle_animal = findViewById(R.id.ib_megusta_detalle_animal);
    }

    private void cargarImagenesAnimal() {
        //Creamos el JSON que vamos a mandar al servidor
        JSONObject json = new JSONObject();
        try {
            json.put(Tags.USUARIO_ID, Preferencias.getID(DetallesAnimal.this));
            json.put(Tags.TOKEN, Preferencias.getToken(DetallesAnimal.this));
            json.put(Tags.ANIMAL, animal.getPk());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /* Se hace petición de login al servidor. */
        json = JSONUtil.hacerPeticionServidor("protectora/cargar_imagenes_animal/", json);

        try {
            String p = json.getString(Tags.RESULTADO);

            /* Se comprueba la conexión al servidor. */
            if (p.contains(Tags.ERRORCONEXION)) {
                Intent intentmain = getIntent();
                setResult(Activity.RESULT_CANCELED, intentmain);
                Toast.makeText(DetallesAnimal.this, getResources().getString(R.string.error_conexion), Toast.LENGTH_LONG).show();
                finish();
            }
            /* En caso de que conecte */
            else if (p.contains(Tags.OK)) {
                JSONArray array = json.getJSONArray(Tags.FOTO);
                if (array != null) {
                    for (int i = 0; i < array.length(); i++) {
                        imagenes.add(array.get(i).toString());
                    }
                    Log.v("imagenesparadescargar", imagenes.toString());
                }
            }

            /* Resultado falla por otro error. */
            else if (p.contains(Tags.ERROR)) {
                Intent intentmain = getIntent();
                setResult(Activity.RESULT_CANCELED, intentmain);
                Toast.makeText(DetallesAnimal.this, json.getString(Tags.MENSAJE), Toast.LENGTH_LONG).show();
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
