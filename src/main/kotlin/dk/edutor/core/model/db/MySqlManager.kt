package dk.edutor.core.model.db

import dk.edutor.core.model.PersistenceManager
import dk.edutor.core.view.ChallengeSummary
import dk.edutor.eduport.Challenge
import dk.edutor.eduport.StringChallenge
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*


operator fun Properties.get(key: String, default: String) = this.getProperty(key, default)


object MySqlManager : PersistenceManager {
  val configuration = Properties()

  init {
    configuration.load(this.javaClass.classLoader.getResourceAsStream("exposed.properties"))
    Database.connect("jdbc:mysql://localhost:3306/edutor",
        driver = "com.mysql.jdbc.Driver",
        user = configuration["user", ""],
        password = configuration["password", ""]
        )
    transaction {
      create(CHALLENGES)
      create(STRING_CHALLENGES)
      create(TAGS)
      }
    }

  override fun listChallengeSummaries(): List<ChallengeSummary> {
    return transaction {
      CHALLENGES
          .selectAll()
          .map {
            ChallengeSummary(it[CHALLENGES.id], it[CHALLENGES.description])
            }
      }
    }

  override fun findChallenge(id: Int): Challenge {
    TODO("not implemented")
    }

  override fun saveChallenge(challenge: Challenge): Int {
    val dtype = challenge.javaClass.name

    if (challenge.id == 0)
      return transaction {
        val challengeId = CHALLENGES.insert {
          // it[CHALLENGES.id] = challenge.id
          it[CHALLENGES.dtype] = dtype
          it[CHALLENGES.description] = challenge.description
          } get CHALLENGES.id

        if (challengeId == null) throw RuntimeException("oups")
        when (challenge) {
          is StringChallenge -> STRING_CHALLENGES.insert {
              it[STRING_CHALLENGES.id] = challengeId
              it[STRING_CHALLENGES.question] = challenge.question
              }
          else -> throw RuntimeException("Unknown type")
          }
        challengeId
        }
    else return transaction {
      CHALLENGES.update({ CHALLENGES.id eq challenge.id }) {
        it[CHALLENGES.description] = challenge.description
        }
      when (challenge) {
        is StringChallenge -> STRING_CHALLENGES.update({ STRING_CHALLENGES.id eq challenge.id }) {
            it[STRING_CHALLENGES.question] = challenge.question
            }
        else -> throw RuntimeException("Unknown type")
        }
      }

    }

  override fun dropChallenge(id: Int): Challenge {
    TODO("not implemented")
    }

}