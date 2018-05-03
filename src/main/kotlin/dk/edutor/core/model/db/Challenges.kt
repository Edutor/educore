package dk.edutor.core.model.db

import org.jetbrains.exposed.sql.Table

object CHALLENGES : Table() {
  val id = integer("id").autoIncrement().primaryKey()
  val dtype = varchar("dtype", 40)
  val description = varchar("description", 200)
  }

object STRING_CHALLENGES : Table() {
  val id = integer("id").references(CHALLENGES.id)
  val question = varchar("question", 200)
  }

