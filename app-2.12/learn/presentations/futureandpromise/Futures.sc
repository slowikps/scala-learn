import java.util.concurrent.TimeUnit

import org.apache.commons.lang3.StringUtils

import scala.concurrent.Future
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global

val start = System.currentTimeMillis

def info(msg: String) = printf("%.2f: [%.20s] %s\n", (System.currentTimeMillis - start) / 1000.0, StringUtils.rightPad(Thread.currentThread().toString, 20, ' '), msg)


