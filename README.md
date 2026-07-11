<div align="center">

# 🌿 CarbonPulse
### *Next-Generation Carbon Trading Desktop Simulator*

[![Java Version](https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=java&logoColor=white)](https://www.oracle.com/java/)
[![MySQL](https://img.shields.io/badge/MySQL-3307-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Architecture](https://img.shields.io/badge/Architecture-MVC-green?style=for-the-badge)](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93controller)
[![Status](https://img.shields.io/badge/UAS-Project%20SI-blue?style=for-the-badge)](https://github.com/)

<p align="center">
  <b>Proyek Akhir Pemrograman Berorientasi Objek (PBO)</b> <br />
  Simulasi Bursa Perdagangan Emisi Karbon Global berbasis Desktop (Java Swing + MySQL). <br />
</p>

---
`S1 Sistem Informasi` • `UPN "Veteran" Jakarta` • `Semester 4 (2026)`
---

</div>

## 📌 Kenapa Kita Bikin CarbonPulse?

Jadi, projek ini kita bikin buat nyelesain problem perdagangan karbon dunia lewat simulasi terintegrasi. Di sini kita bagi perannya jadi dua biar kelihatan bisnis logikanya:
*   **Delegasi Negara**: Bertindak sebagai regulator makro. Kerjanya mantau batasan emisi sama narik estimasi pajak tahunan otomatis berdasarkan nilai GDP entitas.
*   **Korporasi**: Motor penggerak ekonomi. Bisa nge-run industri buat dapet profit keuangan, tapi kuota karbonnya bakal kemakan sama emisi pabrik.

Nah, kalau kuota karbon si Korporasi ini abis akibat kebanyakan produksi, mereka wajib belanja kuota di bursa global. Di sinilah **Priority Queue** berperan buat nyariin harga jual kuota termurah dari entitas lain yang surplus!

---

## 👥 Tim Pengembang (Beban Kerja Kelompok)



| Role / Fokus                                                                      | Job Desk Utama                                                                                 |
|:----------------------------------------------------------------------------------|:-----------------------------------------------------------------------------------------------|
| 👑 **Project Manager & Data Structure & Engine Lead** (Dzulhas Syahara Muthahari) | Ngoding core engine bursa, nge-sort antrean *Priority Queue*, sama ngurus logika *Comparable*.         |
| 🛡️ **DB Lead** (Al-Ghifari Rahbani Ramadhan)                                     | Ngurusin skema MySQL XAMPP (Port 3307), *Multi-Instance Run*, sama *logic* auth login.         |
| ⚙️ **OOP Architecture Lead** (Muhammad Fawwaz Ferdian Satriadi)                   | Bikin struktur blueprint kelas induk, anak, inheritance, sama rumus polimorfisme pajak. |
| 🎨 **Front-End & Exception Lead** (Bagas Malik Ibrahim)                           | Desain UI Java Swing (Dashboard, Bursa, Audit), sama nge-handle *Custom Exception Handling*.   |

---

## 🛠️ Pembuktian Syarat Akademis (Kriteria Nilai A)

Biar nilai UAS kelompok kita aman, semua materi dari dosen udah kita implementasiin penuh di level *source code*:

### 🧩 1. Konsep Dasar PBO (OOP Principles)
*   **Encapsulation**: Properti krusial kayak saldo duit dan sisa kuota karbon kita set `private`. Akses mutasi data wajib lewat *Getter* dan *Setter* biar gak bisa di-bypass sembarangan.
*   **Inheritance**: Kelas `DelegasiNegara` sama `Korporasi` itu *subclass* konkrit turunan langsung dari *abstract class* `User`.
*   **Abstraction & Polymorphism**: Method `hitungPajakTahunan()` kita taruh di kelas induk sebagai *abstract method*, terus di-*override* beda rumus di kelas anak. Dasbor bakal otomatis ngenalin rumus mana yang dipake tergantung akun siapa yang lagi login!

### 📊 2. Java Collections Framework
*   `PriorityQueue` + `Comparable`: Dipake di papan bursa. Tiap ada user klik "Jual", datanya masuk antrean memori Java dan otomatis disortir biar harga paling murah selalu nangkring di *Top Antrean* (pucuk bursa).
*   `ArrayList`: Kita pake sebagai *Ledger* atau buku besar lokal buat nampung *history* transaksi sukses sebelum di-dump ke tabel UI.

### 🛑 3. Robust Exception Handling (Anti-Crash)
Projek ini anti-crash karena udah dilengkapi penanganan eror yang ketat lewat *Custom Exception*:
*   `InsufficientCarbonException`: Ke-trigger otomatis kalau korporasi maksain produksi padahal kuota karbonnya udah zong.
*   `BankruptcyException`: Transaksi bursa otomatis diblokir sama Java kalau pembeli kedeteksi bokek (saldo kas kurang buat bayar total belanjaan bursa).

---

## 🗄️ Relasi Skema Database (MySQL)

Biar sinkron antar-jendela aplikasi pas di-run barengan, data kita lempar ke database `db_carbonpulse` dengan skema relasi tabel berikut:

| Tabel Asal | Hubungan | Tabel Tujuan | Keterangan |
| :--- | :---: | :--- | :--- |
| **users** | 1 : 1 | **entitas_status** | Menyimpan data Saldo Finansial & Sisa Kuota Karbon |
| **users** | 1 : N | **riwayat_bursa** | Mencatat antrean orderan dengan status PENDING / SUCCESSED |

---

## 🚀 Cara Run Projeknya di Laptop Kalian

### 🧱 Prasyarat Awal
*   Udah instal JDK 17 atau yang terbaru.
*   Aplikasi XAMPP Control Panel udah jalan (Port MySQL harus diganti ke **3307** ya, biar gak bentrok!).
*   *Library* `mysql-connector-j-9.7.0.jar` udah di-add ke *Dependencies Module* IntelliJ masing-masing.

### ⚡ Langkah Eksekusi
1. *Clone* repositori tim kita ini lewat git bash atau terminal:  
   `git clone https://github.com/dezel21/CarbonPulse.git`
2. Buka folder projeknya via **IntelliJ IDEA**.
3. Import file database yang udah disiapin di grup WA ke phpMyAdmin kalian.
4. **PENTING:** Masuk ke *Run Configurations* `Main.java` di IntelliJ, klik *Modify Options*, terus centang **"Allow multiple instances"**. Ini wajib biar bisa nge-run aplikasi 2 kali secara barengan buat nyobain simulasi transaksi antar-akun secara *real-time*.
5. Klik tombol hijau **Run** dan selamat nyobain hasil begadang kita!

---
<div align="center">
  <sub>CarbonPulse © 2026 • Projek UAS PBO Kelompok S1 Sistem Informasi UPNVJ.</sub>
</div>