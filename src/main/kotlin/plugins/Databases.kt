package plugins

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.github.cdimascio.dotenv.dotenv
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object DatabaseFactory {
    private lateinit var database: Database

    val dotenv = dotenv()

    fun init() {
        val hikariConfig = HikariConfig().apply {
            jdbcUrl = dotenv["DB_URL"]
            driverClassName = "org.postgresql.Driver"
            username = dotenv["DB_USER"]
            password = dotenv["DB_PASSWORD"]
            maximumPoolSize = 10
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"

            validate()
        }

        val dataSource = HikariDataSource(hikariConfig)
        database = Database.connect(dataSource)


        println("Database connected!")
    }


    /**
     * Ejecutar una query suspendida (async)
     */
    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}
