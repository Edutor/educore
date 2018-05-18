// import dk.edutor.core.routes.*
//import dk.edutor.eduport.
import dk.edutor.core.model.db.MySqlManager
import dk.edutor.core.routes.quest
import dk.edutor.eduport.StringChallenge
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
        install(CORS){
            anyHost()
            }
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
//                MySqlManager.saveChallenge(StringChallenge(1, "First challenge", emptyList(), "Who is first?"))
//                MySqlManager.saveChallenge(StringChallenge(2, "Second challenge", emptyList(), "Who is next?"))
                call.respond("goodbye")
                }

            quest()

            // experimental()

            static("") {
                resources("www")
                defaultResource("index.html", "www")
            }

        }

    }
    server.start(wait = true)
}

