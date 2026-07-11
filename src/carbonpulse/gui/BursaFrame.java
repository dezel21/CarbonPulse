package carbonpulse.gui;

import carbonpulse.model.BursaEngine;
import carbonpulse.model.TransactionOrder;
import carbonpulse.model.User;

import javax.swing.*;
import java.awt.*;
import java.util.PriorityQueue;

public class BursaFrame extends JFrame {
    private User currentUser;
    private DashboardFrame mainDashboard;
    private DefaultListModel<String> listModel;
    private JList<String> listAntrean;

    public BursaFrame(User user, DashboardFrame dashboard) {
        this.currentUser = user;
        this.mainDashboard = dashboard;

        // Memuat antrean bursa ter-update dari database
        BursaEngine.inisiasiPasar();

        setTitle("CarbonPulse - Live Order Book");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Header Atas
        JLabel lblJudul = new JLabel(" Bursa Karbon Global", SwingConstants.CENTER);
        lblJudul.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblJudul, BorderLayout.NORTH);

        // Bagian Tengah: Menampilkan Daftar Antrean Priority Queue
        listModel = new DefaultListModel<>();
        listAntrean = new JList<>(listModel);
        listAntrean.setFont(new Font("Monospaced", Font.PLAIN, 14));
        refreshAntrean(); // Memanggil fungsi untuk menarik data pertama kali

        JScrollPane scrollPane = new JScrollPane(listAntrean);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Antrean Penjualan (Harga Termurah di Atas)"));
        add(scrollPane, BorderLayout.CENTER);

        // Panel Bawah: Kumpulan Tombol Transaksi
        JPanel actionPanel = new JPanel();
        JButton btnJualKredit = new JButton("Jual Kuota Saya");
        JButton btnBeliKredit = new JButton("Beli Kuota (Top Antrean)");
        JButton btnTutup = new JButton("Tutup Bursa");

        actionPanel.add(btnJualKredit);
        actionPanel.add(btnBeliKredit);
        actionPanel.add(btnTutup);
        add(actionPanel, BorderLayout.SOUTH);

        // --- AKSI TOMBOL ---

        // 1. Tombol Tutup Jendela
        btnTutup.addActionListener(e -> dispose());

        // 2. Tombol Jual Kredit (Memasukkan Pesanan Baru ke MySQL)
        btnJualKredit.addActionListener(e -> {
            String hargaInput = JOptionPane.showInputDialog(this, "Masukkan harga jual per ton ($):");
            if (hargaInput != null && !hargaInput.isEmpty()) {
                try {
                    double harga = Double.parseDouble(hargaInput);
                    // Simulasi melepas pesanan jual sebanyak 10 Ton ke bursa
                    TransactionOrder orderBaru = new TransactionOrder(currentUser, 10, harga);
                    BursaEngine.tambahOrderJual(orderBaru);

                    // Segarkan layar bursa seketika
                    refreshAntrean();
                    JOptionPane.showMessageDialog(this, "Pesanan jual berhasil masuk bursa!");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Format harga harus berupa angka!", "Error Input", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // 3. Tombol Beli Kredit (Eksekusi Transaksi Antar Akun secara Real-Time)
        btnBeliKredit.addActionListener(e -> {
            try {
                // Jalankan transaksi di level objek Java
                BursaEngine.prosesTransaksi(currentUser);

                // Paksa simpan perubahan saldo/kuota pembeli saat ini ke MySQL laptop
                carbonpulse.db.DatabaseManager.updateStatusEntitas(currentUser);

                // Segarkan tampilan bursa dan dasbor utama di belakangnya
                refreshAntrean();
                mainDashboard.refreshDataUI();

                JOptionPane.showMessageDialog(this, "Transaksi Berhasil! Saldo dan Kuota Anda telah diperbarui.", "Sukses Transaksi", JOptionPane.INFORMATION_MESSAGE);

            } catch (carbonpulse.exception.BankruptcyException ex) {
                JOptionPane.showMessageDialog(this, "[BANKRUPT] " + ex.getMessage(), "Gagal Membeli", JOptionPane.ERROR_MESSAGE);
            } catch (carbonpulse.exception.InsufficientCarbonException ex) {
                JOptionPane.showMessageDialog(this, "[STOCK KOSONG] " + ex.getMessage(), "Gagal Membeli", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Peringatan", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    // Fungsi khusus untuk menarik antrean dari PriorityQueue dan menyusunnya rapi di GUI
    private void refreshAntrean() {
        listModel.clear();
        PriorityQueue<TransactionOrder> antrean = BursaEngine.getAntreanPasar();

        // Trik menyalin antrean agar urutannya tidak rusak saat dibaca berulang di layar Swing
        PriorityQueue<TransactionOrder> copyAntrean = new PriorityQueue<>(antrean);
        while (!copyAntrean.isEmpty()) {
            listModel.addElement(copyAntrean.poll().toString());
        }
    }
}