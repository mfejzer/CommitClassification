package pl.umk.bugclassification.scmparser
import pl.umk.bugclassification.scmparser.utils.GitParserInvoker
import pl.umk.bugclassification.scmparser.utils.CommitParser

object Launcher {

  def main(args: Array[String]): Unit = {
    val parser = new GitParserInvoker("/home/mfejzer/projekt/kbc")

    val commitsParsed = parser.listLoggedCommits()
    commitsParsed.foreach(x => println(x))
//    parser.tmpListLoggedCommits()
//temporaryTests()
  }

  def temporaryTests() = {
//    println(CommitParser.parse(CommitParser.authorName, "Mikołaj"))
//    //    println(CommitParser.parse(CommitParser.authorName, "Mikołaj\n"))
//    println(CommitParser.parse(CommitParser.author, "Author: Mikołaj\n"))
//    //    println(CommitParser.parse(CommitParser.author, "Author: Mikołaj"))
//    println(CommitParser.parse(CommitParser.sha1Text, "b15d78fd348c963d5df649a986b31c9b2dd36b43\n"))
//    println(CommitParser.parse(CommitParser.dateValue, "Tue Nov 6 22:37:00 2012 +0100"))
//    println(CommitParser.parse(CommitParser.date, "Date:   Tue Nov 6 22:37:00 2012 +0100\n"))
//    //    println(CommitParser.parse(CommitParser.date, "Date:   Tue Nov 6 22:37:00 2012 +0100"))
//    println(CommitParser.parse(CommitParser.messageText, """
//    Removed ArgsState, added AlgorithmStatus
//    
//    kb,kbc and persistance layer changed to use new type
//
//"""))
//    println(CommitParser.parse(CommitParser.messageText, """
//    Removed ArgsState, added AlgorithmStatus
//    
//    kb,kbc and persistance layer changed to use new type
//
//commit 1ccdd6fc09cd8cfebeb5e5a796f644294ac46208
//"""))

val commit="""commit b15d78fd348c963d5df649a986b31c9b2dd36b43
Author: Mikołaj Fejzer <mfejzer@gmail.com>
Date:   Tue Nov 6 22:37:00 2012 +0100

    Removed ArgsState, added AlgorithmStatus
    
    kb,kbc and persistance layer changed to use new type
"""

val log="""commit b15d78fd348c963d5df649a986b31c9b2dd36b43
Author: Mikołaj Fejzer <mfejzer@gmail.com>
Date:   Tue Nov 6 22:37:00 2012 +0100

    Removed ArgsState, added AlgorithmStatus
    
    kb,kbc and persistance layer changed to use new type

commit 1ccdd6fc09cd8cfebeb5e5a796f644294ac46208
Author: Mikołaj Fejzer <mfejzer@gmail.com>
Date:   Mon Nov 5 21:36:13 2012 +0100

    Changed ReductionRule to use record syntax

commit ff9b7ce7810330984eb346437a58f80b4f6c33ed
Author: Mikołaj Fejzer <mfejzer@gmail.com>
Date:   Sun Sep 30 19:49:00 2012 +0200

    Fixed kb to remove axioms with lhs and rhs equal

"""
  println(CommitParser.parse(CommitParser.commit,commit))
  
  println(CommitParser.parse(CommitParser.commitList,log))
  }
}