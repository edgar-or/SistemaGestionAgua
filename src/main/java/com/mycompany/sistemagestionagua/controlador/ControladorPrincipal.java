/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemagestionagua.controlador;

import com.mycompany.sistemagestionagua.modelo.ModeloPrincipal;
import com.mycompany.sistemagestionagua.vista.VistaPrincipal;
import javax.swing.JFrame;

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
        
        
        
        
        
        
        
        
    }
    
    
    
    
    public void iniciar(){
        vista.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        vista.setExtendedState(JFrame.MAXIMIZED_BOTH);
        vista.setVisible(true);
    }
    
    
    
    
}
