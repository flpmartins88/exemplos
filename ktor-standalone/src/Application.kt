package br.com.empresa.ktor

import br.com.empresa.ktor.br.com.empresa.ktor.user.UserHandler
import br.com.empresa.ktor.br.com.empresa.ktor.user.userRoutes
import br.com.empresa.ktor.user.UserRepository
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.features.*
import com.fasterxml.jackson.databind.*
import io.ktor.jackson.*
import io.ktor.server.netty.EngineMain
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext

fun main(args: Array<String>): Unit {
    StandAloneContext.startKoin(listOf(mainModule))
    EngineMain.main(args)
}

@Suppress("unused") // Referenced in application.conf
@JvmOverloads
fun Application.module(testing: Boolean = false) {

    setupDatabase()

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
            disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE
        }
    }

    install(StatusPages) {
        exception<AuthenticationException> { cause ->
            call.respond(HttpStatusCode.Unauthorized)
        }
        exception<AuthorizationException> { cause ->
            call.respond(HttpStatusCode.Forbidden)
        }
    }

    routing {
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }
        userRoutes()
    }
}

val mainModule = module {
    single { UserRepository() }
    single { UserHandler(get()) }
}

class AuthenticationException : RuntimeException()
class AuthorizationException : RuntimeException()

