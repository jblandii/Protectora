package com.example.jblandii.protectora.Models;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.jblandii.protectora.peticionesBD.Tags;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by JBlanDii.
 */
public class Usuario {

    public static final int ADMINISTRADOR = 1;

    private String username;
    private String password;
    private String nombre;
    private String apellidos;
    private String correo;
    private String telefono;
    private String imagenURL;

    public Usuario(String username, String pass, String correo, String nombre, String apellidos, String telefono) {
        this.setUsername(username);
        this.password = pass;
        this.setCorreo(correo);
        this.setNombre(nombre);
        this.setApellidos(apellidos);
        this.setTelefono(telefono);
    }

    /**
     * En el constructor del Usuario se le da un valor a las distintas variables.
     * Estas se sacan del JSON que nos ha retornado el servidor.
     *
     * @param json
     */
    public Usuario(JSONObject json) {
        try {
            setUsername(json.getString(Tags.USERNAME));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            setCorreo(json.getString(Tags.EMAIL));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            setNombre(json.getString(Tags.NOMBRE));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            setApellidos(json.getString(Tags.APELLIDOS));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            setTelefono(json.getString(Tags.TELEFONO));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            this.imagenURL = json.getString(Tags.FOTO);
            Log.i("USUARIO", this.imagenURL);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static String getIDToken(String token) {
        if (token != null && !token.equals(""))
            return token.substring(0, token.indexOf("_"));
        else
            return -1 + "";
    }

    public static String getID(Context context) {
        String token = getToken(context);
        if (token != null && !token.equals(""))
            return token.substring(0, token.indexOf("_"));
        else
            return -1 + "";
    }

    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * metodo para guardar el usuario y token en el sharedpreferences
     *
     * @param act
     * @param usuario
     * @param token
     */
    public static void guardarEnPref(Activity act, String usuario, String token) {
        SharedPreferences pref = act.getSharedPreferences("preferencias", Activity.MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString("usuario", usuario);
        edit.putString("token", token);
        edit.putBoolean("login1", false);
        edit.apply();
    }


    public static void cerrarSesion(Activity act, String usuario, String token) {
        SharedPreferences pref = act.getSharedPreferences("preferencias", Activity.MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.clear();
        edit.apply();
    }

    public static void borrarToken(Context context) {
        SharedPreferences pref = context.getSharedPreferences("preferencias", Activity.MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.clear();
        edit.apply();
    }

    public static boolean comprobarLogin1(Context context) {
        SharedPreferences pref = context.getSharedPreferences("preferencias", Activity.MODE_PRIVATE);
        boolean login1 = pref.getBoolean("login1", false);
        return login1;
    }

    public static void guardarLogin1(Context context, boolean login) {

        SharedPreferences pref = context.getSharedPreferences("preferencias", Activity.MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putBoolean("login1", login);
        edit.apply();

    }

    /**
     * Accede a sharedPreferences para obtener el token
     *
     * @return devuelve token
     */
    public static String getToken(Context context) {
        Log.i("DEBUG", "Accediendo a las preferencias");
        SharedPreferences pref = context.getSharedPreferences("preferencias", Activity.MODE_PRIVATE);
        String token = pref.getString("token", "");
        return token;
    }


    public String getImagenURL() {
        return this.imagenURL;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }
}