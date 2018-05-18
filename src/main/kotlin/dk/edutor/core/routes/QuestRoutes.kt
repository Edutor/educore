package dk.edutor.core.routes

import dk.edutor.core.model.db.MySqlManager
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get

fun Routing.quest() {
  get ("/challenge") {
    call.respond(MySqlManager.listChallengeSummaries())
    }
  }
