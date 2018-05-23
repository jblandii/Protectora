package com.example.jblandii.protectora.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.jblandii.protectora.peticionesBD.Tags;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jblandii on 22/05/18.
 */

public class Mensaje implements Parcelable {
    private int pk;
    private Usuario usuario;
    private Protectora protectora;
    private String mensaje;
    private String hora;

    public Mensaje(int pk, Usuario usuario, Protectora protectora, String mensaje, String hora) {
        this.pk = pk;
        this.usuario = usuario;
        this.protectora = protectora;
        this.mensaje = mensaje;
        this.hora = hora;
    }

    protected Mensaje(Parcel in) {
        pk = in.readInt();
        usuario = in.readParcelable(Usuario.class.getClassLoader());
        protectora = in.readParcelable(Protectora.class.getClassLoader());
        mensaje = in.readString();
        hora = in.readString();
    }

    public static final Creator<Mensaje> CREATOR = new Creator<Mensaje>() {
        @Override
        public Mensaje createFromParcel(Parcel in) {
            return new Mensaje(in);
        }

        @Override
        public Mensaje[] newArray(int size) {
            return new Mensaje[size];
        }
    };

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getHora() {
        return hora;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Protectora getProtectora() {
        return protectora;
    }

    public void setProtectora(Protectora protectora) {
        this.protectora = protectora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public Mensaje(JSONObject json) {
        try {
            setPk(json.getInt(Tags.PK));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            setUsuario(new Usuario(json.getJSONObject(Tags.USUARIO)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            setProtectora(new Protectora(json.getJSONObject(Tags.PROTECTORA)));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            setMensaje(json.getString(Tags.MSG));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            setHora(json.getString(Tags.HORA));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(pk);
        dest.writeParcelable(usuario, flags);
        dest.writeParcelable(protectora, flags);
        dest.writeString(mensaje);
        dest.writeString(hora);
    }
}
