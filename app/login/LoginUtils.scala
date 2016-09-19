package login

import java.util.UUID

import com.mongodb.casbah.Imports._
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.mindrot.jbcrypt.BCrypt
import play.api.libs.functional.syntax._
import play.api.libs.json._
import utils.Constants

/**
  * Created by Mark Fearnley on 17/05/16.
  *
  * Utility class to aid in the login process.
  */
trait LoginUtils {

  // Format of date being stored in the DB
  val dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"

  // Read object allowing the Json library to read a Joda DateTime from a Json object
  val jodaDateReads = Reads[DateTime](js =>
    js.validate[String].map[DateTime](dtString =>
      DateTime.parse(dtString, DateTimeFormat.forPattern(dateFormat))
    )
  )

  // Write object allowing the Json library to parse a Joda DateTime into a Json object
  val jodaDateWrites: Writes[DateTime] = new Writes[DateTime] {
    def writes(d: DateTime): JsValue = JsString(d.toString())
  }

  // Read oject allowing the Json library read a User from a Json object
  val userReads: Reads[User] = (
    (JsPath \ "username").read[String] and
      (JsPath \ "password").read[String] and
      (JsPath \ "authToken").read[String] and
      (JsPath \ "authDate").read[DateTime](jodaDateReads)
    ) (User.apply _)

  // Write object allowing the Json library to parse a User into a Json object
  val userWrites = new Writes[User] {
    def writes(user: User) = Json.obj(
        "username" -> user.username,
        "password" -> user.password,
        "authToken" -> user.authToken,
        "authDate" -> JsString(user.authDate.toString()))
  }

  implicit val userFormat: Format[User] = Format(userReads, userWrites)

  /**
    * Get a JsValue object for a User.
    *
    * @param user User object
    * @return JsValue object that can be passed as a Json response.
    */
  def getJsonForUser(user: User): JsValue = Json.toJson(user)

  def getUserFromJson(json: String): User = Json.fromJson[User](Json.parse(json)).get
  /**
    * Generate a random token to be used as an Authentication token.
    *
    * @return 128-bit random String.
    */
  def generateAuthToken(): String = UUID.randomUUID().toString

  /**
    * Hash a plaintext password using BCrypt (generating a random salt)
    *
    * @param ptPassword Password in plaintext
    * @return Bcrypted String of the password.
    */
  def hashPassword(ptPassword: String): String = BCrypt.hashpw(ptPassword, BCrypt.gensalt())

  /**
    * Assesses whether a Plaintext password matches one ecrypted by BCrypt.
    *
    * @param enteredPass  Plaintext password
    * @param hashedPass   Password encrypted with BCrypt
    * @return             True if the password is valid (matches), false otherwise
    */
  def validPassword(enteredPass: String, hashedPass: String): Boolean = BCrypt.checkpw(enteredPass, hashedPass)

  /**
    * Authenticates a user, checking if the users auth token is valid.
    *
    * @param username       Username of user to authenticate
    * @param authToken      Auth token to check
    * @param userCollection Mongo Collection in which the User is stored.
    * @return               If the User is successfully authenticated, the User object. Otherwise, None is returned.
    */
  def authenticateUser(username: String, authToken: String, userCollection: MongoCollection): Option[User] = {

    val userQuery = "username" $eq username
    val user = userCollection.findOne(userQuery)

    if (user.isEmpty) {
      None
    } else {
      val u = new User(user.get)

      // Return true if the auth token hasn't expired and is valid
      if (u.authDate.plusMillis(Constants.authTokenTimeout).isAfterNow
        && u.authToken.equals(authToken)) {
        Some(u)
      } else {
        None
      }
    }
  }
}

object LoginUtils extends LoginUtils {
}