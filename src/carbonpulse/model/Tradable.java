package carbonpulse.model;

import carbonpulse.exception.InsufficientCarbonException;
import carbonpulse.exception.BankruptcyException;

public interface Tradable {
    // Semua objek yang bisa bertransaksi di bursa wajib memakai fungsi ini
    void beliKreditKarbon(int jumlah, double hargaTotal) throws BankruptcyException;
    void jualKreditKarbon(int jumlah, double hargaTotal) throws InsufficientCarbonException;
}