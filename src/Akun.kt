import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.util.Scanner
open class Akun(val username:String, val password:String) {

    val scanner = Scanner(System.`in`)
    var id_akun: Int = 0
    var role: String = " "
    var conn: Connection? = null
    var stmt: java.sql.Statement? = null
    var rs: ResultSet? = null
    var rsAkun: ResultSet? = null
    protected var log: log = log()

    init {
        val JDBC_DRIVER = "com.mysql.cj.jdbc.Driver"
        val DB_URL = "jdbc:mysql://localhost/tubespbo"
        val USER = "root"
        val PASS = ""

        Class.forName(JDBC_DRIVER)
        conn = DriverManager.getConnection(DB_URL, USER, PASS)
        stmt = conn?.createStatement()

        val sql = "SELECT * FROM akun WHERE username='$username' AND password='$password'"
        rsAkun = stmt?.executeQuery(sql)

        log = log()
    }

    fun login(): Boolean {
        if (rsAkun?.next() == null) {
            return false
        } else {
            try {
                id_akun = rsAkun!!.getInt("id_akun")
                role = rsAkun!!.getString("role")
                return true
            } catch (e: Exception) {
                println("Akun tidak ditemukan")
            }
        }
        return false
    }


    private fun passValidation(password: String): Boolean {
        if (password.length < 8) return false //Cek panjang password
        if (password.contains(" ")) return false //Cek ada spasi atau tidak
        if (!password.any { it.isDigit() }) return false //Cek ada angka atau tidak
        if (!password.any { it.isLowerCase() }) return false //Cek ada huruf kecil atau tidak
        if (!password.any { it.isUpperCase() }) return false //Cek ada huruf besar atau tidak

        return true //Jika semua kondisi terpenuhi
    }

    fun tambahAkun() {
    if (role == "admin") {
        println("Masukkan username dan password baru : ")
        print("Username : ")
        val username = readLine()!!
        print("Password : ")
        val password = readLine()!!
        if (passValidation(password)) {
            val sql =
                "INSERT INTO akun (username, password, role) VALUES ('$username', '$password', 'user')"
            stmt?.executeUpdate(sql)
            println("Akun berhasil ditambahkan")
        } else {
            println("Password tidak valid")
        }

    } else {
        println("Anda tidak memiliki akses untuk menambah akun")
        }
    }

    fun delAkun() {
        try {
            if (role == "admin") {
                try {
                    print("Masukkan username yang akan dihapus: ")
                    val username = readLine()!!

                    val checkSql = "SELECT COUNT(*) FROM akun WHERE username='$username'"
                    rs = stmt?.executeQuery(checkSql)
                    var exists = 0
                    while (rs!!.next()) {
                        exists = rs!!.getInt(1)
                    }
                    if (exists > 0) {
                        val sql = "DELETE FROM akun WHERE username='$username'"
                        stmt?.executeUpdate(sql)
                        println("Akun berhasil dihapus")
                    } else {
                        println("Username tidak ditemukan")
                    }
                } catch (e: Exception) {
                    println(e.printStackTrace())
                }
            } else {
                println("Anda tidak memiliki akses untuk menghapus akun")
            }
        } catch (e: Exception) {
            println(e.printStackTrace())
        }
    }
}
