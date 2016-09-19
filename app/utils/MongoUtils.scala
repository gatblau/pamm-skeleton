package utils

import com.mongodb.casbah.Imports._
import com.mongodb.casbah.MongoCollection
import play.api.Play
import com.mongodb.casbah.commons.conversions.scala._

/**
  * Created by Mark Fearnley on 12/05/2016.
  *
  * Utility class to provide easy access to Mongo features.
  */
trait MongoUtils {

  /**
    * Get a Mongo collection given the DB and Collection name.
    *
    * @param dbName     Name of the Mongo database
    * @param collection Name of the Mongo collection
    * @return           MongoCollection object to perform db actions on.
    */
  def getCollection(dbName: String, collection: String): MongoCollection = {
    RegisterJodaTimeConversionHelpers()
    val mongoUri = Play.current.configuration.getString("mongodb.uri")
    if (mongoUri.isDefined)
      MongoClient(MongoClientURI(mongoUri.get))(dbName)(collection)
    else
      MongoClient()(dbName)(collection)
  }
}

object MongoUtils extends MongoUtils
