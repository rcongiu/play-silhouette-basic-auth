import com.google.inject.{AbstractModule, Provides}
import java.time.Clock

import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api._
import com.mohiva.play.silhouette.api.services.AuthenticatorService
import com.mohiva.play.silhouette.api.util.PasswordHasherRegistry
import com.mohiva.play.silhouette.impl.authenticators.{DummyAuthenticator, DummyAuthenticatorService}
import com.mohiva.play.silhouette.impl.providers.BasicAuthProvider
import com.mohiva.play.silhouette.password.BCryptPasswordHasher
import _root_.services._
import utils.auth._
import utils.auth.repositories.HtpasswdAuthInfoRepository
// use scala guice binding
import net.codingwell.scalaguice.{ ScalaModule, ScalaPrivateModule }
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * This class is a Guice module that tells Guice how to bind several
 * different types. This Guice module is created when the Play
 * application starts.

 * Play will automatically use any class called `Module` that is in
 * the root package. You can create modules in other locations by
 * adding `play.modules.enabled` settings to the `application.conf`
 * configuration file.
 */
class Module extends AbstractModule with ScalaModule {

  override def configure() = {
    // Use the system clock as the default implementation of Clock
    bind[Clock].toInstance(Clock.systemDefaultZone)
    // Ask Guice to create an instance of ApplicationTimer when the
    // application starts.
    bind[ApplicationTimer].asEagerSingleton()
    // Set AtomicCounter as the implementation for Counter.
    bind[Counter].to[AtomicCounter]

    // authentication - silhouette bindings
    bind[Silhouette[DefaultEnv]].to[SilhouetteProvider[DefaultEnv]]
    bind[RequestProvider].to[BasicAuthProvider].asEagerSingleton()

    bind[UserService].to[ConfigUserServiceImpl]
    bind[PasswordHasherRegistry].toInstance(PasswordHasherRegistry(
      current = new BCryptPasswordHasher(),
      // if you want
      // current = new DummyPasswordHasher(),
      deprecated = Seq()
    ))
    bind[AuthenticatorService[DummyAuthenticator]].toInstance(new DummyAuthenticatorService)

    // configure a single username/password in play config
    //bind[AuthInfoRepository].to[ConfigAuthInfoRepository].asEagerSingleton()
    // or bind to htpasswd, set its location and its crypto hashing algorithm in config
    bind[AuthInfoRepository].to[HtpasswdAuthInfoRepository]
  }


  @Provides
  def provideEnvironment(
                          userService:          UserService,
                          authenticatorService: AuthenticatorService[DummyAuthenticator],
                          eventBus:             EventBus,
                          requestProvider:      RequestProvider
                        ): Environment[DefaultEnv] = {

    Environment[DefaultEnv](
      userService,
      authenticatorService,
      Seq(requestProvider),
      eventBus
    )
  }

}
