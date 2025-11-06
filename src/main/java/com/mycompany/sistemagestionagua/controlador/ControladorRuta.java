/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemagestionagua.controlador;

import com.mycompany.sistemagestionagua.modelo.Base;
import com.mycompany.sistemagestionagua.modelo.ModeloRuta;
import com.mycompany.sistemagestionagua.modelo.Usuario;
import com.mycompany.sistemagestionagua.vista.VistaPrincipal;
import com.mycompany.sistemagestionagua.vista.VistaRuta;
import java.awt.Dimension;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ayala
 */
public class ControladorRuta {

    private Base base;
    private VistaRuta visRuta;
    private ModeloRuta modelRuta;
    private VistaPrincipal vistaPrincipal;

    public ControladorRuta(Base base, VistaPrincipal vistaPrincipal) {
        this.vistaPrincipal = vistaPrincipal;
        this.base = base;
        this.modelRuta = new ModeloRuta();
        this.visRuta = new VistaRuta();

        eventos();

    }

    private void eventos() {
        visRuta.btnGudar.addActionListener(e -> agregarRuta());
        visRuta.btnCerrar.addActionListener(e -> visRuta.dispose());
        visRuta.btnModificar.addActionListener(e -> mostrarModificar());
        visRuta.btnEliminar.addActionListener(e -> eliminarRuta());

    }

    public void mostrarVista() {

        visRuta.setSize(600, 400);
        visRuta.setVisible(true);

        // 2️⃣ Centrar la vista
        Dimension desktopSize = vistaPrincipal.escritorio.getSize();
        Dimension internal = visRuta.getSize();
        int x = (desktopSize.width - internal.width) / 2;
        int y = (desktopSize.height - internal.height) / 2;
        visRuta.setLocation(x, y);
        vistaPrincipal.escritorio.remove(visRuta);
        vistaPrincipal.escritorio.add(visRuta);

        // 3️⃣ Mostrar y traer al frente
        visRuta.toFront();

    }

    public void agregarRuta() {

        if (visRuta.btnGudar.getText().equals("Guardar")) {
            String depto = visRuta.txtDepto.getText();
            String muni = visRuta.txtMunicipio.getText();
            String colonia = visRuta.txtColonia.getText();
            String descripcion = visRuta.txtDescripcion.getText();

            if (depto.isEmpty() || muni.isEmpty() || colonia.isEmpty() || descripcion.isEmpty()) {
                JOptionPane.showMessageDialog(vistaPrincipal, "Complete los campos", "ANDA", JOptionPane.WARNING_MESSAGE);

            } else {
                String id = generarSiguienteIdRuta();

                if (!base.agregarRuta(new ModeloRuta(id, depto, muni, colonia, descripcion))) {
                    JOptionPane.showMessageDialog(vistaPrincipal, "No se pudieron guardar los datos", "ANDA", JOptionPane.WARNING_MESSAGE);

                } else {
                    JOptionPane.showMessageDialog(vistaPrincipal, "Datos guardados", "ANDA", JOptionPane.INFORMATION_MESSAGE);
                    visRuta.txtDepto.setText("");
                    visRuta.txtMunicipio.setText("");
                    visRuta.txtColonia.setText("");
                    visRuta.txtDescripcion.setText("");

                    mostrarRutasTabla(base.getRutas());

                }
            }

        } else if (visRuta.btnGudar.getText().equals("Modificar")) {
            String depto1 = visRuta.txtDepto.getText();
            String muni1 = visRuta.txtMunicipio.getText();
            String col1 = visRuta.txtColonia.getText();
            String desc1 = visRuta.txtDescripcion.getText();

            boolean modifico = base.modificarRuta(getRutaSeleccionado(), depto1, muni1, col1, desc1);

            if (modifico != false) {
                JOptionPane.showMessageDialog(vistaPrincipal, "Datos Modificados", "ANDA", JOptionPane.INFORMATION_MESSAGE);
                mostrarRutasTabla(base.getRutas());
                visRuta.btnGudar.setText("Guardar");
                visRuta.txtDepto.setText("");
                visRuta.txtColonia.setText("");
                visRuta.txtDescripcion.setText("");
                visRuta.txtMunicipio.setText("");
            } else {
                JOptionPane.showMessageDialog(vistaPrincipal, "No se pudo modificar la ruta", "ANDA", JOptionPane.WARNING_MESSAGE);
            }

        }

    }

    public void mostrarRutasTabla(ArrayList<ModeloRuta> rutas) {
        DefaultTableModel modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // <-- evita edición en todas las columnas
            }
        };

        String titulos[] = {"N°", "Departamento", "Municipio", "Colonia", "Descripcion"};
        modeloTabla.setColumnIdentifiers(titulos);

        for (ModeloRuta ruta : rutas) {

            Object datos[] = {ruta.getId(), ruta.getDepartametno(), ruta.getMunicipio(), ruta.getColonia(), ruta.getDescripcion()};
            modeloTabla.addRow(datos);
        }
        this.visRuta.Tabla.setModel(modeloTabla);

    }

    private String generarSiguienteIdRuta() {
        ArrayList<ModeloRuta> rutas = base.getRutas();

        int siguienteId = rutas.size() + 1;

        return String.valueOf(siguienteId);
    }

    private void mostrarModificar() {
        String seleccionado = getRutaSeleccionado();

        if (seleccionado != null) {
            for (ModeloRuta ruta : base.getRutas()) {
                if (ruta.getId().equals(seleccionado)) {
                    visRuta.txtDepto.setText(ruta.getDepartametno());
                    visRuta.txtColonia.setText(ruta.getColonia());
                    visRuta.txtMunicipio.setText(ruta.getMunicipio());
                    visRuta.txtDescripcion.setText(ruta.getDescripcion());

                    visRuta.btnGudar.setText("Modificar");
                }
            }
        }

    }

    private void eliminarRuta() {
        String seleccionado = getRutaSeleccionado();

        if (seleccionado != null) {

            int opcion = JOptionPane.showConfirmDialog(
                    visRuta,
                    "¿Está seguro que desea eliminar esta ruta? ",
                    "Confirmación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (opcion == JOptionPane.YES_OPTION) {
                if (base.eliminarRuta(seleccionado) != false) {
                    JOptionPane.showMessageDialog(vistaPrincipal, "Ruta Eliminada", "ANDA", JOptionPane.INFORMATION_MESSAGE);
                    mostrarRutasTabla(base.getRutas());
                } else {
                    JOptionPane.showMessageDialog(vistaPrincipal, "No se pudo eliminar la ruta", "ANDA", JOptionPane.WARNING_MESSAGE);
                }
            }

        }
    }

    private String getRutaSeleccionado() {
        int fila = visRuta.Tabla.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(visRuta, "Seleccione una ruta de la tabla");
            return null;

        }

        String id = visRuta.Tabla.getValueAt(fila, 0).toString();
        return id;

    }

}
