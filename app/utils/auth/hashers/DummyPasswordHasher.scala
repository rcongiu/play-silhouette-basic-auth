package utils.auth.hashers

import com.mohiva.play.silhouette.api.util.{PasswordHasher, PasswordInfo}

/**
  * A Dummy password hasher that does not hash the password at all.
  * Use at your own risk at is is insecure, but it may be useful when
  * secrets are passed from k8s as plaintext/config or as secrets mounted on the fs.
  */
class DummyPasswordHasher extends PasswordHasher {
  override def id: String = "dummy-hasher"

  override def hash(plainPassword: String): PasswordInfo = PasswordInfo(id, plainPassword)

  override def matches(passwordInfo: PasswordInfo, suppliedPassword: String): Boolean = passwordInfo.password == suppliedPassword

  override def isDeprecated(passwordInfo: PasswordInfo): Option[Boolean] = None
}
