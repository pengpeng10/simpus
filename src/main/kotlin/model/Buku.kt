package model

class Buku(
    id: String,
    judul: String,
    tahunTerbit: Int,
    val penulis: String,
    val isbn: String
) : Item(id, judul, tahunTerbit) {
    override fun deskripsi(): String =
        "[BUKU] $judul ($tahunTerbit) - $penulis | ISBN: $isbn"
}
