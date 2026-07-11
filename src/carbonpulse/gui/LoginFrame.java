package carbonpulse.gui;

import carbonpulse.db.DatabaseManager;
import carbonpulse.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private JTextField textUsername;
    private JPasswordField textPassword;
    private JButton btnLogin;

    public LoginFrame() {
        // Pengaturan dasar jendela aplikasi
        setTitle("CarbonPulse - Login Entitas");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Membuat jendela muncul di tengah layar layar
        setLayout(new GridLayout(3, 2, 10, 10));

        // Inisiasi komponen GUI
        add(new JLabel(" Username:"));
        textUsername = new JTextField();
        add(textUsername);

        add(new JLabel(" Password:"));
        textPassword = new JPasswordField();
        add(textPassword);

        add(new JLabel("")); // Spacer kosong
        btnLogin = new JButton("Masuk Bursa");
        add(btnLogin);

        // Menambahkan aksi ketika tombol ditekan
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = textUsername.getText();
                String password = new String(textPassword.getPassword());

                // Memanggil DatabaseManager untuk autentikasi
                User loggedInUser = DatabaseManager.loginUser(username, password);

                if (loggedInUser != null) {
                    JOptionPane.showMessageDialog(null, "Login Berhasil! Selamat datang, " + loggedInUser.getUsername());
                    dispose(); // Menutup jendela login

                    // Membuka jendela Dashboard dan mengirim data user yang login
                    new DashboardFrame(loggedInUser).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Login Gagal! Cek kembali username/password.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}