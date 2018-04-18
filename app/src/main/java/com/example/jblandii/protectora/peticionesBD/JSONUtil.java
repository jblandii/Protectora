package com.example.jblandii.protectora.peticionesBD;

import android.os.AsyncTask;

import com.example.jblandii.protectora.Models.Usuario;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class JSONUtil {
    private static final String TAG = "constante";

    /**
     * Comprueba si un token pasado como parametro es valido en la base de datos
     *
     * @param token
     * @return true si es correcto
     */
    public static boolean compruebaToken(String token) {
        if (token != null) {
            if (!token.equals("")) {
                JSONObject json = new JSONObject();
                try {
                    json.put(Tags.USUARIO_ID, Usuario.getIDToken(token));
                    json.put(Tags.TOKEN, token);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                json = JSONUtil.hacerPeticionServidor("usuarios/java/comprobar_token/", json);
//				Log.e("JSON", json.toString());
                try {
                    if (json.getString(Tags.RESULTADO).equals(Tags.OK) && json.getString(Tags.MENSAJE).equals(Tags.USUARIOLOGUEADO))
                        return true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }


    /**
     * Realiza una peticion al servidor de la url pasada con un json pasado
     *
     * @param url  servidor
     * @param json información de la peticion
     * @return
     */
    public static JSONObject hacerPeticionServidorFiles(String url, ArrayList<String> rutas, JSONObject json) {
        JSONObject resultado = null;
        Utilidades2 utilidades = new Utilidades2(rutas, json);
        utilidades.execute(url);
        try {
            resultado = utilidades.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        utilidades.cancel(true);
        return resultado;
    }


    /**
     * clase para hacer peticiones asincronas de JSON y un array de rutas de archivos
     *
     * @author brian
     */
    private static class Utilidades2 extends AsyncTask<String, Void, JSONObject> {
        ArrayList<String> rutas;
        JSONObject resultado;
        JSONObject json_enviar;

        public Utilidades2(ArrayList<String> rutas, JSONObject json) {
            this.rutas = rutas;
            this.json_enviar = json;
        }

        @Override
        protected JSONObject doInBackground(String... arg0) {
            // //Log.d("DEBUG", "Tarea asincrona de JSON");
            resultado = new JSONObject();
            Thread.currentThread().setName("JSONUTIL_AsyncTask" + arg0[0]);
            resultado = JSONParser.makeHttpRequestFiles(Tags.SERVIDOR + arg0[0], rutas, json_enviar);
            // Log.i("JSONUtil", resultado.toString());
            return resultado;
        }
    }


    /**
     * Realiza una peticion al servidor de la url pasada con un json pasado
     *
     * @param url  servidor
     * @param json información de la peticion
     * @return
     */
    public static JSONObject hacerPeticionServidorFile(String url, String ruta, JSONObject json) {
        JSONObject resultado = null;
        Utilidades3 utilidades = new Utilidades3(ruta, json);
        utilidades.execute(url);
        try {
            resultado = utilidades.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        utilidades.cancel(true);
        return resultado;
    }


    /**
     * clase para hacer peticiones asincronas de JSON
     *
     * @author brian
     */
    private static class Utilidades3 extends AsyncTask<String, Void, JSONObject> {

        JSONObject resultado;
        JSONObject json_enviar;
        String rutaFile;

        public Utilidades3(String rut, JSONObject json) {
            this.rutaFile = rut;
            this.json_enviar = json;
        }

        @Override
        protected JSONObject doInBackground(String... arg0) {
            // //Log.d("DEBUG", "Tarea asincrona de JSON");
            resultado = new JSONObject();
            Thread.currentThread().setName("JSONUTIL_AsyncTask" + arg0[0]);
            resultado = JSONParser.makeHttpRequestFile(Tags.SERVIDOR + arg0[0], rutaFile, json_enviar);
            // Log.i("JSONUtil", resultado.toString());
            return resultado;
        }
    }


    /**
     * Realiza una peticion al servidor de la url pasada con un json pasado
     *
     * @param url  servidor
     * @param json información de la peticion
     * @return
     */
    public static JSONObject hacerPeticionServidor(String url, JSONObject json) {
        JSONObject resultado = null;
        Utilidades utilidades = new Utilidades(json);
        utilidades.execute(url, json.toString());
        try {
            resultado = utilidades.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        utilidades.cancel(true);
        return resultado;
    }


    /**
     * clase para hacer peticiones asincronas de JSON
     *
     * @author brian
     */
    private static class Utilidades extends AsyncTask<String, Void, JSONObject> {

        JSONObject resultado;
        JSONObject json_enviar;

        public Utilidades(JSONObject json) {

            this.json_enviar = json;
        }

        @Override
        protected JSONObject doInBackground(String... arg0) {
            // //Log.d("DEBUG", "Tarea asincrona de JSON");
            resultado = new JSONObject();
            Thread.currentThread().setName("JSONUTIL_AsyncTask" + arg0[0]);
            resultado = JSONParser.makeHttpRequest1(Tags.SERVIDOR + arg0[0], json_enviar);
            // Log.i("JSONUtil", resultado.toString());
            return resultado;
        }
    }
}
