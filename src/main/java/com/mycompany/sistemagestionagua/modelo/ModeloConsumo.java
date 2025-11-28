/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemagestionagua.modelo;


public class ModeloConsumo {
    
    private String numeroCuenta;
    private String idConsumo; 
    private String mes;
    private int numMes;
    private int año; 
    private int metrosCubicos;
    private boolean cancelado; 

    public ModeloConsumo(String numeroCuenta, String idConsumo, String Mes, int numMes, int año, int metrosCubicos, boolean cancelado) {
        this.numeroCuenta = numeroCuenta;
        this.mes = Mes;
        this.metrosCubicos = metrosCubicos;
        this.numMes = numMes;
        this.año = año; 
        this.cancelado = cancelado; 
        this.idConsumo = idConsumo; 
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public String getMes() {
        return mes;
    }

    public int getMetrosCubicos() {
        return metrosCubicos;
    }

    public int getNumMes() {
        return numMes;
    }

    public boolean isCancelado() {
        return cancelado;
    }

    public String getIdConsumo() {
        return idConsumo;
    }

    public void setCancelado(boolean cancelado) {
        this.cancelado = cancelado;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public void setIdConsumo(String idConsumo) {
        this.idConsumo = idConsumo;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public void setNumMes(int numMes) {
        this.numMes = numMes;
    }

    public void setMetrosCubicos(int metrosCubicos) {
        this.metrosCubicos = metrosCubicos;
    }

    public int getAño() {
        return año;
    }

    public void setAño(int año) {
        this.año = año;
    }
    
    
    
    
    
    
    
    
    
            
    
}
