package com.example.coffeetimefoods;

public class Adicionales_Entidad {
    String sProducto;
    int iNumAdicional;
    String sNombre;
    int iPrecio;


    public Adicionales_Entidad(String sProducto, int iNumAdicional, String sNombre, int iPrecio) {
        this.sProducto = sProducto;
        this.iNumAdicional = iNumAdicional;
        this.sNombre = sNombre;
        this.iPrecio = iPrecio;

    }

    public int getiNumAdicional() {
        return iNumAdicional;
    }

    public void setiNumAdicional(int iNumAdicional) {
        this.iNumAdicional = iNumAdicional;
    }

    public String getsProducto() {
        return sProducto;
    }

    public void setsProducto(String sProducto) {
        this.sProducto = sProducto;
    }

    public String getsNombre() {
        return sNombre;
    }

    public void setsNombre(String sNombre) {
        this.sNombre = sNombre;
    }

    public int getiPrecio() {
        return iPrecio;
    }

    public void setiPrecio(int iPrecio) {
        this.iPrecio = iPrecio;
    }
}
