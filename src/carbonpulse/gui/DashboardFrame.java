package carbonpulse.gui;

import carbonpulse.exception.InsufficientCarbonException;
import carbonpulse.model.User;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {
    private User currentUser;

    // Deklarasi label di luar constructor agar angkanya bisa di-update
    private JLabel lblSaldo;
    private JLabel lblKuota;
    private JLabel lblPajak;

    public DashboardFrame(User user) {
        this.currentUser = user;

        setTitle("CarbonPulse Dashboard - " + currentUser.getUsername());
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- PANEL HEADER ---
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(34, 139, 34));
        JLabel lblWelcome = new JLabel("Status Entitas: " + currentUser.getClass().getSimpleName());
        lblWelcome.setForeground(Color.WHITE);
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(lblWelcome);
        add(headerPanel, BorderLayout.NORTH);

        // --- PANEL TENGAH (Status) ---
        JPanel statusPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        statusPanel.setBorder(BorderFactory.createTitledBorder("Ringkasan Keuangan & Emisi"));

        lblSaldo = new JLabel();
        lblKuota = new JLabel();
        lblPajak = new JLabel();

        statusPanel.add(lblSaldo);
        statusPanel.add(lblKuota);
        statusPanel.add(lblPajak);

        refreshDataUI(); // Memanggil fungsi untuk mengisi angka pertama kali
        add(statusPanel, BorderLayout.CENTER);

        // --- PANEL BAWAH (Tombol Aksi) ---
        JPanel actionPanel = new JPanel();
        JButton btnProduksi = new JButton("Jalankan Produksi Industri");
        JButton btnBursa = new JButton("Buka Bursa");
        JButton btnLogout = new JButton("Logout");
        JButton btnAudit = new JButton("Lihat Log Audit");
        actionPanel.add(btnAudit);

        actionPanel.add(btnProduksi);
        actionPanel.add(btnBursa);
        actionPanel.add(btnLogout);
        add(actionPanel, BorderLayout.SOUTH);

        // --- AKSI TOMBOL ---

        // 1. Aksi Tombol Produksi (Gameplay Loop)
        btnProduksi.addActionListener(e -> {
            try {
                // Simulasi: Memproduksi barang menghasilkan $500 tapi memakan 50 Ton Karbon
                currentUser.produksiBarang(50, 500.0);

                refreshDataUI(); // Update angka di layar
                JOptionPane.showMessageDialog(this, "Produksi Sukses! Keuntungan +$500 masuk ke kas. Kuota karbon berkurang 50 Ton.", "Laporan Industri", JOptionPane.INFORMATION_MESSAGE);

            } catch (InsufficientCarbonException ex) {
                // Eror akan muncul jika sisa karbon < 50
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Pelanggaran Emisi", JOptionPane.ERROR_MESSAGE);
            }
        });

        // 2. Aksi Tombol Bursa
        btnBursa.addActionListener(e -> {
            // Melempar data user dan objek dasbor ini sendiri (this) ke dalam bursa
            new BursaFrame(currentUser, this).setVisible(true);
        });

        // 3. Aksi Tombol Logout
        btnLogout.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        // Aksi Tombol Log Audit
        btnAudit.addActionListener(e -> {
            new LogAuditFrame().setVisible(true);
        });
    }

    // Fungsi khusus untuk memperbarui teks label secara real-time
    // Fungsi untuk memaksa dasbor menarik data paling fresh dari database MySQL
    // Fungsi refresh yang aman tanpa mengganggu koneksi data bursa
    public void refreshDataUI() {
        if (currentUser != null) {
            // Langsung update teks di layar GUI berdasarkan kondisi objek saat ini
            lblSaldo.setText(" Saldo Keuangan: $ " + currentUser.getSaldoKeuangan());
            lblKuota.setText(" Sisa Kuota Karbon: " + currentUser.getSisaKuotaKarbon() + " Ton");
            lblPajak.setText(" Estimasi Pajak / Denda: $ " + currentUser.hitungPajakTahunan());
        }
    }
}