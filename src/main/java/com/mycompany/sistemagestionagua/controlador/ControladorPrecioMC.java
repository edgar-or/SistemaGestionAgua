
package com.mycompany.sistemagestionagua.controlador;

import com.mycompany.sistemagestionagua.modelo.PrecioMC;
import com.mycompany.sistemagestionagua.vista.VistaAgregarPrecios;
import com.mycompany.sistemagestionagua.vista.VistaModificarPrecioMC;
import com.mycompany.sistemagestionagua.vista.VistaNuevoPrecioMC;
import com.mycompany.sistemagestionagua.vista.VistaPrincipal;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

public class ControladorPrecioMC implements ActionListener {

    private final VistaAgregarPrecios vista;
    private final VistaPrincipal vistaPrincipal;

    private VistaNuevoPrecioMC vistaNuevo;
    private VistaModificarPrecioMC vistaModificar;

    private final DefaultTableModel tableModel;

    public ControladorPrecioMC(VistaAgregarPrecios vista, VistaPrincipal vistaPrincipal) {
        this.vista = vista;
        this.vistaPrincipal = vistaPrincipal;

        tableModel = new DefaultTableModel(new Object[]{"Desde", "Hasta", "Precio"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        this.vista.TablaPrecios.setModel(tableModel);
        this.vista.TablaPrecios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        this.vista.btnAgregarNuevoRyP.addActionListener(this);
        this.vista.btnModificarRyP.addActionListener(this);
        this.vista.btnEliminarRyP.addActionListener(this);
        this.vista.btnCerrarPrecioMC.addActionListener(this);

        this.vista.TablaPrecios.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                boolean filaSeleccionada = vista.TablaPrecios.getSelectedRow() != -1;
                vista.btnModificarRyP.setEnabled(filaSeleccionada);
                vista.btnEliminarRyP.setEnabled(filaSeleccionada);
            }
        });

        inicializarVista();
        cargarTablaDesdeModelo();
    }

    private void inicializarVista() {
        if (vista.getParent() == null) {
            vistaPrincipal.escritorio.add(vista);
        }
        centrarInternalFrame(vista);
        vista.setVisible(true);
        vista.toFront();

        vista.btnModificarRyP.setEnabled(false);
        vista.btnEliminarRyP.setEnabled(false);
    }

    private void centrarInternalFrame(JInternalFrame frame) {
        Dimension ds = vistaPrincipal.escritorio.getSize();
        Dimension is = frame.getSize();
        frame.setLocation((ds.width - is.width) / 2, (ds.height - is.height) / 2);
    }

    private void cargarTablaDesdeModelo() {
        tableModel.setRowCount(0);

        for (PrecioMC p : PrecioMC.obtenerTodos()) {
            tableModel.addRow(new Object[]{
                    p.getDesde(),
                    p.getHasta(),
                    p.getPrecio()
            });
        }

        boolean hay = tableModel.getRowCount() > 0;
        vista.btnModificarRyP.setEnabled(hay && vista.TablaPrecios.getSelectedRow() != -1);
        vista.btnEliminarRyP.setEnabled(hay && vista.TablaPrecios.getSelectedRow() != -1);

        if (PrecioMC.total() > 0) {
            PrecioMC primero = PrecioMC.obtener(0);
            vista.lblTarifaMinima.setText(
                    "Tarifa mínima: $" + primero.getPrecio() + "   (0 - " + primero.getHasta() + " m³)"
            );
        } else {
            vista.lblTarifaMinima.setText("No hay tarifa mínima registrada.");
        }
    }

    private void abrirVistaNuevo() {
        if (vistaNuevo == null || vistaNuevo.isClosed()) {

            vistaNuevo = new VistaNuevoPrecioMC();
            vistaPrincipal.escritorio.add(vistaNuevo);
            centrarInternalFrame(vistaNuevo);

            //COMPORTAMIENTO PARA TARIFA MINIMA (primer rango
            if (PrecioMC.total() == 0) {
                // primer rango → tarifa minima
                vistaNuevo.jLabel3.setText("Tarifa mínima $");
                vistaNuevo.txtRango1.setText("0");
                vistaNuevo.txtRango1.setEnabled(false);
            } else {
                // rango normal
                vistaNuevo.jLabel3.setText("Precio de metros cúbicos $");
                vistaNuevo.txtRango1.setEnabled(true);
            }

            vistaNuevo.btnCerrarAggRyP.addActionListener(e -> vistaNuevo.dispose());

            vistaNuevo.btnAgregarRyP.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    accionAgregarDesdeVistaNuevo();
                }
            });
        }

        vistaNuevo.setVisible(true);
        vistaNuevo.toFront();
    }

    // AGREGAR NUEVO RANGO
    private void accionAgregarDesdeVistaNuevo() {
        try {
            int desde = Integer.parseInt(vistaNuevo.txtRango1.getText().trim());
            int hasta = Integer.parseInt(vistaNuevo.txtRango2.getText().trim());
            BigDecimal precio = new BigDecimal(vistaNuevo.txtPreciosMC.getText().trim());
            
            // VALIDACIÓN: Impedir precio igual a tarifa mínima en rangos posteriores
            if (PrecioMC.total() > 0) {
                BigDecimal tarifaMinima = PrecioMC.obtener(0).getPrecio();

            if (precio.compareTo(tarifaMinima) == 0) {
                JOptionPane.showMessageDialog(vistaNuevo, "El precio ingresado no puede ser igual a la tarifa minima.\n" +
                "La tarifa minima solo aplica al primer rango.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
                return;
                }
            }


            if (desde < 0 || hasta < 0) {
                JOptionPane.showMessageDialog(vistaNuevo, "Los rangos no pueden ser negativos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (desde >= hasta) {
                JOptionPane.showMessageDialog(vistaNuevo, "'Desde' debe ser menor que 'Hasta'.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (precio.compareTo(BigDecimal.ZERO) < 0) {
                JOptionPane.showMessageDialog(vistaNuevo, "El precio no puede ser negativo.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (existeTraslape(-1, desde, hasta)) {
                JOptionPane.showMessageDialog(vistaNuevo, "El rango traslapa con uno existente.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            PrecioMC.agregar(new PrecioMC(desde, hasta, precio));
            cargarTablaDesdeModelo();

            vistaNuevo.dispose();
            vistaNuevo = null;

            JOptionPane.showMessageDialog(vista, "Rango agregado correctamente.", "Info", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vistaNuevo, "Datos inválidos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ABRIR VISTA MODIFICAR
    private void abrirVistaModificar(int filaIndex) {

        if (filaIndex < 0 || filaIndex >= PrecioMC.total()) {
            JOptionPane.showMessageDialog(vista, "Seleccione un rango válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (vistaModificar == null || vistaModificar.isClosed()) {

            vistaModificar = new VistaModificarPrecioMC();
            vistaPrincipal.escritorio.add(vistaModificar);
            centrarInternalFrame(vistaModificar);

            vistaModificar.btnCerrarModRyP.addActionListener(e -> vistaModificar.dispose());

            vistaModificar.btnModRyP.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    accionModificarDesdeVistaModificar(filaIndex);
                }
            });
        }

        PrecioMC p = PrecioMC.obtener(filaIndex);

        vistaModificar.txtModRango1.setText(String.valueOf(p.getDesde()));
        vistaModificar.txtModRango2.setText(String.valueOf(p.getHasta()));
        vistaModificar.txtModPreciosMC.setText(String.valueOf(p.getPrecio()));

        // si es tarifa mínima, solo se puede modificar PRECIO
        if (filaIndex == 0) {
            vistaModificar.txtModRango1.setEnabled(false);
        } else {
            vistaModificar.txtModRango1.setEnabled(true);
        }

        vistaModificar.setVisible(true);
        vistaModificar.toFront();
    }

    // MODIFICAR REGISTRO
    private void accionModificarDesdeVistaModificar(int filaIndex) {

        try {
            int desde = Integer.parseInt(vistaModificar.txtModRango1.getText().trim());
            int hasta = Integer.parseInt(vistaModificar.txtModRango2.getText().trim());
            BigDecimal precio = new BigDecimal(vistaModificar.txtModPreciosMC.getText().trim());

            if (filaIndex == 0) {
                // primer rango → tarifa minima → desde siempre debe ser 0
                desde = 0;
            }

            if (desde < 0 || hasta < 0) {
                JOptionPane.showMessageDialog(vistaModificar, "Los rangos no pueden ser negativos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (desde >= hasta) {
                JOptionPane.showMessageDialog(vistaModificar, "'Desde' debe ser menor que 'Hasta'.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (precio.compareTo(BigDecimal.ZERO) < 0) {
                JOptionPane.showMessageDialog(vistaModificar, "El precio no puede ser negativo.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (existeTraslape(filaIndex, desde, hasta)) {
                JOptionPane.showMessageDialog(vistaModificar, "El rango traslapa con otro existente.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            PrecioMC.modificar(filaIndex, new PrecioMC(desde, hasta, precio));
            cargarTablaDesdeModelo();

            vistaModificar.dispose();
            vistaModificar = null;

            JOptionPane.showMessageDialog(vista, "Rango modificado correctamente.", "Info", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vistaModificar, "Datos inválidos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarFilaSeleccionada() {

        int fila = vista.TablaPrecios.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(vista, "Seleccione una fila para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (JOptionPane.showConfirmDialog(vista, "¿Eliminar rango?", "Confirmar", JOptionPane.YES_NO_OPTION)
                != JOptionPane.YES_OPTION) return;

        PrecioMC.eliminar(fila);
        cargarTablaDesdeModelo();

        JOptionPane.showMessageDialog(vista, "Rango eliminado.", "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    private boolean existeTraslape(int excludeIndex, int desde, int hasta) {

        for (int i = 0; i < PrecioMC.total(); i++) {

            if (i == excludeIndex) continue;

            PrecioMC p = PrecioMC.obtener(i);

            if (!(hasta < p.getDesde() || desde > p.getHasta())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        Object src = e.getSource();

        if (src == vista.btnAgregarNuevoRyP) {
            abrirVistaNuevo();
            return;
        }

        if (src == vista.btnModificarRyP) {
            int fila = vista.TablaPrecios.getSelectedRow();
            if (fila != -1) abrirVistaModificar(fila);
            return;
        }

        if (src == vista.btnEliminarRyP) {
            eliminarFilaSeleccionada();
            return;
        }

        if (src == vista.btnCerrarPrecioMC) {
            vista.dispose();
        }
    }
}
