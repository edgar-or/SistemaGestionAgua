/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemagestionagua.modelo;

public class ModeloRuta {

    private String ruta;
    private String municipio;
    private String colonia;
    private String descripcion;

    public ModeloRuta(String ruta, String municipio, String colonia, String descripcion) {
        this.ruta = ruta;
        this.municipio = municipio;
        this.colonia = colonia;
        this.descripcion = descripcion;
    }

    public String getRuta() {
        return ruta;
    }

    public String getMunicipio() {
        return municipio;
    }

    public String getColonia() {
        return colonia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}
