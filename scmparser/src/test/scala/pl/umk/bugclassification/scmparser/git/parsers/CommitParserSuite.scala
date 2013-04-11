package pl.umk.bugclassification.scmparser.git.parsers

import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CommitParserSuite extends FunSuite {

  test("parsing correct log") {
    val log = """commit b15d78fd348c963d5df649a986b31c9b2dd36b43
Author: Mikołaj Fejzer <mfejzer@gmail.com>
Date:   Tue Nov 6 22:37:00 2012 +0100

    Removed ArgsState, added AlgorithmStatus
    
    kb,kbc and persistance layer changed to use new type

DiagramWrapper.hs
KnuthBendixCompletion/Algorithm.hs
KnuthBendixCompletion/Datatypes.hs
KnuthBendixCompletion/Tests.hs
Main.hs
Persistance.hs

commit 1ccdd6fc09cd8cfebeb5e5a796f644294ac46208
Author: Mikołaj Fejzer <mfejzer@gmail.com>
Date:   Mon Nov 5 21:36:13 2012 +0100

    Changed ReductionRule to use record syntax

DiagramWrapper.hs
KnuthBendixCompletion/Algorithm.hs
KnuthBendixCompletion/Datatypes.hs
KnuthBendixCompletion/Tests.hs
Parser.hs

commit ff9b7ce7810330984eb346437a58f80b4f6c33ed
Author: Mikołaj Fejzer <mfejzer@gmail.com>
Date:   Sun Sep 30 19:49:00 2012 +0200

    Fixed kb to remove axioms with lhs and rhs equal

KnuthBendixCompletion/Algorithm.hs
KnuthBendixCompletion/Tests.hs

"""
    val result = CommitParser.parse(CommitParser.commitList, log)
    assert(result.successful)
    assert(result.get.length == 3)
    assert(result.get.map(x => (x.author == "Mikołaj Fejzer <mfejzer@gmail.com>")).toList.reduce((x, y) => (x && y)))
  }

  test("parrsing correct log containing commit with no files"){
    val log = """commit b15d78fd348c963d5df649a986b31c9b2dd36b43
Author: Mikołaj Fejzer <mfejzer@gmail.com>
Date:   Tue Nov 6 22:37:00 2012 +0100

    Removed ArgsState, added AlgorithmStatus
    
    kb,kbc and persistance layer changed to use new type

DiagramWrapper.hs
KnuthBendixCompletion/Algorithm.hs
KnuthBendixCompletion/Datatypes.hs
KnuthBendixCompletion/Tests.hs
Main.hs
Persistance.hs

commit 1ccdd6fc09cd8cfebeb5e5a796f644294ac46208
Author: Mikołaj Fejzer <mfejzer@gmail.com>
Date:   Mon Nov 5 21:36:13 2012 +0100

    Changed ReductionRule to use record syntax

DiagramWrapper.hs
KnuthBendixCompletion/Algorithm.hs
KnuthBendixCompletion/Datatypes.hs
KnuthBendixCompletion/Tests.hs
Parser.hs

commit ff9b7ce7810330984eb346437a58f80b4f6c33ed
Author: Mikołaj Fejzer <mfejzer@gmail.com>
Date:   Sun Sep 30 19:49:00 2012 +0200

    Fixed kb to remove axioms with lhs and rhs equal
"""
    val result = CommitParser.parse(CommitParser.commitList, log)
    assert(result.successful)
    assert(result.get.length == 3)
    assert(result.get.map(x => (x.author == "Mikołaj Fejzer <mfejzer@gmail.com>")).toList.reduce((x, y) => (x && y)))
  }
  
  test("parsing correct commit") {
    val commit = """commit b15d78fd348c963d5df649a986b31c9b2dd36b43
Author: Mikołaj Fejzer <mfejzer@gmail.com>
Date:   Tue Nov 6 22:37:00 2012 +0100

    Removed ArgsState, added AlgorithmStatus
    
    kb,kbc and persistance layer changed to use new type

DiagramWrapper.hs
KnuthBendixCompletion/Algorithm.hs
KnuthBendixCompletion/Datatypes.hs
KnuthBendixCompletion/Tests.hs
Main.hs
Persistance.hs
"""

    val result = CommitParser.parse(CommitParser.commit, commit)
    assert(result.successful)
    assert(result.get.author == "Mikołaj Fejzer <mfejzer@gmail.com>")
    assert(result.get.date == "Tue Nov 6 22:37:00 2012 +0100")
    assert(result.get.sha1 == "b15d78fd348c963d5df649a986b31c9b2dd36b43")
    assert(result.get.containsFix() == false)
  }

  
  test("parsing correct sha1") {
    val what = """commit b15d78fd348c963d5df649a986b31c9b2dd36b43
      """
    val result = CommitParser.parse(CommitParser.sha1, what)
    assert(result.successful)
  }

  test("parsing correct author") {
    val what = """Author: Mikołaj Fejzer <mfejzer@gmail.com>
      """
    val result = CommitParser.parse(CommitParser.author, what)
    assert(result.successful)
  }

  test("parsing correct date") {
    val what = """Date:   Tue Nov 6 22:37:00 2012 +0100
      """
    val result = CommitParser.parse(CommitParser.date, what)
    assert(result.successful)
  }

  test("parsing correct message") {
    val what = """
    Removed ArgsState, added AlgorithmStatus
    
    kb,kbc and persistance layer changed to use new type
"""
    val result = CommitParser.parse(CommitParser.message, what)
    assert(result.successful)
  }

  test("parsing correct message with another commit") {
    val what = """
    Removed ArgsState, added AlgorithmStatus
    
    kb,kbc and persistance layer changed to use new type

commit 1ccdd6fc09cd8cfebeb5e5a796f644294ac46208
"""
    val result = CommitParser.parse(CommitParser.message, what)
    assert(result.successful)
    assert(!result.get.contains("commit 1ccdd6fc09cd8cfebeb5e5a796f644294ac46208"))
  }

}