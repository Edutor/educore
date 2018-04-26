<<<<<<< HEAD
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dk.edutor.eduport.Port
import dk.edutor.eduport.jarchecker.JarChecker

import dk.edutor.eduport.Challenge
import dk.edutor.eduport.ChallengeSet
import dk.edutor.eduport.MCChallenge
=======
import dk.edutor.eduport.*
import dk.edutor.eduport.jarchecker.JarChecker
import dk.edutor.eduport.simple.SimpleChecker
>>>>>>> e0addf8bc075b3870a87b738651b8d91884f8cd3
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

<<<<<<< HEAD
val gson = GsonBuilder().setPrettyPrinting().create()
=======
val jarChecker = JarChecker()
val simpleChecker = SimpleChecker()

>>>>>>> e0addf8bc075b3870a87b738651b8d91884f8cd3
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
<<<<<<< HEAD
            get("/challenge"){
                call.respond(gson.toJson(getChallengeSet(listOf("Math"))))
            }
=======
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
>>>>>>> e0addf8bc075b3870a87b738651b8d91884f8cd3
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