package carbonpulse.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogTransaksi {
    private String namaPembeli;
    private String namaPenjual;
    private int jumlahKredit;
    private double totalHarga;
    private String waktuTransaksi;

    public LogTransaksi(String namaPembeli, String namaPenjual, int jumlahKredit, double totalHarga) {
        this.namaPembeli = namaPembeli;
        this.namaPenjual = namaPenjual;
        this.jumlahKredit = jumlahKredit;
        this.totalHarga = totalHarga;

        // Mengambil waktu saat transaksi terjadi secara otomatis
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        this.waktuTransaksi = LocalDateTime.now().format(dtf);
    }

    // Getter untuk menarik data ke dalam tabel GUI nanti
    public String getNamaPembeli() { return namaPembeli; }
    public String getNamaPenjual() { return namaPenjual; }
    public int getJumlahKredit() { return jumlahKredit; }
    public double getTotalHarga() { return totalHarga; }
    public String getWaktuTransaksi() { return waktuTransaksi; }
}