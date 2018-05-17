import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

object People : Table() {
  val id = integer("id").autoIncrement().primaryKey()
  val name = varchar("name", length = 80)
  val email = varchar("email", length = 80).nullable()
  }

class Configuration {
  val properties = Properties()

  init {
    properties.load(this.javaClass.classLoader.getResourceAsStream("exposed.properties"))
    for (k in properties.stringPropertyNames()) {
      println("$k -> ${properties[k]}")
      }
    }

  operator fun get(key: String) =
      properties.getProperty(key, "")
  }


fun main(args: Array<String>) {
  val configuration = Configuration()
  //Database.connect("jdbc:mysql://206.189.49.65/edutor",
  Database.connect("jdbc:mysql://localhost:3306/edutor",
      driver = "com.mysql.jdbc.Driver",
      user = configuration["user"],
      password = configuration["password"]
      )

  transaction {
    create(People)

    val kurtId = People.insert {
      it[name] = "Kurt"
      it[email] = "kurt@mail.dk"
      }.get(People.id)

    val sonjaRef = People.insert {
      it[name] = "Sonja"
      it[email] = "sonja@post.dk"
      }

    val sonjaId1 = sonjaRef.get(People.id)
    val sonjaId2 = sonjaRef get People.id

    People.selectAll().forEach { println("${it[People.id]}: ${it[People.name]} ${it[People.email]}") }

    }

  }


