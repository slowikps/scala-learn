package learn.presentations.futureandpromise.utils

import org.apache.commons.lang3.StringUtils

trait FancyLogging {
  val start = System.currentTimeMillis
  def info(msg: String) = printf("%.2f: [%.20s] %s\n", (System.currentTimeMillis - start) / 1000.0, StringUtils.rightPad(Thread.currentThread().toString, 20, ' '), msg)
}
