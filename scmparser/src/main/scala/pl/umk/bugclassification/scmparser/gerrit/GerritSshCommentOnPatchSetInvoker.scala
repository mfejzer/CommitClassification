package pl.umk.bugclassification.scmparser.gerrit

import pl.umk.bugclassification.scmparser.Invoker
import com.codahale.logula.Logging

class GerritSshCommentOnPatchSetInvoker(private val port: Int, private val hostname: String)
  extends Invoker with Logging {
  def comment(sha1: String, message: String): Unit = {
    log.info("Sending " + message + " for " + sha1 + " to " + hostname + ":" + port.toString)
    runProcess(GerritSshCommentOnPatshSetCommand(port, hostname, sha1, message))
  }
}