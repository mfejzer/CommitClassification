package pl.umk.bugclassification.scmparser
import pl.umk.bugclassification.scmparser.utils.GitParserInvoker
import pl.umk.bugclassification.scmparser.utils.CommitParser

object Launcher {

  def main(args: Array[String]): Unit = {
    val parser = new GitParserInvoker("/home/mfejzer/projekt/kbc")

    val commitsParsed = parser.listLoggedCommits()
    commitsParsed.foreach(x => {println(x);println("Contains fix: "+x.containsFix())})
//    parser.tmpListLoggedCommits()
  }

  def temporaryTests() = {
    println(CommitParser.parse(CommitParser.authorName, "Mikołaj"))
    //    println(CommitParser.parse(CommitParser.authorName, "Mikołaj\n"))
    println(CommitParser.parse(CommitParser.author, "Author: Mikołaj\n"))
    //    println(CommitParser.parse(CommitParser.author, "Author: Mikołaj"))
    println(CommitParser.parse(CommitParser.sha1Text, "b15d78fd348c963d5df649a986b31c9b2dd36b43\n"))
    println(CommitParser.parse(CommitParser.dateValue, "Tue Nov 6 22:37:00 2012 +0100"))
    println(CommitParser.parse(CommitParser.date, "Date:   Tue Nov 6 22:37:00 2012 +0100\n"))
    println(CommitParser.parse(CommitParser.date, "Date:   Tue Nov 6 22:37:00 2012 +0100"))
  }
}