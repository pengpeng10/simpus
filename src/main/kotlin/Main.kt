import exception.PerpustakaanException
import model.Anggota
import model.Buku
import model.Dvd
import model.Majalah
import service.Perpustakaan

private val perpus = Perpustakaan()

fun main() {
    seedData()
    println("==================================================")
    println("     SELAMAT DATANG DI SIMPUS - Perpustakaan      ")
    println("==================================================")

    while (true) {
        tampilkanMenu()
        when (bacaInput("Pilih menu [0-7]: ")) {
            "1" -> tambahItemMenu()
            "2" -> tambahAnggotaMenu()
            "3" -> pinjamMenu()
            "4" -> kembalikanMenu()
            "5" -> cariMenu()
            "6" -> lihatSemuaItem()
            "7" -> lihatPeminjamanAktif()
            "0" -> {
                perpus.simpanKeFile()
                println("Terima kasih! Sampai jumpa 👋")
                return
            }
            else -> println("! Menu tidak dikenali, coba lagi.")
        }
        println()
    }
}

private fun tampilkanMenu() {
    println("""
        ----------------- MENU -----------------
        1. Tambah Item (Buku/Majalah/DVD)
        2. Tambah Anggota
        3. Pinjam Item
        4. Kembalikan Item
        5. Cari Item
        6. Lihat Semua Item
        7. Lihat Peminjaman Aktif
        0. Keluar & Simpan
    """.trimIndent())
}

private fun bacaInput(prompt: String): String {
    print(prompt)
    return readlnOrNull()?.trim().orEmpty()
}

/** Membaca angka; terus bertanya sampai pengguna mengetik angka yang benar. */
private fun bacaAngka(prompt: String): Int {
    while (true) {
        val angka = bacaInput(prompt).toIntOrNull()
        if (angka != null && angka >= 0) return angka
        println("! Harap masukkan angka yang valid (contoh: 2024).")
    }
}

private fun tambahItemMenu() {
    println("\nJenis item: 1) Buku  2) Majalah  3) DVD")
    val jenis = bacaInput("Pilih jenis: ")
    if (jenis !in listOf("1", "2", "3")) {
        println("! Jenis tidak valid. Pilih 1, 2, atau 3.")
        return
    }
    val id = bacaInput("ID                 : ")
    val judul = bacaInput("Judul              : ")
    val tahun = bacaAngka("Tahun              : ")

    val item = when (jenis) {
        "1" -> Buku(id, judul, tahun, bacaInput("Penulis            : "), bacaInput("ISBN               : "))
        "2" -> Majalah(id, judul, tahun, bacaAngka("Edisi              : "), bacaInput("Bulan              : "))
        else -> Dvd(id, judul, tahun, bacaAngka("Durasi (menit)     : "), bacaInput("Sutradara          : "))
    }

    jalankanAman {
        perpus.tambahItem(item)
        println("✓ Item berhasil ditambahkan: ${item.deskripsi()}")
    }
}

private fun tambahAnggotaMenu() {
    val a = Anggota(
        bacaInput("ID Anggota : "),
        bacaInput("Nama       : "),
        bacaInput("Email      : ")
    )
    jalankanAman {
        perpus.tambahAnggota(a)
        println("✓ Anggota '${a.nama}' terdaftar.")
    }
}

private fun pinjamMenu() {
    val itemId = bacaInput("ID Item    : ")
    val anggotaId = bacaInput("ID Anggota : ")
    jalankanAman {
        val p = perpus.pinjam(itemId, anggotaId)
        println("✓ Berhasil! ID Peminjaman: ${p.id}")
    }
}

private fun kembalikanMenu() {
    val id = bacaInput("ID Peminjaman: ")
    jalankanAman {
        perpus.kembalikan(id)
        println("✓ Item telah dikembalikan.")
    }
}

private fun cariMenu() {
    val kw = bacaInput("Kata kunci: ")
    val hasil = perpus.cariItem(kw)
    if (hasil.isEmpty()) println("Tidak ada hasil untuk '$kw'.")
    else hasil.forEachIndexed { i, item -> println("${i + 1}. ${item.deskripsi()}") }
}

private fun lihatSemuaItem() {
    val list = perpus.semuaItem()
    if (list.isEmpty()) { println("Belum ada item."); return }
    list.forEach { println("${if (it.tersedia) "🟢" else "🔴"} [${it.id}] ${it.deskripsi()}") }
}

private fun lihatPeminjamanAktif() {
    val list = perpus.peminjamanAktif()
    if (list.isEmpty()) { println("Tidak ada peminjaman aktif."); return }
    list.forEach { println("• [${it.id}] item=${it.itemId} anggota=${it.anggotaId} sejak ${it.tanggalPinjam}") }
}

private inline fun jalankanAman(block: () -> Unit) {
    try {
        block()
    } catch (e: PerpustakaanException) {
        println("! ${e.message}")
    } catch (e: Exception) {
        println("! Terjadi kesalahan: ${e.message}")
    }
}

private fun seedData() {
    perpus.tambahItem(Buku("B001", "Clean Code", 2008, "Robert C. Martin", "978-0132350884"))
    perpus.tambahItem(Buku("B002", "Kotlin in Action", 2017, "Dmitry Jemerov", "978-1617293290"))
    perpus.tambahItem(Majalah("M001", "National Geographic", 2023, 245, "Juli"))
    perpus.tambahItem(Dvd("D001", "Inception", 2010, 148, "Christopher Nolan"))
    perpus.tambahAnggota(Anggota("A001", "Andi", "andi@mail.com"))
    perpus.tambahAnggota(Anggota("A002", "Bunga", "bunga@mail.com"))
}
