import carbonpulse.gui.LoginFrame;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Menjalankan GUI di thread yang aman (best practice Java Swing)
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Membuka jendela login pertama kali
                new LoginFrame().setVisible(true);
            }
        });
    }
}