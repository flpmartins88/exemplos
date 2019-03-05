package br.com.empresa.ktor

import br.com.empresa.ktor.user.Users
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun setupDatabase() {
    Database.connect(hikariDataSource())
    transaction {
        SchemaUtils.create(Users)
    }
}

/*
 * O Ktor usa o modelo async.
 * No mundo async bloquear uma thread que coisa dos requests pode ser problematico
 * então é melhor usar uma coroutine do kotlin para passar o processamento
 * para uma outra thread interna
 *
 * Após o processamento, a thread do request é restaurada e o processamento
 * volta para ela
 */
suspend fun <T> database(block: () -> T): T = withContext(Dispatchers.IO) {
    transaction { block() }
}

private fun hikariDataSource() = HikariDataSource(HikariConfig().apply {
    driverClassName = "org.h2.Driver"
    jdbcUrl = "jdbc:h2:mem:test"
    maximumPoolSize = 3
    isAutoCommit = false
    transactionIsolation = "TRANSACTION_REPEATABLE_READ"
    validate()
})