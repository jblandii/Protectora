package com.example.jblandii.protectora.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.jblandii.protectora.peticionesBD.Tags;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jblandii on 23/05/18.
 */

public class Conversacion implements Parcelable {

    private int pk;
    private Protectora protectora;

    public Conversacion(int pk, Protectora protectora) {
        this.pk = pk;
        this.protectora = protectora;
    }

    protected Conversacion(Parcel in) {
        pk = in.readInt();
        protectora = in.readParcelable(Protectora.class.getClassLoader());
    }

    public static final Creator<Conversacion> CREATOR = new Creator<Conversacion>() {
        @Override
        public Conversacion createFromParcel(Parcel in) {
            return new Conversacion(in);
        }

        @Override
        public Conversacion[] newArray(int size) {
            return new Conversacion[size];
        }
    };

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public Protectora getProtectora() {
        return protectora;
    }

    public void setProtectora(Protectora protectora) {
        this.protectora = protectora;
    }

    @Override
    public String toString() {
        return "Conversacion{" +
                "pk=" + pk +
                ", protectora=" + protectora +
                '}';
    }

    public Conversacion(JSONObject json) {
        try {
            setPk(json.getInt(Tags.PK));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            setProtectora(new Protectora(json.getJSONObject(Tags.PROTECTORA)));
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
        dest.writeParcelable(protectora, flags);
    }
}
