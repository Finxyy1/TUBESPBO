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

    fun tambahLog(idAkun: Int, idBarang: Int, keterangan: String) {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val tanggal = currentDateTime.format(formatter)

        val sql = """
            INSERT INTO log (id_akun, id_barang, keterangan, tanggal) 
            VALUES ($idAkun, $idBarang, '$keterangan', '$tanggal')
        """.trimIndent()

        stmt?.executeUpdate(sql)
    }

    fun tampilkanLog() {
        val sql = "SELECT l.*, a.username, m.nama_barang FROM log l " +
                "JOIN akun a ON l.id_akun = a.id_akun " +
                "JOIN barang m ON l.id_barang = m.id_barang " +
                "ORDER BY l.tanggal DESC"

        rs = stmt?.executeQuery(sql)

        println("\n=== RIWAYAT AKTIVITAS ===")
        println("ID\tUsername\tBarang\t\tKeterangan\t\tTanggal")
        println("================================================================")

        while (rs?.next() == true) {
            val idLog = rs?.getInt("id_log")
            val username = rs?.getString("username")
            val barang = rs?.getString("nama_barang")
            val keterangan = rs?.getString("keterangan")
            val tanggal = rs?.getString("tanggal")

            println("$idLog\t$username\t$barang\t\t$keterangan\t\t$tanggal")
        }
    }

    fun cariLogByID(idAkun: Int) {
        val sql = "SELECT l.*, a.username, m.nama_barang FROM log l " +
                "JOIN akun a ON l.id_akun = a.id_akun " +
                "JOIN barang m ON l.id_barang = m.id_barang " +
                "WHERE a.id_akun = '$idAkun' " +
                "ORDER BY l.tanggal DESC"

        rs = stmt?.executeQuery(sql)

        println("\n=== RIWAYAT AKTIVITAS $idAkun ===")
        println("ID\tBarang\t\tKeterangan\t\tTanggal")
        println("================================================================")

        while (rs?.next() == true) {
            val idLog = rs?.getInt("id_log")
            val barang = rs?.getString("nama_barang")
            val keterangan = rs?.getString("keterangan")
            val tanggal = rs?.getString("tanggal")

            println("$idLog\t$barang\t\t$keterangan\t\t$tanggal")
        }
    }

    fun cariLogByUsername(username: String) {
        val sql = "SELECT l.*, a.username, m.nama_barang FROM log l " +
                "JOIN akun a ON l.id_akun = a.id_akun " +
                "JOIN barang m ON l.id_barang = m.id_barang " +
                "WHERE a.username = '$username' " +
                "ORDER BY l.tanggal DESC"
        rs = stmt?.executeQuery(sql)
        println("\n=== RIWAYAT AKTIVITAS $username ===")
        println("ID\tBarang\t\tKeterangan\t\tTanggal")
        println("================================================================")
        while (rs?.next() == true) {
            val idLog = rs?.getInt("id_log")
            val barang = rs?.getString("nama_barang")
            val keterangan = rs?.getString("keterangan")
            val tanggal = rs?.getString("tanggal")
            println("$idLog\t$barang\t\t$keterangan\t\t$tanggal")
        }
    }
}
