/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemagestionagua.modelo;

import java.math.BigDecimal;

/**
 *
 * @author MINEDUCYT
 */
public class PrecioMC {
    private BigDecimal precioMC;

    public PrecioMC(BigDecimal precioMC) {
        this.precioMC = precioMC;
    }

    public BigDecimal getPrecioMC() {
        return precioMC;
    }

    public void setPrecioMC(BigDecimal precioMC) {
        this.precioMC = precioMC;
    }
    
    public static BigDecimal precioActual = BigDecimal.ZERO;
    
}

