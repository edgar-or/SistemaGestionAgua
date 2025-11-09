/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemagestionagua.modelo;


public class ModeloConsumo {
    
    private String numeroCuenta;
    private String mes;
    private int metrosCubicos;

    public ModeloConsumo(String numeroCuenta, String Mes, int metrosCubicos) {
        this.numeroCuenta = numeroCuenta;
        this.mes = Mes;
        this.metrosCubicos = metrosCubicos;
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
    
    
            
    
}
