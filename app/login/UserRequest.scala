package login

import play.api.mvc.{Request, WrappedRequest}

/**
  * Created by mfearnley on 21/07/16.
  */
class UserRequest[A] (val user: User, val request: Request[A]) extends WrappedRequest[A](request) {

}
