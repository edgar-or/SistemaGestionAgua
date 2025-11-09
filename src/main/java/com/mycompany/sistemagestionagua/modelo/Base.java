
package com.mycompany.sistemagestionagua.modelo;

import java.util.ArrayList;
import java.util.HashSet;

public class Base {

    ArrayList<Usuario> usuarios;
    ArrayList<Servicio> servicios;
    ArrayList<ModeloRuta> rutas; 
    ArrayList<ModeloConsumo> consumos;

    public Base() {
        usuarios = new ArrayList<>();
        servicios = new ArrayList<>();
        rutas = new ArrayList<>();
        consumos= new ArrayList<>();
        
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

    public ArrayList<ModeloRuta> getRutas() {
        return rutas;
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


    public ArrayList<String> bNumCuenta(String dui) {
        ArrayList<String> temp = new ArrayList<>();
        
        for (Servicio servicio : servicios) {
            if (dui.equals(servicio.getDuiPropietario())) {
                temp.add(servicio.getnumeroCuenta());
            }
        }
        return temp;
    }

    

    public String buscarServicioPorNumCuenta(String buscar) {
        for (Servicio servicio : servicios) {
            if (servicio.equals(buscar)) {
                return servicio.getDuiPropietario();

            }
        }
        return null;
    }
    public Servicio datosServicios(String numCuenta){
        for (Servicio servi : servicios) {
            if (numCuenta.equals(servi.getNumeroCuenta())) {
                return servi; 
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
    
    //arralist de metodo servicios
    
    
    public String nombrePorDui(String dui){
        for (Usuario usuario : usuarios) {
            if (dui.equals(usuario.getDui())) {
                return usuario.getNombre();
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
    
    public String buscarPorApellido(String apellido){
        for (Usuario usuario : usuarios) {
            if (usuario.getNombre().toLowerCase().contains(apellido.toLowerCase())) {
                return usuario.getDui();
            }
        }
        return null; // si no encuentra, retorna null
    }
    
    
    
    
    
    public ArrayList<Servicio> buscarServicio(String identificador, String busca) {
        ArrayList<Servicio> temp = new ArrayList<>();
        Servicio encontrado = null;

        for (Servicio servicio : servicios) {
            if (identificador.equalsIgnoreCase("dui") && servicio.getDuiPropietario().toLowerCase().contains(busca.toLowerCase())) {
                encontrado = servicio;
                temp.add(encontrado);

            }

        }
        return temp;

    
    }
    
    
     public boolean modificarServicio(String numCuenta, String nuevoIdRuta, String nuevoNumMedidor, String NuevoMetrosCubicos) {
        for (Servicio servi : servicios) {
            if (servi.getNumeroCuenta().equals(numCuenta)) {
                servi.setIdRuta(nuevoIdRuta); 
                servi.setNumMedidor(nuevoNumMedidor);
                servi.setMetrosCubicos(Integer.parseInt(NuevoMetrosCubicos));                        
                return true; // Se modificó con éxito

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
    
    
    //logica de rutas. 
    
    public boolean agregarRuta(ModeloRuta o){
        try {
            rutas.add(o);
            return true; 
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean modificarRuta(String id, String depto, String muni, String col,String desc){
        for (ModeloRuta ruta : rutas) {
            if (ruta.getId().equals(id)) {
                ruta.setDepartamento(depto);
                ruta.setColonia(col);
                ruta.setDescripcion(desc);
                ruta.setMunicipio(muni);
                return true; 
            }
        }
        return false; 
    }
    
    public boolean eliminarRuta (String id){
        
        for (ModeloRuta ruta : rutas) {
            if (ruta.getId().equals(id)) {
                rutas.remove(ruta);
                return true;
            }
            
        }
        return false;
    }

    public String nombre(String n){
        for (Usuario usuario : usuarios) {
            if (usuario.getDui().equals(n)) {
                return usuario.getNombre();

            }
            else{
                return null;
            }
        }
        return null;
        
    }
    
    public String apellido(String a){
        for (Usuario usuario : usuarios) {
            if (usuario.getDui().equals(a)) {
                return usuario.getApellido();

            }
           
        }
        return null;
        
    }
    
        public boolean eliminarServicio (String NumCuenta){
        
        for (Servicio servicio : servicios) {
            if (servicio.getnumeroCuenta().equals(NumCuenta)) {
                servicios.remove(servicio);
                return true;
            }
            
        }
        return false;
    }
        
        public String rutaActual (String numCuenta){
            for (Servicio servicio : servicios) {
                if (numCuenta.equals(servicio.getNumeroCuenta())) {
                    return servicio.getIdRuta();
                }
            }
            return null; 
        }
         public boolean agregarConsumos(ModeloConsumo c) {
        try {
            consumos.add(c);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
