package carbonpulse.model;

// Menggunakan antarmuka Comparable agar PriorityQueue tahu cara menyortir objek ini
public class TransactionOrder implements Comparable<TransactionOrder> {
    private User penjual;
    private int jumlahKredit;
    private double hargaPerKredit;

    public TransactionOrder(User penjual, int jumlahKredit, double hargaPerKredit) {
        this.penjual = penjual;
        this.jumlahKredit = jumlahKredit;
        this.hargaPerKredit = hargaPerKredit;
    }

    // Getter untuk mengambil data pesanan
    public User getPenjual() { return penjual; }
    public int getJumlahKredit() { return jumlahKredit; }
    public double getHargaPerKredit() { return hargaPerKredit; }

    // Logika Otomatis: Menyortir antrean bursa dari harga TERMURAH ke termahal
    @Override
    public int compareTo(TransactionOrder orderLain) {
        return Double.compare(this.hargaPerKredit, orderLain.hargaPerKredit);
    }

    @Override
    public String toString() {
        return penjual.getUsername() + " | Jual: " + jumlahKredit + " Ton | Harga: $" + hargaPerKredit + "/ton";
    }
}