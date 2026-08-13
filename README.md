<div align="center">

#  Currency Converter

**Aplikasi desktop Java bertema gelap yang elegan untuk konversi mata uang secara real-time.**

[![Java](https://img.shields.io/badge/Java-8%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.java.com)
[![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)](https://maven.apache.org)
[![SQLite](https://img.shields.io/badge/SQLite-07405E?style=for-the-badge&logo=sqlite&logoColor=white)](https://www.sqlite.org)
[![API](https://img.shields.io/badge/Frankfurter_API-Gratis-00B4D8?style=for-the-badge&logo=europeanunion&logoColor=white)](https://www.frankfurter.app)
[![License](https://img.shields.io/badge/Lisensi-MIT-green?style=for-the-badge)](LICENSE)

</div>

---

##  Tampilan (Preview)

> *Mode gelap, kartu membulat, ikon vektor — semuanya dibuat dari nol menggunakan Java Swing.*

```text
+--------------------------------------------------+
|             Currency Converter                   |
+--------------------------------------------------+
|  Amount     |  100.000                           |
|  From       |  IDR                  [ Swap ]     |
|  To         |  USD                               |
|                                                  |
|  [ Convert ]              $6.12     [ Copy ]     |
+--------------------------------------------------+
|  Conversion History              [Clear History] |
|  IDR -> USD | Rp 100.000 | $6.12  | 2026-08-03   |
+--------------------------------------------------+
```

*(Tips: Kamu bisa mengganti teks preview ini dengan screenshot asli! Cukup simpan gambar dengan nama `preview.png` di folder ini dan ubah kode di atas menjadi `![Preview](preview.png)`)*

---

##  Fitur Unggulan

| Fitur | Deskripsi |
|---|---|
|  **Kurs Real-Time** | Nilai tukar mata uang langsung dari Bank Sentral Eropa via Frankfurter API — **tanpa API key** |
|  **UI Gelap Kustom** | Desain bertema gelap yang elegan, dibangun sepenuhnya dengan Java 2D Graphics (tanpa library UI eksternal) |
|  **30 Mata Uang Utama** | Mendukung berbagai mata uang global lengkap dengan bendera negaranya |
|  **Riwayat Offline** | Semua konversi otomatis disimpan di database lokal SQLite |
|  **Format Angka Pintar** | Mengenali format penulisan angka Indonesia (`10.000,50`) maupun format US (`10,000.50`) |
|  **Tukar Cepat (Swap)** | Tukar posisi mata uang "Dari" dan "Ke" secara instan dengan satu klik |
|  **Salin ke Papan Klip** | Salin hasil konversi dengan mudah, dilengkapi animasi notifikasi "Copied!" |
|  **Enter untuk Konversi** | Tidak perlu repot klik tombol — cukup tekan `Enter` untuk melakukan konversi |
|  **Hapus Riwayat** | Bersihkan riwayat konversimu dengan dialog konfirmasi khusus bertema gelap |
|  **Animasi Loading** | Indikator `Converting...` yang bergerak saat mengambil data dari internet |

---

##  Teknologi yang Digunakan

- **Bahasa**: Java (Swing / AWT)
- **Build Tool**: Apache Maven
- **Database**: SQLite (via `xerial/sqlite-jdbc`)
- **API**: [Frankfurter API](https://www.frankfurter.app/) — Gratis, tidak butuh API key
- **Desain**: Render vektor kustom menggunakan Java 2D (tanpa JavaFX, tanpa framework UI eksternal)

---

##  Cara Menjalankan

### Persyaratan Sistem
- Java JDK **8 atau lebih baru**
- Apache **Maven** terinstal

### Langkah-langkah

```bash
# 1. Clone repository ini
git clone https://github.com/Benaya-Bagas-Christianto/java-currency-converter.git

# 2. Masuk ke direktori project
cd java-currency-converter

# 3. Compile dan jalankan
mvn clean compile exec:java -Dexec.mainClass="com.mycompany.currencyconverter.Main"
```

>  Selesai! Tidak perlu setting API key atau konfigurasi tambahan. Tinggal clone dan jalankan.

---

##  Mata Uang yang Didukung

`AUD` `BRL` `CAD` `CHF` `CNY` `CZK` `DKK` `EUR` `GBP` `HKD`
`HUF` `IDR` `ILS` `INR` `ISK` `JPY` `KRW` `MXN` `MYR` `NOK`
`NZD` `PHP` `PLN` `RON` `SEK` `SGD` `THB` `TRY` `USD` `ZAR`

---

##  Struktur Project

```text
CurrencyConverter/
├── src/
│   └── main/java/com/mycompany/currencyconverter/
│       ├── Main.java                  # Titik masuk utama aplikasi
│       ├── api/
│       │   └── ExchangeRateAPI.java   # Integrasi dengan Frankfurter API
│       ├── db/
│       │   └── DatabaseHelper.java    # Operasi CRUD SQLite
│       └── gui/
│           └── MainFrame.java         # UI utama (semua komponen & logika)
├── pom.xml                            # Dependensi Maven
└── .gitignore
```

---

##  Lisensi

Project ini bersifat open-source di bawah [Lisensi MIT](LICENSE).  
Bebas digunakan, dimodifikasi, dan didistribusikan.  Jangan lupa berikan bintang (star) jika kamu menyukai project ini!

---

<p align="center">
  Dibuat dan dirancang oleh <b><a href="https://github.com/Benaya-Bagas-Christianto">Benaya Bagas Christianto</a></b><br>
  <i>Berdedikasi untuk menciptakan aplikasi desktop interaktif berbasis Java yang fungsional dan efisien.</i>
</p>
