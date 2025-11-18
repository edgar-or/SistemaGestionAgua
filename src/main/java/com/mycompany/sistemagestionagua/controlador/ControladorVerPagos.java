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
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ayala
 */
public class ControladorVerPagos {
    private VistaPagos visPagos; 
    private ControladorPrincipal controladorPrincipal; 
    private Base base; 
    private VistaPrincipal vistaPrincipal;
    private PrecioMC precio;
    
  


    public ControladorVerPagos(VistaPrincipal vistaPrincipal, Base base) {
        this.vistaPrincipal = vistaPrincipal; 
        this.visPagos = new VistaPagos();
        this.base = base;
        
        eventos();
    }

    private void eventos() {
        visPagos.btnCerrar.addActionListener(e-> visPagos.dispose());
        visPagos.btnPagar.addActionListener(e-> pagarCosumo(base.getConsumos()));
        
    }
    
    public void mostrarVista() {
        visPagos.setSize(1000, 600);
        visPagos.setVisible(true);

        // 2️⃣ Centrar la vista
        Dimension desktopSize = vistaPrincipal.escritorio.getSize();
        Dimension internal = visPagos.getSize();
        int x = (desktopSize.width - internal.width) / 2;
        int y = (desktopSize.height - internal.height) / 2;
        visPagos.setLocation(x, y);
        vistaPrincipal.escritorio.remove(visPagos);
        vistaPrincipal.escritorio.add(visPagos);

        // 3️⃣ Mostrar y traer al frente
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

    String titulos[] = {"Id Consumo", "N° Cuenta", "N° de DUI", "Nombre de propietario",
        "Mes Lectura", "Lectura anterior", "Lectura actual", "Metros consumidos", "Monto", "Pago"};
    modeloTabla.setColumnIdentifiers(titulos);

    for (ModeloConsumo cons : consumos) {

        Servicio serv = base.encontrarServicioPorNumCuenta(cons.getNumeroCuenta());
        String nombre = base.nombrePorDui(serv.getDuiPropietario());
        String apellido = base.apellido(serv.getDuiPropietario());
        String pagoStr = cons.isCancelado() ? "Cancelado" : "Pendiente";

        // --- Lectura anterior ---
        int lecturaAnterior;
        int metrosConsumidos;

        if (cons.getNumMes() - 1 == 0) {
            lecturaAnterior = serv.getMetrosCubicos();
        } else {
            lecturaAnterior = base.obtenerLecturaAnterior(cons.getNumMes() - 1);
        }

        metrosConsumidos = cons.getMetrosCubicos() - lecturaAnterior;

        // --- Obtener precio según rangos ---
        BigDecimal precio = obtenerPrecioPorRango(metrosConsumidos);
        BigDecimal metrosBD = BigDecimal.valueOf(metrosConsumidos);
        BigDecimal monto = metrosBD.multiply(precio);

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

     
     private ArrayList<ModeloConsumo> listaPendientes(ArrayList<ModeloConsumo> consumos){
         ArrayList<ModeloConsumo> pagos = new ArrayList<>();
         for (ModeloConsumo consumo : consumos) {
             if (consumo.isCancelado()==false) {
                 pagos.add(consumo);
             }
             
            
         }
         return pagos; 
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

                    mostrarConsumosTabla(listaPendientes(base.getConsumos()));
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
     
     
         public String getConsumoSeleccionado() {
        int fila = visPagos.tablaConsumos.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(visPagos, "Seleccione un consumo de la tabla");
            return null;

        }

        String idConsumo = visPagos.tablaConsumos.getValueAt(fila, 0).toString(); // Columna 0 = idConsumo
        return idConsumo;

    }
         private BigDecimal obtenerPrecioPorRango(int metros) {

    for (PrecioMC rango : PrecioMC.obtenerTodos()) {

        // Rango abierto (hasta = -1)
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

    JOptionPane.showMessageDialog(vistaPrincipal,
            "No se encontró un rango válido para " + metros + " m³.\n" +
            "Verifique los rangos registrados.",
            "Error en rangos", JOptionPane.ERROR_MESSAGE);

    return BigDecimal.ZERO;
}

    
    
    
    
    
    
}
