 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemagestionagua.controlador;

import com.mycompany.sistemagestionagua.modelo.Base;
import com.mycompany.sistemagestionagua.modelo.ModeloConsumo;
import com.mycompany.sistemagestionagua.modelo.Usuario;
import com.mycompany.sistemagestionagua.vista.VistaAgregarConsumo;
import com.mycompany.sistemagestionagua.vista.VistaPrincipal;
import com.mycompany.sistemagestionagua.vista.vistaVerServicios;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class ControladorConsumo {

    private VistaPrincipal vistaPrincipal;
    private Base base;
    private VistaAgregarConsumo vistaAgrgarConsumo;
    private vistaVerServicios vistaVerServicios;
    private ControladorVerServicios controladorVerServicios;

    public ControladorConsumo(VistaPrincipal vistaPrincipal, Base base, vistaVerServicios vistaVerServicios, ControladorVerServicios controladorVerServicios) {
        this.vistaPrincipal = vistaPrincipal;
        this.base = base;
        this.vistaAgrgarConsumo = new VistaAgregarConsumo();
        this.vistaVerServicios = vistaVerServicios;
        this.controladorVerServicios = controladorVerServicios;

        onEvento();
    }

    private void onEvento() {
        vistaAgrgarConsumo.btnCerrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                vistaAgrgarConsumo.dispose();
            }
        });

        vistaAgrgarConsumo.btnAgrgarConsumo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String Consumo = vistaAgrgarConsumo.txtConsumo.getText();
                String numCuenta = vistaAgrgarConsumo.txtNumeroCuenta.getText();
                String mesSeleccionado = (String) vistaAgrgarConsumo.comboAgragarC.getSelectedItem();
                int indiceSelec = vistaAgrgarConsumo.comboAgragarC.getSelectedIndex();

                int numMes = indiceSelec + 1;

                if (Consumo.isEmpty()) {
                    JOptionPane.showMessageDialog(vistaPrincipal, "Complete los campos", "ANDA", JOptionPane.WARNING_MESSAGE);

                } else {
                    String idConsumo = generarSiguienteIdConsumo();
                    
                    if (base.agregarConsumos(new ModeloConsumo(numCuenta,idConsumo, mesSeleccionado, numMes,  Integer.parseInt(Consumo), false)) != false) {
                        JOptionPane.showMessageDialog(vistaPrincipal, "Consumo Guardado", "ANDA", JOptionPane.INFORMATION_MESSAGE);
                        vistaAgrgarConsumo.dispose();
                    } else {
                        JOptionPane.showMessageDialog(vistaPrincipal, "Datos NO Guardados", "ANDA", JOptionPane.WARNING_MESSAGE);

                    }

                }

            }

        });

    }

    public void mostrarVista() {
        String seleccionado = controladorVerServicios.getServicioSeleccionado();
        if (seleccionado != null) {
            //consulta si ya fue agregada la vista

            if (!vistaAgrgarConsumo.isVisible()) {
                if (vistaAgrgarConsumo.getParent() == null) {
                    vistaPrincipal.escritorio.add(vistaAgrgarConsumo);
                }
            }

            vistaAgrgarConsumo.toFront();

            // 2. Centrar
            Dimension desktopSize = vistaPrincipal.escritorio.getSize();
            Dimension internal = vistaAgrgarConsumo.getSize();
            int x = (desktopSize.width - internal.width) / 2;
            int y = (desktopSize.height - internal.height) / 2;
            vistaAgrgarConsumo.setLocation(x, y);

            vistaAgrgarConsumo.setVisible(true);
            vistaAgrgarConsumo.toFront(); // muestra al frenet
            vistaAgrgarConsumo.txtNumeroCuenta.setText(seleccionado);
            vistaAgrgarConsumo.txtNumeroCuenta.setEnabled(false);
            formaAgregarConsumo();

        }
    }

    public void formaAgregarConsumo() {
        String[] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto",
            "Septiembre", "Octubre", "Noviembre", "Diciembre"
        };
        for (String mes : meses) {
            vistaAgrgarConsumo.comboAgragarC.addItem(mes);

        }
    }
    
    private String generarSiguienteIdConsumo() {
        ArrayList<ModeloConsumo> consumos = base.getConsumos();

        int siguienteId = consumos.size() + 1;

        return String.valueOf(siguienteId);
    }

}
