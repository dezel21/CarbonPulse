package carbonpulse.db;

import carbonpulse.model.User;
import carbonpulse.model.DelegasiNegara;
import carbonpulse.model.Korporasi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseManager {

    // 1. Fungsi Autentikasi Login
    public static User loginUser(String username, String password) {
        String query = "SELECT u.id_user, u.username, u.role, es.saldo_keuangan, es.sisa_kuota_karbon " +
                "FROM users u " +
                "JOIN entitas_status es ON u.id_user = es.id_user " +
                "WHERE u.username = ? AND u.password = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id_user");
                String user = rs.getString("username");
                String role = rs.getString("role");
                double saldo = rs.getDouble("saldo_keuangan");
                int kuota = rs.getInt("sisa_kuota_karbon");

                if (role.equalsIgnoreCase("DelegasiNegara")) {
                    return new DelegasiNegara(id, user, saldo, kuota, 50000.0);
                } else if (role.equalsIgnoreCase("Korporasi")) {
                    return new Korporasi(id, user, saldo, kuota, 25000.0);
                }
            }
        } catch (SQLException e) {
            System.out.println("[DATABASE ERROR] Gagal melakukan login: " + e.getMessage());
        }
        return null;
    }

    // 2. Fungsi Update Saldo & Kuota ke Database (HANYA SATU DEFINISI)
    public static void updateStatusEntitas(User user) {
        String query = "UPDATE entitas_status SET saldo_keuangan = ?, sisa_kuota_karbon = ? WHERE id_user = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDouble(1, user.getSaldoKeuangan());
            stmt.setInt(2, user.getSisaKuotaKarbon());
            stmt.setInt(3, user.getIdUser());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("[DB ERROR] Gagal update status: " + e.getMessage());
        }
    }

    // 3. Fungsi Mencatat Order Jual ke Database
    public static void simpanOrderJual(int idPenjual, int jumlah, double harga) {
        String query = "INSERT INTO riwayat_bursa (id_penjual, jumlah_kredit, harga_per_kredit, status) VALUES (?, ?, ?, 'PENDING')";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idPenjual);
            stmt.setInt(2, jumlah);
            stmt.setDouble(3, harga);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("[DB ERROR] Gagal pasang order: " + e.getMessage());
        }
    }

    // 4. Fungsi Mengubah Status Order Menjadi Sukses (Lunas)
    public static void ubahStatusOrderLunas(int idPenjual, int jumlah, double harga) {
        String query = "UPDATE riwayat_bursa SET status = 'SUCCESSED' " +
                "WHERE id_penjual = ? AND jumlah_kredit = ? AND harga_per_kredit = ? AND status = 'PENDING' LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idPenjual);
            stmt.setInt(2, jumlah);
            stmt.setDouble(3, harga);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("[DB ERROR] Gagal mengubah status transaksi: " + e.getMessage());
        }
    }

    // 5. Fungsi Memuat Data dari Database ke PriorityQueue Java
    public static void memuatAntreanPasar(java.util.PriorityQueue<carbonpulse.model.TransactionOrder> antrean) {
        antrean.clear();
        String query = "SELECT r.jumlah_kredit, r.harga_per_kredit, u.id_user, u.username, u.role, es.saldo_keuangan, es.sisa_kuota_karbon " +
                "FROM riwayat_bursa r " +
                "JOIN users u ON r.id_penjual = u.id_user " +
                "JOIN entitas_status es ON u.id_user = es.id_user " +
                "WHERE r.status = 'PENDING'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id_user");
                String user = rs.getString("username");
                String role = rs.getString("role");
                double saldo = rs.getDouble("saldo_keuangan");
                int kuota = rs.getInt("sisa_kuota_karbon");
                int jumlah = rs.getInt("jumlah_kredit");
                double harga = rs.getDouble("harga_per_kredit");

                User penjual;
                if (role.equalsIgnoreCase("DelegasiNegara")) {
                    penjual = new DelegasiNegara(id, user, saldo, kuota, 50000);
                } else {
                    penjual = new Korporasi(id, user, saldo, kuota, 25000);
                }
                antrean.add(new carbonpulse.model.TransactionOrder(penjual, jumlah, harga));
            }
        } catch (SQLException e) {
            System.out.println("[DB ERROR] Gagal memuat bursa: " + e.getMessage());
        }
    }
}