package com.example.jblandii.protectora.Models;

import com.example.jblandii.protectora.peticionesBD.Tags;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jblandii on 24/04/18.
 */

public class Comunidad {
    private int pk;
    private String comunidad_autonoma;

    public Comunidad(int pk, String comunidad_autonoma) {
        this.setPk(pk);
        this.setComunidad_autonoma(comunidad_autonoma);
    }

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public String getComunidad_autonoma() {
        return comunidad_autonoma;
    }

    public void setComunidad_autonoma(String comunidad_autonoma) {
        this.comunidad_autonoma = comunidad_autonoma;
    }

    public Comunidad(JSONObject json) {
        try {
            setPk(json.getInt(Tags.PK));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            setComunidad_autonoma(json.getString(Tags.COMUNIDAD_AUTONOMA));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
