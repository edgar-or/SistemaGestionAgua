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
import java.awt.Font;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;

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

            // 🔹 Activar decoraciones modernas en ventanas
            JFrame.setDefaultLookAndFeelDecorated(true);
            JDialog.setDefaultLookAndFeelDecorated(true);

            // 🔹 Opcional: usar escalado HiDPI
            System.setProperty("sun.java2d.uiScale.enabled", "true");
            System.setProperty("flatlaf.useWindowDecorations", "true");

            // 🔹 Opcional: establecer fuente base (se aplicará a todo)
            UIManager.put("defaultFont", new javax.swing.plaf.FontUIResource("SansSerif", Font.PLAIN, 14));

            VistaPrincipal vista = new VistaPrincipal();
            ModeloPrincipal modelo = new ModeloPrincipal();

            ControladorPrincipal controlador = new ControladorPrincipal(vista, modelo);
            FlatLightLaf.setup();
            controlador.iniciar();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

<<<<<<< HEAD

=======
<<<<<<< HEAD

=======
<<<<<<< HEAD

=======
>>>>>>> dccc2d478868567f2e8534a056f53b99652a99e5
>>>>>>> 7a16f5706f96e4144603d41f14f23b77895fe7ac
>>>>>>> 33b4c3f9f3030c17f58f0d61a6916f1f283d1f3a
        VistaPrincipal vista = new VistaPrincipal();
        ModeloPrincipal modelo = new ModeloPrincipal();

        ControladorPrincipal controlador = new ControladorPrincipal(vista, modelo);
        FlatLightLaf.setup();
        controlador.iniciar();
        
        
        
        System.out.println("rene soy irvin");
        System.out.println("Irvin ");

<<<<<<< HEAD

=======
<<<<<<< HEAD
=======
<<<<<<< HEAD

=======
>>>>>>> dccc2d478868567f2e8534a056f53b99652a99e5
>>>>>>> 7a16f5706f96e4144603d41f14f23b77895fe7ac
>>>>>>> 33b4c3f9f3030c17f58f0d61a6916f1f283d1f3a
    }
}
