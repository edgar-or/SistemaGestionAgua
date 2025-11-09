/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemagestionagua.controlador;

import com.mycompany.sistemagestionagua.modelo.Base;
import com.mycompany.sistemagestionagua.modelo.ModeloRuta;
import com.mycompany.sistemagestionagua.modelo.Servicio;
import com.mycompany.sistemagestionagua.modelo.Usuario;
import com.mycompany.sistemagestionagua.vista.VistaPrincipal;
import com.mycompany.sistemagestionagua.vista.vistaModificarServicio;
import com.mycompany.sistemagestionagua.vista.vistaVerServicios;
import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ayala
 */
public class ControladorVerServicios {
    
    private VistaPrincipal vistaPrincipal; 
    private vistaVerServicios visVerServicios; 
    private Base base; 
    private vistaModificarServicio visModificarServicio;

    public ControladorVerServicios(VistaPrincipal visPrincipal, vistaVerServicios visVerServicios, Base base) {
        this.vistaPrincipal = visPrincipal;
        this.visVerServicios = visVerServicios;
        this.visModificarServicio = new vistaModificarServicio(); 
        this.base = base;
        
        
        eventos();
    }

    private void eventos() {
        visVerServicios.btnCerrar.addActionListener(e-> visVerServicios.dispose());
        visVerServicios.btnEliminar.addActionListener(e-> eliminarServicio() );
        visVerServicios.btnModificar.addActionListener(e-> mostrarModificar());
        visModificarServicio.btnCerrar.addActionListener(e-> visModificarServicio.dispose());
        
        

    }
    
    public void mostrarVista(){
        
         visVerServicios.setSize(800, 600);
        visVerServicios.setVisible(true);

        // 2️⃣ Centrar la vista
        Dimension desktopSize = vistaPrincipal.escritorio.getSize();
        Dimension internal = visVerServicios.getSize();
        int x = (desktopSize.width - internal.width) / 2;
        int y = (desktopSize.height - internal.height) / 2;
        visVerServicios.setLocation(x, y);
        vistaPrincipal.escritorio.remove(visVerServicios);
        vistaPrincipal.escritorio.add(visVerServicios);

        // 3️⃣ Mostrar y traer al frente
        visVerServicios.toFront();
        
        mostrarServicesTabla(base.getServicios());
        
    }
    
    private void mostrarModificar(){
        String seleccionado = getServicioSeleccionado();

        if (seleccionado != null) {

            //consulta si ya fue agregada la vista
            if (!visModificarServicio.isVisible()) {
                if (visModificarServicio.getParent() == null) {
                    vistaPrincipal.escritorio.add(visModificarServicio);
                }
            }

            visModificarServicio.setSize(600, 400);

            // 2. Centrar
            Dimension desktopSize = vistaPrincipal.escritorio.getSize();
            Dimension internal = visModificarServicio.getSize();
            int x = (desktopSize.width - internal.width) / 2;
            int y = (desktopSize.height - internal.height) / 2;
            visModificarServicio.setLocation(x, y);

            visModificarServicio.setVisible(true);
            visModificarServicio.toFront(); // muestra al frenet

            visModificarServicio.txtNumDui.setText(seleccionado);
            visModificarServicio.txtNumDui.setEnabled(false);
            visModificarServicio.txtNumCuenta.setText(base.datosServicios(seleccionado).getNumeroCuenta());
            visModificarServicio.txtNumCuenta.setEnabled(false);
            visModificarServicio.txtMetrosCubicos.setText(String.valueOf(base.datosServicios(seleccionado).getMetrosCubicos()) );
            visModificarServicio.txtNumMedidor.setText(base.datosServicios(seleccionado).getNumMedidor());
            
            
            llenarComboModificar(base.datosServicios(seleccionado).getIdRuta());


        }
                
        
    }
    
    
     
    public void llenarComboModificar(String idRutaActual) {
    // Objeto para almacenar la referencia al ModeloRuta que debe ser seleccionado
    ModeloRuta rutaASeleccionar = null;

    // 1. Llenar el combo con todos los objetos ModeloRuta
    for (ModeloRuta ruta : base.getRutas()) {
        // Almacenamos el objeto completo, no solo el ID
        visModificarServicio.comboRuta.addItem(ruta); 
        
        // 2. Comparar el ID de la ruta en la lista con el ID actual
        if (ruta.getId().equals(idRutaActual)) {
            // Cuando encontramos una coincidencia, guardamos el objeto completo
            rutaASeleccionar = ruta;
        }
    }
    
    // 3. Pre-seleccionar el elemento usando el objeto completo
    if (rutaASeleccionar != null) {
        // setSelectedItem necesita el objeto EXACTO para funcionar
        visModificarServicio.comboRuta.setSelectedItem(rutaASeleccionar);
    }
}

    
    private void eliminarServicio(){
        String seleccionado = getServicioSeleccionado(); 
        
        if (seleccionado!= null ) {
            
            int opcion = JOptionPane.showConfirmDialog(
                    visVerServicios,
                    "¿Está seguro que desea eliminar este servicio? ",
                    "Confirmación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );
            
             if (opcion == JOptionPane.YES_OPTION) { // Si confirma

                boolean eliminado = base.eliminarServicio(seleccionado);
                if (eliminado != false) {
                    JOptionPane.showMessageDialog(vistaPrincipal, "Servicio  eliminado con exito ", "ANDA", JOptionPane.INFORMATION_MESSAGE);
                    mostrarServicesTabla(base.getServicios());
                } else {
                    JOptionPane.showMessageDialog(vistaPrincipal, "El servicio NO se pudo eliminar", "ANDA", JOptionPane.WARNING_MESSAGE);
                }

            }
        } else {
            JOptionPane.showMessageDialog(vistaPrincipal, "Operacion cancelada ", "ANDA", JOptionPane.INFORMATION_MESSAGE);

        }

                
            
        }
        
    

    
    
    
    
    public void mostrarServicesTabla(ArrayList<Servicio> servicios) {
        DefaultTableModel modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // <-- evita edición en todas las columnas
            }
        };

        String titulos[] = {"N°", "N° Cuenta", "N° Dui Propietario","Nombre de propietario", "Apellido de propietario" ,"Id Ruta"};
        modeloTabla.setColumnIdentifiers(titulos);

        String resp = "";

        for (Servicio service : servicios) {
            
            String nombre  = base.nombrePorDui(service.getDuiPropietario());
            String apellido = base.apellido(service.getDuiPropietario());


            Object datos[] = {modeloTabla.getRowCount() + 1, service.getnumeroCuenta(), service.getDuiPropietario(),nombre, apellido, service.getIdRuta()};
            modeloTabla.addRow(datos);
        }
        this.visVerServicios.tablaServicios.setModel(modeloTabla);

    }
    
    
    private String getServicioSeleccionado() {
        int fila = visVerServicios.tablaServicios.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(visVerServicios, "Seleccione un servicio de la tabla");
            return null;

        }

        String numCuenta = visVerServicios.tablaServicios.getValueAt(fila, 1).toString(); // Columna 1 = DUI
        return numCuenta;

    }
    
    
    
    
    
}
