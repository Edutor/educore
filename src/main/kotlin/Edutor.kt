import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dk.edutor.eduport.Port
import dk.edutor.eduport.jarchecker.JarChecker

import dk.edutor.eduport.Challenge
import dk.edutor.eduport.ChallengeSet
import dk.edutor.eduport.MCChallenge
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

val gson = GsonBuilder().setPrettyPrinting().create()
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
            get("/challenge"){
                call.respond(gson.toJson(getChallengeSet(listOf("Math"))))
            }
            static("") {
                resources("www")
                defaultResource("index.html", "www")
                }
            }
        }
    server.start(wait = true)
}

fun getChallengeSet(tags: List<String>): List<Challenge> {
    var list: MutableList<Challenge> = ArrayList()
    for (c in allChallenges)
        if(c.tags.intersect(tags).size >= 1)
            list.add(c);
    return list
}
val allChallenges = listOf<Challenge>(
        MCChallenge(answers = mapOf("3" to false, "4" to true, "5" to false), description = "", question = "What is 2 + 2", tags = listOf("Math")),
        MCChallenge(answers = mapOf("0" to false, "18" to false, "20" to true), description = "", question = "What is 2 * 10", tags = listOf("Math")),
        MCChallenge(answers = mapOf("Babirusa" to true, "Crocodile" to false, "Camel" to true), description = "", question = "What animals are mammals", tags = listOf("Bio")),
        MCChallenge(answers = mapOf("Beethoven" to true, "Einstein" to false, "Mozart" to true), description = "", question = "Who were great composers", tags = listOf("Music"))
)