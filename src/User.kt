class User(username:String,password:String):Akun(username, password) {
    fun ambilBarang() {
        if (login()) {
            print("Masukkan nama barang : ")
            val nama_barang = readLine()!!
            try {
                val sql = "SELECT id_barang,stok FROM barang WHERE nama_barang='$nama_barang'"
                rs = stmt?.executeQuery(sql)
                var id_barang: Int = 0
                var stok:Int = 0
                while (rs!!.next()) {
                    id_barang = rs!!.getInt(1)
                    stok = rs!!.getInt(2)
                }
                print("Jumlah barang yang ingin diambil: ")
                val stok_baru = scanner.nextInt()
                if(stok_baru > stok){
                    println("Stok tidak mencukupi")
                } else if(stok_baru <= stok && stok_baru > 0) {
                    val updateSql = "UPDATE barang SET stok='${stok - stok_baru}' WHERE id_barang='$id_barang'"
                    log.tambahLog(id_akun,id_barang,"Mengambil barang dengan nama $nama_barang sebanyak $stok_baru, sisa stok ${stok - stok_baru}")
                    stmt?.executeUpdate(updateSql)
                    println("Stok barang berhasil diupdate")
                }else if(stok_baru <= 0){
                    println("Stok tidak boleh kurang dari 0")
                }
            } catch (e: Exception) {
                println(e.printStackTrace())
            }
        }
    }
    fun lihatBarang() {
        if(login()) {
            try {
                val sql = "SELECT b.nama_barang, b.stok, j.nama_jenis FROM barang b JOIN jenis j ON b.id_jenis = j.id_jenis WHERE j.ketersediaan = 1"
                rs = stmt?.executeQuery(sql)

                println("\n=== DAFTAR BARANG ===")
                println("Nama Barang\t\tStok\t\tJenis")
                println("----------------------------------------")

                while (rs!!.next()) {
                    val namaBarang = rs!!.getString("nama_barang")
                    val stok = rs!!.getInt("stok")
                    val jenisBarang = rs!!.getString("nama_jenis")
                    println("$namaBarang\t\t$stok\t\t$jenisBarang")
                }
                println("----------------------------------------")

            } catch (e: Exception) {
                println(e.printStackTrace())
            }
        }
    }

    fun lihatJenis() {
        try {
            val sql = "SELECT nama_jenis FROM jenis WHERE ketersediaan = 1"
            rs = stmt?.executeQuery(sql)

            println("\n=== DAFTAR JENIS BARANG ===")
            println("Nama Jenis")
            println("--------------------")

            while (rs!!.next()) {
                val namaJenis = rs!!.getString("nama_jenis")
                println(namaJenis)
            }
            println("--------------------")

        } catch (e: Exception) {
            println(e.printStackTrace())
        }
    }


    fun cariBarangByNama() {
        try {
            print("Masukkan nama barang: ")
            val searchName = readLine()!!

            val sql = "SELECT b.nama_barang, b.stok, j.nama_jenis FROM barang b JOIN jenis j ON b.id_jenis = j.id_jenis WHERE b.nama_barang LIKE '%$searchName%' AND j.ketersediaan = 1"
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
                println("$namaBarang\t\t$stok\t\t\t$jenisBarang")
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