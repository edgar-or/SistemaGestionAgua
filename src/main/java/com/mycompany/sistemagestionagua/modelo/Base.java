/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemagestionagua.modelo;

import java.util.ArrayList;

/**
 *
 * @author ayala
 */
public class Base {
    
    ArrayList<Usuario> usuarios; 

     public Base() {
        usuarios = new ArrayList<>();
    }

    public boolean agregar(Usuario e) {
        try {
            usuarios.add(e);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public ArrayList<Usuario> getEmpleados() {
        return usuarios;
    }

    public Usuario buscar(String texto) {
        Usuario encontrado = null;

        for (Usuario empleado : usuarios) {
            if (empleado.getNombre().toLowerCase().contains(texto.toLowerCase())) {
                encontrado = empleado;
                break;

            }

        }
        return encontrado;

    }

    public ArrayList<Usuario> buscarTodos(String texto) {

        ArrayList<Usuario> temp = new ArrayList<>();
        for (Usuario empleado : usuarios) {
            if (empleado.getNombre().toLowerCase().contains(texto.toLowerCase())) {
                temp.add(empleado);

            }

        }

        if (temp.isEmpty()) {
            return null;

        } else {
            return temp;
        }

    }
    
    
    
}
