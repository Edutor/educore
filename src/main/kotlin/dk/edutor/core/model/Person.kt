package dk.edutor.core.model

import javax.persistence.*

@Entity
@Table(name = "PEOPLE")
@NamedQuery(name = "allPeople", query = "SELECT p FROM Person p")
data class Person(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,

    var name: String
    )

