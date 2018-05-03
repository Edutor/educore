package dk.edutor.core.model.db

import dk.edutor.core.model.PersistenceManager
import dk.edutor.eduport.Challenge

object MySqlManager : PersistenceManager {

  override fun listChallenges(): List<Challenge> {
    TODO("not implemented")
    }

  override fun findChallenge(id: Int): Challenge {
    TODO("not implemented")
    }

  override fun saveChallenge(challenge: Challenge): Int {
    TODO("not implemented")
    }

  override fun dropChallenge(id: Int): Challenge {
    TODO("not implemented")
    }

}