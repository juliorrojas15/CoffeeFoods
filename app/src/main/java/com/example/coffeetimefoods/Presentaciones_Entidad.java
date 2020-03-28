package com.example.coffeetimefoods;

public class Presentaciones_Entidad {
    String sProducto;
    int iNumPresentacion;
    String sNombre;
    int iPrecio;


    public Presentaciones_Entidad(String sProducto, int iNumPresentacion, String sNombre, int iPrecio) {
        this.sProducto = sProducto;
        this.iNumPresentacion = iNumPresentacion;
        this.sNombre = sNombre;
        this.iPrecio = iPrecio;

    }

    public int getiNumPresentacion() {
        return iNumPresentacion;
    }

    public void setiNumPresentacion(int iNumPresentacion) {
        this.iNumPresentacion = iNumPresentacion;
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
