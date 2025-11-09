package com.mycompany.sistemagestionagua.controlador;

import com.mycompany.sistemagestionagua.modelo.Base;
import com.mycompany.sistemagestionagua.modelo.ModeloPrincipal;
import com.mycompany.sistemagestionagua.modelo.ModeloRuta;
import com.mycompany.sistemagestionagua.modelo.Servicio;
import com.mycompany.sistemagestionagua.modelo.Usuario;
import com.mycompany.sistemagestionagua.vista.VistaAgregarConsumo;
import com.mycompany.sistemagestionagua.vista.VistaAgregarUsuario;
import com.mycompany.sistemagestionagua.vista.VistaConsultarUsurioIndividual;
import com.mycompany.sistemagestionagua.vista.VistaEliminarUsuario;
import com.mycompany.sistemagestionagua.vista.VistaModificarUsuario;
import com.mycompany.sistemagestionagua.vista.VistaPrincipal;
import com.mycompany.sistemagestionagua.vista.VistaRuta;
import com.mycompany.sistemagestionagua.vista.vistaAgregarServicio;
import com.mycompany.sistemagestionagua.vista.vistaVerServicios;
import com.mycompany.sistemagestionagua.vista.vistaVerUsuarios;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class ControladorPrincipal {

    VistaPrincipal vista;
    Base base;
    vistaVerUsuarios visVerUsers;
    ControladorUsuario controladorUsuario;
    ControladorServicio controladorServicio;
    ControladorRuta controladorRuta;
    vistaVerServicios visVerServices;
    ControladorVerServicios controladorVerServices; 
    ControladorConsumo controladorConsumo;

    public ControladorPrincipal(VistaPrincipal vista) {
        this.vista = vista;
        this.visVerServices = new vistaVerServicios();
        this.base = new Base();
        this.visVerUsers = new vistaVerUsuarios();
        this.controladorUsuario = new ControladorUsuario(vista, visVerUsers,base);
        this.controladorServicio = new ControladorServicio(vista, controladorUsuario, visVerUsers,base);
        this.controladorRuta = new ControladorRuta(base, vista); 
        this.controladorVerServices = new ControladorVerServicios(vista,visVerServices, base);
        this.controladorConsumo= new ControladorConsumo(vista, base,  visVerServices, controladorVerServices);
        
        onEvento();
        llenarDatos();
        
        

    }

    public void iniciar() {
        vista.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        vista.setExtendedState(JFrame.MAXIMIZED_BOTH);
        vista.setVisible(true);
    }

    private void onEvento() {
        
        
        vista.menuRegistrarUsuario.addActionListener(e->controladorUsuario.mostrarVistaAgregar());
        vista.menuVerUsers.addActionListener(e-> controladorUsuario.mostrarVistaVerUsuarios());
        visVerUsers.btnAgregarServicio.addActionListener(e-> controladorServicio.mostrarAgregarServicio());
        vista.menuVerRutas.addActionListener(e-> controladorRuta.mostrarVista());
        vista.menuVerServicios.addActionListener(e-> controladorVerServices.mostrarVista());
        visVerServices.btnAgrgarConsumo.addActionListener(e-> controladorConsumo.mostrarVista());
        
  

    } //no tocar
    
    private void llenarDatos(){
        base.agregar(new Usuario("Edgar", "Ayala", "12345678-1"));
        base.agregar(new Usuario("Orladno", "Alvarez", "12345678-2"));
        
        base.agregarRuta(new ModeloRuta("1", "San Vicente", "San Vicente", "La Arenera", "Calle prinicpal a Lempa"));
        base.agregarRuta(new ModeloRuta("2", "San Vicente", "San Vicente", "Los Jobos", "Calle prinicpal a Iglesia catolica"));
        
        base.agregarServicio(new Servicio("12345678-1", "2025-0001", "1", 3, "12"));
        base.agregarServicio(new Servicio("12345678-2", "2025-0003", "2", 4, "13"));


    }
}


