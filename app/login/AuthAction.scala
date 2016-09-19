package login

import java.util.Base64

import play.api.mvc._
import play.mvc.Http.Status
import utils.{Constants, MongoUtils}

import scala.concurrent.Future

/**
  * Created by mfearnley on 18/07/16.
  */
object AuthAction extends ActionBuilder[UserRequest] {

  override def invokeBlock[A](request: Request[A], block: (UserRequest[A]) => Future[Result]): Future[Result] = {
    val user = getUser(request)

    if (user.isDefined) {
      block(new UserRequest[A](user.get, request))
    } else {
      Future.successful { Results.Status(Status.UNAUTHORIZED) }
    }
  }

  def getUser[A](request: Request[A]): Option[User] = {
    val authHeader = request.headers.get(Constants.authHeader)

    if (authHeader.isDefined && authHeader.get.split(" ")(0).equals("Bearer")) {
      val b64s = authHeader.get.split(" ")(1)
      val userCreds = new String(Base64.getMimeDecoder.decode(b64s),"UTF-8")
      val userCollection = MongoUtils.getCollection(Constants.databaseName, Constants.userCollection)
      // Return the user
      return LoginUtils.authenticateUser(userCreds.split(":")(0), userCreds.split(":")(1), userCollection)
    }

    Option.empty
  }

  def populateRequestWithUser[A](request: Request[A], user: Option[User]) =
    request.headers.remove(Constants.authHeader)
      .add((Constants.userHeader, LoginUtils.getJsonForUser(user.get).toString()))
}
