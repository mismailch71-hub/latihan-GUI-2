# Smart Wallet, Savings & Priority Planner

Aplikasi desktop berbasis **Java Swing** untuk membantu pengelolaan anggaran bulanan, pencatatan pengeluaran berdasarkan skala prioritas, pemantauan target tabungan dengan *progress bar*, serta penyimpanan catatan evaluasi finansial.

---

##  Fitur Utama

1. **Pengelola Keuangan & Prioritas (`PanelKeuangan`)**
   - Mencatat pengeluaran harian berdasarkan 3 kategori prioritas: `[Tinggi] Primer`, `[Sedang] Sekunder`, dan `[Rendah] Tersier`.
   - Kalkulasi sisa anggaran (*budget*) otomatis secara *real-time* dari anggaran awal sebesar **Rp 1.000.000**.
   - Indikator peringatan visual otomatis (warna teks sisa budget berubah menjadi merah apabila saldo tersisa di bawah Rp 200.000).
   - Fitur hapus transaksi yang otomatis mengembalikan nominal ke sisa budget.
   - Klik ganda (*double-click*) pada item daftar transaksi untuk menampilkan detail rincian dalam kotak dialog.

2. **Target & Tabungan (`PanelTabungan`)**
   - Mencatat pemasukan tabungan beserta sumber dana/keterangannya secara terpisah dari budget pengeluaran.
   - Visualisasi persentase pencapaian target tabungan menggunakan `JProgressBar`.
   - Fitur ubah target tabungan secara dinamis dan notifikasi otomatis saat target tabungan tercapai.
   - Fitur hapus riwayat setoran yang otomatis menyesuaikan ulang total tabungan terkumpul.

3. **Catatan Finansial (`PanelCatatan`)**
   - Area teks multibaris (`JTextArea`) dengan fitur *auto-wrap* dan *scrollbar* untuk menulis evaluasi keuangan bulanan.
   - Tombol **Reset Catatan Default** untuk mengembalikan template catatan standar.

4. **Status Bar & Konfirmasi Keluar (`AppUtama`)**
   - Status bar interaktif di bagian bawah jendela yang memberikan umpan balik langsung saat pengguna mengetik atau melakukan transaksi.
   - Dialog konfirmasi saat pengguna menekan tombol tutup jendela (`X`).

---

##  Arsitektur Komponen GUI (Java Swing)

Struktur kode aplikasi ini dibangun dengan menerapkan klasifikasi komponen GUI Java Swing berikut:

| Kategori Arsitektur | Kelas / Komponen yang Digunakan |
| :--- | :--- |
| **Top-level Container** | `JFrame` (`AppUtama`), `JOptionPane` |
| **Intermediate Container** | `JPanel` (`PanelKeuangan`, `PanelTabungan`, `PanelCatatan`), `JTabbedPane`, `JScrollPane` |
| **Atomic Component** | `JLabel`, `JTextField`, `JTextArea`, `JComboBox`, `JButton`, `JList`, `JProgressBar` |
| **Layout Managers** | `BorderLayout`, `GridLayout`, `FlowLayout` |
| **Event Listeners & Adapters** | `ActionListener`, `KeyListener`, `WindowAdapter`, `MouseAdapter` |

---

