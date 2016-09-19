package utils

/**
  * Created by mfearnley on 18/07/16.
  */
object Constants {

  val databaseName = "pamm_skeleton"
  val userCollection = "user"
  val helloCollection = "hello_play"

  // How long an auth token has before it expires, in milliseconds
  val authTokenTimeout = 604800000

  // Header for the user object in the request (only exists if the request has been authenticated)
  val userHeader = "User"
  val authHeader = "Authorization"
}
