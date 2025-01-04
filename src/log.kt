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

    fun tambahLog(idAkun: Int, namaBarang: String, keterangan: String) {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val tanggal = currentDateTime.format(formatter)

        val sql = "INSERT INTO log (id_akun, nama_barang, keterangan, tanggal) VALUES ('$idAkun', '$namaBarang', '$keterangan', '$tanggal')"
        stmt?.execute(sql)
    }

    fun tampilkanLog() {
        val sql = "SELECT l.*, a.username, nama_barang FROM log l " +
                "JOIN akun a ON l.id_akun = a.id_akun " +
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
        val sql = "SELECT l.*, a.username, nama_barang FROM log l " +
                "JOIN akun a ON l.id_akun = a.id_akun " +
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
        val sql = "SELECT l.*, a.username, nama_barang FROM log l " +
                "JOIN akun a ON l.id_akun = a.id_akun " +
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
}
