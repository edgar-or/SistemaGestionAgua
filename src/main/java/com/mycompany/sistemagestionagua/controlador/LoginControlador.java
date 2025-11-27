package com.mycompany.sistemagestionagua.controlador;


import com.mycompany.sistemagestionagua.modelo.Base;
import com.mycompany.sistemagestionagua.modelo.LoginModelo;
import com.mycompany.sistemagestionagua.modelo.ModeloRuta;
import com.mycompany.sistemagestionagua.modelo.Servicio;
import com.mycompany.sistemagestionagua.modelo.Usuario;
import com.mycompany.sistemagestionagua.vista.LoginVista;
import com.mycompany.sistemagestionagua.vista.VistaPrincipal;
import javax.swing.JOptionPane; 


public class LoginControlador {

    private final LoginVista loginVista;
    private final LoginModelo loginModelo;
    VistaPrincipal vista; 
    ControladorPrincipal controladorPrincipal; 
    Base base; 
    

    public LoginControlador(LoginVista vistaLogin, LoginModelo modeloLogin) {
        this.loginVista = vistaLogin;
        this.loginModelo = modeloLogin;
        this.vista  = null; 
        this.base = new Base();
        //Para boton Enter
        this.loginVista.getRootPane().setDefaultButton(this.loginVista.btnLogin);

        this.loginVista.btnLogin.addActionListener(e -> login() );
 
    }
    
     private void login() {

        String usuario = loginVista.txtUsuario.getText();
        String password = new String(loginVista.txtContra.getPassword());

        if (usuario.isEmpty() || password.isEmpty()) {
            mostrarError("El usuario y la contraseña no pueden estar vacíos.");
            return; 
        }

        loginModelo.setUsuario(usuario);
        loginModelo.setPassword(password);

        boolean esValido = loginModelo.validarCredenciales();

        if (esValido) {

            vista = new VistaPrincipal(); 

            base.cargarDatosIniciales();

            controladorPrincipal = new ControladorPrincipal(vista, base);
            controladorPrincipal.iniciar();

            // Registrar listener DESPUÉS de crear la vista
            vista.btnCerrarSesion.addActionListener(e -> cerrarSesion());

            cerrar(); 
        } else {
            mostrarError("Usuario o contraseña incorrectos.");
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(loginVista, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void cerrar() {
        loginVista.dispose();
    }
    
    public void iniciar(){
        
        loginVista.setLocationRelativeTo(null);
        loginVista.getRootPane().setDefaultButton(loginVista.btnLogin);
        loginVista.setVisible(true);
    }
    
     private void cerrarSesion(){
        vista.dispose();
        vista = null; 
        iniciar();
        
        loginVista.txtUsuario.setText("");
       loginVista.txtContra.setText("");
       loginVista.txtUsuario.requestFocus();
    }
    
     
     
}