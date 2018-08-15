package utils.auth

import com.mohiva.play.silhouette.api.Identity

/**
  * Model for your users. In Basic Auth, you may only have a username,
  * so we keep it simple and we just have the username here.
  * @param username
  */
case class User(username: String) extends Identity
