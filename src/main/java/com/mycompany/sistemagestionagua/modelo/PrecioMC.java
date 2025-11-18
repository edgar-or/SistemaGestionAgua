/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemagestionagua.modelo;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 *
 * @author MINEDUCYT
 */
public class PrecioMC {

    private int desde;
    private int hasta;
    private BigDecimal precio;

    // Lista que contiene todos los registros de precios
    private static final ArrayList<PrecioMC> listaPrecios = new ArrayList<>();

    public PrecioMC(int desde, int hasta, BigDecimal precio) {
        this.desde = desde;
        this.hasta = hasta;
        this.precio = precio;
    }

    public int getDesde() {
        return desde;
    }

    public int getHasta() {
        return hasta;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setDesde(int desde) {
        this.desde = desde;
    }

    public void setHasta(int hasta) {
        this.hasta = hasta;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public static void agregar(PrecioMC p) {
        listaPrecios.add(p);
    }

    public static void eliminar(int index) {
        listaPrecios.remove(index);
    }

    public static PrecioMC obtener(int index) {
        return listaPrecios.get(index);
    }

    public static void modificar(int index, PrecioMC nuevo) {
        listaPrecios.set(index, nuevo);
    }

    public static int total() {
        return listaPrecios.size();
    }

    public static ArrayList<PrecioMC> obtenerTodos() {
        return listaPrecios;
    }

}
