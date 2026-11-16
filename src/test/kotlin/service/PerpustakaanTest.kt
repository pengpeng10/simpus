package service

import exception.*
import model.Anggota
import model.Buku
import kotlin.test.*

class PerpustakaanTest {

    private lateinit var perpus: Perpustakaan

    @BeforeTest
    fun siapkanData() {
        perpus = Perpustakaan()
        perpus.tambahItem(Buku("B001", "Clean Code", 2008, "Robert C. Martin", "978-0132350884"))
        perpus.tambahAnggota(Anggota("A001", "Andi", "andi@mail.com"))
    }

    @Test
    fun `menambah item dengan ID yang sama harus gagal`() {
        assertFailsWith<DuplikasiIdException> {
            perpus.tambahItem(Buku("B001", "Buku Lain", 2020, "Seseorang", "123"))
        }
    }

    @Test
    fun `meminjam item membuat item tidak tersedia`() {
        val p = perpus.pinjam("B001", "A001")
        assertEquals("B001", p.itemId)
        assertFalse(perpus.semuaItem().first().tersedia)
        assertEquals(1, perpus.peminjamanAktif().size)
    }

    @Test
    fun `meminjam item yang sedang dipinjam harus gagal`() {
        perpus.pinjam("B001", "A001")
        assertFailsWith<ItemTidakTersediaException> {
            perpus.pinjam("B001", "A001")
        }
    }

    @Test
    fun `meminjam oleh anggota yang tidak terdaftar harus gagal`() {
        assertFailsWith<AnggotaTidakDitemukanException> {
            perpus.pinjam("B001", "A999")
        }
    }

    @Test
    fun `meminjam item yang tidak ada harus gagal`() {
        assertFailsWith<ItemTidakDitemukanException> {
            perpus.pinjam("X999", "A001")
        }
    }

    @Test
    fun `mengembalikan item membuatnya tersedia lagi`() {
        val p = perpus.pinjam("B001", "A001")
        perpus.kembalikan(p.id)
        assertTrue(perpus.semuaItem().first().tersedia)
        assertTrue(perpus.peminjamanAktif().isEmpty())
    }

    @Test
    fun `pencarian tidak membedakan huruf besar dan kecil`() {
        assertEquals(1, perpus.cariItem("clean").size)
        assertEquals(0, perpus.cariItem("python").size)
    }

    @Test
    fun `email tanpa tanda at harus ditolak`() {
        assertFailsWith<InputTidakValidException> {
            perpus.tambahAnggota(Anggota("A002", "Bunga", "bunga-mail.com"))
        }
    }
}
