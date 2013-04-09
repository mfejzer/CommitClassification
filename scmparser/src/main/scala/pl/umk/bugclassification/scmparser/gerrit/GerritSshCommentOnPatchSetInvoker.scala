package pl.umk.bugclassification.scmparser.gerrit

import pl.umk.bugclassification.scmparser.Invoker

class GerritSshCommentOnPatchSetInvoker(private val port: Int, private val hostname: String) extends Invoker {
  def comment(sha1: String, message: String):Unit = {
    runProcess(GerritSshCommentOnPatshSetCommand(port, hostname, sha1, message))
  }
}