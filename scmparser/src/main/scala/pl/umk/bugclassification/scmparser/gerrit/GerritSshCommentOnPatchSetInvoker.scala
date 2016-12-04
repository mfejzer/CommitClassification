package pl.umk.bugclassification.scmparser.gerrit

import org.slf4j.LoggerFactory
import pl.umk.bugclassification.scmparser.invokers.Invoker

class GerritSshCommentOnPatchSetInvoker(private val port: Int, private val hostname: String) extends Invoker {
  val log = LoggerFactory.getLogger(classOf[GerritSshCommentOnPatchSetInvoker])

  def comment(sha1: String, message: String): Unit = {
    log.info("Sending " + message + " for " + sha1 + " to " + hostname + ":" + port.toString)
    runProcess(GerritSshCommentOnPatshSetCommand(port, hostname, sha1, message))
  }
}