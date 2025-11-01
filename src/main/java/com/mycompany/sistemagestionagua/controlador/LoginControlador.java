package com.mycompany.sistemagestionagua.controlador;


import com.mycompany.sistemagestionagua.modelo.LoginModelo;
import com.mycompany.sistemagestionagua.vista.LoginVista;
import com.mycompany.sistemagestionagua.vista.VistaPrincipal;
import javax.swing.JOptionPane; 


public class LoginControlador {

    private final LoginVista loginVista;
    private final LoginModelo loginModelo;
    

    public LoginControlador(LoginVista vistaLogin, LoginModelo modeloLogin) {
        this.loginVista = vistaLogin;
        this.loginModelo = modeloLogin;

        this.loginVista.btnLogin.addActionListener(e -> {
            
            String usuario = vistaLogin.txtUsuario.getText();
            String password = new String(vistaLogin.txtContra.getPassword());

            if (usuario.isEmpty() || password.isEmpty()) {
                mostrarError("El usuario y la contraseña no pueden estar vacíos.");
                return; 
            }

            modeloLogin.setUsuario(usuario);
            modeloLogin.setPassword(password);

            boolean esValido = modeloLogin.validarCredenciales();

            if (esValido) {
                VistaPrincipal vista = new  VistaPrincipal(); 
                
               vista.setVisible(true);
                
                
                new ControladorPrincipal(vista); 
                
                
                
                cerrar(); 
            } else {
                mostrarError("Usuario o contraseña incorrectos.");
            }
        });
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(loginVista, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void cerrar() {
        loginVista.dispose();
    }
    
    public void iniciar(){
        
        loginVista.setLocationRelativeTo(null);
        loginVista.setVisible(true);
    }
}