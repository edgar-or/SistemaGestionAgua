/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemagestionagua.controlador;

import com.mycompany.sistemagestionagua.modelo.Base;
import com.mycompany.sistemagestionagua.vista.VistaPrincipal;
import com.mycompany.sistemagestionagua.vista.verDetalleConsumo;
import java.awt.Dimension;

/**
 *
 * @author ayala
 */
public class ControladorVerDetalle {
    
    private verDetalleConsumo visVerDetalle; 
    private Base base; 
    private VistaPrincipal vistaPrincipal; 

    public ControladorVerDetalle(VistaPrincipal vistaPrincipal, Base base) {
        this.visVerDetalle = new verDetalleConsumo();
        this.vistaPrincipal= vistaPrincipal; 
        this.base = base;
        
        enventos();
        
        
    }

    private void enventos() {
        
        visVerDetalle.btnCerrar.addActionListener(e-> visVerDetalle.dispose());
    }
    
    
     public void mostrarVista() {
        visVerDetalle.setSize(600, 400);
        visVerDetalle.setVisible(true);

        // 2️⃣ Centrar la vista
        Dimension desktopSize = vistaPrincipal.escritorio.getSize();
        Dimension internal = visVerDetalle.getSize();
        int x = (desktopSize.width - internal.width) / 2;
        int y = (desktopSize.height - internal.height) / 2;
        visVerDetalle.setLocation(x, y);
        vistaPrincipal.escritorio.remove(visVerDetalle);
        vistaPrincipal.escritorio.add(visVerDetalle);

        // 3️⃣ Mostrar y traer al frente
        visVerDetalle.toFront();

    }
     
     
     
     
    
    
    
    
    
}
