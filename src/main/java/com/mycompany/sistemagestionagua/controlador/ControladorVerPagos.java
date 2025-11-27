/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemagestionagua.controlador;

import com.mycompany.sistemagestionagua.modelo.Base;
import com.mycompany.sistemagestionagua.modelo.ModeloConsumo;
import com.mycompany.sistemagestionagua.modelo.PrecioMC;
import com.mycompany.sistemagestionagua.modelo.Servicio;
import com.mycompany.sistemagestionagua.vista.VistaPagos;
import com.mycompany.sistemagestionagua.vista.VistaPrincipal;
import java.awt.Dimension;
import java.math.BigDecimal;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ayala
 */
public class ControladorVerPagos {

    private VistaPagos visPagos;
    private VistaPrincipal vistaPrincipal;
    private Base base;
    private PrecioMC precio;

    public ControladorVerPagos(VistaPrincipal vistaPrincipal, Base base) {
        this.vistaPrincipal = vistaPrincipal;
        this.visPagos = new VistaPagos();
        this.base = base;

        eventos();
    }

    private void eventos() {
        visPagos.btnCerrar.addActionListener(e -> visPagos.dispose());
        visPagos.btnPagar.addActionListener(e -> pagarCosumo(base.getConsumos()));
        visPagos.btnBuscar.addActionListener(E -> buscarPago());
        
           // --- Desactivar buscadores ---
           
        eventoCampo(visPagos.txtBuscar1, visPagos.txtBuscar2, visPagos.txtBuscar3);
        eventoCampo(visPagos.txtBuscar2, visPagos.txtBuscar1, visPagos.txtBuscar3);
        eventoCampo(visPagos.txtBuscar3, visPagos.txtBuscar1, visPagos.txtBuscar2);
        
    }

    public void mostrarVista() {
        visPagos.setSize(1000, 600);
        visPagos.setVisible(true);

        Dimension desktopSize = vistaPrincipal.escritorio.getSize();
        Dimension internal = visPagos.getSize();
        visPagos.setLocation(
                (desktopSize.width - internal.width) / 2,
                (desktopSize.height - internal.height) / 2
        );

        vistaPrincipal.escritorio.remove(visPagos);
        vistaPrincipal.escritorio.add(visPagos);
        visPagos.toFront();

        mostrarConsumosTabla(listaPendientes(base.getConsumos()));
    }

    // ───────────────────────────────────────────────────────────────
    //      TABLA DE PAGOS — AHORA USA TARIFA MÍNIMA
    // ───────────────────────────────────────────────────────────────
    private void mostrarConsumosTabla(ArrayList<ModeloConsumo> consumos) {

        DefaultTableModel modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String titulos[] = {
            "Id Consumo", "N° Cuenta", "N° de DUI", "Nombre de propietario",
            "Mes Lectura", "Lectura anterior", "Lectura actual",
            "Metros consumidos", "Monto", "Pago"
        };

        modeloTabla.setColumnIdentifiers(titulos);

        for (ModeloConsumo cons : consumos) {

            Servicio serv = base.encontrarServicioPorNumCuenta(cons.getNumeroCuenta());
            String nombre = base.nombrePorDui(serv.getDuiPropietario());
            String apellido = base.apellido(serv.getDuiPropietario());
            String pagoStr = cons.isCancelado() ? "Cancelado" : "Pendiente";

            // Obtener lectura anterior
            int lecturaAnterior;
            if (cons.getNumMes() - 1 == 0) {
                lecturaAnterior = serv.getMetrosCubicos();
            } else {
                lecturaAnterior = base.obtenerLecturaAnterior(cons.getNumMes() - 1);
            }

            int metrosConsumidos = cons.getMetrosCubicos() - lecturaAnterior;

            // ───────────────────────────────────
            //   *** Monto con TARIFA MÍNIMA ***
            // ───────────────────────────────────
            BigDecimal monto = calcularMontoConTarifaMinima(metrosConsumidos);

            Object datos[] = {
                cons.getIdConsumo(),
                cons.getNumeroCuenta(),
                serv.getDuiPropietario(),
                nombre + " " + apellido,
                cons.getMes(),
                lecturaAnterior,
                cons.getMetrosCubicos(),
                metrosConsumidos,
                monto,
                pagoStr
            };

            modeloTabla.addRow(datos);
        }

        visPagos.tablaConsumos.setModel(modeloTabla);
    }

    private ArrayList<ModeloConsumo> listaPendientes(ArrayList<ModeloConsumo> consumos) {
        ArrayList<ModeloConsumo> pendientes = new ArrayList<>();
        for (ModeloConsumo consumo : consumos) {
            if (!consumo.isCancelado()) {
                pendientes.add(consumo);
            }
        }
        return pendientes;
    }

    private void pagarCosumo(ArrayList<ModeloConsumo> consumos) {
        String seleccionado = getConsumoSeleccionado();

        if (seleccionado != null) {
            boolean encontrado = false;

            for (ModeloConsumo cons : consumos) {
                if (cons.getIdConsumo().equals(seleccionado)) {

                    cons.setCancelado(true);
                    encontrado = true;

                    JOptionPane.showMessageDialog(
                            vistaPrincipal,
                            "Consumo cancelado con éxito",
                            "ANDA",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    mostrarConsumosTabla(listaPendientes(base.getConsumos()));
                    break;
                }
            }

            if (!encontrado) {
                JOptionPane.showMessageDialog(
                        vistaPrincipal,
                        "Error al cancelar el consumo",
                        "ANDA",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        }
    }

    public String getConsumoSeleccionado() {
        int fila = visPagos.tablaConsumos.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(visPagos, "Seleccione un consumo de la tabla");
            return null;
        }

        return visPagos.tablaConsumos.getValueAt(fila, 0).toString();
    }

    // ───────────────────────────────────────────────────────────────
    //         OBTENER PRECIO DEL RANGO
    // ───────────────────────────────────────────────────────────────
    private BigDecimal obtenerPrecioPorRango(int metros) {

        for (PrecioMC rango : PrecioMC.obtenerTodos()) {

            // Rango abierto
            if (rango.getHasta() == -1) {
                if (metros >= rango.getDesde()) {
                    return rango.getPrecio();
                }
            }

            // Rango normal
            if (metros >= rango.getDesde() && metros <= rango.getHasta()) {
                return rango.getPrecio();
            }
        }

        JOptionPane.showMessageDialog(
                vistaPrincipal,
                "No se encontró un rango válido para " + metros + " m³.\nVerifique los rangos.",
                "Error en rangos",
                JOptionPane.ERROR_MESSAGE
        );

        return BigDecimal.ZERO;
    }

    // ───────────────────────────────────────────────────────────────
    //       *** CALCULO DE MONTO CON TARIFA MÍNIMA ***
    // ───────────────────────────────────────────────────────────────
    private BigDecimal calcularMontoConTarifaMinima(int metrosConsumidos) {

        // 1. Obtener el primer rango (desde 0)
        PrecioMC rangoMin = null;

        for (PrecioMC r : PrecioMC.obtenerTodos()) {
            if (r.getDesde() == 0) {
                if (rangoMin == null || r.getHasta() < rangoMin.getHasta()) {
                    rangoMin = r;
                }
            }
        }

        if (rangoMin == null) {
            JOptionPane.showMessageDialog(null,
                    "No existe un rango inicial con 'desde = 0'.",
                    "Error en tarifa mínima",
                    JOptionPane.ERROR_MESSAGE
            );
            return BigDecimal.ZERO;
        }

        // A) Está dentro del primer rango → COBRO FIJO
        if (metrosConsumidos <= rangoMin.getHasta()) {
            return rangoMin.getPrecio();
        }

        // B) Supera el primer rango → COBRO NORMAL MULTIPLICADO
        BigDecimal precioUnitario = obtenerPrecioPorRango(metrosConsumidos);

        return precioUnitario.multiply(BigDecimal.valueOf(metrosConsumidos));
    }
    
    
     //evento que desabilita los textfield de los buscadores 
    private void eventoCampo(JTextField activo, JTextField... otros) {
        activo.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                bloquear();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                bloquear();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                bloquear();
            }

            private void bloquear() {
                if (!activo.getText().isEmpty()) {
                    for (JTextField t : otros) {
                        t.setEditable(false);
                    }
                } else {
                    for (JTextField t : otros) {
                        t.setEditable(true);
                    }
                }
            }
        });

    }
   private void buscarPago() {
        String busca = null;
        String identificador = null;
        if (!visPagos.txtBuscar1.getText().isEmpty()) {
            identificador = "dui";
            busca = visPagos.txtBuscar1.getText().trim();

        } else if (!visPagos.txtBuscar2.getText().isEmpty()) {
            identificador = "cuenta";
            busca = visPagos.txtBuscar2.getText().trim();

        } else if (!visPagos.txtBuscar3.getText().isEmpty()) {
            identificador = "nombre";
            busca = visPagos.txtBuscar3.getText().trim();

        }
        if (busca == null) {
            JOptionPane.showMessageDialog(vistaPrincipal, "Ingrese una informacion de consumo para buscar", "ANDA", JOptionPane.WARNING_MESSAGE);
            mostrarConsumosTabla(base.getConsumos());
            //detiene la ejecucion
            return;
            
        } else if (base.buscarConsumo(identificador, busca).isEmpty()) {
            mostrarConsumosTabla(base.buscarConsumo(identificador, busca));
            JOptionPane.showMessageDialog(vistaPrincipal, "No se encontraron consumos con la informacion ingresada", "ANDA", JOptionPane.WARNING_MESSAGE);
            visPagos.txtBuscar1.setText("");
            visPagos.txtBuscar2.setText("");
            visPagos.txtBuscar3.setText("");
            mostrarConsumosTabla(base.getConsumos());

            
            //detiene la ejecucion
            return;
        }
        
                mostrarConsumosTabla(base.buscarConsumo(identificador, busca));


   }

}