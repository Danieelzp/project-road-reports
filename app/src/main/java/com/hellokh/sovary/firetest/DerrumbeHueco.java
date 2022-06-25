package com.hellokh.sovary.firetest;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class DerrumbeHueco implements Serializable
{


    @Exclude
    private String key;
    private String canton;
    private String distrito;
    public DerrumbeHueco(){}
    public DerrumbeHueco(String canton, String distrito)
    {
        this.canton = canton;
        this.distrito = distrito;
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
}
