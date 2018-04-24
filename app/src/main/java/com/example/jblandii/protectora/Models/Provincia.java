package com.example.jblandii.protectora.Models;

import com.example.jblandii.protectora.peticionesBD.Tags;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jblandii on 24/04/18.
 */

public class Provincia {
    private int pk;
    private String provincia;

    public Provincia(int pk, String provincia) {
        this.setPk(pk);
        this.setProvincia(provincia);
    }

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public Provincia(JSONObject json) {
        try {
            setPk(json.getInt(Tags.PK));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            setProvincia(json.getString(Tags.COMUNIDAD_AUTONOMA));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Comunidad{" +
                "pk=" + pk +
                ", comunidad_autonoma='" + provincia + '\'' +
                '}';
    }
}
