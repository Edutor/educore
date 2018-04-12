import dk.edutor.eduport.Port
import dk.edutor.eduport.jarchecker.JarChecker
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.*
import io.ktor.gson.gson
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import java.text.DateFormat

fun main(args: Array<String>) {
    val server = embeddedServer(Netty, 8080) {
        install(DefaultHeaders)
        install(Compression)
        install(ContentNegotiation) {
            gson {
                setDateFormat(DateFormat.LONG)
                setPrettyPrinting()
            }
        }
        install(CallLogging)
        routing {
            get("/hello") {
                call.respond("goodbye")
            }
        }
    }
    server.start(wait = true)
}
