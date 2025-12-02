package sistemapedidos;

import ui.LoginSistemaPedidos;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Look and feel simplificado - sin errores
        
        try {
            // Usar Nimbus si está disponible
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
            // Si falla, no hacer nada - usar look and feel por defecto
            System.out.println("No se pudo cargar Nimbus look and feel");
        }
        
        // Inicializar base de datos
        DatabaseConfig.inicializarBaseDatos();
        
        // Mostrar login
        SwingUtilities.invokeLater(() -> {
            new LoginSistemaPedidos().setVisible(true);
        });
    }
}