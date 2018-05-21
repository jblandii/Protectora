package com.example.jblandii.protectora.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.jblandii.protectora.peticionesBD.Tags;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by jblandii on 25/04/18.
 */

public class Protectora implements Serializable, Parcelable {
    private int pk;
    private String nombre;
    private String provincia;
    private String direccion;
    private String codigo_postal;
    private String descripcion;

    public Protectora(int pk, String nombre, String direccion, String provincia, String codigo_postal, String descripcion) {
        this.setPk(pk);
        this.setNombre(nombre);
        this.setDireccion(direccion);
        this.setProvincia(provincia);
        this.setCodigo_postal(codigo_postal);
        this.setDescripcion(descripcion);
    }

    protected Protectora(Parcel in) {
        pk = in.readInt();
        nombre = in.readString();
        provincia = in.readString();
        direccion = in.readString();
        codigo_postal = in.readString();
        descripcion = in.readString();
    }

    public static final Creator<Protectora> CREATOR = new Creator<Protectora>() {
        @Override
        public Protectora createFromParcel(Parcel in) {
            return new Protectora(in);
        }

        @Override
        public Protectora[] newArray(int size) {
            return new Protectora[size];
        }
    };

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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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
        try {
            setDescripcion(json.getString(Tags.DESCRIPCION));
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
                ", descripcion='" + descripcion + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(pk);
        dest.writeString(nombre);
        dest.writeString(provincia);
        dest.writeString(direccion);
        dest.writeString(codigo_postal);
        dest.writeString(descripcion);
    }
}
