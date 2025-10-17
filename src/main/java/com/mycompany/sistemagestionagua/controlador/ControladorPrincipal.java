/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemagestionagua.controlador;

import com.mycompany.sistemagestionagua.modelo.Base;
import com.mycompany.sistemagestionagua.modelo.ModeloPrincipal;
import com.mycompany.sistemagestionagua.modelo.Usuario;
import com.mycompany.sistemagestionagua.vista.VistaPrincipal;
import com.mycompany.sistemagestionagua.vista.VistaAgregarUsuario;
import com.mycompany.sistemagestionagua.vista.VistaConsultarUsurioIndividual;
import com.mycompany.sistemagestionagua.vista.VistaEliminarUsuario;
import com.mycompany.sistemagestionagua.vista.vistaAgregarServicio;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

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
    

    public ControladorPrincipal(VistaPrincipal vista, ModeloPrincipal modelo) {
        this.vista = vista;
        this.modelo = modelo;
        this.base = new Base();
        this.visConsulUser = new VistaConsultarUsurioIndividual(); 
        this.visEliminarUser = new VistaEliminarUsuario(); 

        this.vistaAgregarUsuario = new VistaAgregarUsuario();
        onEvento();

    }

    public void iniciar() {
        vista.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        vista.setExtendedState(JFrame.MAXIMIZED_BOTH);
        vista.setVisible(true);
    }

   private void onEvento() {
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
                    // 🧩 Validar el formato del DUI ANTES de crear el objeto
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
        
        vistaAgregarUsuario.btnCerrarAgrelUsuario.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                vistaAgregarUsuario.dispose();

            }       
        
        });
        
        //pantalla de Consultar Usuario
        vista.menuConsultarUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                visConsulUser.setSize(600, 400);
                visConsulUser.setVisible(true);
                
                Dimension desktopSize = vista.escritorio.getSize();
                Dimension internal = visConsulUser.getSize();

                int x = (desktopSize.width - internal.width) / 2;
                int y = (desktopSize.height - internal.height) / 2;

                visConsulUser.setLocation(x, y);
                
                vista.escritorio.add(visConsulUser);
                
            }
        });
        
         visConsulUser.btnCerrarConsultarUser.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                visConsulUser.dispose();

            }       
        
        });
         
         
         this.visConsulUser.btnBuscarUsuario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String texto = visConsulUser.txtBuscarUsuario.getText();
                

                if (texto.isEmpty()) {
                    JOptionPane.showMessageDialog(vista, "¿Que estas buscando?", "Mi empresa", JOptionPane.WARNING_MESSAGE);
                } else {
                    Usuario temp = base.buscar(texto); 
                    if (temp!= null) {
                        visConsulUser.txtNombreAgreUsuario.setText(temp.getNombre());
                                                visConsulUser.txtApellidoAgreUsuario.setText(temp.getApellido());
                        visConsulUser.txtduiUsuario.setText(String.valueOf(temp.getDui()));

                    }else{
                                            JOptionPane.showMessageDialog(vista, "Dato NO encontrado", "Mi empresa", JOptionPane.WARNING_MESSAGE);

                    }
                }

            }

           
        });
         
         
         //pantalla de Eliminar Usuario
        vista.menuEliminarUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                visEliminarUser.setSize(600, 400);
                visEliminarUser.setVisible(true);
                
                Dimension desktopSize = vista.escritorio.getSize();
                Dimension internal = visEliminarUser.getSize();

                int x = (desktopSize.width - internal.width) / 2;
                int y = (desktopSize.height - internal.height) / 2;

                visEliminarUser.setLocation(x, y);
                
                vista.escritorio.add(visEliminarUser);
                
            }
        });
        
        
        visEliminarUser.btnCerrarConsultarUser.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                visEliminarUser.dispose();

            }       
        
        });
         
         
         this.visEliminarUser.btnBuscarUsuario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String texto = visEliminarUser.txtBuscarUsuario.getText();
                

                if (texto.isEmpty()) {
                    JOptionPane.showMessageDialog(vista, "¿Que estas buscando?", "Mi empresa", JOptionPane.WARNING_MESSAGE);
                } else {
                    Usuario temp = base.buscarEliminar(texto); 
                    if (temp!= null) {
                        visEliminarUser.txtNombreAgreUsuario.setText(temp.getNombre());
                                                visEliminarUser.txtApellidoAgreUsuario.setText(temp.getApellido());

                    }else{
                                            JOptionPane.showMessageDialog(vista, "Dato NO encontrado", "Mi empresa", JOptionPane.WARNING_MESSAGE);

                    }
                }

            }

           
        });
         
         
         this.visEliminarUser.btnEliminarUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String texto = visEliminarUser.txtBuscarUsuario.getText(); 
                
                if (base.eliminar(texto)==true) {
                                                                JOptionPane.showMessageDialog(vista, "Usuario Eliminado", "Mi empresa", JOptionPane.WARNING_MESSAGE);

                }else{
                                                                                    JOptionPane.showMessageDialog(vista, "Usuario NO Eliminado", "Mi empresa", JOptionPane.WARNING_MESSAGE);

                }

                

            }

           
        });
         
         
        
        
         
         
         



        
    }

}
