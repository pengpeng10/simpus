package model

class Dvd(
    id: String,
    judul: String,
    tahunTerbit: Int,
    val durasiMenit: Int,
    val sutradara: String
) : Item(id, judul, tahunTerbit) {
    override fun deskripsi(): String =
        "[DVD] $judul ($tahunTerbit) - $sutradara | $durasiMenit menit"
}
