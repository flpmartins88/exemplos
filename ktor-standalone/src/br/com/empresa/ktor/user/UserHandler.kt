package br.com.empresa.ktor.br.com.empresa.ktor.user

import br.com.empresa.ktor.user.User
import br.com.empresa.ktor.user.UserRepository
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import org.koin.ktor.ext.inject


class UserHandler(private val userRepository: UserRepository) {

    suspend fun create(call: ApplicationCall) {
        val userRequest = call.receive(UserRequest::class)
        this.userRepository.save(userRequest.toUser())
        call.respond(HttpStatusCode.Created, "user created")
    }

    suspend fun listAll(call: ApplicationCall) {
        val users = this.userRepository.findAll()
        call.respond(users)
    }

    suspend fun get(call: ApplicationCall) {
        val id = call.parameters["id"]

        if (id.isNullOrBlank()) {
            call.respond(HttpStatusCode.BadRequest, "user id is mandatory")
            return
        }

        val user = this.userRepository.findById(id.toInt())
        if (user == null) {
            call.respond(HttpStatusCode.NotFound)
            return
        }

        call.respond(UserResponse(user))
    }

}

fun Routing.userRoutes() {

    val userHandler: UserHandler by inject()

    route("/users") {
        post {
            userHandler.create(call)
        }
        get {
            userHandler.listAll(call)
        }
        get("/{id}") {
            userHandler.get(call)
        }
    }
}

class UserRequest(val name: String) {

    fun toUser() = User(name = this.name)
}

class UserResponse(val id: Int, val name: String) {
    constructor(user: User): this(user.id!!, user.name)
}
