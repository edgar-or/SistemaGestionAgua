/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemagestionagua.controlador;

import com.mycompany.sistemagestionagua.modelo.Base;
import com.mycompany.sistemagestionagua.modelo.Usuario;
import com.mycompany.sistemagestionagua.vista.VistaAgregarUsuario;
import com.mycompany.sistemagestionagua.vista.VistaModificarUsuario;
import com.mycompany.sistemagestionagua.vista.VistaPrincipal;
import com.mycompany.sistemagestionagua.vista.vistaVerUsuarios;
import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ayala
 */
public class ControladorUsuario {

    private VistaPrincipal vistaPrincipal;
    private VistaAgregarUsuario visAgregarUser;
    private VistaModificarUsuario visModificarUser;
    private vistaVerUsuarios visVerUser;
    private Base base;

    public ControladorUsuario(VistaPrincipal vistaPrincipal, vistaVerUsuarios visVerUser, Base base) {
        this.vistaPrincipal = vistaPrincipal;
        this.visAgregarUser = new VistaAgregarUsuario();
        this.visModificarUser = new VistaModificarUsuario();
        this.visVerUser = visVerUser;
        this.base = base;

        configurarEventos();

    }

    private void configurarEventos() {

        // --- Vista Agregar Usuario ---
        visAgregarUser.btnAgregarUsuario.addActionListener(e -> agregarUsuario());
        visAgregarUser.btnCerrarAgrelUsuario.addActionListener(e -> visAgregarUser.dispose());

        // --- Vista Ver Usuarios ---
        visVerUser.btnBuscar.addActionListener(e -> buscarUser());
        visVerUser.btnEliminar.addActionListener(e -> eliminarUser());
        visVerUser.btnModificar.addActionListener(e -> mostrarVistaModificar());
        visModificarUser.btnModificarUsuario.addActionListener(e -> modificarUsuario());
        visModificarUser.btCerrar.addActionListener(e -> visModificarUser.dispose());
        visVerUser.btnCerrar.addActionListener(e -> visVerUser.dispose());

        // --- Desactivar buscadores ---
        eventoCampo(visVerUser.txtBuscar1, visVerUser.txtBuscar2, visVerUser.txtBuscar3);
        eventoCampo(visVerUser.txtBuscar2, visVerUser.txtBuscar1, visVerUser.txtBuscar3);
        eventoCampo(visVerUser.txtBuscar3, visVerUser.txtBuscar1, visVerUser.txtBuscar2);

    }

   private void agregarUsuario() {

    String dui = visAgregarUser.txtduiUsuario.getText();
    String nombre = visAgregarUser.txtNombreAgreUsuario.getText();
    String apellido = visAgregarUser.txtApellidoAgreUsuario.getText();

    // 1️⃣ Validar campos vacíos
    if (dui.isEmpty() || nombre.isEmpty() || apellido.isEmpty()) {
        JOptionPane.showMessageDialog(vistaPrincipal, "Complete los campos", "ANDA", JOptionPane.WARNING_MESSAGE);
        return;
    }

    // 2️⃣ Validar que NOMBRE solo tenga letras
    if (!soloLetras(nombre)) {
        JOptionPane.showMessageDialog(vistaPrincipal,
                "El nombre solo debe contener letras",
                "ANDA",
                JOptionPane.WARNING_MESSAGE);
        visAgregarUser.txtNombreAgreUsuario.requestFocus();
        return;
    }

    // 3️⃣ Validar que APELLIDO solo tenga letras
    if (!soloLetras(apellido)) {
        JOptionPane.showMessageDialog(vistaPrincipal,
                "El apellido solo debe contener letras",
                "ANDA",
                JOptionPane.WARNING_MESSAGE);
        visAgregarUser.txtApellidoAgreUsuario.requestFocus();
        return;
    }

    // 4️⃣ Validar DUI
    if (!Usuario.validarDUI(dui)) {
        JOptionPane.showMessageDialog(vistaPrincipal,
                "Formato de DUI inválido.\nEjemplo: 12345678-9",
                "ANDA",
                JOptionPane.WARNING_MESSAGE);
        visAgregarUser.txtduiUsuario.requestFocus();
        return;
    }

    // 5️⃣ Guardar datos
    try {
        if (base.agregar(new Usuario(nombre, apellido, dui))) {
            JOptionPane.showMessageDialog(vistaPrincipal, "Datos guardados", "ANDA", JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(vistaPrincipal, "No se pudieron guardar los datos", "ANDA", JOptionPane.WARNING_MESSAGE);
            limpiarCampos();
        }
    } catch (Exception ex) {
        ex.printStackTrace();
    }
}


    private void limpiarCampos() {
        visAgregarUser.txtduiUsuario.setText("");
        visAgregarUser.txtNombreAgreUsuario.setText("");
        visAgregarUser.txtApellidoAgreUsuario.setText("");
    }

    public static boolean soloLetras(String texto) {
        return texto.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$");
    }

    public void mostrarVistaAgregar() {
        mostrarVista(visAgregarUser);
        visAgregarUser.toFront();
    }

    public void mostrarVistaVerUsuarios() {
        mostrarVista(visVerUser);
        visVerUser.toFront();
        mostrarUsersTabla(base.getUsuario());
    }

    public void mostrarUsersTabla(ArrayList<Usuario> usuarios) {
        DefaultTableModel modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // <-- evita edición en todas las columnas
            }
        };

        String titulos[] = {"N°", "Dui", "Nombre", "Apellido", "Numeros de Servicios"};
        modeloTabla.setColumnIdentifiers(titulos);

        String resp = "";

        for (Usuario usuario : usuarios) {
            ArrayList<String> temp = base.bNumCuenta(usuario.getDui());

            if (temp == null || temp.isEmpty()) {
                resp = "No tiene ningun servicio";
            } else {

                resp = String.join(", ", temp);

            }

            Object datos[] = {modeloTabla.getRowCount() + 1, usuario.getDui(), usuario.getNombre(), usuario.getApellido(), resp};
            modeloTabla.addRow(datos);
        }
        this.visVerUser.tablaUsuarios.setModel(modeloTabla);

    }

    private void mostrarVista(JInternalFrame frame) {

        frame.toFront();
        frame.setVisible(true);

        Dimension desktopSize = vistaPrincipal.escritorio.getSize();
        Dimension internal = frame.getSize();

        int x = (desktopSize.width - internal.width) / 2;
        int y = (desktopSize.height - internal.height) / 2;

        frame.setLocation(x, y);
        vistaPrincipal.escritorio.add(frame);

    }

    private void buscarUser() {
        String busca = null;
        String identificador = null;
        if (!visVerUser.txtBuscar1.getText().isEmpty()) {
            identificador = "nombre";
            busca = visVerUser.txtBuscar1.getText().trim();

        } else if (!visVerUser.txtBuscar2.getText().isEmpty()) {
            identificador = "apellido";
            busca = visVerUser.txtBuscar2.getText().trim();

        } else if (!visVerUser.txtBuscar3.getText().isEmpty()) {
            identificador = "dui";
            busca = visVerUser.txtBuscar3.getText().trim();

        }
        if (busca == null) {
            JOptionPane.showMessageDialog(vistaPrincipal, "Ingrese una informacion de Usuario para buscar", "ANDA", JOptionPane.WARNING_MESSAGE);
            mostrarUsersTabla(base.getUsuario());
            //detiene la ejecucion
            return;
        } else if (base.buscarUsuario(identificador, busca).isEmpty()) {
            mostrarUsersTabla(base.getUsuario());
            JOptionPane.showMessageDialog(vistaPrincipal, "No se encontraron usuarios con la informacion ingresada", "ANDA", JOptionPane.WARNING_MESSAGE);
            visVerUser.txtBuscar1.setText("");
            visVerUser.txtBuscar2.setText("");
            visVerUser.txtBuscar3.setText("");
            //detiene la ejecucion
            return;
        }

        mostrarUsersTabla(base.buscarUsuario(identificador, busca));

    }

    private void eliminarUser() {
        String seleccionado = getUsuarioSeleccionado();

        if (seleccionado != null) {
            int opcion = JOptionPane.showConfirmDialog(
                    visVerUser,
                    "¿Está seguro que desea eliminar este usuario? ",
                    "Confirmación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (opcion == JOptionPane.YES_OPTION) { // Si confirma

                boolean eliminado = base.eliminarUsuario(seleccionado);
                if (eliminado != false) {
                    JOptionPane.showMessageDialog(vistaPrincipal, "Usuario  eliminado con exito ", "ANDA", JOptionPane.INFORMATION_MESSAGE);
                    mostrarUsersTabla(base.getUsuario());
                } else {
                    JOptionPane.showMessageDialog(vistaPrincipal, "El usuario NO se pudo eliminar", "ANDA", JOptionPane.WARNING_MESSAGE);
                }

            }
        } else {
            JOptionPane.showMessageDialog(vistaPrincipal, "Operacion cancelada ", "ANDA", JOptionPane.INFORMATION_MESSAGE);

        }

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

    private String getUsuarioSeleccionado() {
        int fila = visVerUser.tablaUsuarios.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(visVerUser, "Seleccione un usuario de la tabla");
            return null;

        }

        String dui = visVerUser.tablaUsuarios.getValueAt(fila, 1).toString(); // Columna 1 = DUI
        return dui;

    }

    private void mostrarVistaModificar() {
        String seleccionado = getUsuarioSeleccionado();
        if (seleccionado != null) {

            // 1. Agregar primero al escritorio
            vistaPrincipal.escritorio.add(visModificarUser);

            // 2. Centrar
            Dimension desktopSize = vistaPrincipal.escritorio.getSize();
            Dimension internal = visModificarUser.getSize();
            int x = (desktopSize.width - internal.width) / 2;
            int y = (desktopSize.height - internal.height) / 2;
            visModificarUser.setLocation(x, y);
            visModificarUser.setVisible(true);

            //cargar datos de usuario
            ArrayList<Usuario> usuarios = base.buscarUsuario("dui", seleccionado);

            for (Usuario usuario : usuarios) {
                visModificarUser.txtNombreAgreUsuario.setText(usuario.getNombre());
                visModificarUser.txtApellidoAgreUsuario.setText(usuario.getApellido());
                visModificarUser.txtduiUsuario.setText(usuario.getDui());

            }

        }
    }

    private void modificarUsuario() {

        String seleccionado = getUsuarioSeleccionado();

        String nuevoNombre = visModificarUser.txtNombreAgreUsuario.getText();
        String nuevoApellido = visModificarUser.txtApellidoAgreUsuario.getText();
        String nuevoDui = visModificarUser.txtduiUsuario.getText();

        if (base.modificarUsuario(seleccionado, nuevoDui, nuevoNombre, nuevoApellido)) {
            JOptionPane.showMessageDialog(vistaPrincipal, "Usuario modificado con éxito", "ANDA", JOptionPane.INFORMATION_MESSAGE);
            visModificarUser.dispose();

            mostrarUsersTabla(base.getUsuario());

        } else {
            JOptionPane.showMessageDialog(vistaPrincipal, "El usuario NO se pudo modificar", "ANDA", JOptionPane.WARNING_MESSAGE);
        }

    }

}
