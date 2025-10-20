/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemagestionagua.controlador;

import com.mycompany.sistemagestionagua.modelo.Base;
import com.mycompany.sistemagestionagua.modelo.ModeloPrincipal;
import com.mycompany.sistemagestionagua.modelo.Usuario;
import com.mycompany.sistemagestionagua.vista.VistaAgregarUsuario;
import com.mycompany.sistemagestionagua.vista.VistaConsultarUsurioIndividual;
import com.mycompany.sistemagestionagua.vista.VistaEliminarUsuario;
import com.mycompany.sistemagestionagua.vista.VistaPrincipal;
import com.mycompany.sistemagestionagua.vista.vistaAgregarServicio;
import com.mycompany.sistemagestionagua.vista.vistaVerUsuarios;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ayala
 */
public class ControladorPrincipal {

    VistaPrincipal vista;
    ModeloPrincipal modelo;
    VistaAgregarUsuario vistaAgregarUsuario;
    Base base;
    VistaConsultarUsurioIndividual visConsulUser;
    VistaEliminarUsuario visEliminarUser;
    vistaVerUsuarios visVerUsers;

    public ControladorPrincipal(VistaPrincipal vista, ModeloPrincipal modelo) {
        this.vista = vista;
        this.modelo = modelo;
        this.base = new Base();
        this.visConsulUser = new VistaConsultarUsurioIndividual();
        this.visEliminarUser = new VistaEliminarUsuario();
        this.visVerUsers = new vistaVerUsuarios();

        this.vistaAgregarUsuario = new VistaAgregarUsuario();
        onEvento();

    }

    public void iniciar() {
        vista.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        vista.setExtendedState(JFrame.MAXIMIZED_BOTH);
        vista.setVisible(true);
    }

    private void onEvento() {

        //regiatrar Usuario
        vista.menuRegistrarUsuario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                vistaAgregarUsuario.setSize(600, 400);
                vistaAgregarUsuario.setVisible(true);

                Dimension desktopSize = vista.escritorio.getSize();
                Dimension internal = vistaAgregarUsuario.getSize();

                int x = (desktopSize.width - internal.width) / 2;
                int y = (desktopSize.height - internal.height) / 2;

                vistaAgregarUsuario.setLocation(x, y);

                vista.escritorio.add(vistaAgregarUsuario);
            }
        });

        vistaAgregarUsuario.btnAgregarUsuario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dui = vistaAgregarUsuario.txtduiUsuario.getText();
                String nombre = vistaAgregarUsuario.txtNombreAgreUsuario.getText();
                String apellido = vistaAgregarUsuario.txtApellidoAgreUsuario.getText();

                if (dui.isEmpty() || nombre.isEmpty() || apellido.isEmpty()) {
                    JOptionPane.showMessageDialog(vista, "Complete los campos", "ANDA", JOptionPane.WARNING_MESSAGE);

                } else {
                    // Validar el formato del DUI ANTES de crear el objeto
                    if (!Usuario.validarDUI(dui)) {
                        JOptionPane.showMessageDialog(vista, "Formato de DUI inválido.\nEjemplo: 12345678-9", "ANDA", JOptionPane.WARNING_MESSAGE);
                        vistaAgregarUsuario.txtduiUsuario.requestFocus();
                        return;
                    }

                    try {

                        if (base.agregar(new Usuario(nombre, apellido, dui))) {
                            JOptionPane.showMessageDialog(vista, "Datos guardados", "ANDA", JOptionPane.INFORMATION_MESSAGE);
                            limpiar();

                        } else {
                            JOptionPane.showMessageDialog(vista, "No se pudieron guardar los datos", "ANDA", JOptionPane.WARNING_MESSAGE);
                            limpiar();

                        }

                    } catch (Exception ex) {

                    }
                }

            }

            private void limpiar() {
                vistaAgregarUsuario.txtNombreAgreUsuario.setText("");
                vistaAgregarUsuario.txtApellidoAgreUsuario.setText("");
                vistaAgregarUsuario.txtduiUsuario.setText("");
                vistaAgregarUsuario.txtNombreAgreUsuario.requestFocus();
            }
        });

        vistaAgregarUsuario.btnCerrarAgrelUsuario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                vistaAgregarUsuario.dispose();

            }
            
        });



        //ver Usuarios Tabla. 
           
            
        vista.menuVerUsers.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                visVerUsers.setSize(900, 600);
                visVerUsers.setVisible(true);

                Dimension desktopSize = vista.escritorio.getSize();
                Dimension internal = visVerUsers.getSize();

                int x = (desktopSize.width - internal.width) / 2;
                int y = (desktopSize.height - internal.height) / 2;

                visVerUsers.setLocation(x, y);

                vista.escritorio.add(visVerUsers);

                mostrarUsersTabla(base.getUsuario());

                // agregamos los eventos a los JTextField
                eventoCampo(visVerUsers.txtBuscar1, visVerUsers.txtBuscar2, visVerUsers.txtBuscar3);
                eventoCampo(visVerUsers.txtBuscar2, visVerUsers.txtBuscar1, visVerUsers.txtBuscar3);
                eventoCampo(visVerUsers.txtBuscar3, visVerUsers.txtBuscar1, visVerUsers.txtBuscar2);

            }
        });

        visVerUsers.btnCerrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                visVerUsers.dispose();

            }

        });

        visVerUsers.btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String busca = null;
                String identificador = null;
                if (!visVerUsers.txtBuscar1.getText().isEmpty()) {
                    identificador = "nombre";
                    busca = visVerUsers.txtBuscar1.getText().trim();

                } else if (!visVerUsers.txtBuscar2.getText().isEmpty()) {
                    identificador = "apellido";
                    busca = visVerUsers.txtBuscar2.getText().trim();

                } else if (!visVerUsers.txtBuscar3.getText().isEmpty()) {
                    identificador = "dui";
                    busca = visVerUsers.txtBuscar3.getText().trim();

                }
                if (busca == null) {
                    JOptionPane.showMessageDialog(vista, "Ingrese una informacion de Usuario para buscar", "ANDA", JOptionPane.WARNING_MESSAGE);
                    mostrarUsersTabla(base.getUsuario());
                    //detiene la ejecucion
                    return;
                } else if (base.buscarUsuario(identificador, busca).isEmpty()) {
                    mostrarUsersTabla(base.getUsuario());
                    JOptionPane.showMessageDialog(vista, "No se encontraron usuarios con la informacion ingresada", "ANDA", JOptionPane.WARNING_MESSAGE);
                    visVerUsers.txtBuscar1.setText("");
                    visVerUsers.txtBuscar2.setText("");
                    visVerUsers.txtBuscar3.setText("");
                    //detiene la ejecucion
                    return;
                }

                mostrarUsersTabla(base.buscarUsuario(identificador, busca));

            }
        });

        visVerUsers.btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String seleccionado = getUsuarioSeleccionado();

                if (seleccionado != null) {
                    int opcion = JOptionPane.showConfirmDialog(
                            visVerUsers,
                            "¿Está seguro que desea eliminar este usuario? ",
                            "Confirmación",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE
                    );

                    if (opcion == JOptionPane.YES_OPTION) { // Si confirma

                        boolean eliminado = base.eliminarUsuario(seleccionado);
                        if (eliminado != false) {
                            JOptionPane.showMessageDialog(vista, "Usuario  eliminado con exito ", "ANDA", JOptionPane.INFORMATION_MESSAGE);
                            mostrarUsersTabla(base.getUsuario());
                        } else {
                            JOptionPane.showMessageDialog(vista, "El usuario NO se pudo eliminar", "ANDA", JOptionPane.WARNING_MESSAGE);
                        }

                    }
                } else {
                    JOptionPane.showMessageDialog(vista, "Operacion cancelada ", "ANDA", JOptionPane.INFORMATION_MESSAGE);

                }

            }
        });



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

        //ArrayList<Usuario> usuarios = base.getUsuario();
        String resp = "";

        for (Usuario usuario : usuarios) {

            if (base.bNumCuenta(usuario.getDui()) == null) {
                resp = "No tiene ningun servicio";
            } else {
                resp = base.bNumCuenta(usuario.getDui());
            }

            Object datos[] = {modeloTabla.getRowCount() + 1, usuario.getDui(), usuario.getNombre(), usuario.getApellido(), resp};
            modeloTabla.addRow(datos);
        }
        this.visVerUsers.tablaUsuarios.setModel(modeloTabla);

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
        int fila = visVerUsers.tablaUsuarios.getSelectedRow();
        
        

        if (fila == -1) {

            JOptionPane.showMessageDialog(visVerUsers, "Seleccione un usuario de la tabla");
            return null;

        }
        
         

        
        String dui = visVerUsers.tablaUsuarios.getValueAt(fila, 1).toString(); // Columna 1 = DUI
        return dui;

    }

}

