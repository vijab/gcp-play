package utils

import java.util.concurrent.TimeUnit

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Directive0
import akka.http.scaladsl.server.Directives.extractClientIP
import akka.http.scaladsl.model.StatusCodes
import com.google.common.cache.{CacheBuilder, CacheLoader, LoadingCache}
import com.google.common.util.concurrent.RateLimiter
import com.typesafe.scalalogging.LazyLogging

object Limiters {

  val throttleRateLocal = 0.5 // Throttling to 1 request/2 seconds/IP

  val throttleRateGlobal = 10 // Throttling to 10 requests/second globally

  val limiterCache: LoadingCache[String, RateLimiter] = CacheBuilder.newBuilder()
    .expireAfterWrite(24, TimeUnit.HOURS)
    .build(new CacheLoader[String, RateLimiter] {
      override def load(key: String): RateLimiter = RateLimiter.create(throttleRateLocal)
    })

  val globalLimiter = RateLimiter.create(throttleRateGlobal)

}

trait RequestLimiter extends HasConfig
  with LazyLogging {

  def throttle: Directive0 = extractClientIP flatMap (ip => {
    val ipAddr = if(ip.isUnknown()) "unknown" else ip.getAddress().get().getCanonicalHostName
    logger.info(s"Received request from $ipAddr")
    val localLimiter = Limiters.limiterCache(ipAddr)
    val globalLimiter = Limiters.globalLimiter

    if (localLimiter.tryAcquire(1) && globalLimiter.tryAcquire(1)) {
      logger.info(s"Local Rate = ${localLimiter.getRate}; Global Rate = ${globalLimiter.getRate}")
      logger.info(Limiters.limiterCache.asMap().toString)
      pass
    } else {
      logger.info(s"Local Rate = ${localLimiter.getRate}; Global Rate = ${globalLimiter.getRate}")
      complete(StatusCodes.TooManyRequests)
    }
  })

}
