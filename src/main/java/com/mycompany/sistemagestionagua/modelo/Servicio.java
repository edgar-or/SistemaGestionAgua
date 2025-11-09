
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

    public void setDuiPropietario(String duiPropietario) {
        this.duiPropietario = duiPropietario;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public void setIdRuta(String idRuta) {
        this.idRuta = idRuta;
    }

    public void setMetrosCubicos(int metrosCubicos) {
        this.metrosCubicos = metrosCubicos;
    }

    public void setNumMedidor(String numMedidor) {
        this.numMedidor = numMedidor;
    }
    
    
    
    
    
    
    
    
    
}
