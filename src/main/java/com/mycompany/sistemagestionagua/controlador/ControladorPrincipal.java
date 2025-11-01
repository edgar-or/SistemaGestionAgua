package com.mycompany.sistemagestionagua.controlador;

import com.mycompany.sistemagestionagua.modelo.Base;
import com.mycompany.sistemagestionagua.modelo.ModeloPrincipal;
import com.mycompany.sistemagestionagua.modelo.Servicio;
import com.mycompany.sistemagestionagua.modelo.Usuario;
import com.mycompany.sistemagestionagua.vista.VistaAgregarConsumo;
import com.mycompany.sistemagestionagua.vista.VistaAgregarUsuario;
import com.mycompany.sistemagestionagua.vista.VistaConsultarUsurioIndividual;
import com.mycompany.sistemagestionagua.vista.VistaEliminarUsuario;
import com.mycompany.sistemagestionagua.vista.VistaModificarUsuario;
import com.mycompany.sistemagestionagua.vista.VistaPrincipal;
import com.mycompany.sistemagestionagua.vista.vistaAgregarServicio;
import com.mycompany.sistemagestionagua.vista.vistaVerServicios;
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
    vistaVerServicios verServicio;
    VistaAgregarConsumo agregarconsumo;
    vistaVerUsuarios visVerUsers;
    VistaModificarUsuario visModficarUser;
    vistaAgregarServicio visAgregarServicio;

    public ControladorPrincipal(VistaPrincipal vista) {
        this.vista = vista;
        this.modelo = modelo;
        this.base = new Base();
        this.visVerUsers = new vistaVerUsuarios();
        this.visModficarUser = new VistaModificarUsuario();
        this.visAgregarServicio = new vistaAgregarServicio();

        this.verServicio = new vistaVerServicios();

        this.agregarconsumo = new VistaAgregarConsumo();



        this.agregarconsumo=new VistaAgregarConsumo();


        this.visModficarUser = new VistaModificarUsuario();


        this.agregarconsumo=new VistaAgregarConsumo();

        this.visModficarUser = new VistaModificarUsuario();


        this.verServicio = new vistaVerServicios();

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

        //fin de tabla ver usuarios
        //boton modificar en ver usuarios. 
        visVerUsers.btnModificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String seleccionado = getUsuarioSeleccionado();
                if (seleccionado != null) {

                    visModficarUser.setSize(600, 400);

                    // 1. Agregar primero al escritorio
                    vista.escritorio.add(visModficarUser);

                    // 2. Centrar
                    Dimension desktopSize = vista.escritorio.getSize();
                    Dimension internal = visModficarUser.getSize();
                    int x = (desktopSize.width - internal.width) / 2;
                    int y = (desktopSize.height - internal.height) / 2;
                    visModficarUser.setLocation(x, y);

                    //cargar datos de usuario
                    ArrayList<Usuario> usuarios = base.buscarUsuario("dui", seleccionado);

                    for (Usuario usuario : usuarios) {
                        visModficarUser.txtNombreAgreUsuario.setText(usuario.getNombre());
                        visModficarUser.txtApellidoAgreUsuario.setText(usuario.getApellido());
                        visModficarUser.txtduiUsuario.setText(usuario.getDui());

                    }

                    // 3. Mostrar al final
                    visModficarUser.setVisible(true);

                }

            }
        });

        visModficarUser.btnModificarUsuario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String seleccionado = getUsuarioSeleccionado();

                String nuevoNombre = visModficarUser.txtNombreAgreUsuario.getText();
                String nuevoApellido = visModficarUser.txtApellidoAgreUsuario.getText();
                String nuevoDui = visModficarUser.txtduiUsuario.getText();

                if (base.modificarUsuario(seleccionado, nuevoDui, nuevoNombre, nuevoApellido)) {
                    JOptionPane.showMessageDialog(vista, "Usuario modificado con éxito", "ANDA", JOptionPane.INFORMATION_MESSAGE);
                    visModficarUser.dispose();

                    mostrarUsersTabla(base.getUsuario());

                } else {
                    JOptionPane.showMessageDialog(vista, "El usuario NO se pudo modificar", "ANDA", JOptionPane.WARNING_MESSAGE);
                }

            }
        });

        visModficarUser.btCerrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                visModficarUser.dispose();

            }

        });

        //Agregar Servicios
        visVerUsers.btnAgregarServicio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String seleccionado = getUsuarioSeleccionado();

                if (seleccionado != null) {

                    visAgregarServicio.setSize(600, 400);

                    // 1. Agregar primero al escritorio
                    vista.escritorio.add(visAgregarServicio);

                    // 2. Centrar
                    Dimension desktopSize = vista.escritorio.getSize();
                    Dimension internal = visAgregarServicio.getSize();
                    int x = (desktopSize.width - internal.width) / 2;
                    int y = (desktopSize.height - internal.height) / 2;
                    visAgregarServicio.setLocation(x, y);

                    visAgregarServicio.setVisible(true);

                    visAgregarServicio.txtNumDui.setText(seleccionado);
                    visAgregarServicio.txtNumDui.setEnabled(false);
                    visAgregarServicio.txtNumCuenta.setText(generarNumCuenta());
                    visAgregarServicio.txtNumCuenta.setEnabled(false);


                }

            }
        });

        visAgregarServicio.btnAgregarSer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!visAgregarServicio.txtDireccion.getText().isEmpty()) {
                    String dui = visAgregarServicio.txtNumDui.getText();
                    String numCuenta = visAgregarServicio.txtNumCuenta.getText();
                    String direccion = visAgregarServicio.txtDireccion.getText();

                    try {
                        base.agregarServicio(new Servicio(dui, numCuenta, direccion));
                        JOptionPane.showMessageDialog(vista, "Servicio agregado correctamente", "ANDA", JOptionPane.INFORMATION_MESSAGE);
                        visAgregarServicio.txtDireccion.setText("");
                        mostrarUsersTabla(base.getUsuario());
                        visAgregarServicio.dispose();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(vista, "El servicio NO se pudo agregar", "ANDA", JOptionPane.WARNING_MESSAGE);

                    }

                }
            }
        });

        visAgregarServicio.btnCerrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                visAgregarServicio.dispose();
            }
        });

        //inicio servicios
        vista.menuVerServicios.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                verServicio.setSize(900, 600);
                verServicio.setVisible(true);

                Dimension desktopSize = vista.escritorio.getSize();
                Dimension internal = verServicio.getSize();

                int x = (desktopSize.width - internal.width) / 2;
                int y = (desktopSize.height - internal.height) / 2;

                verServicio.setLocation(x, y);

                vista.escritorio.add(verServicio);

                mostrarServiciosTabla(base.getServicios());

                // agregamos los eventos a los JTextField
                eventoCampo(verServicio.txtBuscarDui, verServicio.txtBuscarCuenta, verServicio.txtBuscarDireccion);
                eventoCampo(verServicio.txtBuscarCuenta, verServicio.txtBuscarDui, verServicio.txtBuscarDireccion);
                eventoCampo(verServicio.txtBuscarDireccion, verServicio.txtBuscarDui, verServicio.txtBuscarCuenta);

            }
        });

        verServicio.btnCerrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verServicio.dispose();

            }

        });

        verServicio.btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String busca = null;
                String identificador = null;
                if (!verServicio.txtBuscarDui.getText().isEmpty()) {
                    identificador = "nombre";
                    busca = verServicio.txtBuscarDui.getText().trim();

                } else if (!verServicio.txtBuscarCuenta.getText().isEmpty()) {
                    identificador = "DUI";
                    busca = verServicio.txtBuscarCuenta.getText().trim();

                } else if (!verServicio.txtBuscarDireccion.getText().isEmpty()) {
                    identificador = "Cuenta";
                    busca = verServicio.txtBuscarDireccion.getText().trim();

                }
                if (busca == null) {
                    JOptionPane.showMessageDialog(vista, "Ingrese una informacion de servicio para buscar", "ANDA", JOptionPane.WARNING_MESSAGE);
                    mostrarUsersTabla(base.getUsuario());
                    //detiene la ejecucion
                    return;
                } else if (base.buscarUsuario(identificador, busca).isEmpty()) {
                    mostrarUsersTabla(base.getUsuario());
                    JOptionPane.showMessageDialog(vista, "No se encontraron servicios con la informacion ingresada", "ANDA", JOptionPane.WARNING_MESSAGE);
                    verServicio.txtBuscarDui.setText("");
                    verServicio.txtBuscarCuenta.setText("");
                    verServicio.txtBuscarDireccion.setText("");
                    //detiene la ejecucion
                    return;
                }

                mostrarUsersTabla(base.buscarUsuario(identificador, busca));

            }
        });

        verServicio.btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String seleccionado = getUsuarioSeleccionado();

                if (seleccionado != null) {
                    int opcion = JOptionPane.showConfirmDialog(
                            verServicio,
                            "¿Está seguro que desea eliminar este servicio? ",
                            "Confirmación",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE
                    );

                    if (opcion == JOptionPane.YES_OPTION) { // Si confirma

                        boolean eliminado = base.eliminarUsuario(seleccionado);
                        if (eliminado != false) {
                            JOptionPane.showMessageDialog(vista, "Servicio  eliminado con exito ", "ANDA", JOptionPane.INFORMATION_MESSAGE);
                            mostrarUsersTabla(base.getUsuario());
                        } else {
                            JOptionPane.showMessageDialog(vista, "El servicio NO se pudo eliminar", "ANDA", JOptionPane.WARNING_MESSAGE);
                        }

                    }
                } else {
                    JOptionPane.showMessageDialog(vista, "Operacion cancelada ", "ANDA", JOptionPane.INFORMATION_MESSAGE);

                }

            }
        });// fin tabla de servicios


        
        //vistaparaconsumo
        verServicio.btnAgrgarConsumo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String seleccionado = getUsuarioSeleccionado();
                if (seleccionado != null) {

                    agregarconsumo.setSize(600, 400);

                    // 1. Agregar primero al escritorio
                    vista.escritorio.add(agregarconsumo);

                    // 2. Centrar
                    Dimension desktopSize = vista.escritorio.getSize();
                    Dimension internal = agregarconsumo.getSize();
                    int x = (desktopSize.width - internal.width) / 2;
                    int y = (desktopSize.height - internal.height) / 2;
                    agregarconsumo.setLocation(x, y);

                    //cargar datos de consumo
                    /*
                    ArrayList<Usuario> usuarios = base.buscarUsuario("dui", seleccionado);

                    for (Usuario usuario : usuarios) {
                        visModficarUser.txtNombreAgreUsuario.setText(usuario.getNombre());
                        visModficarUser.txtApellidoAgreUsuario.setText(usuario.getApellido());
                        visModficarUser.txtduiUsuario.setText(usuario.getDui());

                    }*/
                    // 3. Mostrar al final
                    agregarconsumo.setVisible(true);

                }

            }
        });

        agregarconsumo.btnCerrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarconsumo.dispose();

            }

        });

    }//no tocar

    //fumciones aqui abajo 
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
            ArrayList<String> temp = base.bNumCuenta(usuario.getDui());

            if (temp== null|| temp.isEmpty()) {
                resp = "No tiene ningun servicio";
            } else {

                    resp = String.join(", ", temp);
                

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

    //funcion para servicio
    public void mostrarServiciosTabla(ArrayList<Servicio> servicios) {
        DefaultTableModel modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // <-- evita edición en todas las columnas
            }
        };

        String titulos[] = {"N°", "DUI propietario", "Nombre", "Apellido", "Direccion", "N° Cuenta"};
        modeloTabla.setColumnIdentifiers(titulos);

        String resp = "";

        for (Servicio servicio : servicios) {

            if (servicio.getDuiPropietario() == null) {
                resp = "No tiene ningun servicio";
            } else {
                resp = servicio.getDuiPropietario();
            }

            Object datos[] = {modeloTabla.getRowCount() + 1, servicio.getDuiPropietario(), servicio.getnumeroCuenta(), servicio.getDireccion(), resp};
            modeloTabla.addRow(datos);
        }
        this.verServicio.tablaServicios.setModel(modeloTabla);

    }
    
    
    private static int correlativo = 1;

private String generarNumCuenta() {
    int año = java.time.Year.now().getValue(); // obtiene el año actual
    String numero = String.format("%04d", correlativo); 
    correlativo++; 
    return año + "-" + numero;
}

    private String getServicioSeleccionado() {
        int fila = verServicio.tablaServicios.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(verServicio, "Seleccione un usuario de la tabla");
            return null;

        }

        String dui = verServicio.tablaServicios.getValueAt(fila, 1).toString(); // Columna 1 = DUI
        return dui;

    }

}
