import dk.edutor.eduport.Port
import dk.edutor.eduport.jarchecker.JarChecker
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.content.*
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
        install(CallLogging)
        install(ContentNegotiation) {
            gson {
                setDateFormat(DateFormat.LONG)
                setPrettyPrinting()
                }
            }
        routing {
            get("/hello") {
                call.respond("goodbye")
                }
            static("") {
                resources("www")
                defaultResource("index.html", "www")
                }
            }
        }
    server.start(wait = true)
    }
