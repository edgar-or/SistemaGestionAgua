/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemagestionagua.controlador;

import com.mycompany.sistemagestionagua.modelo.Base;
import com.mycompany.sistemagestionagua.modelo.Servicio;
import com.mycompany.sistemagestionagua.vista.VistaPrincipal;
import com.mycompany.sistemagestionagua.vista.vistaAgregarServicio;
import com.mycompany.sistemagestionagua.vista.vistaVerUsuarios;
import java.awt.Dimension;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author ayala
 */
public class ControladorServicio {

    private VistaPrincipal vistaPrincipal;
    private vistaAgregarServicio visAgregarServicio;
    private vistaVerUsuarios visVerUser;
    private Base base;
    private ControladorUsuario controladorUsuario;

    public ControladorServicio(VistaPrincipal vistaPrincipal, vistaVerUsuarios visUsuarios, ControladorUsuario controladorUsuario, Base base) {
        this.vistaPrincipal = vistaPrincipal;
        this.visAgregarServicio = new vistaAgregarServicio();
        this.visVerUser = visUsuarios;
        this.base = base;
        this.controladorUsuario = controladorUsuario;

        eventos();
    }

    private void eventos() {
        visVerUser.btnAgregarServicio.addActionListener(e -> mostrarAgregarServicio());
        visAgregarServicio.btnAgregarSer.addActionListener(e -> agregarServicio());
        visAgregarServicio.btnCerrar.addActionListener(e -> visAgregarServicio.dispose());

    }

    public void mostrarAgregarServicio() {
        String seleccionado = getUsuarioSeleccionado();

        if (seleccionado != null) {

            //consulta si ya fue agregada la vista
            if (!visAgregarServicio.isVisible()) {
                if (visAgregarServicio.getParent() == null) {
                    vistaPrincipal.escritorio.add(visAgregarServicio);
                }
            }

            visAgregarServicio.setSize(600, 400);

         

            // 2. Centrar
            Dimension desktopSize = vistaPrincipal.escritorio.getSize();
            Dimension internal = visAgregarServicio.getSize();
            int x = (desktopSize.width - internal.width) / 2;
            int y = (desktopSize.height - internal.height) / 2;
            visAgregarServicio.setLocation(x, y);

            visAgregarServicio.setVisible(true);
            visAgregarServicio.toFront(); // muestra al frenet

            visAgregarServicio.txtNumDui.setText(seleccionado);
            visAgregarServicio.txtNumDui.setEnabled(false);
            visAgregarServicio.txtNumCuenta.setText(generarNumCuenta());
            visAgregarServicio.txtNumCuenta.setEnabled(false);

        }

    }

    public void agregarServicio() {
        if (!visAgregarServicio.txtDireccion.getText().isEmpty()) {
            String dui = visAgregarServicio.txtNumDui.getText();
            String numCuenta = visAgregarServicio.txtNumCuenta.getText();
            String direccion = visAgregarServicio.txtDireccion.getText();

            try {
                base.agregarServicio(new Servicio(dui, numCuenta, direccion));
                JOptionPane.showMessageDialog(vistaPrincipal, "Servicio agregado correctamente", "ANDA", JOptionPane.INFORMATION_MESSAGE);
                visAgregarServicio.txtDireccion.setText("");
                controladorUsuario.mostrarUsersTabla(base.getUsuario());
                visAgregarServicio.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(vistaPrincipal, "El servicio NO se pudo agregar", "ANDA", JOptionPane.WARNING_MESSAGE);

            }

        }

    }

    private String getUsuarioSeleccionado() {
        int fila = visVerUser.tablaUsuarios.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(visVerUser, "Seleccione un usuario de la tabla");
            return null;

        }

        String dui = visVerUser.tablaUsuarios.getValueAt(fila, 1).toString(); // Columna 1 = DUI
        return dui;

    }

    private static int correlativo = 1;

    private String generarNumCuenta() {
        int año = java.time.Year.now().getValue(); // obtiene el año actual
        String numero = String.format("%04d", correlativo);
        correlativo++;
        return año + "-" + numero;
    }

}
