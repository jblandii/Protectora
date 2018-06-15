package com.example.jblandii.protectora.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jblandii.protectora.CambiarContrasena;
import com.example.jblandii.protectora.CambiarDatos;
import com.example.jblandii.protectora.CambiarEmail;
import com.example.jblandii.protectora.MainActivity;
import com.example.jblandii.protectora.Models.Usuario;
import com.example.jblandii.protectora.R;
import com.example.jblandii.protectora.Util.DescargarImagen;
import com.example.jblandii.protectora.Util.Utilidades;
import com.example.jblandii.protectora.peticionesBD.JSONUtil;
import com.example.jblandii.protectora.peticionesBD.Preferencias;
import com.example.jblandii.protectora.peticionesBD.Tags;

import org.json.JSONException;
import org.json.JSONObject;

import static android.app.Activity.RESULT_OK;

public class ConfiguracionFragment extends Fragment {
    TextView tv_nombre_usuario, tv_username;
    ImageView iv_perfil_usuario;
    CardView cv_cerrar_sesion, cv_cambiar_contrasena, cv_cambiar_email, cv_cambiar_datos, cv_cambiar_imagen;
    Usuario usuario;
    Bitmap bitmap;
    private Handler puente;
    private static final int PERFIL_CARGADO = 1, ABRIR_FICHERO = 2;

    public ConfiguracionFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_configuracion_fragment, container, false);
        /* Cargando el contenido que hay en la vista. */
        tv_nombre_usuario = view.findViewById(R.id.tv_nombre_usuario);
        tv_username = view.findViewById(R.id.tv_username);
        iv_perfil_usuario = view.findViewById(R.id.iv_perfil_usuario);
        cv_cerrar_sesion = view.findViewById(R.id.cv_cerrar_sesion);
        cv_cambiar_contrasena = view.findViewById(R.id.cv_cambiar_contrasena);
        cv_cambiar_email = view.findViewById(R.id.cv_cambiar_email);
        cv_cambiar_datos = view.findViewById(R.id.cv_cambiar_datos);
        cv_cambiar_imagen = view.findViewById(R.id.cv_cambiar_imagen);


        /* Funcionamiento. */
        cv_cerrar_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarSesion();
            }
        });
        cv_cambiar_contrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentContrasena = new Intent(getContext(), CambiarContrasena.class);
                startActivityForResult(intentContrasena, Tags.CAMBIAR_CONTRASENA);
            }
        });

        cv_cambiar_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentEmail = new Intent(getContext(), CambiarEmail.class);
                startActivityForResult(intentEmail, Tags.CAMBIAR_EMAIL);
            }
        });

        cv_cambiar_datos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentDatos = new Intent(getContext(), CambiarDatos.class);
                intentDatos.putExtra("usuario", usuario);
                startActivityForResult(intentDatos, Tags.CAMBIAR_DATOS);
            }
        });

        cv_cambiar_imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*|file/*");
                startActivityForResult(intent, ABRIR_FICHERO);
            }
        });

        cargarUsuario();
        cargarDatos();

        puente = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case PERFIL_CARGADO:
                        if (bitmap != null) {
                            iv_perfil_usuario.setImageBitmap(bitmap);
                        } else {
                            iv_perfil_usuario.setImageResource(R.mipmap.ic_usuario);
                        }

                }
            }
        };
        return view;
    }

    /**
     * Metodo que permite cargar los datos del usuario.
     */
    public void cargarUsuario() {
        String token = Preferencias.getToken(getActivity());
        final String usuario_id = Preferencias.getID(getActivity());
        //Creamos el JSON que vamos a mandar al servidor
        JSONObject json = new JSONObject();
        try {
            json.put(Tags.TOKEN, token);
            json.put(Tags.USUARIO_ID, usuario_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /* Se hace petición de login al servidor. */
        json = JSONUtil.hacerPeticionServidor("usuarios/java/cargar_usuario/", json);

        try {
            String p = json.getString(Tags.RESULTADO);

            /* Se comprueba la conexión al servidor. */
            if (p.contains(Tags.ERRORCONEXION)) {
                Toast.makeText(getContext(), getResources().getString(R.string.error_conexion), Toast.LENGTH_LONG).show();
            } else if (p.contains(Tags.OK)) {
                JSONObject jsonusuario = json.getJSONObject(Tags.USUARIO);
                usuario = new Usuario(jsonusuario);
                new Thread() {
                    @Override
                    public void run() {
//                        if (descargar) {
                        bitmap = DescargarImagen.descargarImagen(Tags.SERVIDOR + "static/media/" + usuario.getImagenURL());
//                        }
                        Message msg = new Message();
                        msg.what = PERFIL_CARGADO;
                        puente.sendMessage(msg);
                    }
                }.start();
            }
            /* Resultado falla por otro error. */
            else if (p.contains(Tags.ERROR)) {
                Toast.makeText(getContext(), json.getString(Tags.MENSAJE), Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo que permite cargar los datos del usuario.
     */
    public void cargarDatos() {
        String nombreyapellidos = "";
        if (!(usuario.getNombre().isEmpty() && usuario.getApellidos().isEmpty())) {
            nombreyapellidos = usuario.getNombre() + " " + usuario.getApellidos();
        }
        tv_nombre_usuario.setText(nombreyapellidos);
        tv_username.setText(usuario.getUsername());
    }

    /**
     * Metodo que permite cerrar sesión en la aplicación.
     */
    private void cerrarSesion() {
        JSONObject jsonEnviado = new JSONObject();
        try {
            jsonEnviado.put(Tags.USUARIO_ID, Preferencias.getID(getActivity()));
            jsonEnviado.put(Tags.TOKEN, Preferencias.getToken(getActivity()));

            JSONObject jsonRecibido = new JSONObject();
            jsonRecibido = JSONUtil.hacerPeticionServidor("usuarios/java/logout/", jsonEnviado);
            Usuario.borrarToken(getContext());
            ((MainActivity) getActivity()).lanzarLogin();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(getContext(), "Problemas al cerrar sesion", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Tags.CAMBIAR_CONTRASENA:
                    break;
                case Tags.CAMBIAR_EMAIL:
                    break;
                case ABRIR_FICHERO:
                    Uri uri = data.getData();
                    String ruta_real = "";

                    ruta_real = Utilidades.getPathFromUri(getContext(), uri);
                    JSONObject jsonConsulta = new JSONObject();
                    try {
                        jsonConsulta.put(Tags.USUARIO_ID, Usuario.getID(getActivity()));
                        jsonConsulta.put(Tags.TOKEN, Usuario.getToken(getActivity()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //Hacemos petición de lista de centros al servidor
                    jsonConsulta = JSONUtil.hacerPeticionServidorFile("usuarios/java/set_foto/", ruta_real, jsonConsulta);
                    try {
                        if (jsonConsulta.getString(Tags.RESULTADO).contains("ok")) {
                            cargarUsuario();
                        } else {
                            Toast.makeText(getContext(), jsonConsulta.getString(Tags.MENSAJE), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case Tags.CAMBIAR_DATOS:
//                  Recibir los datos cambiados para cambiar los datos del objeto usuario.
                    Usuario usuarioModificado = data.getParcelableExtra("usuario");
                    usuario.setNombre(usuarioModificado.getNombre());
                    usuario.setApellidos(usuarioModificado.getApellidos());
                    usuario.setCodigo_postal(usuarioModificado.getCodigo_postal());
                    usuario.setDireccion(usuarioModificado.getDireccion());
                    usuario.setTelefono(usuarioModificado.getTelefono());
                    cargarDatos();
                    break;
                default:
                    super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
}
