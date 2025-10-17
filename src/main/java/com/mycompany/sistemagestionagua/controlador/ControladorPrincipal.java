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
    

    public ControladorPrincipal(VistaPrincipal vista, ModeloPrincipal modelo) {
        this.vista = vista;
        this.modelo = modelo;
        this.base = new Base();

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



        
    }

}
