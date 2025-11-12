/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemagestionagua.controlador;

import com.mycompany.sistemagestionagua.modelo.PrecioMC;
import com.mycompany.sistemagestionagua.vista.VistaAgregarPrecios;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import javax.swing.JOptionPane;

public class ControladorPrecioMC implements ActionListener {

    private  VistaAgregarPrecios vista;

   

    public ControladorPrecioMC(VistaAgregarPrecios vista) {
        this.vista = vista;
        this.vista.btnAgregarPrecioMC.addActionListener(this);
        this.vista.btnCerrarPrecioMC.addActionListener(this);


        this.vista.thPrecioMC.setEditable(false);
        this.vista.thPrecioMC.setText(String.valueOf(PrecioMC.precioActual));
    }
    
    

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == vista.btnAgregarPrecioMC) {
            guardarPrecio();
        }

        if (e.getSource() == vista.btnCerrarPrecioMC) {
            vista.dispose();
        }
    }

    private void guardarPrecio() {

        String txt = vista.txtPrecioMC.getText().trim();

        if (txt.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Por favor complete el campo de precio.");
            return;
        }

        try {
            BigDecimal precio = new BigDecimal(txt);
            PrecioMC.precioActual = precio;
            vista.thPrecioMC.setText(String.valueOf(precio));

            JOptionPane.showMessageDialog(vista, "Precio guardado correctamente.");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vista, "Dato inválido. Debe ingresar un número.");
        }
    }
}
