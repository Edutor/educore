package dk.edutor.core.model

import javax.persistence.*

@Entity
@Table(name = "PEOPLE")
data class Person(
  @Id
  val id: Long,
  var name: String)

