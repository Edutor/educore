package dk.edutor.core.model

import javax.persistence.Persistence

object DataManager {
  private val entityManagerFactory = Persistence.createEntityManagerFactory("edudata")
  fun createEntityManager() = entityManagerFactory.createEntityManager()
  fun close() { entityManagerFactory.close() }
  }
