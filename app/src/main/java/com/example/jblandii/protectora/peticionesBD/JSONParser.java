/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.jblandii.protectora.peticionesBD;

/**
 * @author JBlanDii
 */

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;


public class JSONParser {

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
    public static final int CONNECTION_TIMEOUT = 3 * 1000;

    // constructor
    public JSONParser() {

    }

    /**
     * metodo para realizar una peticion al servidor y devuelve un json
     *
     * @param url
     * @param json_enviar
     * @return
     */
    public static JSONObject makeHttpRequest1(String url, JSONObject json_enviar) {
        JSONObject resultado = null;
        resultado = new JSONObject();

        try {
            final MultipartUtility http = new MultipartUtility(new URL(url));
            http.addFormField("data", json_enviar.toString());
            final byte[] bytes = http.finish();
            is = new ByteArrayInputStream(bytes);

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();

        } catch (Exception e) {
            try {
                resultado.put("result", "error conexion: " + e.getMessage());
                return resultado;
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

        }

        // try parse the string to a JSON object
        try {
            resultado = new JSONObject(json);
        } catch (JSONException e) {
        }

        // return JSON String
        return resultado;

    }


    public static JSONObject makeHttpRequestFiles(String url,
                                                  ArrayList<String> rutas, JSONObject json_enviar) {
        JSONObject resultado = null;
        resultado = new JSONObject();

        try {
            final MultipartUtility http = new MultipartUtility(new URL(url));
            for (int i = 0; i < rutas.size(); i++) {
                File file = new File(rutas.get(i));
                http.addFilePart("file", file);
            }
            http.addFormField("data", json_enviar.toString());
            final byte[] bytes = http.finish();
            is = new ByteArrayInputStream(bytes);

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();

        } catch (Exception e) {
            try {
                resultado.put("result", "error conexion: " + e.getMessage());
                return resultado;
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

        }

        // try parse the string to a JSON object
        try {
            resultado = new JSONObject(json);
        } catch (JSONException e) {
        }

        // return JSON String
        return resultado;

    }


    public static JSONObject makeHttpRequestFile(String url,
                                                 String ruta, JSONObject json_enviar) {
        JSONObject resultado = null;
        resultado = new JSONObject();

        try {
            final MultipartUtility http = new MultipartUtility(new URL(url));
            File file = new File(ruta);
            http.addFilePart("file", file);
            http.addFormField("data", json_enviar.toString());
            final byte[] bytes = http.finish();
            is = new ByteArrayInputStream(bytes);

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();

        } catch (Exception e) {
            try {
                resultado.put("result", "error conexion: " + e.getMessage());
                return resultado;
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

        }

        // try parse the string to a JSON object
        try {
            resultado = new JSONObject(json);
        } catch (JSONException e) {
        }

        // return JSON String
        return resultado;

    }

}