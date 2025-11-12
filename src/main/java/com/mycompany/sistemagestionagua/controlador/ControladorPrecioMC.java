/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemagestionagua.controlador;

import com.mycompany.sistemagestionagua.modelo.PrecioMC;
import com.mycompany.sistemagestionagua.vista.VistaAgregarPrecios;
import com.mycompany.sistemagestionagua.vista.VistaPrincipal;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import javax.swing.JOptionPane;

public class ControladorPrecioMC implements ActionListener {

    private final VistaAgregarPrecios vista;
    private final VistaPrincipal vistaPrincipal;
    private boolean modoModificacion = false;

    public ControladorPrecioMC(VistaAgregarPrecios vista, VistaPrincipal vistaPrincipal) {
        this.vista = vista;
        this.vistaPrincipal = vistaPrincipal;

        this.vista.btnAgregarPrecioMC.addActionListener(this);
        this.vista.btnCerrarPrecioMC.addActionListener(this);
        this.vista.thPrecioMC.setEditable(false);
        this.vista.titlePrecio.setEditable(false);

        inicializarVista();
    }

    private void inicializarVista() {
        // Configurar tamaño
        vista.setSize(349, 265);
        // Agregar al escritorio si aún no está
        if (vista.getParent() == null) {
            vistaPrincipal.escritorio.add(vista);
        }

        // Centrar dentro del escritorio
        Dimension desktopSize = vistaPrincipal.escritorio.getSize();
        Dimension internal = vista.getSize();
        int x = (desktopSize.width - internal.width) / 2;
        int y = (desktopSize.height - internal.height) / 2;
        vista.setLocation(x, y);

        // Mostrar al final (después de ubicar)
        vista.setVisible(true);
        vista.toFront();

        // --- Lógica de inicialización ---
        if (PrecioMC.precioActual == null || PrecioMC.precioActual.compareTo(BigDecimal.ZERO) == 0) {
            vista.thPrecioMC.setText("0, Sin precio asignado");
            vista.txtPrecioMC.setVisible(true);
            vista.txtPrecioMC.setText("");
            vista.txtPrecioMC.setEditable(true);
            vista.btnAgregarPrecioMC.setText("Agregar Precio");
        } else {
            vista.thPrecioMC.setText(String.valueOf(PrecioMC.precioActual));
            vista.txtPrecioMC.setVisible(false);
            vista.txtPrecioMC.setText("");
            vista.titlePrecio.setVisible(false);
            vista.btnAgregarPrecioMC.setText("Modificar Precio");
        }

        vista.revalidate();
        vista.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnAgregarPrecioMC) {
            if (PrecioMC.precioActual == null || PrecioMC.precioActual.compareTo(BigDecimal.ZERO) == 0) {
                guardarPrecio();
            } else {
                if (!modoModificacion) {
                    modoModificacion = true;
                    vista.txtPrecioMC.setVisible(true);
                    vista.txtPrecioMC.setEditable(true);
                    vista.txtPrecioMC.setText(String.valueOf(PrecioMC.precioActual));
                    vista.txtPrecioMC.requestFocus();
                    vista.btnAgregarPrecioMC.setText("Guardar Cambio");
                } else {
                    guardarPrecio();
                    modoModificacion = false;
                    vista.txtPrecioMC.setVisible(false);
                    vista.txtPrecioMC.setEditable(false);
                    vista.btnAgregarPrecioMC.setText("Modificar Precio");
                    vista.titlePrecio.setVisible(false);
                }

                vista.revalidate();
                vista.repaint();
            }
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
            BigDecimal nuevoPrecio = new BigDecimal(txt);

            if (nuevoPrecio.compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(vista, "El precio debe ser mayor que 0.");
                return;
            }

            boolean esNuevo = (PrecioMC.precioActual == null || PrecioMC.precioActual.compareTo(BigDecimal.ZERO) == 0);
            PrecioMC.precioActual = nuevoPrecio;

            vista.thPrecioMC.setText(String.valueOf(nuevoPrecio));
            vista.txtPrecioMC.setText("");

            if (esNuevo) {
                JOptionPane.showMessageDialog(vista, "Precio agregado correctamente.");
                vista.txtPrecioMC.setVisible(false);
                vista.txtPrecioMC.setEditable(false);
                vista.btnAgregarPrecioMC.setText("Modificar Precio");
            } else {
                JOptionPane.showMessageDialog(vista, "Precio modificado correctamente.");
            }

            vista.revalidate();
            vista.repaint();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vista, "Dato inválido. Debe ingresar un número válido.");
        }
    }
}
