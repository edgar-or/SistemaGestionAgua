/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemagestionagua.controlador;

import com.mycompany.sistemagestionagua.modelo.Base;
import com.mycompany.sistemagestionagua.modelo.ModeloConsumo;
import com.mycompany.sistemagestionagua.modelo.PrecioMC;
import com.mycompany.sistemagestionagua.modelo.Servicio;
import com.mycompany.sistemagestionagua.vista.VistaModificarConsumo;
import com.mycompany.sistemagestionagua.vista.VistaPrincipal;
import com.mycompany.sistemagestionagua.vista.VistaVerConsumos;
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
public class ControladorVerConsumos {

    private VistaVerConsumos visVerConsumos;
    private VistaPrincipal vistaPrincipal;
    private Base base;
    private ModeloConsumo consumo;
    private VistaModificarConsumo visModificarConsumo;

    public ControladorVerConsumos(VistaPrincipal vistaPrincipal, ModeloConsumo consumo, Base base) {
        this.visVerConsumos = new VistaVerConsumos();
        this.vistaPrincipal = vistaPrincipal;
        this.base = base;
        this.consumo = consumo;
        this.visModificarConsumo = new VistaModificarConsumo();

        eventos();
    }

    private void eventos() {
        visVerConsumos.btnCerrar.addActionListener(e -> visVerConsumos.dispose());
        visVerConsumos.btnBuscar.addActionListener(e -> buscarConsumo());
        visVerConsumos.btnPagar.addActionListener(e -> pagarCosumo(base.getConsumos()));
        visVerConsumos.btnModificar.addActionListener(e -> mostrarVistaModificar());

        visModificarConsumo.btnModificar.addActionListener(e -> modificarConsumo());

        visModificarConsumo.btnCerrar.addActionListener(e -> visModificarConsumo.dispose());

        // --- Desactivar buscadores ---
        eventoCampo(visVerConsumos.txtBuscar1, visVerConsumos.txtBuscar2, visVerConsumos.txtBuscar3);
        eventoCampo(visVerConsumos.txtBuscar2, visVerConsumos.txtBuscar1, visVerConsumos.txtBuscar3);
        eventoCampo(visVerConsumos.txtBuscar3, visVerConsumos.txtBuscar1, visVerConsumos.txtBuscar2);
    }

    public void mostrarVista() {
        visVerConsumos.setSize(1000, 600);
        visVerConsumos.toFront();
        visVerConsumos.setVisible(true);

        // 2️⃣ Centrar la vista
        Dimension desktopSize = vistaPrincipal.escritorio.getSize();
        Dimension internal = visVerConsumos.getSize();
        int x = (desktopSize.width - internal.width) / 2;
        int y = (desktopSize.height - internal.height) / 2;
        visVerConsumos.setLocation(x, y);
        vistaPrincipal.escritorio.remove(visVerConsumos);
        vistaPrincipal.escritorio.add(visVerConsumos);

        // 3️⃣ Mostrar y traer al frente
        visVerConsumos.toFront();

        mostrarConsumosTabla(base.getConsumos());
    }

    private void mostrarConsumosTabla(ArrayList<ModeloConsumo> consumos) {
        DefaultTableModel modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String titulos[] = {"Id Consumo", "N° Cuenta", "N° de DUI", "Nombre de propietario", "Mes Lectura",
            "Lectura anterior", "Lectura actual", "Metros consumidos", "Monto", "Pago"};
        modeloTabla.setColumnIdentifiers(titulos);

        for (ModeloConsumo cons : consumos) {

            Servicio serv = base.encontrarServicioPorNumCuenta(cons.getNumeroCuenta());

            String nombre = "";
            String apellido = "";
            if (serv != null) {
                nombre = base.nombrePorDui(serv.getDuiPropietario());
                apellido = base.apellido(serv.getDuiPropietario());
            }

            String Pago = cons.isCancelado() ? "Cancelado" : "Pendiente";

            int lecturaAnterior;

            if (cons.getNumMes() - 1 == 0) {
                lecturaAnterior = (serv != null) ? serv.getMetrosCubicos() : 0;
            } else {
                lecturaAnterior = base.obtenerLecturaAnterior(cons.getNumMes() - 1);
            }

            int metrosConsumidos = cons.getMetrosCubicos() - lecturaAnterior;

            // Cálculo del monto: ahora usando tarifa mínima cuando corresponda
            BigDecimal monto = calcularMontoConTarifaMinima(metrosConsumidos);

            Object datos[] = {
                cons.getIdConsumo(),
                cons.getNumeroCuenta(),
                (serv != null) ? serv.getDuiPropietario() : "",
                nombre + " " + apellido,
                cons.getMes(),
                lecturaAnterior,
                cons.getMetrosCubicos(),
                metrosConsumidos,
                monto,
                Pago
            };

            modeloTabla.addRow(datos);
        }

        this.visVerConsumos.tablaConsumos.setModel(modeloTabla);
    }

    private void pagarCosumo(ArrayList<ModeloConsumo> consumos) {
        String seleccionado = getConsumoSeleccionado();

        if (seleccionado != null) {
            boolean encontrado = false;

            for (ModeloConsumo cons : consumos) {
                if (cons.getIdConsumo().equals(seleccionado)) {
                    cons.setCancelado(true);
                    encontrado = true;

                    JOptionPane.showMessageDialog(vistaPrincipal,
                            "Consumo cancelado con éxito",
                            "ANDA",
                            JOptionPane.INFORMATION_MESSAGE);

                    mostrarConsumosTabla(base.getConsumos());
                    break;
                }
            }

            // Si después de recorrer todo NO lo encontró → error
            if (!encontrado) {
                JOptionPane.showMessageDialog(vistaPrincipal,
                        "Error al cancelar el consumo",
                        "ANDA",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    public void mostrarVistaModificar() {

        String seleccionado = getConsumoSeleccionado();
        if (seleccionado != null) {
            visModificarConsumo.setVisible(true);

            // 2️⃣ Centrar la vista
            Dimension desktopSize = vistaPrincipal.escritorio.getSize();
            Dimension internal = visModificarConsumo.getSize();
            int x = (desktopSize.width - internal.width) / 2;
            int y = (desktopSize.height - internal.height) / 2;
            visModificarConsumo.setLocation(x, y);
            vistaPrincipal.escritorio.remove(visModificarConsumo);
            vistaPrincipal.escritorio.add(visModificarConsumo);

            // 3️⃣ Mostrar y traer al frente
            visModificarConsumo.toFront();

            ModeloConsumo cons = base.encontrarConsumo(seleccionado);

            visModificarConsumo.txtConsumo.setText(String.valueOf(cons.getMetrosCubicos()));
            visModificarConsumo.txtNumeroCuenta.setText(cons.getNumeroCuenta());
            visModificarConsumo.txtNumeroCuenta.setEnabled(false);
            visModificarConsumo.txtIdConsumo.setText(cons.getIdConsumo());
            visModificarConsumo.txtIdConsumo.setEditable(false);

            llenarComboModificar(base.encontrarConsumo(seleccionado).getMes());

            if (base.encontrarConsumo(seleccionado).isCancelado()) {
                visModificarConsumo.btnCancelado.setSelected(true);
            }

        }

    }

    private void modificarConsumo() {
        boolean pagado = visModificarConsumo.btnCancelado.isSelected();

        String idConsumo = visModificarConsumo.txtIdConsumo.getText();
        String mes = (String) visModificarConsumo.comboAgragarC.getSelectedItem();
        String metros = visModificarConsumo.txtConsumo.getText();

        boolean modificado = base.modificarConsumo(idConsumo, mes, metros, pagado);

        if (modificado) {
            JOptionPane.showMessageDialog(vistaPrincipal,
                    "Consumo modificado con éxito",
                    "ANDA",
                    JOptionPane.INFORMATION_MESSAGE);
            visModificarConsumo.dispose();
            mostrarConsumosTabla(base.getConsumos());
        } else {
            JOptionPane.showMessageDialog(vistaPrincipal,
                    "No se pudo modificar el consumo",
                    "ANDA",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void buscarConsumo() {
        String busca = null;
        String identificador = null;
        if (!visVerConsumos.txtBuscar1.getText().isEmpty()) {
            identificador = "dui";
            busca = visVerConsumos.txtBuscar1.getText().trim();

        } else if (!visVerConsumos.txtBuscar2.getText().isEmpty()) {
            identificador = "cuenta";
            busca = visVerConsumos.txtBuscar2.getText().trim();

        } else if (!visVerConsumos.txtBuscar3.getText().isEmpty()) {
            identificador = "nombre";
            busca = visVerConsumos.txtBuscar3.getText().trim();

        }
        if (busca == null) {
            JOptionPane.showMessageDialog(vistaPrincipal, "Ingrese una informacion de consumo para buscar", "ANDA", JOptionPane.WARNING_MESSAGE);
            mostrarConsumosTabla(base.getConsumos());
            //detiene la ejecucion
            return;
        } else if (base.buscarConsumo(identificador, busca).isEmpty()) {
            mostrarConsumosTabla(base.getConsumos());
            JOptionPane.showMessageDialog(vistaPrincipal, "No se encontraron consumos con la informacion ingresada", "ANDA", JOptionPane.WARNING_MESSAGE);
            visVerConsumos.txtBuscar1.setText("");
            visVerConsumos.txtBuscar2.setText("");
            visVerConsumos.txtBuscar3.setText("");
            //detiene la ejecucion
            return;
        }

        mostrarConsumosTabla(base.buscarConsumo(identificador, busca));

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

    private BigDecimal obtenerPrecioPorRango(int metros) {

        for (PrecioMC rango : PrecioMC.obtenerTodos()) {
            if (rango.getHasta() == -1) {
                if (metros >= rango.getDesde()) {
                    return rango.getPrecio();
                }
            }

            if (metros >= rango.getDesde() && metros <= rango.getHasta()) {
                return rango.getPrecio();
            }
        }

        JOptionPane.showMessageDialog(vistaPrincipal,
                "No se encontró un rango válido para " + metros + " m³.\n" + "Verifique los rangos registrados.",
                "Error en rangos", JOptionPane.ERROR_MESSAGE);

        return BigDecimal.ZERO;
    }

    private BigDecimal calcularMontoConTarifaMinima(int metrosConsumidos) {

        // --- Buscar el primer rango (tarifa mínima) ---
        PrecioMC rangoMin = null;

        for (PrecioMC r : PrecioMC.obtenerTodos()) {
            if (r.getDesde() == 0) {
                if (rangoMin == null || r.getHasta() < rangoMin.getHasta()) {
                    rangoMin = r;
                }
            }
        }

        if (rangoMin == null) {
            return BigDecimal.ZERO;
        }
        // A) Si está dentro del primer rango -> tarifa fija
        if (metrosConsumidos <= rangoMin.getHasta()) {
            return rangoMin.getPrecio();
        }

        // B) Si lo supera -> calcular multiplicando según rangos
        BigDecimal precioUnitario = obtenerPrecioPorRango(metrosConsumidos);
        return precioUnitario.multiply(BigDecimal.valueOf(metrosConsumidos));
    }

    public String getConsumoSeleccionado() {
        int fila = visVerConsumos.tablaConsumos.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(visVerConsumos, "Seleccione un consumo de la tabla");
            return null;

        }

        String idConsumo = visVerConsumos.tablaConsumos.getValueAt(fila, 0).toString(); // Columna 0 = idConsumo
        return idConsumo;

    }

    private void llenarComboModificar(String mesSelect) {

        visModificarConsumo.comboAgragarC.removeAllItems(); // Limpia por si ya tenía valores

        String[] meses = {
            "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
        };

        // Agregar los meses al combo
        for (String mes : meses) {
            visModificarConsumo.comboAgragarC.addItem(mes);
        }

        // Seleccionar el mes correcto
        visModificarConsumo.comboAgragarC.setSelectedItem(mesSelect);
    }

}
