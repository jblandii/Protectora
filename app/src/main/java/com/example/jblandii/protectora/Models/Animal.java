package com.example.jblandii.protectora.Models;

import com.example.jblandii.protectora.peticionesBD.Tags;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jblandii on 25/04/18.
 */

public class Animal {
    private int pk;
    private String mascota;
    private String raza;
    private String color;
    private String edad;
    private String tipo_pelaje;
    private String sexo;
    private String tamano;
    private String peso;
    private String enfermedad;
    private String vacuna;
    private String chip;
    private String id_protectora;
    private String me_gusta;
    private String descripcion;

    public Animal(int pk, String mascota, String raza, String color, String edad, String tipo_pelaje, String sexo, String tamano, String peso,
                  String enfermedad, String vacuna, String chip, String id_protectora, String me_gusta, String descripcion) {
        this.setPk(pk);
        this.setMascota(mascota);
        this.setRaza(raza);
        this.setColor(color);
        this.setEdad(edad);
        this.setTipo_pelaje(tipo_pelaje);
        this.setSexo(sexo);
        this.setTamano(tamano);
        this.setPeso(peso);
        this.setEnfermedad(enfermedad);
        this.setVacuna(vacuna);
        this.setChip(chip);
        this.setId_protectora(id_protectora);
        this.setMe_gusta(me_gusta);
        this.setMe_gusta(descripcion);
    }

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public String getMascota() {
        return mascota;
    }

    public void setMascota(String mascota) {
        this.mascota = mascota;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public String getTipo_pelaje() {
        return tipo_pelaje;
    }

    public void setTipo_pelaje(String tipo_pelaje) {
        this.tipo_pelaje = tipo_pelaje;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getTamano() {
        return tamano;
    }

    public void setTamano(String tamano) {
        this.tamano = tamano;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public String getEnfermedad() {
        return enfermedad;
    }

    public void setEnfermedad(String enfermedad) {
        this.enfermedad = enfermedad;
    }

    public String getVacuna() {
        return vacuna;
    }

    public void setVacuna(String vacuna) {
        this.vacuna = vacuna;
    }

    public String getChip() {
        return chip;
    }

    public void setChip(String chip) {
        this.chip = chip;
    }

    public String getId_protectora() {
        return id_protectora;
    }

    public void setId_protectora(String id_protectora) {
        this.id_protectora = id_protectora;
    }

    public String getMe_gusta() {
        return me_gusta;
    }

    public void setMe_gusta(String me_gusta) {
        this.me_gusta = me_gusta;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Animal(JSONObject json) {
        try {
            setPk(json.getInt(Tags.PK));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            setMascota(json.getString(Tags.MASCOTA));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            setRaza(json.getString(Tags.RAZA));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            setColor(json.getString(Tags.COLOR));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            setEdad(json.getString(Tags.EDAD));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            setTipo_pelaje(json.getString(Tags.PELAJE));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            setSexo(json.getString(Tags.SEXO));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            setTamano(json.getString(Tags.TAMANO));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            setPeso(json.getString(Tags.PESO));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            setEnfermedad(json.getString(Tags.ENFERMEDAD));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            setVacuna(json.getString(Tags.VACUNA));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            setChip(json.getString(Tags.CHIP));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            setId_protectora(json.getString(Tags.ID_PROTECTORA));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            setMe_gusta(json.getString(Tags.MEGUSTA));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            setDescripcion(json.getString(Tags.DESCRIPCION));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
