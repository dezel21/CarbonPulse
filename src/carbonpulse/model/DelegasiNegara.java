package carbonpulse.model;

import carbonpulse.exception.BankruptcyException;
import carbonpulse.exception.InsufficientCarbonException;

// INHERITANCE: DelegasiNegara mewarisi User
public class DelegasiNegara extends User {
    private double gdp; // Atribut khusus negara

    public DelegasiNegara(int idUser, String username, double saldoKeuangan, int sisaKuotaKarbon, double gdp) {
        super(idUser, username, saldoKeuangan, sisaKuotaKarbon);
        this.gdp = gdp;
    }

    // POLYMORPHISM: Logika pajak khusus untuk entitas negara
    @Override
    public double hitungPajakTahunan() {
        return (gdp * 0.05) + (getSaldoKeuangan() * 0.01);
    }

    @Override
    public void beliKreditKarbon(int jumlah, double hargaTotal) throws BankruptcyException {
        if (getSaldoKeuangan() < hargaTotal) {
            throw new BankruptcyException("Kas Negara " + getUsername() + " tidak cukup untuk beli kredit!");
        }
        setSaldoKeuangan(getSaldoKeuangan() - hargaTotal);
        setSisaKuotaKarbon(getSisaKuotaKarbon() + jumlah);
    }

    @Override
    public void jualKreditKarbon(int jumlah, double hargaTotal) throws InsufficientCarbonException {
        if (getSisaKuotaKarbon() < jumlah) {
            throw new InsufficientCarbonException("Kuota Karbon " + getUsername() + " tidak mencukupi untuk dijual!");
        }
        setSaldoKeuangan(getSaldoKeuangan() + hargaTotal);
        setSisaKuotaKarbon(getSisaKuotaKarbon() - jumlah);
    }
}