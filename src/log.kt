import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class log() {
    var conn: Connection? = null
    var stmt: java.sql.Statement? = null
    var rs: ResultSet? = null
    init {
        val JDBC_DRIVER = "com.mysql.cj.jdbc.Driver"
        val DB_URL = "jdbc:mysql://localhost/tubespbo"
        val USER = "root"
        val PASS = ""

        Class.forName(JDBC_DRIVER)
        conn = DriverManager.getConnection(DB_URL, USER, PASS)
        stmt = conn?.createStatement()
    }

    fun tambahLog(idAkun: Int, idBarang: Int, keterangan: String, jumlah: Int = 0) {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val tanggal = currentDateTime.format(formatter)

        val sql = "INSERT INTO log (id_akun, id_barang, jumlah, keterangan, tanggal) VALUES ('$idAkun', '$idBarang', '$jumlah', '$keterangan', '$tanggal')"
        stmt?.execute(sql)
    }

    fun tampilkanLog() {
        val sql = "SELECT l.*, a.username, l.id_barang, b.nama_barang FROM log l " +
                "JOIN akun a ON l.id_akun = a.id_akun JOIN barang b ON l.id_barang = b.id_barang " +
                "ORDER BY l.tanggal DESC"

        rs = stmt?.executeQuery(sql)

        println("\n=== RIWAYAT AKTIVITAS ===")
        println("Tanggal\t\t\t\t\tUsername\t\tBarang\t\t\t\t\tKeterangan")
        println("===========================================================================================================================")

        while (rs?.next() == true) {
            val username = rs?.getString("username")
            val barang = rs?.getString("nama_barang")
            val keterangan = rs?.getString("keterangan")
            val tanggal = rs?.getString("tanggal")

            println("$tanggal\t\t$username\t\t\t$barang\t\t\t\t\t$keterangan")
        }
    }

    fun cariLogByID(idAkun: Int) {
        val sql = "SELECT l.*, a.username, l.id_barang, b.nama_barang FROM log l " +
                "JOIN akun a ON l.id_akun = a.id_akun JOIN barang b ON l.id_barang = b.id_barang " +
                "WHERE a.id_akun = '$idAkun' " +
                "ORDER BY l.tanggal DESC"

        rs = stmt?.executeQuery(sql)

        println("\n=== RIWAYAT AKTIVITAS $idAkun ===")
        println("Tanggal\t\t\t\t\tBarang\t\t\tKeterangan")
        println("=============================================================================================================")

        while (rs?.next() == true) {
            val barang = rs?.getString("nama_barang")
            val keterangan = rs?.getString("keterangan")
            val tanggal = rs?.getString("tanggal")

            println("$tanggal\t\t$barang\t\t\t$keterangan")
        }
    }

    fun cariLogByUsername(username: String) {
        val sql = "SELECT l.*, a.username, l.id_barang, b.nama_barang FROM log l " +
                "JOIN akun a ON l.id_akun = a.id_akun JOIN barang b ON l.id_barang = b.id_barang " +
                "WHERE a.username = '$username' " +
                "ORDER BY l.tanggal DESC"
        rs = stmt?.executeQuery(sql)
        println("\n=== RIWAYAT AKTIVITAS $username ===")
        println("Tanggal\t\t\t\t\t\tBarang\t\t\t\tKeterangan")
        println("==============================================================================================================================")
        while (rs?.next() == true) {
            val barang = rs?.getString("nama_barang")
            val keterangan = rs?.getString("keterangan")
            val tanggal = rs?.getString("tanggal")
            println("$tanggal\t\t\t$barang\t\t\t\t$keterangan")
        }
    }

    fun barangPopuler(){
        val sql = "SELECT l.id_barang, b.nama_barang, COUNT(l.id_barang) AS jumlah_transaksi FROM log l JOIN barang b ON l.id_barang=b.id_barang  WHERE keterangan LIKE 'Mengambil%' GROUP BY l.id_barang ORDER BY jumlah_transaksi DESC LIMIT 1"
        rs = stmt?.executeQuery(sql)
        println("\n=== BARANG TERSIBUK ===")
        println("Nama Barang\t\tJumlah Transaksi")
        println("=========================================")
        while (rs?.next() == true) {
            val namaBarang = rs?.getString("nama_barang")
            val jumlahTransaksi = rs?.getInt("jumlah_transaksi")
            println("$namaBarang\t\t\t$jumlahTransaksi")
        }
    }

    fun barangTerlaku() {
        val sql = "SELECT l.id_barang, b.nama_barang, SUM(l.jumlah) AS total_barang\n" +
                "FROM log l JOIN barang b ON l.id_barang = b.id_barang\n" +
                "WHERE l.keterangan LIKE 'Mengambil%'\n" +
                "GROUP BY l.id_barang, b.nama_barang\n" +
                "HAVING total_barang = (SELECT MAX(total)FROM (SELECT SUM(jumlah) AS total FROM log WHERE keterangan LIKE 'Mengambil%' GROUP BY id_barang) AS subquery)"
        rs = stmt?.executeQuery(sql)
        println("\n=== BARANG TERLARIS ===")
        println("Nama Barang\t\tTotal Barang")
        println("=========================================")
        while (rs?.next() == true) {
            val namaBarang = rs?.getString("nama_barang")
            val jumlahTransaksi = rs?.getInt("total_barang")
            println("$namaBarang\t\t$jumlahTransaksi")
        }
    }

    fun userTeraktif() {
        val sql = "SELECT l.id_akun, a.username, COUNT(l.id_akun) AS aktivitas, SUM(jumlah) AS total FROM log AS l JOIN akun AS a ON l.id_akun = a.id_akun WHERE keterangan LIKE 'Mengambil%' GROUP BY id_akun\n" +
                "HAVING total = (SELECT MAX(total_brg) FROM (SELECT SUM(jumlah) AS total_brg FROM log WHERE keterangan LIKE 'Mengambil%' GROUP BY id_akun) AS sub)  ORDER BY aktivitas DESC"
        rs = stmt?.executeQuery(sql)
        println("\n=== USER TERAKTIF ===")
        println("Username\t\tAktivitas\t\tTotal Barang")
        println("=========================================")
        while (rs?.next() == true) {
            val username = rs?.getString("username")
            val jumlahTransaksi = rs?.getInt("aktivitas")
            val total = rs?.getInt("total")
            println("$username\t\t\t$jumlahTransaksi\t\t\t$total")
        }
    }
}
