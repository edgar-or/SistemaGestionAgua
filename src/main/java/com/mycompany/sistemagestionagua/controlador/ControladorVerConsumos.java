/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemagestionagua.controlador;

import com.mycompany.sistemagestionagua.modelo.Base;
import com.mycompany.sistemagestionagua.modelo.ModeloConsumo;
import com.mycompany.sistemagestionagua.modelo.PrecioMC;
import com.mycompany.sistemagestionagua.modelo.Servicio;
import com.mycompany.sistemagestionagua.vista.VistaPrincipal;
import com.mycompany.sistemagestionagua.vista.VistaVerConsumos;
import java.awt.Dimension;
import java.math.BigDecimal;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ayala
 */
public class ControladorVerConsumos {
    
    private VistaVerConsumos visVerConsumos; 
    private VistaPrincipal vistaPrincipal; 
    private Base base; 
    private ModeloConsumo consumo; 
    private PrecioMC precio;

    public ControladorVerConsumos(VistaPrincipal vistaPrincipal , ModeloConsumo consumo, Base base) {
        this.visVerConsumos = new VistaVerConsumos();
        this.vistaPrincipal = vistaPrincipal;
        this.base = base;
        this.consumo = consumo; 
        
        eventos(); 
    }

    private void eventos() {
        visVerConsumos.btnCerrar.addActionListener(e-> visVerConsumos.dispose());
        visVerConsumos.btnBuscar.addActionListener(e-> buscarConsumo());
        
        
          // --- Desactivar buscadores ---
        eventoCampo(visVerConsumos.txtBuscar1, visVerConsumos.txtBuscar2, visVerConsumos.txtBuscar3);
        eventoCampo(visVerConsumos.txtBuscar2, visVerConsumos.txtBuscar1, visVerConsumos.txtBuscar3);
        eventoCampo(visVerConsumos.txtBuscar3, visVerConsumos.txtBuscar1, visVerConsumos.txtBuscar2);
    }
    
    public void mostrarVista(){
        visVerConsumos.setSize(1000, 600);
        visVerConsumos.setVisible(true);

        // 2️⃣ Centrar la vista
        Dimension desktopSize = vistaPrincipal.escritorio.getSize();
        Dimension internal = visVerConsumos.getSize();
        int x = (desktopSize.width - internal.width) / 2;
        int y = (desktopSize.height - internal.height) / 2;
        visVerConsumos.setLocation(x, y);
        vistaPrincipal.escritorio.remove(visVerConsumos);
        vistaPrincipal.escritorio.add(visVerConsumos);

        // 3️⃣ Mostrar y traer al frente
        visVerConsumos.toFront();

        mostrarConsumosTabla(base.getConsumos());
    }
    
   

    private void mostrarConsumosTabla(ArrayList<ModeloConsumo> consumos) {
          DefaultTableModel modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // <-- evita edición en todas las columnas
            }
        };

        String titulos[] = {"N°", "N° Cuenta", "N° de DUI" ,"Nombre de propietario", "Mes Lectura", "Lectura anterior", "Lectura actual", "Metros consumidos", "Monto"};
        modeloTabla.setColumnIdentifiers(titulos);

        String resp = "";
        
        int metrosConsumidos = 0; 
        int lecturaAnterior = 0;
        

        for (ModeloConsumo cons : consumos) {
            
            Servicio serv = base.encontrarServicioPorNumCuenta(cons.getNumeroCuenta());

            String nombre = base.nombrePorDui(serv.getDuiPropietario());
            String apellido = base.apellido(serv.getDuiPropietario());
            
            
            if (cons.getNumMes()-1 == 0) {
                lecturaAnterior = serv.getMetrosCubicos();
                metrosConsumidos= cons.getMetrosCubicos() - lecturaAnterior; 
            }else{
                lecturaAnterior = base.obtenerLecturaAnterior(cons.getNumMes()-1); 

                metrosConsumidos = (cons.getMetrosCubicos()) - lecturaAnterior;

            }
            
            
            BigDecimal metros = BigDecimal.valueOf(metrosConsumidos);
            
            BigDecimal precioo = precio.precioActual; 
            
            BigDecimal monto =  metros.multiply(precioo);

            Object datos[] = {modeloTabla.getRowCount() + 1, cons.getNumeroCuenta(), serv.getDuiPropietario() ,nombre + " " + apellido, cons.getMes(), lecturaAnterior, cons.getMetrosCubicos(),metrosConsumidos , monto  };
            modeloTabla.addRow(datos);
        }
        this.visVerConsumos.tablaConsumos.setModel(modeloTabla);

    }
    
     private void buscarConsumo() {
        String busca = null;
        String identificador = null;
        if (!visVerConsumos.txtBuscar1.getText().isEmpty()) {
            identificador = "dui";
            busca = visVerConsumos.txtBuscar1.getText().trim();

        } else if (!visVerConsumos.txtBuscar2.getText().isEmpty()) {
            identificador = "cuenta";
            busca = visVerConsumos.txtBuscar2.getText().trim();

        } else if (!visVerConsumos.txtBuscar3.getText().isEmpty()) {
            identificador = "nombre";
            busca = visVerConsumos.txtBuscar3.getText().trim();

        }
        if (busca == null) {
            JOptionPane.showMessageDialog(vistaPrincipal, "Ingrese una informacion de Usuario para buscar", "ANDA", JOptionPane.WARNING_MESSAGE);
            mostrarConsumosTabla(base.getConsumos());
            //detiene la ejecucion
            return;
        } else if (base.buscarUsuario(identificador, busca).isEmpty()) {
            mostrarConsumosTabla(base.getConsumos());
            JOptionPane.showMessageDialog(vistaPrincipal, "No se encontraron usuarios con la informacion ingresada", "ANDA", JOptionPane.WARNING_MESSAGE);
            visVerConsumos.txtBuscar1.setText("");
            visVerConsumos.txtBuscar2.setText("");
            visVerConsumos.txtBuscar3.setText("");
            //detiene la ejecucion
            return;
        }

        mostrarConsumosTabla(base.buscarConsumo(identificador, busca));

    }
    
    
      //evento que desabilita los textfield de los buscadores 
    private void eventoCampo(JTextField activo, JTextField... otros) {
        activo.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                bloquear();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                bloquear();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                bloquear();
            }

            private void bloquear() {
                if (!activo.getText().isEmpty()) {
                    for (JTextField t : otros) {
                        t.setEditable(false);
                    }
                } else {
                    for (JTextField t : otros) {
                        t.setEditable(true);
                    }
                }
            }
        });

    }
    
    
    
    
}
