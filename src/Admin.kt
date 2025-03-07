class Admin(username:String,password:String):Akun(username, password) {
    // halo
    fun tambahJenis() {
        try {
            print("Masukkan nama jenis : ")
            val nama_jenis = readLine()!!

            if (nama_jenis.isEmpty()) {
                println("Data tidak boleh kosong")
                return
            }

            val sql = "SELECT COUNT(id_jenis),ketersediaan FROM jenis WHERE nama_jenis='$nama_jenis'"
            rs = stmt?.executeQuery(sql)
            var jmlh: Int = 0
            var ketersediaan: Int = 0
            while (rs!!.next()) {
                jmlh = rs!!.getInt(1)
                ketersediaan = rs!!.getInt(2)
            }
            if (jmlh == 0) {
                val sql = "INSERT INTO jenis (nama_jenis) VALUES ('$nama_jenis')"
                stmt?.executeUpdate(sql)
                println("Jenis ditambahkan")
            } else if (jmlh == 1 && ketersediaan == 0) {
                val sql = "UPDATE jenis SET ketersediaan=1 WHERE nama_jenis='$nama_jenis'"
                println("Jenis dipulihkan")
                stmt?.executeUpdate(sql)
            } else {
                println("Jenis sudah ada")
            }
        } catch (e: Exception) {
            println(e.printStackTrace())
        }
    }


    fun delJenis() {
        try {
            this.lihatJenis()
            print("Masukkan nama jenis : ")
            val nama_jenis = readLine()!!

            if (nama_jenis.isEmpty()) {
                println("Data tidak boleh kosong")
                return
            }

            val sql = "SELECT COUNT(id_jenis),ketersediaan FROM jenis WHERE nama_jenis='$nama_jenis'"
            rs = stmt?.executeQuery(sql)
            var jmlh: Int = 0
            var ketersediaan: Int = 0
            while (rs!!.next()) {
                jmlh = rs!!.getInt(1)
                ketersediaan = rs!!.getInt(2)
            }
            if (jmlh != 0 && ketersediaan == 1) {
                val sql = "UPDATE jenis SET ketersediaan = '0' WHERE nama_jenis='$nama_jenis'"
                stmt?.executeUpdate(sql)
                println("Jenis berhasil dihapus")
            } else {
                println("Jenis tidak ada")
            }
        } catch (e: Exception) {
            println(e.printStackTrace())
        }
    }

    fun tambahBarang() {
        println("Masukkan nama barang, stok, dan jenis barang : ")
        print("Nama barang : ")
        val nama_barang = readLine()!!
        print("Stok barang : ")
        val stok_barang = readLine()!!

        this.lihatJenis()
        print("Jenis barang : ")
        val jenis = readLine()!!

        if (nama_barang.isEmpty() || stok_barang.isEmpty() || jenis.isEmpty()) {
            println("Data tidak boleh kosong")
            return
        }
        try {
            val sql = "SELECT id_jenis, ketersediaan FROM jenis WHERE nama_jenis='$jenis' AND ketersediaan='1'"

            rs = stmt?.executeQuery(sql)
            var id_jenis: Int = 0
            while (rs!!.next()) {
                id_jenis = rs!!.getInt(1)
            }

            val sql2 = "SELECT id_barang, ketersediaan FROM barang WHERE nama_barang='$nama_barang'"
            rs = stmt?.executeQuery(sql2)
            var id_barang = 0
            var ketersediaan = 0

            while (rs!!.next()) {
                id_barang = rs!!.getInt(1)
                ketersediaan = rs!!.getInt(2)
            }

            if (id_jenis == 0) {
                println("Jenis tidak ditemukan")
                return
            }

            if (id_barang == 0) {
                val sql =
                    "INSERT INTO barang (nama_barang, id_jenis, stok) VALUES ('$nama_barang','$id_jenis','$stok_barang')"
                stmt?.executeUpdate(sql)

                val id = "SELECT id_barang FROM barang WHERE nama_barang='$nama_barang'"
                rs = stmt?.executeQuery(id)
                while (rs!!.next()) {
                    id_barang = rs!!.getInt(1)
                }

                log.tambahLog(id_akun, id_barang, "Tambah barang dengan nama $nama_barang dengan stok $stok_barang", stok_barang.toInt())
                println("Berhasil menambahkan barang ${nama_barang} dengan stok awal ${stok_barang}")
            } else if (id_barang != 0 && ketersediaan == 0) {
                val sql =
                    "UPDATE barang SET stok = stok + '$stok_barang', ketersediaan = '1' WHERE id_barang='$id_barang'"
                stmt?.executeUpdate(sql)
                log.tambahLog(id_akun, id_barang, "Tambah barang dengan nama $nama_barang dengan stok $stok_barang", stok_barang.toInt())
                println("Berhasil memulihkan barang ${nama_barang} dengan stok awal ${stok_barang}")
            } else {
                println("Barang sudah ada")
            }
        } catch (e: Exception) {
            println(e.printStackTrace())
        }
    }

    fun delBarang() {
        this.lihatBarang()

        print("Masukkan nama barang : ")
        val nama_barang = readLine()!!

        if (nama_barang.isEmpty()) {
            println("Data tidak boleh kosong")
            return
        }

        try {
            val sql = "SELECT id_barang FROM barang WHERE nama_barang='$nama_barang' AND ketersediaan = 1"
            rs = stmt?.executeQuery(sql)
            var id_barang: Int = 0
            while (rs!!.next()) {
                id_barang = rs!!.getInt(1)
            }
            if (id_barang != 0) {
                val sql = "UPDATE barang SET ketersediaan = '0' WHERE id_barang='$id_barang'"
                stmt?.executeUpdate(sql)
                log.tambahLog(id_akun, id_barang,"Hapus barang dengan nama $nama_barang")
                println("Barang berhasil dihapus")
            } else {
                println("Barang tidak ditemukan")
            }
        } catch (e: Exception) {
            println(e.printStackTrace())
        }
    }

    fun upBarang() {
        this.lihatBarang()

        print("Masukkan nama barang : ")
        val nama_barang = readLine()!!

        if (nama_barang.isEmpty()) {
            println("Data tidak boleh kosong")
            return
        }

        try {
            val sql = "SELECT id_barang,stok FROM barang WHERE nama_barang='$nama_barang' AND ketersediaan = 1"
            rs = stmt?.executeQuery(sql)
            var id_barang: Int = 0
            var stok: Int = 0
            while (rs!!.next()) {
                id_barang = rs!!.getInt(1)
                stok = rs!!.getInt(2)
            }
            if (id_barang != 0) {
                println("Pilih menu update:")
                println("1. Update nama barang")
                println("2. Update stok barang")
                print("Masukkan pilihan (1/2): ")

                when (scanner.nextInt()) {
                    1 -> {
                        print("Masukkan nama baru: ")
                        val nama_baru = readLine()!!

                        if (nama_baru.isEmpty()) {
                            println("Data tidak boleh kosong")
                            return
                        }

                        val updateSql = "UPDATE barang SET nama_barang='$nama_baru' WHERE id_barang='$id_barang'"
                        stmt?.executeUpdate(updateSql)
                        log.tambahLog(id_akun, id_barang,"Update barang dengan nama $nama_barang menjadi $nama_baru")

                        println("Nama barang berhasil diupdate")
                    }

                    2 -> {
                        print("Masukkan stok baru masuk: ")
                        val stok_baru = readLine()!!

                        if (stok_baru.isEmpty()) {
                            println("Data tidak boleh kosong")
                            return
                        }

                        if (stok_baru.toInt() <= 0) {
                            println("Stok tidak boleh kurang dari/sama dengan 0")
                            return
                        }

                        val updateSql = "UPDATE barang SET stok = stok + '$stok_baru' WHERE id_barang='$id_barang'"
                        stmt?.executeUpdate(updateSql)
                        log.tambahLog(super.id_akun, id_barang,"Update stok barang dengan nama $nama_barang dari $stok menjadi ${stok + stok_baru.toInt()}", stok_baru.toInt())

                        println("Stok barang berhasil ditambahkan")
                    }

                    else -> println("Pilihan tidak valid")
                }
            } else {
                println("Barang tidak ditemukan")
            }
        } catch (e: Exception) {
           println(e.printStackTrace())
        }
    }

    fun lihatBarang() {
        try {
            val sql = "SELECT b.nama_barang, b.stok, j.nama_jenis FROM barang b JOIN jenis j ON b.id_jenis = j.id_jenis WHERE j.ketersediaan = 1 AND b.ketersediaan = 1"
            rs = stmt?.executeQuery(sql)

            println()
            println("\n=== DAFTAR BARANG ===")
            println("Nama Barang\t\tStok\t\tJenis")
            println("----------------------------------------")

            while (rs!!.next()) {
                val namaBarang = rs!!.getString("nama_barang")
                val stok = rs!!.getInt("stok")
                val jenisBarang = rs!!.getString("nama_jenis")
                println("$namaBarang\t\t\t$stok\t\t\t$jenisBarang")
            }
            println("----------------------------------------")
            println()

        } catch (e: Exception) {
            println(e.printStackTrace())
        }
    }

    fun lihatJenis() {
        try {
            val sql = "SELECT nama_jenis FROM jenis WHERE ketersediaan = 1"
            rs = stmt?.executeQuery(sql)
            println()
            println("\n=== DAFTAR JENIS BARANG ===")
            println("Nama Jenis")
            println("--------------------")

            while (rs!!.next()) {
                val namaJenis = rs!!.getString("nama_jenis")
                println(namaJenis)
            }
            println("--------------------")
            println()

        } catch (e: Exception) {
            println(e.printStackTrace())
        }
    }

    fun cariBarangByNama() {
        try {
            print("Masukkan nama barang: ")
            val searchName = readLine()!!

            if (searchName.isEmpty()) {
                println("Data tidak boleh kosong")
                return
            }

            val sql = "SELECT b.nama_barang, b.stok, j.nama_jenis FROM barang b JOIN jenis j ON b.id_jenis = j.id_jenis WHERE b.nama_barang LIKE '%$searchName%' AND j.ketersediaan = 1 AND b.ketersediaan = 1"
            rs = stmt?.executeQuery(sql)

            println("\n=== HASIL PENCARIAN BARANG ===")
            println("Nama Barang\t\tStok\t\tJenis")
            println("----------------------------------------")

            var found = false
            while (rs!!.next()) {
                found = true
                val namaBarang = rs!!.getString("nama_barang")
                val stok = rs!!.getInt("stok")
                val jenisBarang = rs!!.getString("nama_jenis")
                println("$namaBarang\t\t$stok\t\t$jenisBarang")
            }

            if (!found) {
                println("Barang tidak ditemukan")
            }
            println("----------------------------------------")

        } catch (e: Exception) {
            println(e.printStackTrace())
        }
    }

    fun cariBarangByJenis() {
        try {
            print("Masukkan jenis barang: ")
            val searchJenis = readLine()!!

            if (searchJenis.isEmpty()) {
                println("Data tidak boleh kosong")
                return
            }

            val sql = "SELECT b.nama_barang, b.stok, j.nama_jenis FROM barang b JOIN jenis j ON b.id_jenis = j.id_jenis WHERE j.nama_jenis LIKE '%$searchJenis%' AND j.ketersediaan = 1"
            rs = stmt?.executeQuery(sql)

            println("\n=== HASIL PENCARIAN BERDASARKAN JENIS ===")
            println("Nama Barang\t\tStok\t\tJenis")
            println("----------------------------------------")

            var found = false
            while (rs!!.next()) {
                found = true
                val namaBarang = rs!!.getString("nama_barang")
                val stok = rs!!.getInt("stok")
                val jenisBarang = rs!!.getString("nama_jenis")
                println("$namaBarang\t\t$stok\t\t$jenisBarang")
            }

            if (!found) {
                println("Tidak ada barang dengan jenis tersebut")
            }
            println("----------------------------------------")

        } catch (e: Exception) {
            println(e.printStackTrace())
        }
    }
}
