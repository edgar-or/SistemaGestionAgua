
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

    // Ventanas de agregar/modificar (se crean cuando se necesitan)
    private VistaNuevoPrecioMC vistaNuevo;
    private VistaModificarPrecioMC vistaModificar;

    // Modelo de la tabla
    private final DefaultTableModel tableModel;

    public ControladorPrecioMC(VistaAgregarPrecios vista, VistaPrincipal vistaPrincipal) {
        this.vista = vista;
        this.vistaPrincipal = vistaPrincipal;
        // Configurar columnas de la tabla
        tableModel = new DefaultTableModel(new Object[]{"Desde", "Hasta", "Precio"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // no editable desde la tabla
            }
        };
        this.vista.TablaPrecios.setModel(tableModel);
        this.vista.TablaPrecios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Listeners botones
        this.vista.btnAgregarNuevoRyP.addActionListener(this);
        this.vista.btnModificarRyP.addActionListener(this);
        this.vista.btnEliminarRyP.addActionListener(this);
        this.vista.btnCerrarPrecioMC.addActionListener(this);

        // Listener selección tabla para habilitar botones
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
        tableModel.setRowCount(0); // limpiar
        for (PrecioMC p : PrecioMC.obtenerTodos()) {
            tableModel.addRow(new Object[]{
                    p.getDesde(),
                    p.getHasta(),
                    p.getPrecio()
            });
        }
        // Actualizar estado botones
        boolean hay = tableModel.getRowCount() > 0;
        vista.btnModificarRyP.setEnabled(hay && vista.TablaPrecios.getSelectedRow() != -1);
        vista.btnEliminarRyP.setEnabled(hay && vista.TablaPrecios.getSelectedRow() != -1);
    }

    // Abre la vista de nuevo rango como JInternalFrame centrado
    private void abrirVistaNuevo() {
        if (vistaNuevo == null || vistaNuevo.isClosed()) {
            vistaNuevo = new VistaNuevoPrecioMC();
            vistaPrincipal.escritorio.add(vistaNuevo);
            centrarInternalFrame(vistaNuevo);
            vistaNuevo.btnCerrarAggRyP.addActionListener(e -> vistaNuevo.dispose());

            // Listener del botón agregar en la vistaNuevo
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

    // Agregar registro usando datos de vistaNuevo
    private void accionAgregarDesdeVistaNuevo() {
        try {
            int desde = Integer.parseInt(vistaNuevo.txtRango1.getText().trim());
            int hasta = Integer.parseInt(vistaNuevo.txtRango2.getText().trim());
            BigDecimal precio = new BigDecimal(vistaNuevo.txtPreciosMC.getText().trim());

            // Validaciones
            if (desde < 0 || hasta < 0) {
                JOptionPane.showMessageDialog(vistaNuevo, "Los rangos no pueden ser negativos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (desde >= hasta) {
                JOptionPane.showMessageDialog(vistaNuevo, "El valor 'Desde' debe ser menor que 'Hasta'.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (precio.compareTo(BigDecimal.ZERO) < 0) {
                JOptionPane.showMessageDialog(vistaNuevo, "El precio no puede ser negativo.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Verificar translaciones con los existentes
            if (existeTraslape(-1, desde, hasta)) {
                JOptionPane.showMessageDialog(vistaNuevo, "El rango traslapa con un rango existente.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Guardar en modelo
            PrecioMC.agregar(new PrecioMC(desde, hasta, precio));
            cargarTablaDesdeModelo();

            vistaNuevo.dispose();
            vistaNuevo = null;
            
           

            JOptionPane.showMessageDialog(vista, "Rango agregado correctamente.", "Info", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vistaNuevo, "Datos invalidos. Use números enteros para rangos y numero decimal para precio.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Abre la vista modificar y precarga los datos desde la fila seleccionada
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


            // Listener del botón modificar dentro de la vistaModificar
            vistaModificar.btnModRyP.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    accionModificarDesdeVistaModificar(filaIndex);
                }
            });
        }

        // Precargar datos
        PrecioMC p = PrecioMC.obtener(filaIndex);
        vistaModificar.txtModRango1.setText(String.valueOf(p.getDesde()));
        vistaModificar.txtModRango2.setText(String.valueOf(p.getHasta()));
        vistaModificar.txtModPreciosMC.setText(p.getPrecio().toString());

        vistaModificar.setVisible(true);
        vistaModificar.toFront();
    }

    // Guarda los cambios desde la vistaModificar
    private void accionModificarDesdeVistaModificar(int filaIndex) {
        try {
            int desde = Integer.parseInt(vistaModificar.txtModRango1.getText().trim());
            int hasta = Integer.parseInt(vistaModificar.txtModRango2.getText().trim());
            BigDecimal precio = new BigDecimal(vistaModificar.txtModPreciosMC.getText().trim());

            if (desde < 0 || hasta < 0) {
                JOptionPane.showMessageDialog(vistaModificar, "Los rangos no pueden ser negativos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (desde >= hasta) {
                JOptionPane.showMessageDialog(vistaModificar, "El valor 'Desde' debe ser menor que 'Hasta'.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (precio.compareTo(BigDecimal.ZERO) < 0) {
                JOptionPane.showMessageDialog(vistaModificar, "El precio no puede ser negativo.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validar traslapes excluyendo el índice que estamos modificando
            if (existeTraslape(filaIndex, desde, hasta)) {
                JOptionPane.showMessageDialog(vistaModificar, "El rango traslapa con un rango existente.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Guardar cambios en el modelo
            PrecioMC.modificar(filaIndex, new PrecioMC(desde, hasta, precio));
            cargarTablaDesdeModelo();

            // Cerrar la ventana de modificar
            vistaModificar.dispose();
            vistaModificar = null;
            
            

            JOptionPane.showMessageDialog(vista, "Rango modificado correctamente.", "Info", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vistaModificar, "Datos invalidos. Use números enteros para rangos y número decimal para precio.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Eliminar fila seleccionada
    private void eliminarFilaSeleccionada() {
        int fila = vista.TablaPrecios.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(vista, "Seleccione una fila para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(vista, "¿Eliminar rango seleccionado?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        PrecioMC.eliminar(fila);
        cargarTablaDesdeModelo();
        JOptionPane.showMessageDialog(vista, "Rango eliminado.", "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    // Comprobar si los rangos se transladan entre (desde, hasta) y cualquiera de los rangos existentes

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

    // Manejo de eventos de los botones principales
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == vista.btnAgregarNuevoRyP) {
            abrirVistaNuevo();
            return;
        }

        if (src == vista.btnModificarRyP) {
            int fila = vista.TablaPrecios.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(vista, "Seleccione una fila para modificar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            abrirVistaModificar(fila);
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
