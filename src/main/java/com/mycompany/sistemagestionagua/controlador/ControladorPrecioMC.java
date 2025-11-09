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

    private final VistaAgregarPrecios vista;

    public ControladorPrecioMC(VistaAgregarPrecios vista) {
        this.vista = vista;
        this.vista.btnAgregarPrecioMC.addActionListener(this);
        this.vista.btnCerrarPrecioMC.addActionListener(this);
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
        try {

            BigDecimal precio = new BigDecimal(vista.txtPrecioMC.getText().trim());

            // guarda el precio global del sistema
            PrecioMC.precioActual = precio;

            JOptionPane.showMessageDialog(vista, "Precio guardado correctamente");
            vista.dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "Debe ingresar un número válido");
        }
    }
}
