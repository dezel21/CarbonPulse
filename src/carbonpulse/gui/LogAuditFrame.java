package carbonpulse.gui;

import carbonpulse.model.BursaEngine;
import carbonpulse.model.LogTransaksi;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class LogAuditFrame extends JFrame {

    public LogAuditFrame() {
        setTitle("CarbonPulse - Log Audit Transaksi Global");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Judul Kolom Tabel JTable
        String[] namaKolom = {"Waktu", "Pembeli", "Penjual", "Volume (Ton)", "Total Harga ($)"};
        DefaultTableModel tableModel = new DefaultTableModel(namaKolom, 0);

        // Menarik data langsung dari ArrayList di BursaEngine
        ArrayList<LogTransaksi> riwayat = BursaEngine.getRiwayatTransaksi();

        // Memindahkan data dari ArrayList ke baris JTable
        for (LogTransaksi log : riwayat) {
            Object[] barisData = {
                    log.getWaktuTransaksi(),
                    log.getNamaPembeli(),
                    log.getNamaPenjual(),
                    log.getJumlahKredit(),
                    log.getTotalHarga()
            };
            tableModel.addRow(barisData);
        }

        JTable tabelAudit = new JTable(tableModel);
        tabelAudit.setEnabled(false); // Read-only

        JScrollPane scrollPane = new JScrollPane(tabelAudit);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Buku Besar Perdagangan Karbon (Memori Lokal)"));
        add(scrollPane, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel();
        JButton btnTutup = new JButton("Tutup Laporan");
        btnTutup.addActionListener(e -> dispose());
        actionPanel.add(btnTutup);
        add(actionPanel, BorderLayout.SOUTH);
    }
}