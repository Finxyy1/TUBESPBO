import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.*
import java.util.Scanner

val logActivity = log()
lateinit var akun: Akun
lateinit var admin:Admin
lateinit var user:User
var status:String = "logout"
var scanner = Scanner(System.`in`)


fun main(args: Array<String>) {
    val JDBC_DRIVER = "com.mysql.cj.jdbc.Driver"
    val DB_URL = "jdbc:mysql://localhost/tubespbo"
    val USER = "root"
    val PASS = ""


    var conn: Connection? = null
    var stmt: java.sql.Statement? = null
    var rs: ResultSet? = null

    Class.forName(JDBC_DRIVER)
    conn = DriverManager.getConnection(DB_URL, USER, PASS)
    stmt = conn.createStatement()

    do {
        println("=== SELAMAT DATANG ===")
        println("Silahkan login terlebih dahulu")
        println("=====================")
        println("1. Login")
        println("2. Exit")
        print("Masukkan pilihan anda = ")
        var pilihan = scanner.nextInt()
        when (pilihan) {
            1 -> {
                loginValidation()
            }

            2 -> {
                println("Terima kasih telah menggunakan program ini")
                System.exit(0)
            }

            else -> {
                println("Pilihan tidak valid")
            }
        }
    }while (status == "logout")
}

fun loginValidation(){
    println("=== LOGIN ===")
    print("Masukkan username = ")
    var userName = readLine()!!
    print("Masukkan password = ")
    var password = readLine()!!
    akun = Akun(userName, password)
    if (akun.login()) {
        println("Login berhasil!")
        println("Selamat datang, ${akun.username} !")
        status = "login"
        if (akun.role == "admin") {
            admin = Admin(userName, password)
            showAdminMenu(admin)
        } else {
            user = User(userName, password)
            showUserMenu(user)
        }
    }
}

fun showAdminMenu(admin: Admin) {
    while (true) {
        println("\n=== MENU ADMIN ===")
        println("1. Kelola Jenis Barang")
        println("2. Kelola Barang")
        println("3. Kelola Akun")
        println("4. Lihat Log Aktivitas")
        println("5. Logout")
        print("Pilih menu (1-5): ")

        when (readLine()) {
            "1" -> {
                println("\n=== KELOLA JENIS BARANG ===")
                println("1. Tambah Jenis")
                println("2. Hapus Jenis")
                println("3. Lihat Jenis")
                println("4. Kembali")
                print("Pilih menu (1-4): ")

                when (readLine()) {
                    "1" -> admin.tambahJenis()
                    "2" -> admin.delJenis()
                    "3" -> admin.lihatJenis()
                    "4" -> continue
                    else -> println("Menu tidak valid!")
                }
            }

            "2" -> {
                println("\n=== KELOLA BARANG ===")
                println("1. Tambah Barang")
                println("2. Hapus Barang")
                println("3. Update Barang")
                println("4. Lihat Semua Barang")
                println("5. Cari Barang berdasarkan Nama")
                println("6. Cari Barang berdasarkan Jenis")
                println("7. Kembali")
                print("Pilih menu (1-7): ")

                when (readLine()) {
                    "1" -> admin.tambahBarang()
                    "2" -> admin.delBarang()
                    "3" -> admin.upBarang()
                    "4" -> admin.lihatBarang()
                    "5" -> admin.cariBarangByNama()
                    "6" -> admin.cariBarangByJenis()
                    "7" -> continue
                    else -> println("Menu tidak valid!")
                }
            }

            "3" -> {
                println("\n=== KELOLA AKUN ===")
                println("1. Tambah Akun")
                println("2. Hapus Akun")
                println("3. Kembali")
                print("Pilih menu (1-3): ")

                when (readLine()) {
                    "1" -> admin.tambahAkun()
                    "2" -> admin.delAkun()
                    "3" -> continue
                    else -> println("Menu tidak valid!")
                }
            }

            "4" -> {
                val log = log()
                println("\n=== MENU LOG ===")
                println("1. Lihat Semua Log")
                println("2. Cari Log berdasarkan Username")
                println("3. Kembali")
                print("Pilih menu (1-3): ")

                when (readLine()) {
                    "1" -> log.tampilkanLog()
                    "2" -> {
                        print("Masukkan username: ")
                        val username = readLine()!!
                        log.cariLogByUsername(username)
                    }
                    "3" -> continue
                    else -> println("Menu tidak valid!")
                }
            }

            "5" -> {
                println("Logout berhasil")
                status = "logout"
                break
            }

            else -> println("Menu tidak valid!")
        }
    }
}

fun showUserMenu(user: User) {
    while (true) {
        println("\n=== MENU USER ===")
        println("1. Lihat Katalog")
        println("2. Riwayat")
        println("3. Cari Barang")
        println("4. Ambil Barang")
        println("5. Logout")
        print("Pilih menu (1-4): ")

        when (readLine()) {
            "1" -> {
                println("\n=== KATALOG BARANG ===")
                println("1. Lihat Semua Barang")
                println("2. Lihat Berdasarkan Jenis")
                println("3. Kembali")
                print("Pilih menu (1-3): ")

                when (readLine()) {
                    "1" -> user.lihatBarang()
                    "2" -> {
                        user.lihatJenis()
                        user.cariBarangByJenis()
                    }
                    "3" -> continue
                    else -> println("Menu tidak valid!")
                }
            }
            "2" -> {
                println("\n=== RIWAYAT===")
                logActivity.cariLogByID(user.id_akun)
            }

            "3" -> {
                println("\n=== CARI BARANG ===")
                println("1. Cari berdasarkan Nama")
                println("2. Cari berdasarkan Jenis")
                println("3. Kembali")
                print("Pilih menu (1-3): ")

                when (readLine()) {
                    "1" -> {
                        print("Masukkan nama barang: ")
                        val nama = readLine()!!
                        user.cariBarangByNama()
                    }
                    "2" -> {
                        print("Masukkan jenis barang: ")
                        val jenis = readLine()!!
                        user.cariBarangByJenis()
                    }
                    "3" -> continue
                    else -> println("Menu tidak valid!")
                }
            }
            "4" -> {
                println("\n=== AMBIL BARANG ===")
                user.ambilBarang()
            }

            "5" -> {
                println("Logout berhasil!")
                status = "logout"
                break
            }

            else -> println("Menu tidak valid! Silakan pilih 1-4")
        }
    }
}

