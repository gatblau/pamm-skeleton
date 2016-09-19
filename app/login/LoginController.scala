package login

import java.util.Base64

import com.mongodb.casbah.Imports._
import com.mongodb.casbah.commons.MongoDBObject
import org.joda.time.DateTime
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import utils.{ValidationUtils, Constants, MongoUtils}

/**
  * Created by Mark Fearnley on 17/05/16.
  *
  * Controller to handle requests surrounding Login and Registration.
  */
class LoginController extends Controller {

  /**
    * Register a new user. Assumes the Request is a JSON Object with the following structure:
    * {
    *   username: [USERNAME]
    *   password: [PASSWORD]
    * }
    *
    * @return A Json response, with 'success' being true if the user was successfully registered.
    */
  def register() = Action { request =>
    val json = request.body.asJson

    val username = (json.get \ "username").as[String].toLowerCase

    // Hashing the password using b-crypt, this is just a basic example.
    //TODO  The credentials should be passed in via BasicAuth - the current approach is insecure and unsuitable for a
    //TODO  production app.
    val ptPassword = (json.get \ "password").as[String]

    if (!ValidationUtils.validateUsername(username)) {
      Ok(Json.obj("success" -> false, "error" -> "Invalid username: The username must be 3 or more characters and alphanumeric."))
    } else if (!ValidationUtils.validatePassword(ptPassword)) {
      Ok(Json.obj("success" -> false, "error" -> "Invalid password: The password must be 6 characters or more and contain at least 1 letter and 1 number."))
    } else {

      val hashedPass = LoginUtils.hashPassword(ptPassword)

      val userCollection = MongoUtils.getCollection(Constants.databaseName, Constants.userCollection)

      // Check no user exists with the given username
      val query = "username" $eq username
      if (userCollection.find(query).limit(1).nonEmpty) {
        Ok(Json.obj("success" -> false, "error" -> "The username has already been taken."))
      } else {

        // The username is unique, so save it and return the result
        val user = User(username, hashedPass, LoginUtils.generateAuthToken(), DateTime.now())
        val userDBO = user.getDBObject
        userCollection += userDBO

        Ok(Json.obj("success" -> true, "username" -> user.username, "authToken" -> user.authToken))
      }
    }
  }

  /**
    * Authenticate a user
    *
    * @return An authentication token which must be passed with all further requests.
    */
  def authenticate() = Action { request =>
    val authHeader = request.headers.get(Constants.authHeader)

    if (authHeader.isEmpty || !authHeader.get.split(" ")(0).equals("Basic")) {
      if (authHeader.isEmpty) BadRequest("No login parameters given")
    }


    val b64s = authHeader.get.split(" ")(1)
    val userCreds = new String(Base64.getMimeDecoder.decode(b64s), "UTF-8")

    val username = userCreds.split(":")(0).toLowerCase
    val password = userCreds.split(":")(1)

    val userCollection = MongoUtils.getCollection(Constants.databaseName, Constants.userCollection)
    val userQuery = MongoDBObject("username" -> username)

    val user = userCollection.findOne(userQuery)

    if (user.isEmpty) {
      Ok(Json.obj("success" -> false, "error" -> "The Username or Password is invalid."))
    } else {
      val u = new User(user.get)
      if (!LoginUtils.validPassword(password, u.password)) {
        // If the password is invalid, send back a false authentication
        Ok(Json.obj("success" -> false, "error" -> "The Username or Password is invalid."))
      } else {
        // Generate a new auth token and reset the auth token date
        u.authToken = LoginUtils.generateAuthToken()
        u.authDate = DateTime.now()
        userCollection.update(userQuery, u.getDBObject)
        Ok(Json.obj("success" -> true, "username" -> u.username, "authToken" -> u.authToken))
      }
    }
  }

}
