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
    private String numCuenta; 
    private String duiPropietario;
    private String direccion; 

    public Servicio(String numCuenta, String duiPropietario, String direccion) {
        this.numCuenta = numCuenta;
        this.duiPropietario = duiPropietario;
        this.direccion = direccion;
    }

    public String getNumCuenta() {
        return numCuenta;
    }

    public String getDuiPropietario() {
        return duiPropietario;
    }

    public String getDireccion() {
        return direccion;
    }
    
    
    
    
    
}
