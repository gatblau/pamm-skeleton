package login

import com.mongodb.DBObject
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.commons.MongoDBObject
import org.joda.time.DateTime

/**
  * Created by Mark Fearnley on 17/05/16.
  *
  * User object representing the users stored in the DB.
  */
case class User (val username: String,
                 val password: String,
                 var authToken: String,
                 var authDate: DateTime) {

  /**
    * Alternate constructor to generate a User from a Mongo DBObject.
    *
    * @param dbObject DBObject with data representing a User.
    * @return         Newly instantiated User.
    */
  def this(dbObject: DBObject) = this(
    dbObject.as[String]("username"),
    dbObject.as[String]("password"),
    dbObject.as[String]("authToken"),
    dbObject.as[DateTime]("authDate"))

  /**
    * Get a Mongo DBObject populated with data for the user.
    *
    * @return Fully populated DBObject
    */
  def getDBObject: DBObject = MongoDBObject(
      "username" -> username,
      "password" -> password,
      "authToken" -> authToken,
      "authDate" -> authDate
    )
}
