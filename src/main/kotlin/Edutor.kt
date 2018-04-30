import com.google.gson.GsonBuilder
import dk.edutor.eduport.*
import dk.edutor.eduport.jarchecker.JarChecker
import dk.edutor.eduport.simple.SimpleChecker
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.content.defaultResource
import io.ktor.content.resources
import io.ktor.content.static
import io.ktor.features.*
import io.ktor.gson.gson
import io.ktor.request.PartData
import io.ktor.request.isMultipart
import io.ktor.request.receiveMultipart
import io.ktor.response.header
import io.ktor.response.respond
import io.ktor.response.respondWrite
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import java.text.DateFormat
import java.time.Duration

val gson = GsonBuilder().setPrettyPrinting().create()

val jarChecker = JarChecker()
val simpleChecker = SimpleChecker()


fun main(args: Array<String>) {
    val server = embeddedServer(Netty, 8080)
    {
        install(DefaultHeaders){
//            header("access-Control-Allow-Origin", "*")
//            header("access-control-allow-methods", "GET, HEAD")

        }
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
                call.respond("goodbye")
            }
            get("/tag"){
                call.respond(gson.toJson(allTags))
            }
            get("/challenge/tag/{tagname}"){
                val tagname = call.parameters["tagname"]?:"" //Should maybe change to !! (double bang) to get an exception when tagname is null?
                call.respond(gson.toJson(getChallengeSet(listOf(tagname))))
            }
            post("/postdemo/"){
                val multipart = call.receiveMultipart()
                call.respondWrite {
                    if (!call.request.isMultipart()) {
                        appendln("Not a multipart request")
                    } else {
                        while (true) {
                            val part = multipart.readPart() ?: break

                            when (part) {
                                is PartData.FormItem -> {
                                    val value = part.value
                                    println(value)//

                                    //VIRKER IKKE ENDNU
                                    val solution = gson.fromJson(value, MCSolution::class.java)
                                    println(solution)
//                                    appendln("Form field: ${part.partName} = ${part.value}")
//                                    val solution = MCSolution()
                                }
                                is PartData.FileItem -> appendln("File field: ${part.partName} -> ${part.originalFileName} of ${part.contentType}")
                            }
                            part.dispose()
                        }
                    }
                }
            }
            post("/challenge/submit"){
                throw UnsupportedOperationException("This method is not implemented yet")
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
data class ChallengeWrapper(val id: Int, val question:String, val choices: List<String>)
fun MCChallenge.removeSolution(): ChallengeWrapper {
    return ChallengeWrapper(this.id, this.question, this.answers.keys.toList())
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
        MCChallenge(1, answers = mapOf("3" to false, "4" to true, "5" to false), description = "", question = "What is 2 + 2", tags = listOf("Math","Addition")),
        MCChallenge(2, answers = mapOf("0" to false, "18" to false, "20" to true), description = "", question = "What is 2 * 10", tags = listOf("Math", "Multiplication")),
        MCChallenge(3, answers = mapOf("Babirusa" to true, "Crocodile" to false, "Camel" to true), description = "", question = "What animals are mammals", tags = listOf("Bio")),
        MCChallenge(4, answers = mapOf("Beethoven" to true, "Einstein" to false, "Mozart" to true), description = "", question = "Who were great composers", tags = listOf("Music"))
)
val allTags = listOf<String>(
        "Math", "Multiplication", "Bio", "Music"
)
//val testSol = MCSolution()