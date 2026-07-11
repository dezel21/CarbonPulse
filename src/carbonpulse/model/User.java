package carbonpulse.model;

public abstract class User implements Tradable {
    // ENCAPSULATION: Modifier private melindungi data krusial
    private int idUser;
    private String username;
    private double saldoKeuangan;
    private int sisaKuotaKarbon;
    private int totalEmisi;

    // Constructor
    public User(int idUser, String username, double saldoKeuangan, int sisaKuotaKarbon) {
        this.idUser = idUser;
        this.username = username;
        this.saldoKeuangan = saldoKeuangan;
        this.sisaKuotaKarbon = sisaKuotaKarbon;
        this.totalEmisi = 0;
    }

    // Getter & Setter sebagai satu-satunya pintu akses data
    public double getSaldoKeuangan() { return saldoKeuangan; }
    public void setSaldoKeuangan(double saldoKeuangan) { this.saldoKeuangan = saldoKeuangan; }

    public int getSisaKuotaKarbon() { return sisaKuotaKarbon; }
    public void setSisaKuotaKarbon(int sisaKuotaKarbon) { this.sisaKuotaKarbon = sisaKuotaKarbon; }

    public String getUsername() { return username; }
    public int getIdUser() { return idUser; }
    // ABSTRACTION: Method ini wajib di-override oleh class turunannya
    public abstract double hitungPajakTahunan();

    // Fitur produksi yang menghasilkan profit tapi memakan kuota karbon
    public void produksiBarang(int tingkatEmisi, double potensiKeuntungan) throws carbonpulse.exception.InsufficientCarbonException {
        if (this.sisaKuotaKarbon < tingkatEmisi) {
            // Melempar Custom Exception jika kuota tidak cukup
            throw new carbonpulse.exception.InsufficientCarbonException("KRITIS! Kuota karbon Anda (" + this.sisaKuotaKarbon + " Ton) tidak cukup untuk skala produksi ini. Segera beli kredit di Bursa!");
        }

        // Jika aman, kuota dipotong, emisi bertambah, dan mendapat uang hasil produksi
        this.totalEmisi += tingkatEmisi;
        this.sisaKuotaKarbon -= tingkatEmisi;
        this.saldoKeuangan += potensiKeuntungan;
    }
}