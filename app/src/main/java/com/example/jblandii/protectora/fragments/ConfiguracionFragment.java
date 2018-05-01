package com.example.jblandii.protectora.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jblandii.protectora.Models.Animal;
import com.example.jblandii.protectora.Models.Usuario;
import com.example.jblandii.protectora.R;
import com.example.jblandii.protectora.peticionesBD.JSONUtil;
import com.example.jblandii.protectora.peticionesBD.Preferencias;
import com.example.jblandii.protectora.peticionesBD.Tags;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.jblandii.protectora.Util.ValidatorUtil.ucFirst;

public class ConfiguracionFragment extends Fragment {
    TextView tv_nombre_usuario, tv_email_usuario;
    ImageView iv_perfil_usuario;

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
        tv_email_usuario = view.findViewById(R.id.tv_email_usuario);
        iv_perfil_usuario = view.findViewById(R.id.iv_perfil_usuario);

        cargarUsuario();

        return view;
    }

    /**
     * Metodo que permite cargar los datos del usuario.
     */
    public void cargarUsuario() {
        String token = Preferencias.getToken(getActivity());
        String usuario_id = Preferencias.getID(getActivity());
        //Creamos el JSON que vamos a mandar al servidor
        JSONObject json = new JSONObject();
        try {
            json.put(Tags.TOKEN, token);
            json.put(Tags.USUARIO_ID, usuario_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /* Se hace petición de login al servidor. */
        json = JSONUtil.hacerPeticionServidor("protectora/cargar_usuario/", json);

        try {
            String p = json.getString(Tags.RESULTADO);

            /* Se comprueba la conexión al servidor. */
            if (p.contains(Tags.ERRORCONEXION)) {
//                mensaje = "Error de conexión";
            }
            /* En caso de que conecte y no haya animales para dicha busqueda. */
            else if (p.contains(Tags.OK_SIN_ANIMALES)) {
                Toast.makeText(getContext(), ucFirst(json.getString(Tags.MENSAJE)), Toast.LENGTH_LONG).show();
            } else if (p.contains(Tags.OK)) {
                String res = json.getString(Tags.RESULTADO);
                JSONObject objeto_usuario = json.getJSONObject(Tags.LISTA_ANIMALES);
                Log.v("usuariosjson", objeto_usuario.toString());
                if (objeto_usuario != null) {
                    Usuario usuario = new Usuario(objeto_usuario.getJSONObject("nombre"));
                    Log.v("usuario", usuario.toString());
                }
            }
            /* Resultado falla por otro error. */
            else if (p.contains(Tags.ERROR)) {
                String msg = json.getString(Tags.MENSAJE);
//                mensaje = msg;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
