# SIMPUS - Sistem Informasi Manajemen Perpustakaan

## Deskripsi
Aplikasi Sistem Informasi Manajemen Perpustakaan berbasis CLI yang dibangun menggunakan Kotlin dengan menerapkan prinsip Pemrograman Berorientasi Objek (OOP): Abstraction, Encapsulation, Inheritance, dan Polymorphism.

## Fitur
- [x] Menambahkan koleksi item perpustakaan (Buku, Majalah, DVD)
- [x] Menambahkan data anggota perpustakaan
- [x] Peminjaman item oleh anggota
- [x] Pengembalian item yang sedang dipinjam
- [x] Pencarian item berdasarkan kata kunci (judul atau deskripsi)
- [x] Melihat daftar seluruh koleksi item beserta status ketersediaannya
- [x] Melihat daftar peminjaman aktif
- [x] Penyimpanan otomatis data ke file CSV (`data/perpustakaan.csv`)

## Arsitektur
Arsitektur berlapis (*layered architecture*):
- **Model**: `Item` (*abstract class*), `Buku`, `Majalah`, `Dvd`, `Anggota` (*data class*), `Peminjaman` (*data class*)
- **Service**: `Pencarian` (*interface*), `Perpustakaan` (*business logic*)
- **Exception**: `PerpustakaanException` (*sealed class*) dan turunan *custom exception*
- **UI (CLI)**: `Main.kt` (antarmuka konsol)

```mermaid
classDiagram
    class Item {
        <<abstract>>
        +String id
        +String judul
        +Int tahunTerbit
        +Boolean tersedia
        +deskripsi()* String
        +tandaiDipinjam()
        +tandaiDikembalikan()
    }
    class Buku {
        +String penulis
        +String isbn
        +deskripsi() String
    }
    class Majalah {
        +Int edisi
        +String bulan
        +deskripsi() String
    }
    class Dvd {
        +Int durasiMenit
        +String sutradara
        +deskripsi() String
    }
    class Anggota {
        <<data>>
        +String id
        +String nama
        +String email
    }
    class Peminjaman {
        <<data>>
        +String id
        +String itemId
        +String anggotaId
        +LocalDate tanggalPinjam
        +LocalDate tanggalKembali
        +Boolean sedangDipinjam
    }
    class Pencarian {
        <<interface>>
        +cariItem(keyword: String)* List~Item~
    }
    class Perpustakaan {
        -Map~String, Item~ items
        -Map~String, Anggota~ anggotas
        -List~Peminjaman~ peminjamans
        +tambahItem(item: Item)
        +tambahAnggota(a: Anggota)
        +pinjam(itemId: String, anggotaId: String) Peminjaman
        +kembalikan(peminjamanId: String)
        +cariItem(keyword: String) List~Item~
        +semuaItem() List~Item~
        +peminjamanAktif() List~Peminjaman~
        +simpanKeFile(path: String)
    }

    Item <|-- Buku
    Item <|-- Majalah
    Item <|-- Dvd
    Pencarian <|.. Perpustakaan
    Perpustakaan *-- Item
    Perpustakaan *-- Anggota
    Perpustakaan *-- Peminjaman
```

## Cara Menjalankan

### Lokal
```bash
./gradlew run --console=plain
# atau di Windows PowerShell:
.\gradlew.bat run --console=plain
```

### Docker
```bash
docker compose build
docker compose run --rm simpus
```

### Menjalankan Unit Test
```bash
./gradlew test
# atau di Windows PowerShell:
.\gradlew.bat test
```

## Teknologi
- Kotlin
- Gradle
- JUnit Platform / Kotlin Test
- Docker
