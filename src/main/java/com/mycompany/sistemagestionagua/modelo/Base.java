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
        servicios = new ArrayList<>();
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

    public ArrayList<Usuario> getUsuario() {
        return usuarios;
    }

    public ArrayList<Servicio> getServicios() {
        return servicios;
    }

    public ArrayList<Usuario> buscarUsuario(String identificador, String busca) {
        ArrayList<Usuario> temp = new ArrayList<>();
        Usuario encontrado = null;

        for (Usuario usuario : usuarios) {
            if (identificador.equalsIgnoreCase("nombre") && usuario.getNombre().toLowerCase().contains(busca.toLowerCase())) {
                encontrado = usuario;
                temp.add(encontrado);

            } else if (identificador.equalsIgnoreCase("apellido") && usuario.getApellido().toLowerCase().contains(busca.toLowerCase())) {
                encontrado = usuario;
                temp.add(encontrado);

            } else if (identificador.equalsIgnoreCase("dui") && usuario.getDui().equals(busca)) {
                encontrado = usuario;
                temp.add(encontrado);
            }

        }

        return temp;

    }

    public String bNumCuenta(String dui) {
        for (Servicio servicio : servicios) {
            if (dui.equals(servicio.getDuiPropietario())) {
                return servicio.getNumCuenta();
            }
        }
        return null;
    }

    public String buscarPorNombre(String nombre) {
        for (Usuario usuario : usuarios) {
            if (usuario.getNombre().toLowerCase().contains(nombre.toLowerCase())) {
                return usuario.getDui();
            }
        }
        return null; // si no encuentra, retorna null
    }

    public String buscarServicioPorNumCuenta(String buscar) {
        for (Servicio servicio : servicios) {
            if (servicio.equals(buscar)) {
                return servicio.getDuiPropietario();

            }
        }
        return null;
    }

    public String buscarServicio(String buscar) {
        for (Usuario usuario : usuarios) {
            if (usuario.getNombre().toLowerCase().contains(buscar.toLowerCase())) {
                return usuario.getDui();
            } else if (usuario.getDui().equals(buscar)) {
                return usuario.getDui();

            } else {
                buscarServicioPorNumCuenta(buscar);
            }
        }
        return null; // si no encuentra, retorna null
    }

    public Servicio encontrarServicio(String dui) {
        Servicio encontrado = null;

        for (Servicio servicio : servicios) {
            if (dui.equals(servicio.getDuiPropietario())) {
                encontrado = servicio;
                break;

            }

        }
        return encontrado;

    }

    public boolean eliminarUsuario(String dui) {

        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getDui().equals(dui)) {
                usuarios.remove(i);
                return true;
            }

        }
        return false;

    }

    public boolean modificarUsuario(String dui, String nuevoDui, String nuevoNombre, String nuevoApellido) {
        for (Usuario usuario : usuarios) {
            if (usuario.getDui().equals(dui)) {
                usuario.setNombre(nuevoNombre);
                usuario.setDui(nuevoDui);
                usuario.setApellido(nuevoApellido);
                        
                return true; // Se modificó con éxito

            }
        }
        return false;

    }

}
