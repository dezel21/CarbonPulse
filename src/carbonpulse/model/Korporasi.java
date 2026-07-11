package carbonpulse.model;

import carbonpulse.exception.BankruptcyException;
import carbonpulse.exception.InsufficientCarbonException;

// INHERITANCE: Korporasi mewarisi kerangka User
public class Korporasi extends User {
    // ENCAPSULATION: Atribut khusus yang hanya dimiliki oleh perusahaan
    private double profitBersih;

    // Constructor
    public Korporasi(int idUser, String username, double saldoKeuangan, int sisaKuotaKarbon, double profitBersih) {
        // Memanggil constructor dari class induk (User)
        super(idUser, username, saldoKeuangan, sisaKuotaKarbon);
        this.profitBersih = profitBersih;
    }

    // POLYMORPHISM: Logika pajak yang BERBEDA dari DelegasiNegara
    // Di sini pajaknya dihitung 15% dari profit perusahaan
    @Override
    public double hitungPajakTahunan() {
        return this.profitBersih * 0.15;
    }

    // Mengimplementasikan interface Tradable (Beli)
    @Override
    public void beliKreditKarbon(int jumlah, double hargaTotal) throws BankruptcyException {
        if (getSaldoKeuangan() < hargaTotal) {
            throw new BankruptcyException("Budget Korporasi " + getUsername() + " tidak mencukupi untuk transaksi ini!");
        }
        setSaldoKeuangan(getSaldoKeuangan() - hargaTotal);
        setSisaKuotaKarbon(getSisaKuotaKarbon() + jumlah);
    }

    // Mengimplementasikan interface Tradable (Jual)
    @Override
    public void jualKreditKarbon(int jumlah, double hargaTotal) throws InsufficientCarbonException {
        if (getSisaKuotaKarbon() < jumlah) {
            throw new InsufficientCarbonException("Korporasi " + getUsername() + " tidak memiliki sisa kuota karbon untuk dijual!");
        }
        setSaldoKeuangan(getSaldoKeuangan() + hargaTotal);
        setSisaKuotaKarbon(getSisaKuotaKarbon() - jumlah);
    }

    // Getter dan Setter untuk profit
    public double getProfitBersih() { return profitBersih; }
    public void setProfitBersih(double profitBersih) { this.profitBersih = profitBersih; }
}