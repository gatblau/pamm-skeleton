package utils

/**
  * Created by mfearnley on 25/07/16.
  */
trait ValidationUtils {

  /**
    * Validation for registering usernames. This is a sample validation method and should be changed as required.
    *
    * Currently mandates that a username is 3 characters or more and is alphanumeric
    *
    * @param username String containing the proposed username
    * @return True if username is valid, false otherwise
    */
  def validateUsername(username: String): Boolean = {
    (username.length >= 3) && username.matches("""[A-Za-z0-9]+""")
  }

  /**
    * Validation for registering passwords. This is a sample validation method and should be changed as required.
    *
    * Currently mandates that a password is 6 characters or more, and contains at least 1 letter and 1 number
    *
    * @param password String containing the proposed password
    * @return True if the password is valid, false otherwise
    */
  def validatePassword(password: String): Boolean = {
    (password.length >= 6) && password.matches("""^(?=.*[0-9])(?=.*[a-zA-Z])(.+)$""")
  }
}

object ValidationUtils extends ValidationUtils