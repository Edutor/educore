package dk.edutor.core.model

import org.junit.Test

class DataManagerTest {

  @Test
  fun testListPeople() {
    val em = DataManager.createEntityManager()
    val query = em.createQuery("select p from Person p", Person::class.java)
    query.resultList.forEach { println(it) }

    }
  }