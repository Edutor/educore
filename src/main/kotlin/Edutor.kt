import com.google.gson.GsonBuilder
import dk.edutor.eduport.Challenge
import dk.edutor.eduport.MCChallenge
import dk.edutor.eduport.StringChallenge
import dk.edutor.eduport.StringSolution
import dk.edutor.eduport.PersonIdentifier

import dk.edutor.eduport.jarchecker.JarChecker
import dk.edutor.eduport.simple.SimpleChecker
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.content.defaultResource
import io.ktor.content.resources
import io.ktor.content.static
import io.ktor.features.CallLogging
import io.ktor.features.Compression
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.gson.gson
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import java.text.DateFormat

val gson = GsonBuilder().setPrettyPrinting().create()

val jarChecker = JarChecker()
val simpleChecker = SimpleChecker()


fun main(args: Array<String>) {
    val server = embeddedServer(Netty, 8080)
    {

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
            get("/challenge/tag/{tagname}"){
                val tagname = call.parameters["tagname"]?:"" //Should maybe change to !! (double bang) to get an exception when tagname is null?
                call.respond(gson.toJson(getChallengeSet(listOf(tagname))))
            }

            get("/sayHello/{text}") {
                // a ?: b  ->  if (a == null) b else a (java: (a == null) ? b : a )
                val text = call.parameters["text"] ?: ""
                val result = arrayOf(jarChecker.sayHello(text), simpleChecker.sayHello(text))
                call.respond(result)
            }
            get("/check/{answer}") {
                val challenge = StringChallenge("42", "What is 7 multiplied by 6", listOf())
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
//Remove solution from challenge
data class ChallengeWrapper(val question:String, val choices: List<String>)
fun MCChallenge.removeSolution(): ChallengeWrapper {
    return ChallengeWrapper(this.question, this.answers.keys.toList())
}

fun getChallengeSet(tags: List<String>): List<ChallengeWrapper> {
    var list: MutableList<ChallengeWrapper> = ArrayList()
    for (c in allChallenges)
        if(c.tags.intersect(tags).size >= 1){
            when(c){
                is MCChallenge -> list.add(c.removeSolution())
            }
        }
    return list
}
val allChallenges = listOf<Challenge>(
        MCChallenge(answers = mapOf("3" to false, "4" to true, "5" to false), description = "", question = "What is 2 + 2", tags = listOf("Math","Addition")),
        MCChallenge(answers = mapOf("0" to false, "18" to false, "20" to true), description = "", question = "What is 2 * 10", tags = listOf("Math", "Multiplication")),
        MCChallenge(answers = mapOf("Babirusa" to true, "Crocodile" to false, "Camel" to true), description = "", question = "What animals are mammals", tags = listOf("Bio")),
        MCChallenge(answers = mapOf("Beethoven" to true, "Einstein" to false, "Mozart" to true), description = "", question = "Who were great composers", tags = listOf("Music"))
)