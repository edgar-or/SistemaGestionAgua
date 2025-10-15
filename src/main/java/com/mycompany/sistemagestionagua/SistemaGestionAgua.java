/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.sistemagestionagua;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.mycompany.sistemagestionagua.controlador.ControladorPrincipal;
import com.mycompany.sistemagestionagua.modelo.ModeloPrincipal;
import com.mycompany.sistemagestionagua.vista.VistaPrincipal;

/**
 *
 * @author ayala
 */
public class SistemaGestionAgua {

    public static void main(String[] args) {

        try {
            //FlatLightLaf.setup();
            // Si quieres, puedes probar FlatDarkLaf, FlatIntelliJLaf, etc.
             //FlatDarkLaf.setup();
        FlatMacDarkLaf.setup(); // tema macOS oscuro
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        VistaPrincipal vista = new VistaPrincipal();
        ModeloPrincipal modelo = new ModeloPrincipal();

        ControladorPrincipal controlador = new ControladorPrincipal(vista, modelo);
        FlatLightLaf.setup();
        controlador.iniciar();

    }
}
