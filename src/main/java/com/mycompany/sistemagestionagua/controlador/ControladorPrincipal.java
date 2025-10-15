/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemagestionagua.controlador;

import com.mycompany.sistemagestionagua.modelo.ModeloPrincipal;
import com.mycompany.sistemagestionagua.vista.VistaPrincipal;

/**
 *
 * @author ayala
 */
public class ControladorPrincipal {
    
    VistaPrincipal vista; 
    ModeloPrincipal modelo; 

    public ControladorPrincipal(VistaPrincipal vista, ModeloPrincipal modelo) {
        this.vista = vista;
        this.modelo = modelo;
        
        this.vista.txtMensaje.setText(modelo.mostrarMensaje(true));
        
    }
    
    public void iniciar(){
        vista.setLocationRelativeTo(vista);
        vista.setVisible(true);
    }
    
    
    
    
}
