import dk.edutor.eduport.*
import dk.edutor.eduport.jarchecker.JarChecker
import dk.edutor.eduport.simple.SimpleChecker
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

val jarChecker = JarChecker()
val simpleChecker = SimpleChecker()

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
            get("/sayHello/{text}") {
                // a ?: b  ->  if (a == null) b else a (java: (a == null) ? b : a )
                val text = call.parameters["text"] ?: ""
                val result = arrayOf(jarChecker.sayHello(text), simpleChecker.sayHello(text))
                call.respond(result)
                }
            get("/check/{answer}") {
                val challenge = StringChallenge("42", "What is 7 multiplied by 6")
                val answer = call.parameters["answer"] ?: "I don't know"
                val solution = StringSolution(answer, PersonIdentifier(1), 1)
                val assesment = simpleChecker.check(challenge, solution)
                call.respond(assesment)
                }
            static("") {
                resources("www")
                defaultResource("index.html", "www")
                }
            }
        }
    server.start(wait = true)
    }
