/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemagestionagua.modelo;

/**
 *
 * @author USUARIO
 */
public class Servicio {
    private String duiPropietario;
    private String numeroCuenta;
    private String direccion; 

    public Servicio( String duiPropietario, String numeroCuenta, String direccion) {
        this.direccion = direccion;
        this.duiPropietario = duiPropietario;
        this.numeroCuenta = numeroCuenta;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getDuiPropietario() {
        return duiPropietario;
    }

    public String getnumeroCuenta() {
        return numeroCuenta;
    }
    
    
    
    
    
}
