
package com.mycompany.sistemagestionagua.modelo;


public class Servicio {
    private String duiPropietario;
    private String numeroCuenta;
    private String idRuta; 
    private int metrosCubicos; 
    private String numMedidor; 

    public Servicio( String duiPropietario, String numeroCuenta, String idRuta, int metrosCubicos, String numMedidor) {
        this.idRuta = idRuta;
        this.duiPropietario = duiPropietario;
        this.numeroCuenta = numeroCuenta;
        this.metrosCubicos = metrosCubicos; 
        this.numMedidor = numMedidor; 
    }
    
    public String getIdRuta() {
        return idRuta;
    }

    public String getDuiPropietario() {
        return duiPropietario;
    }

    public String getnumeroCuenta() {
        return numeroCuenta;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public int getMetrosCubicos() {
        return metrosCubicos;
    }

    public String getNumMedidor() {
        return numMedidor;
    }
    
    
    
    
    
    
    
}
