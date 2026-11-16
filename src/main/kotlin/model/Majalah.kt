package model

class Majalah(
    id: String,
    judul: String,
    tahunTerbit: Int,
    val edisi: Int,
    val bulan: String
) : Item(id, judul, tahunTerbit) {
    override fun deskripsi(): String =
        "[MAJALAH] $judul Edisi $edisi ($bulan $tahunTerbit)"
}
