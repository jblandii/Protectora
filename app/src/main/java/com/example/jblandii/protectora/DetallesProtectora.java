package com.example.jblandii.protectora;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

    private ArrayList<String> imagenes;
    private ArrayList<RedSocial> redesSociales;
    private CardView cv_protectora_info_animal;
    private LinearLayout ll_protectora_info_animal;
    private TextView tv_provincia_protectora, tv_direccion_protectora, tv_codigopostal_protectora, tv_descripcion_dela_protectora;
    private Protectora protectora;
    Button btn_contactar_protectora;
    ViewPager vp_imagenes_protectora;


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

    /**
     * Metodo que se utiliza para recoger los datos de la protectora que recibe desde otra actividad.
     * @return devuelve true o false dependiendo de si los recoge bien.
     */
    private boolean recogerDatosProtectora() {
        try {
            protectora = getIntent().getExtras().getParcelable("protectora");
            return true;
        } catch (Exception e) {
            Log.v("Exception", e.toString());
            return false;
        }
    }

    /**
     * Metodo que se utiliza para mostrar todos los datos de la protectora en el layout.
     */
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

    /**
     * Metodo que se utiliza para cargar todos los elementos del layout.
     */
    private void cargarBotones() {
        redesSociales = new ArrayList<>();
        imagenes = new ArrayList<>();
        cv_protectora_info_animal = findViewById(R.id.cv_protectora_info_animal);
        ll_protectora_info_animal = findViewById(R.id.ll_protectora_info_animal);
        tv_provincia_protectora = findViewById(R.id.tv_provincia_dela_protectora);
        tv_direccion_protectora = findViewById(R.id.tv_direccion_dela_protectora);
        tv_codigopostal_protectora = findViewById(R.id.tv_codigopostal_dela_protectora);
        tv_descripcion_dela_protectora = findViewById(R.id.tv_descripcion_dela_protectora);
        btn_contactar_protectora = findViewById(R.id.btn_contactar_protectora);
        btn_contactar_protectora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirDialogo();
            }
        });
    }

    /**
     * Metodo que se utiliza para cargar las imagenes de la protectora.
     */
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
                        final RedSocial redSocial = new RedSocial(a_redes.getJSONObject(i));
                        redesSociales.add(redSocial);
                        View view = getLayoutInflater().inflate(R.layout.red_social, null);
                        ImageButton imageButton = view.findViewById(R.id.ib_red_social);
                        if (!redSocial.getRed().isEmpty()) {
                            if (redSocial.getRed().equals("Twitter")) {
                                imageButton.setImageResource(R.drawable.tw);
                                ll_protectora_info_animal.addView(view);
                            } else if (redSocial.getRed().equals("Facebook")) {
                                imageButton.setImageResource(R.drawable.fb);
                                ll_protectora_info_animal.addView(view);
                            } else if (redSocial.getRed().equals("Instagram")) {
                                imageButton.setImageResource(R.drawable.instagram);
                                ll_protectora_info_animal.addView(view);
                            }
                            imageButton.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    Uri uri = Uri.parse(redSocial.getValor());
                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                    startActivity(intent);
                                }
                            });
                        } else {
                            cv_protectora_info_animal.setVisibility(View.GONE);
                        }
                    }
                }
                JSONArray a_imagenes = json.getJSONArray(Tags.FOTO);
                if (a_imagenes != null) {
                    for (int i = 0; i < a_imagenes.length(); i++) {
                        imagenes.add(a_imagenes.get(i).toString());
                    }
                }
                JSONObject json_protectora = json.getJSONObject(Tags.PROTECTORA);
                protectora = new Protectora(json_protectora);
            }

            /* Resultado falla por otro error. */
            else if (p.contains(Tags.ERROR)) {
                Intent intentmain = getIntent();
                setResult(Activity.RESULT_CANCELED, intentmain);
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo que se utiliza para abrir un dialogo para poder contactar con la protectora que se está visualizando.
     */
    private void abrirDialogo() {
        LayoutInflater inflater = LayoutInflater.from(DetallesProtectora.this);
        View subView = inflater.inflate(R.layout.dialogo_contactar, null);
        final TextInputLayout til_mensaje_protectora = subView.findViewById(R.id.til_mensaje_protectora);
        final TextInputEditText tie_mensaje_protectora = subView.findViewById(R.id.tie_mensaje_protectora);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.contactar_con) + " " + protectora.getNombre())
                .setView(subView);
        final AlertDialog alertDialog = builder.create();
        builder.setPositiveButton(getResources().getString(R.string.aceptar), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!tie_mensaje_protectora.getText().toString().isEmpty()) {
                    til_mensaje_protectora.setErrorEnabled(false);
                    mandarMensaje(tie_mensaje_protectora.getText().toString());
                } else {
                    til_mensaje_protectora.setError(getResources().getString(R.string.mensaje_vacio));
                }
            }
        });

        builder.setNegativeButton(getResources().getString(R.string.cancelar), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.show();
    }

    /**
     * Metodo que se utiliza para enviar el mensaje que el usuario desea a la protectora.
     * @param mensaje se le pasa por parametro el mensaje que ha escrito el usuario en el dialogo.
     */
    private void mandarMensaje(String mensaje) {
        //Creamos el JSON que vamos a mandar al servidor
        JSONObject json = new JSONObject();
        try {
            json.put(Tags.USUARIO_ID, Preferencias.getID(DetallesProtectora.this));
            json.put(Tags.TOKEN, Preferencias.getToken(DetallesProtectora.this));
            json.put(Tags.MENSAJE, mensaje);
            json.put(Tags.PROTECTORA, protectora.getPk());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /* Se hace petición de login al servidor. */
        json = JSONUtil.hacerPeticionServidor("conversacion/enviar_mensaje_protectora/", json);

        try {
            String p = json.getString(Tags.RESULTADO);

            /* Se comprueba la conexión al servidor. */
            if (p.contains(Tags.ERRORCONEXION)) {
                mensaje = getResources().getString(R.string.error_conexion);
            }
            /* En caso de que conecte */
            else if (p.contains(Tags.OK)) {
                /* Guarda en las preferencias el token. */
                mensaje = json.getString(Tags.MENSAJE);
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

