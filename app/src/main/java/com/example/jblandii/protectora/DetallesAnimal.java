package com.example.jblandii.protectora;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jblandii.protectora.Adaptadores.AdaptadorDetallesAnimal_ViewPager;
import com.example.jblandii.protectora.Models.Animal;
import com.example.jblandii.protectora.Models.Protectora;
import com.example.jblandii.protectora.Models.Usuario;
import com.example.jblandii.protectora.Util.DescargarImagen;
import com.example.jblandii.protectora.peticionesBD.JSONUtil;
import com.example.jblandii.protectora.peticionesBD.Preferencias;
import com.example.jblandii.protectora.peticionesBD.Tags;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class DetallesAnimal extends AppCompatActivity {

    private ViewPager vp_imagenes_animal;
    private Animal animal;
    private ImageView ib_megusta_detalle_animal;
    private ArrayList<String> imagenes;
    private CardView cv_protectora_info_animal;
    private TextView tv_nombre_protectora, tv_protectora_provincia, tv_sexo_del_animal,
            tv_raza_del_animal, tv_tamano_del_animal, tv_pelaje_del_animal, tv_edad_del_animal,
            tv_chip_del_animal, tv_descripcion_del_animal;
    private Protectora protectora;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_animal);

        Toolbar toolbar = findViewById(R.id.toolbar_animal);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        vp_imagenes_animal = findViewById(R.id.vp_imagenes_animal);
        cargarBotones();
        asignarValores();


        AdaptadorDetallesAnimal_ViewPager adaptadorDetallesAnimal_viewPager = new AdaptadorDetallesAnimal_ViewPager(this, imagenes);
        vp_imagenes_animal.setAdapter(adaptadorDetallesAnimal_viewPager);
    }

    private boolean recogerDatosAnimal() {
        try {
            animal = getIntent().getExtras().getParcelable("animal");
//            Toast.makeText(this, animal.toString(), Toast.LENGTH_LONG).show();
            return true;
        } catch (Exception e) {
            Log.v("Exception", e.toString());
            return false;
        }
    }

    private void asignarValores() {
        if (recogerDatosAnimal()) {
            cargarImagenesAnimal();
            getSupportActionBar().setTitle(animal.getNombre());
            tv_nombre_protectora.setText(protectora.getNombre());
            tv_protectora_provincia.setText(protectora.getProvincia());
            tv_sexo_del_animal.setText(animal.getSexo());
            tv_raza_del_animal.setText(animal.getRaza());
            String tamano = animal.getTamano() + " (" + animal.getPeso() + "Kg.)";
            tv_tamano_del_animal.setText(tamano);
            tv_pelaje_del_animal.setText(animal.getTipo_pelaje());
            String edad = "";
            if (animal.getEdad().equals("1")) {
                edad = animal.getEdad() + " año";
            } else {
                edad = animal.getEdad() + " años";
            }
            tv_edad_del_animal.setText(edad);
            tv_chip_del_animal.setText(animal.getChip());
            tv_descripcion_del_animal.setText(animal.getDescripcion());

            if (animal.getMe_gusta().equals("true")) {
                ib_megusta_detalle_animal.setImageResource(R.drawable.ic_megusta);
            }
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
        tv_protectora_provincia = findViewById(R.id.tv_protectora_provincia);
        tv_nombre_protectora = findViewById(R.id.tv_nombre_protectora);
        tv_sexo_del_animal = findViewById(R.id.tv_sexo_del_animal);
        tv_raza_del_animal = findViewById(R.id.tv_raza_del_animal);
        tv_tamano_del_animal = findViewById(R.id.tv_tamano_del_animal);
        tv_pelaje_del_animal = findViewById(R.id.tv_pelaje_del_animal);
        tv_edad_del_animal = findViewById(R.id.tv_edad_del_animal);
        tv_chip_del_animal = findViewById(R.id.tv_chip_del_animal);
        tv_descripcion_del_animal = findViewById(R.id.tv_descripcion_del_animal);
        cv_protectora_info_animal = findViewById(R.id.cv_protectora_info_animal);

        ib_megusta_detalle_animal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dar_mg()) {
                    if (animal.getMe_gusta().equals("true")) {
                        ib_megusta_detalle_animal.setImageResource(R.drawable.ic_megusta_borde);
                        animal.setMe_gusta("false");
                    } else {
                        Log.v("entro", "entro");
                        ib_megusta_detalle_animal.setImageResource(R.drawable.ic_megusta);
                        animal.setMe_gusta("true");
                    }
                }
            }
        });

        cv_protectora_info_animal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentDetallesProtectora = new Intent(v.getContext(), DetallesProtectora.class);
                Log.v("probandoprotectora", protectora.toString());
                intentDetallesProtectora.putExtra("protectora", (Parcelable) protectora);
                v.getContext().startActivity(intentDetallesProtectora);
            }
        });
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

    public boolean dar_mg() {
        //Creamos el JSON que vamos a mandar al servidor
        JSONObject json = new JSONObject();
        try {
            json.put(Tags.USUARIO_ID, Usuario.getID(DetallesAnimal.this));
            json.put(Tags.TOKEN, Usuario.getToken(DetallesAnimal.this));
            json.put(Tags.ANIMAL, animal.getPk());
            json.put(Tags.MEGUSTA, animal.getMe_gusta());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /* Se hace petición de login al servidor. */
        json = JSONUtil.hacerPeticionServidor("protectora/dar_mg/", json);

        try {
            String p = json.getString(Tags.RESULTADO);

            /* Se comprueba la conexión al servidor. */
            if (p.contains(Tags.ERRORCONEXION)) {
//                mensaje = "Error de conexión";
                return false;
            }
            /* En caso de que conecte */
            else if (p.contains(Tags.OK)) {
                return true;
            }

            /* Resultado falla por otro error. */
            else if (p.contains(Tags.ERROR)) {
                String msg = json.getString(Tags.MENSAJE);
//                mensaje = msg;
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
}
