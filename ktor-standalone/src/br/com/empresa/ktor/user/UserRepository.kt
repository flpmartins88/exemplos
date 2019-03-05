package br.com.empresa.ktor.user

import br.com.empresa.ktor.database
import org.jetbrains.exposed.sql.*

object Users: Table("users") {
    val id = integer("id").primaryKey().autoIncrement()
    val name = varchar("name", 200)
}

class UserRepository {

    suspend fun save(user: User) = database {
        Users.insert {
            it[name] = user.name
        } get Users.id
    }

    suspend fun findAll() = database {
        Users.selectAll().map { toUser(it) }
    }

    suspend fun findById(id: Int) = database {
        Users.select { Users.id eq id }
            .map { toUser(it) }
            .singleOrNull()
    }

    private fun toUser(row: ResultRow) = User(
        id = row[Users.id],
        name = row[Users.name]
    )

}