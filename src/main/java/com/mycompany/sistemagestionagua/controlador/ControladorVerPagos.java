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
import com.mycompany.sistemagestionagua.vista.verDetalleConsumo;
import java.awt.Dimension;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
    private verDetalleConsumo visVerDetalle;

    public ControladorVerPagos(VistaPrincipal vistaPrincipal, Base base) {
        this.vistaPrincipal = vistaPrincipal;
        this.visPagos = new VistaPagos();
        this.base = base;
        this.visVerDetalle = new verDetalleConsumo();

        eventos();
    }

    private void eventos() {
        visPagos.btnCerrar.addActionListener(e -> visPagos.dispose());
        visPagos.btnPagar.addActionListener(e -> pagarCosumo(base.getConsumos()));

        visVerDetalle.btnCerrar.addActionListener(e -> visVerDetalle.dispose());
        visVerDetalle.btnRegistrarPago.addActionListener(e -> registrarPago());
        visVerDetalle.btnFinalizarPago.addActionListener(e-> finalizarPago());
        visPagos.btnBuscar.addActionListener(E -> buscarPago());

        // --- Desactivar buscadores ---
        eventoCampo(visPagos.txtBuscar1, visPagos.txtBuscar2, visPagos.txtBuscar3);
        eventoCampo(visPagos.txtBuscar2, visPagos.txtBuscar1, visPagos.txtBuscar3);
        eventoCampo(visPagos.txtBuscar3, visPagos.txtBuscar1, visPagos.txtBuscar2);

    }

    public void mostrarVista() {
        
        visPagos.toFront();
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

  
    private void mostrarConsumosTabla(ArrayList<ModeloConsumo> consumos) {

        DefaultTableModel modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String titulos[] = {
            "Id Consumo", "N° Cuenta", "N° de DUI", "Nombre",
            "Mes Lectura", "Lectura anterior", "Lectura actual",
            "Metros Consumidos", "Monto", "Pago"
        };

        modeloTabla.setColumnIdentifiers(titulos);
        
        


        for (ModeloConsumo cons : consumos) {
            
            Servicio servicio = base.datosServicios(cons.getNumeroCuenta());
             if (servicio == null) {
                continue;
            }

            Servicio serv = base.encontrarServicioPorNumCuenta(cons.getNumeroCuenta());
            String nombre = base.nombrePorDui(serv.getDuiPropietario());
            String apellido = base.apellido(serv.getDuiPropietario());
            String pagoStr = cons.isCancelado() ? "Cancelado" : "Pendiente";

            // Obtener lectura anterior
            int lecturaAnterior = 0;

// 1️⃣ Leer la lectura inicial desde el servicio (sin fecha)
            int lecturaInicial = servicio.getMetrosCubicos();

// 2️⃣ Obtener primer registro de consumo (primer mes con fecha)
            ModeloConsumo primer = consumos.get(0);
            int mesPrimero = primer.getNumMes();
            int añoPrimero = primer.getAño();

// 3️⃣ Datos del consumo actual
            int mes = cons.getNumMes();
            int año = cons.getAño();

// 4️⃣ Si estamos en el PRIMER consumo registrado con fecha
            if (mes == mesPrimero && año == añoPrimero) {
                lecturaAnterior = lecturaInicial;
            } // 5️⃣ Si estamos en el mismo año del PRIMER consumo pero en un mes posterior
            else if (año == añoPrimero && mes > mesPrimero) {
                lecturaAnterior = base.obtenerLecturaAnterior(mes - 1, año, cons.getNumeroCuenta());
            } // 6️⃣ Si estamos en enero, pero NO es el primer año → buscar diciembre del año anterior
            else if (mes == 1) {
                lecturaAnterior = base.obtenerLecturaAnterior(12, año - 1, cons.getNumeroCuenta());
            } // 7️⃣ Caso general → buscar mes anterior del mismo año
            else {
                lecturaAnterior = base.obtenerLecturaAnterior(mes - 1, año, cons.getNumeroCuenta());
            }

// 8️⃣ Cálculo del consumo
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
                if (consumo.getNumeroCuenta()!=null) {
                     pendientes.add(consumo);
                }
               
            }
        }
        return pendientes;
    }

    private void pagarCosumo(ArrayList<ModeloConsumo> consumos) {
        String seleccionado = getConsumoSeleccionado();
        String ServicioSeleccionado = getServicioSeleccionado();

        Servicio servicio = base.datosServicios(ServicioSeleccionado);

        if (seleccionado != null) {
            boolean encontrado = false;

            for (ModeloConsumo cons : consumos) {
                if (cons.getIdConsumo().equals(seleccionado)) {
                    encontrado = true;



                    
                    visVerDetalle.setVisible(true);

                    // 2️⃣ Centrar la vista
                    Dimension desktopSize = vistaPrincipal.escritorio.getSize();
                    Dimension internal = visVerDetalle.getSize();
                    int x = (desktopSize.width - internal.width) / 2;
                    int y = (desktopSize.height - internal.height) / 2;
                    visVerDetalle.setLocation(x, y);
                    vistaPrincipal.escritorio.remove(visVerDetalle);
                    vistaPrincipal.escritorio.add(visVerDetalle);

                    // 3️⃣ Mostrar y traer al frente
                    visVerDetalle.toFront();

                    visVerDetalle.txtPago.setText("");

                    int lecturaAnterior = 0;

// 1️⃣ Leer la lectura inicial desde el servicio (sin fecha)
                    int lecturaInicial = servicio.getMetrosCubicos();

// 2️⃣ Obtener primer registro de consumo (primer mes con fecha)
                    ModeloConsumo primer = consumos.get(0);
                    int mesPrimero = primer.getNumMes();
                    int añoPrimero = primer.getAño();

// 3️⃣ Datos del consumo actual
                    int mes = cons.getNumMes();
                    int año = cons.getAño();

// 4️⃣ Si estamos en el PRIMER consumo registrado con fecha
                    if (mes == mesPrimero && año == añoPrimero) {
                        lecturaAnterior = lecturaInicial;
                    } // 5️⃣ Si estamos en el mismo año del PRIMER consumo pero en un mes posterior
                    else if (año == añoPrimero && mes > mesPrimero) {
                        lecturaAnterior = base.obtenerLecturaAnterior(mes - 1, año, cons.getNumeroCuenta());
                    } // 6️⃣ Si estamos en enero, pero NO es el primer año → buscar diciembre del año anterior
                    else if (mes == 1) {
                        lecturaAnterior = base.obtenerLecturaAnterior(12, año - 1, cons.getNumeroCuenta());
                    } // 7️⃣ Caso general → buscar mes anterior del mismo año
                    else {
                        lecturaAnterior = base.obtenerLecturaAnterior(mes - 1, año, cons.getNumeroCuenta());
                    }

// 8️⃣ Cálculo del consumo
                    int metrosConsumidos = cons.getMetrosCubicos() - lecturaAnterior;
                    BigDecimal monto = calcularMontoConTarifaMinima(metrosConsumidos);

                    visVerDetalle.labelNumCuenta.setText(seleccionado);
                    visVerDetalle.labelMes.setText(cons.getMes());
                    visVerDetalle.labelConsumoMes.setText(String.valueOf(metrosConsumidos) + " m\u00B3");
                    visVerDetalle.labelDui.setText(base.datosServicios(ServicioSeleccionado).getDuiPropietario());
                    visVerDetalle.labelLecturaActual.setText(String.valueOf(cons.getMetrosCubicos()) + " m\u00B3");
                    visVerDetalle.labelLecturaAnterior.setText(String.valueOf(lecturaAnterior) + " m\u00B3");
                    visVerDetalle.labelAñoLectura.setText(String.valueOf(cons.getAño()));
                    visVerDetalle.labelCosto.setText("$ " + monto
                            .setScale(2, RoundingMode.HALF_UP)
                            .toPlainString());

                }
            }
        }
    }

    private void registrarPago() {

        if (!visVerDetalle.txtPago.getText().isEmpty()) {
            String costoTexto = visVerDetalle.labelCosto.getText()
                    .replace("$", "")
                    .replace(" ", "")
                    .trim();
            double aPagar = Double.parseDouble(costoTexto);

            double pago = Double.parseDouble(visVerDetalle.txtPago.getText());

            if (aPagar != 0) {
                if (pago > aPagar) {
                    double cambio = pago - aPagar;
                    visVerDetalle.labelCambio.setText(String.valueOf(cambio));
                } else if (pago == aPagar) {
                    visVerDetalle.labelCambio.setText(String.valueOf(0));
                } else {
                    JOptionPane.showMessageDialog(vistaPrincipal,
                            "Pago no sufuciente",
                            "ANDA",
                            JOptionPane.WARNING_MESSAGE);
                    visVerDetalle.labelCambio.setText("N/A");
                }
            } else {
                JOptionPane.showMessageDialog(vistaPrincipal,
                        "error en tarifa",
                        "ANDA",
                        JOptionPane.WARNING_MESSAGE);
                visVerDetalle.labelCambio.setText("N/A");
            }

        } else {
            JOptionPane.showMessageDialog(vistaPrincipal,
                    "Ingrese un valor en pago",
                    "ANDA",
                    JOptionPane.WARNING_MESSAGE);
            visVerDetalle.labelCambio.setText("N/A");
        }
    }
      public void finalizarPago() {
        String pago = visVerDetalle.labelCambio.getText() ; 
        
        if (!pago.equals("N/A")) {
            ModeloConsumo cons =  base.encontrarConsumo(getConsumoSeleccionado()); 
            cons.setCancelado(true);
            mostrarConsumosTabla(base.getConsumos());
            JOptionPane.showMessageDialog(vistaPrincipal,
                        "Consumo cancelado",
                        "ANDA",
                        JOptionPane.INFORMATION_MESSAGE);
        }else{
            JOptionPane.showMessageDialog(vistaPrincipal,
                        "Error al cancelar el consumo",
                        "ANDA",
                        JOptionPane.WARNING_MESSAGE);
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

    public String getServicioSeleccionado() {
        int fila = visPagos.tablaConsumos.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(visPagos, "Seleccione un consumo de la tabla");
            return null;
        }

        return visPagos.tablaConsumos.getValueAt(fila, 1).toString();
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
