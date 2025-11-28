/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemagestionagua.modelo;

/**
 *
 * @author ayala
 */
public class Usuario {

    private String nombre;
    private String apellido;
    private String dui;

    public Usuario(String nombre, String apellido, String dui) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dui = dui;
    }

    // 🔍 Método estático de validación
    public static boolean validarDUI(String dui) {
        return dui != null && dui.matches("^\\d{8}-\\d$");
    }
   

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getDui() {
        return dui;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setDui(String dui) {
        this.dui = dui;
    }
    
    

}
