/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemagestionagua.modelo;

public class ModeloRuta {

    private String id;
    private String departamento;
    private String municipio;
    private String colonia;
    private String descripcion;

    public ModeloRuta() {
    }
    
    

    public ModeloRuta(String id, String departamento, String municipio, String colonia, String descripcion) {
        this.id = id;
        this.departamento = departamento;
        this.municipio = municipio;
        this.colonia = colonia;
        this.descripcion = descripcion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    

    public String getDepartametno() {
        return departamento;
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

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
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

    @Override
    public String toString() {
        return this.id;
    }
    
    

}
