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
    ArrayList<Servicio> servicios; 

     public Base() {
        usuarios = new ArrayList<>();
        servicios= new ArrayList<>();
    }

    public boolean agregar(Usuario e) {
        try {
            usuarios.add(e);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
    
    public boolean agregarServicio(Servicio s) {
        try {
            servicios.add(s);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public ArrayList<Usuario> getEmpleados() {
        return usuarios;
    }

    public ArrayList<Servicio> getServicios() {
        return servicios;
    }

    public Usuario buscar(String texto) {
        Usuario encontrado = null;

        for (Usuario usuario : usuarios) {
            if (usuario.getNombre().toLowerCase().contains(texto.toLowerCase())) {
                encontrado = usuario;
                break;

            }

        }
        return encontrado;

    }
    
    public Servicio buscarServicioPorDui(String dui) {
        Servicio encontrado = null;

        for (Servicio servicio : servicios) {
            if (servicio.getDuiPropietario().contains(dui)) {
                encontrado = servicio;
                break;

            }

        }
        return encontrado;

    }
    
     public String buscarPorNombre(String nombre) {
    for (Usuario usuario : usuarios) {
        if (usuario.getNombre().toLowerCase().contains(nombre.toLowerCase())) {
            return usuario.getDui();
        }
    }
    return null; // si no encuentra, retorna null
}
     
     
      public Servicio buscarServicio(String dui) {
        Servicio encontrado = null;
        

        for (Servicio servicio : servicios) {
            if (dui.equals(servicio.getDuiPropietario())) {
                encontrado = servicio;
                break;

            }

        }
        return encontrado;

    }
      
      
      
       public Servicio buscarServicioPorNumCuenta(String numCuenta){
           Servicio encontrado = null; 
           
           for (Servicio servicio : servicios) {
               if (numCuenta.equals(servicio.getNumCuenta())) {
                   encontrado =servicio;
                   break; 
               }
           }
           
           return encontrado; 
       }
       
       
       public boolean eliminarServicio(String dui) {
    return usuarios.removeIf(servicio -> servicio.getDui().equals(dui));
}
     
     
      
     
    
    
    
    public Usuario buscarEliminar(String texto) {
        Usuario encontrado = null;

        for (Usuario usuario : usuarios) {
            if (usuario.getDui().equals(texto)) {
                encontrado = usuario;
                break;

            }

        }
        return encontrado;

    }
    

    public ArrayList<Usuario> buscarTodos(String texto) {

        ArrayList<Usuario> temp = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            if (usuario.getNombre().toLowerCase().contains(texto.toLowerCase())) {
                temp.add(usuario);

            }

        }

        if (temp.isEmpty()) {
            return null;

        } else {
            return temp;
        }

    }
    
    
    public boolean eliminar(String texto) {
    return usuarios.removeIf(usuario -> usuario.getDui().equals(texto));
}
    
    
    
}
