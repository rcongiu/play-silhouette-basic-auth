package utils.auth.repositories

import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.util.PasswordInfo
import com.mohiva.play.silhouette.api.{AuthInfo, LoginInfo}
import com.mohiva.play.silhouette.password.BCryptPasswordHasher
import javax.inject.Inject
import play.api.Configuration

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.io.Source
import scala.reflect.ClassTag

/**
  * Use apache htpasswd to maintain user/passwords.
  * Only supports bcrypt (htpasswd -b ) as algorithm.
  *
  * Set the location of the htpasswd file in "security.htpasswd.file"
  * in application.conf
  *
  * @param configuration
  */

class HtpasswdAuthInfoRepository @Inject()(configuration:Configuration) extends AuthInfoRepository {
  val hasher = configuration.getOptional[String]("security.htpasswd.hasher").
    getOrElse(BCryptPasswordHasher.ID)
  val userMap:Map[String,String] = readPasswordFile(configuration.getOptional[String]("security.htpasswd.file").
    getOrElse("/etc/apache2/htpasswd"))


  override def add[T <: AuthInfo](loginInfo: LoginInfo, authInfo: T): Future[T] = ???

  override def update[T <: AuthInfo](loginInfo: LoginInfo, authInfo: T): Future[T] = Future { authInfo }

  override def save[T <: AuthInfo](loginInfo: LoginInfo, authInfo: T): Future[T] = ???

  override def remove[T <: AuthInfo](loginInfo: LoginInfo)(implicit tag: ClassTag[T]): Future[Unit] = Future {}

  override def find[T <: AuthInfo](loginInfo: LoginInfo)(implicit tag: ClassTag[T]): Future[Option[T]] =
    Future {
      userMap.get(loginInfo.providerKey).map( PasswordInfo(hasher, _).asInstanceOf[T])
    }


  private[this] def readPasswordFile(file:String) = {
    Source.fromFile(file,"UTF-8").getLines.map { l =>
      val kv = l.split(":")

      kv(0) -> (
        /* FIXME: once bcrypt library supports moder hashes, upgrade it
        The bcrypt library used by silhouette is old and only supports hashes
        with salt version $2a$, while modern implementations (like htpasswd)
        use a salt version of $2y$ or $2x$. See https://en.wikipedia.org/wiki/Bcrypt
        I submitted a patch: https://github.com/jeremyh/jBCrypt/pull/16

        In the meanwhile, if we get a newer salt version, just change it to the old one.
         */
        if(hasher == "bcrypt" && (kv(1).startsWith("$2y") || kv(1).startsWith("$2x")))
          "$2a" + kv(1).substring(3)
        else
          kv(1))
    }.toMap
  }
}
