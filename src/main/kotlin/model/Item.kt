package model

abstract class Item(
    val id: String,
    var judul: String,
    var tahunTerbit: Int
) {
    var tersedia: Boolean = true
        protected set

    /** Setiap subclass WAJIB mengimplementasikan */
    abstract fun deskripsi(): String

    fun tandaiDipinjam() {
        require(tersedia) { "Item '$judul' sedang tidak tersedia." }
        tersedia = false
    }

    fun tandaiDikembalikan() {
        tersedia = true
    }

    override fun toString(): String = deskripsi()
}
