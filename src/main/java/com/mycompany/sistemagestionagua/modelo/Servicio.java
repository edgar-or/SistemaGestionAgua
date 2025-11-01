
package com.mycompany.sistemagestionagua.modelo;


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
