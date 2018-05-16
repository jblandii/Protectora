package com.example.jblandii.protectora.Models;

/**
 * Created by jblandii on 11/05/18.
 */

public class Tarea {
    private String funcion;
    private Object dato;
    private Object dato2;
    private Object datoExtra;

    public Tarea(String funcion, Object dato, Object dato2) {
        this.setFuncion(funcion);
        this.setDato(dato);
        this.setDato2(dato2);
    }

    public String getFuncion() {
        return funcion;
    }

    public void setFuncion(String funcion) {
        this.funcion = funcion;
    }

    public Object getDato() {
        return dato;
    }

    public void setDato(Object dato) {
        this.dato = dato;
    }

    public Object getDato2() {
        return dato2;
    }

    public void setDato2(Object dato2) {
        this.dato2 = dato2;
    }

    public Object getDatoExtra() {
        return datoExtra;
    }

    public void setDatoExtra(Object datoExtra) {
        this.datoExtra = datoExtra;
    }
}
