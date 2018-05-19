package com.example.jblandii.protectora.Models;

import com.example.jblandii.protectora.peticionesBD.Tags;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jblandii on 25/04/18.
 */

public class Protectora {
    private int pk;
    private String nombre;
    private String provincia;
    private String direccion;
    private String codigo_postal;

    public Protectora(int pk, String nombre, String direccion, String provincia, String codigo_postal) {
        this.setPk(pk);
        this.setNombre(nombre);
        this.setDireccion(direccion);
        this.setProvincia(provincia);
        this.setCodigo_postal(codigo_postal);
    }

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getCodigo_postal() {
        return codigo_postal;
    }

    public void setCodigo_postal(String codigo_postal) {
        this.codigo_postal = codigo_postal;
    }

    public Protectora(JSONObject json) {
        try {
            setPk(json.getInt(Tags.PK));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            setNombre(json.getString(Tags.NOMBRE));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            setDireccion(json.getString(Tags.DIRECCION));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            setProvincia(json.getString(Tags.PROVINCIA));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            setCodigo_postal(json.getString(Tags.COD_POSTAL));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Protectora{" +
                "pk=" + pk +
                ", nombre='" + nombre + '\'' +
                ", provincia='" + provincia + '\'' +
                ", direccion='" + direccion + '\'' +
                ", codigo_postal='" + codigo_postal + '\'' +
                '}';
    }
}
