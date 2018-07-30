package utils

import com.typesafe.config.ConfigFactory

trait HasConfig {

  val config = ConfigFactory.load("application.conf")

}
