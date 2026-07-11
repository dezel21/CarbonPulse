package carbonpulse.model;

import java.util.PriorityQueue;

public class BursaEngine {
    // COLLECTION MANDATORY: PriorityQueue untuk menyortir antrean pasar secara real-time
    private static PriorityQueue<TransactionOrder> antreanPasar = new PriorityQueue<>();

    // COLLECTION MANDATORY: Menyimpan riwayat transaksi yang sudah sukses
    private static java.util.ArrayList<LogTransaksi> riwayatTransaksi = new java.util.ArrayList<>();

    // Fungsi untuk menarik data riwayat
    public static java.util.ArrayList<LogTransaksi> getRiwayatTransaksi() {
        return riwayatTransaksi;
    }

    // Fungsi untuk melempar pesanan jual ke dalam bursa
    public static void tambahOrderJual(TransactionOrder order) {
        // Simpan langsung ke database MySQL agar permanen
        carbonpulse.db.DatabaseManager.simpanOrderJual(order.getPenjual().getIdUser(), order.getJumlahKredit(), order.getHargaPerKredit());
        // Tambahkan juga ke memori lokal saat ini
        antreanPasar.add(order);
    }

    // Fungsi untuk mengambil seluruh data antrean untuk ditampilkan di tabel GUI
    public static PriorityQueue<TransactionOrder> getAntreanPasar() {
        // Setiap kali GUI meminta data pasar, kita paksa Java mengambil data paling fresh dari MySQL!
        carbonpulse.db.DatabaseManager.memuatAntreanPasar(antreanPasar);
        return antreanPasar;
    }

    // Fungsi dummy awal untuk mengisi bursa agar tidak kosong saat presentasi
    public static void inisiasiPasar() {}

    // Fungsi untuk memproses pembelian dari antrean teratas
    public static void prosesTransaksi(User pembeli) throws Exception {
        if (antreanPasar.isEmpty()) {
            throw new Exception("Bursa sedang kosong, tidak ada kredit yang dijual.");
        }

        // 1. Ambil data order termurah di pucuk antrean
        TransactionOrder orderTermurah = antreanPasar.peek();

        // 2. Validasi agar tidak membeli kuota sendiri
        if (orderTermurah.getPenjual().getUsername().equalsIgnoreCase(pembeli.getUsername())) {
            throw new Exception("Anda tidak bisa membeli kuota milik Anda sendiri!");
        }

        double totalHarga = orderTermurah.getJumlahKredit() * orderTermurah.getHargaPerKredit();

        // 3. Proses mutasi saldo & kuota di level objek Java
        pembeli.beliKreditKarbon(orderTermurah.getJumlahKredit(), totalHarga);
        User penjual = orderTermurah.getPenjual();
        penjual.jualKreditKarbon(orderTermurah.getJumlahKredit(), totalHarga);

        // === KODE YANG KRAUSIAL UNTUK LOG AUDIT ===
        // 4. Catat riwayat ke dalam ArrayList SEBELUM antrean di-poll
        LogTransaksi logBaru = new LogTransaksi(
                pembeli.getUsername(),
                penjual.getUsername(),
                orderTermurah.getJumlahKredit(),
                totalHarga
        );
        riwayatTransaksi.add(logBaru);
        // ==========================================

        // 5. Update data ke MySQL via DatabaseManager milik Tim 1
        carbonpulse.db.DatabaseManager.updateStatusEntitas(pembeli);
        carbonpulse.db.DatabaseManager.updateStatusEntitas(penjual);
        carbonpulse.db.DatabaseManager.ubahStatusOrderLunas(penjual.getIdUser(), orderTermurah.getJumlahKredit(), orderTermurah.getHargaPerKredit());

        // 6. Keluarkan order dari antrean memori lokal
        antreanPasar.poll();
    }
}