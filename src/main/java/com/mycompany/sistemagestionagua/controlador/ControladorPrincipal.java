package com.mycompany.sistemagestionagua.controlador;

import com.mycompany.sistemagestionagua.modelo.Base;
import com.mycompany.sistemagestionagua.modelo.LoginModelo;
import com.mycompany.sistemagestionagua.modelo.ModeloConsumo;
import com.mycompany.sistemagestionagua.modelo.ModeloPrincipal;
import com.mycompany.sistemagestionagua.modelo.ModeloRuta;
import com.mycompany.sistemagestionagua.modelo.Servicio;
import com.mycompany.sistemagestionagua.modelo.Usuario;
import com.mycompany.sistemagestionagua.vista.LoginVista;
import com.mycompany.sistemagestionagua.vista.VistaAgregarConsumo;
import com.mycompany.sistemagestionagua.vista.VistaAgregarUsuario;
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
    ControladorVerConsumos controladorVerConusmos; 
    ModeloConsumo consumo; 
    ControladorVerPagos controladorVerPagos;
  

    public ControladorPrincipal(VistaPrincipal vista, Base base) {
        this.vista = vista;
        this.base = base; 
        this.visVerServices = new vistaVerServicios();
        this.visVerUsers = new vistaVerUsuarios();
        this.controladorUsuario = new ControladorUsuario(vista, visVerUsers,base);
        this.controladorServicio = new ControladorServicio(vista, controladorUsuario, visVerUsers,base);
        this.controladorRuta = new ControladorRuta(base, vista); 
        this.controladorVerServices = new ControladorVerServicios(vista,visVerServices, base);
        this.controladorConsumo= new ControladorConsumo(vista, base,  visVerServices, controladorVerServices);
        this.controladorVerConusmos = new ControladorVerConsumos(vista,consumo , base);
        this.controladorVerPagos = new ControladorVerPagos(vista, base);
        
        
        
        
        onEvento();
        
        

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
        vista.menuVerConsumos.addActionListener(e-> controladorVerConusmos.mostrarVista());
        
        vista.menuPagos.addActionListener(e-> controladorVerPagos.mostrarVista());
        
        
        //cerrar sesion
        
  

    } //no tocar
        

    
    
   
}


