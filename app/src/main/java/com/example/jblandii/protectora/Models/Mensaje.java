package com.example.jblandii.protectora.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.jblandii.protectora.peticionesBD.Tags;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jblandii on 22/05/18.
 */

public class Mensaje{
    private int pk;
    private Usuario usuario;
    private Protectora protectora;
    private String mensaje;
    private String hora;
    private String emisario;

    public Mensaje(int pk, Usuario usuario, Protectora protectora, String mensaje, String hora, String emisario) {
        this.pk = pk;
        this.usuario = usuario;
        this.protectora = protectora;
        this.mensaje = mensaje;
        this.hora = hora;
        this.emisario = emisario;
    }

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

    public String getEmisario() {
        return emisario;
    }

    public void setEmisario(String emisario) {
        this.emisario = emisario;
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
            setMensaje(json.getString(Tags.MSN));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            setHora(json.getString(Tags.HORA));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            setEmisario(json.getString(Tags.EMISARIO));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Mensaje{" +
                "pk=" + pk +
                ", usuario=" + usuario +
                ", protectora=" + protectora +
                ", mensaje='" + mensaje + '\'' +
                ", hora='" + hora + '\'' +
                ", emisario='" + emisario + '\'' +
                '}';
    }
}
