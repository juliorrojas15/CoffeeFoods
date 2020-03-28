package com.example.coffeetimefoods;

public class Opciones_Entidad {
    String sProducto;
    int iNumOpcion;
    String sNombre;
    int iPrecio;


    public Opciones_Entidad(String sProducto, int iNumOpcion, String sNombre, int iPrecio) {
        this.sProducto = sProducto;
        this.iNumOpcion = iNumOpcion;
        this.sNombre = sNombre;
        this.iPrecio = iPrecio;

    }

    public int getiNumOpcion() {
        return iNumOpcion;
    }

    public void setiNumOpcion(int iNumOpcion) {
        this.iNumOpcion = iNumOpcion;
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
