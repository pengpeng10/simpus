package service

import exception.*
import model.*
import java.io.File
import java.time.LocalDate

class Perpustakaan : Pencarian {

    private val items = mutableMapOf<String, Item>()
    private val anggotas = mutableMapOf<String, Anggota>()
    private val peminjamans = mutableListOf<Peminjaman>()

    // -------- ITEM --------
    fun tambahItem(item: Item) {
        if (item.id.isBlank()) throw InputTidakValidException("ID item tidak boleh kosong.")
        if (item.judul.isBlank()) throw InputTidakValidException("Judul tidak boleh kosong.")
        if (items.containsKey(item.id)) throw DuplikasiIdException(item.id)
        items[item.id] = item
    }

    override fun cariItem(keyword: String): List<Item> =
        items.values.filter {
            it.judul.contains(keyword, ignoreCase = true) ||
            it.deskripsi().contains(keyword, ignoreCase = true)
        }

    fun semuaItem(): List<Item> = items.values.toList()

    // -------- ANGGOTA --------
    fun tambahAnggota(a: Anggota) {
        if (a.id.isBlank() || a.nama.isBlank())
            throw InputTidakValidException("ID dan nama anggota wajib diisi.")
        if (!a.email.contains("@"))
            throw InputTidakValidException("Format email tidak valid: '${a.email}'.")
        if (anggotas.containsKey(a.id)) throw DuplikasiIdException(a.id)
        anggotas[a.id] = a
    }

    // -------- TRANSAKSI --------
    fun pinjam(itemId: String, anggotaId: String): Peminjaman {
        val item = items[itemId] ?: throw ItemTidakDitemukanException(itemId)
        if (!anggotas.containsKey(anggotaId)) throw AnggotaTidakDitemukanException(anggotaId)
     //   if (!item.tersedia) throw ItemTidakTersediaException(item.judul)

        item.tandaiDipinjam()
        val p = Peminjaman(itemId = itemId, anggotaId = anggotaId)
        peminjamans.add(p)
        return p
    }

    fun kembalikan(peminjamanId: String) {
        val p = peminjamans.firstOrNull { it.id == peminjamanId && it.sedangDipinjam }
            ?: throw PeminjamanTidakDitemukanException(peminjamanId)
        p.tanggalKembali = LocalDate.now()
        items[p.itemId]?.tandaiDikembalikan()
    }

    fun peminjamanAktif(): List<Peminjaman> = peminjamans.filter { it.sedangDipinjam }

    // -------- PERSISTENCE SEDERHANA --------
    fun simpanKeFile(path: String = "data/perpustakaan.csv") {
        val file = File(path)
        file.parentFile?.mkdirs()
        file.writeText(buildString {
            appendLine("# ITEMS")
            items.values.forEach { i ->
                appendLine("ITEM|${i::class.simpleName}|${i.id}|${i.judul}|${i.tahunTerbit}|${i.tersedia}")
            }
            appendLine("# ANGGOTA")
            anggotas.values.forEach { a ->
                appendLine("ANGGOTA|${a.id}|${a.nama}|${a.email}")
            }
            appendLine("# PEMINJAMAN")
            peminjamans.forEach { p ->
                appendLine("PINJAM|${p.id}|${p.itemId}|${p.anggotaId}|${p.tanggalPinjam}|${p.tanggalKembali ?: "-"}")
            }
        })
        println("✓ Data tersimpan di ${file.absolutePath}")
    }
}
