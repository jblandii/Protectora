package com.example.jblandii.protectora.Models;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.jblandii.protectora.peticionesBD.Tags;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;


/**
 * Created by JBlanDii.
 */
public class Usuario implements Parcelable {

    public static final int ADMINISTRADOR = 1;

    private String username;
    //    private String password;
    private String nombre;
    private String password;
    private String apellidos;
    private String correo;
    private String telefono;
    private String imagenURL;
    private String direccion;
    private String codigo_postal;
    private String provincia;

    public Usuario(String username, String pass, String correo, String nombre, String apellidos, String telefono, String direccion, String codigo_postal, String provincia) {
        this.setUsername(username);
        this.password = pass;
        this.setCorreo(correo);
        this.setNombre(nombre);
        this.setApellidos(apellidos);
        this.setTelefono(telefono);
        this.setDireccion(direccion);
        this.setCodigo_postal(codigo_postal);
        this.setProvincia(provincia);
    }

    public Usuario(String username, String correo, String nombre, String apellidos, String telefono, String direccion, String codigo_postal, String provincia) {
        this.setUsername(username);
        this.setCorreo(correo);
        this.setNombre(nombre);
        this.setApellidos(apellidos);
        this.setApellidos(telefono);
        this.setDireccion(direccion);
        this.setCodigo_postal(codigo_postal);
        this.setProvincia(provincia);
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
            setCodigo_postal(json.getString(Tags.COD_POSTAL));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            setProvincia(json.getString(Tags.PROVINCIA));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            setDireccion(json.getString(Tags.DIRECCION));
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

    protected Usuario(Parcel in) {
        username = in.readString();
        nombre = in.readString();
        password = in.readString();
        apellidos = in.readString();
        correo = in.readString();
        telefono = in.readString();
        imagenURL = in.readString();
        direccion = in.readString();
        codigo_postal = in.readString();
        provincia = in.readString();
    }

    public static final Creator<Usuario> CREATOR = new Creator<Usuario>() {
        @Override
        public Usuario createFromParcel(Parcel in) {
            return new Usuario(in);
        }

        @Override
        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };

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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCodigo_postal() {
        return codigo_postal;
    }

    public void setCodigo_postal(String codigo_postal) {
        this.codigo_postal = codigo_postal;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "username='" + username + '\'' +
                ", nombre='" + nombre + '\'' +
                ", password='" + password + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", correo='" + correo + '\'' +
                ", telefono='" + telefono + '\'' +
                ", imagenURL='" + imagenURL + '\'' +
                ", direccion='" + direccion + '\'' +
                ", codigo_postal='" + codigo_postal + '\'' +
                ", provincia='" + provincia + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(nombre);
        dest.writeString(password);
        dest.writeString(apellidos);
        dest.writeString(correo);
        dest.writeString(telefono);
        dest.writeString(imagenURL);
        dest.writeString(direccion);
        dest.writeString(codigo_postal);
        dest.writeString(provincia);
    }
}