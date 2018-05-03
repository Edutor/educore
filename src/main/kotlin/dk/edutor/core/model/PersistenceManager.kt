package dk.edutor.core.model

import dk.edutor.eduport.Challenge

interface PersistenceManager {
  // create and update logic here

  fun listChallenges(): List<Challenge>
  fun findChallenge(id: Int): Challenge
  fun saveChallenge(challenge: Challenge): Int
  fun dropChallenge(id: Int): Challenge
  }