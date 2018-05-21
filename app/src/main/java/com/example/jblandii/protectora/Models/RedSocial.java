package com.example.jblandii.protectora.Models;

import com.example.jblandii.protectora.peticionesBD.Tags;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jblandii on 21/05/18.
 */

public class RedSocial {
    private int pk;
    private String red;
    private String valor;

    public RedSocial(int pk, String red, String valor) {
        this.pk = pk;
        this.red = red;
        this.valor = valor;
    }

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public String getRed() {
        return red;
    }

    public void setRed(String red) {
        this.red = red;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return "RedSocial{" +
                "pk=" + pk +
                ", red='" + red + '\'' +
                ", valor='" + valor + '\'' +
                '}';
    }

    public RedSocial(JSONObject json) {
        try {
            setPk(json.getInt(Tags.PK));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            setRed(json.getString(Tags.RED));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            setValor(json.getString(Tags.VALOR));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
