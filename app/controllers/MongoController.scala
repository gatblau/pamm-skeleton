package controllers

import com.mongodb.MongoException
import com.mongodb.casbah.Imports._
import play.api.Play
import play.api.mvc.{Action, Controller}
import utils.{Constants, MongoUtils}

/**
  * Created by markfearnley on 10/05/2016.
  */
class MongoController extends Controller {

  final val countTag = "callCount"

  /**
    * Dummy example method demonstrating simple Mongo usage.
    * Maintains a count of how many times the service has been called.
    *
    * @return Count of number of calls made to the service.
    */
  def helloMongo = Action {

    try {

      val collection = MongoUtils.getCollection(Constants.databaseName, Constants.helloCollection)

      // Get a list of all current counts saved to the database
      val q = countTag $exists true
      val counts = for (x <- collection.find(q)) yield x

      // If a count already exists, get the first one (for simplicity) and increment, otherwise the current count is 1
      val count = if (counts.isEmpty) 1 else counts.next().getAs[Int](countTag).get + 1

      if (counts.isEmpty) {
        // If there are no counts, save a new count object
        val newCount = MongoDBObject(countTag -> count)
        collection += newCount
      } else {
        // Otherwise update the existing count object
        val newCount = MongoDBObject(countTag -> count)
        collection.update(q, newCount)
      }

      // Pass back the count
      Ok(String.valueOf(count))
    } catch {
      case ex: MongoException => InternalServerError(Play.current.configuration.getString("mongodb.uri").get)
    }
  }

}
