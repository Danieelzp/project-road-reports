package com.hellokh.sovary.firetest;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class DerrumbeHueco implements Serializable
{


    @Exclude
    private String key;
    private String canton;
    private String distrito;
    private String severidad;
    private String estado;
    private String fecha;
    private String image;
    public DerrumbeHueco(){}
    public DerrumbeHueco(String canton, String distrito, String severidad, String estado, String fecha, String image)
    {
        this.canton = canton;
        this.distrito = distrito;
        this.severidad = severidad;
        this.estado = estado;
        this.fecha = fecha;
        this.image = image;
    }

    public String getCanton()
    {
        return canton;
    }

    public void setCanton(String canton)
    {
        this.canton = canton;
    }

    public String getDistrito()
    {
        return distrito;
    }

    public void setDistrito(String distrito)
    {
        this.distrito = distrito;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public String getSeveridad() {
        return severidad;
    }

    public void setSeveridad(String severidad) {
        this.severidad = severidad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
