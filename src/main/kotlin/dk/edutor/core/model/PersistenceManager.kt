package dk.edutor.core.model

import dk.edutor.core.view.ChallengeSummary
import dk.edutor.eduport.Challenge

interface PersistenceManager {
  // create and update logic here

  fun listChallengeSummaries(): List<ChallengeSummary>
  fun findChallenge(id: Int): Challenge
  fun saveChallenge(challenge: Challenge): Int
  fun dropChallenge(id: Int): Challenge
  }