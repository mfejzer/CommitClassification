package pl.umk.bugclassification.scmparser.git.parsers

import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import pl.umk.bugclassification.scmparser.TestLoggingConf

@RunWith(classOf[JUnitRunner])
class CommitParserSuite extends FunSuite with TestLoggingConf {

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

  test("parsing correct log containing multiple commits with no files") {
    val log =
      """commit a1f7a37246b744539384a10e77a73efdc7242dfb
Author: Max Kanat-Alexander <mkanat@bugzilla.org>
Date:   Fri Jul 9 22:15:39 2010 -0700

    Bug 577754: Make updating bugs_fulltext during checksetup.pl WAY faster
    for MySQL.
    r=mkanat, a=mkanat (module owner)

Bugzilla/Install/DB.pm

commit cb25db759a43927ee9c064ee656249ac8c0b4f38
Author: Max Kanat-Alexander <mkanat@bugzilla.org>
Date:   Fri Jul 9 22:07:51 2010 -0700

    Bug 577602: Don't put multi-select fields into the GROUP BY in Search.pm,
    because they are created by an aggregate. (This fixes multi-select buglist
    columns on Pg.)
    r=mkanat, a=mkanat (module owner)

Bugzilla/Search.pm

commit 33c83afdc1bfa2d96004265e7c977063eda25567
Author: Max Kanat-Alexander <mkanat@bugzilla.org>
Date:   Fri Jul 9 21:33:44 2010 -0700

    Remove the empty "index" directory from Dusk.

commit 06546f6d247901e10a05bbc0fb0d7f63e6295f8c
Author: Max Kanat-Alexander <mkanat@bugzilla.org>
Date:   Fri Jul 9 21:25:27 2010 -0700

    Remove the js/yui/utilities directory, because it's empty and keeps being
    added (annoyingly) by the CVS mirror script.

commit f9c5529281c464551500c195b032400ab9eaafce
Author: Max Kanat-Alexander <mkanat@bugzilla.org>
Date:   Fri Jul 9 21:09:18 2010 -0700

    Bug 451219: Allow altering from one SERIAL type to another on PostgreSQL,
    for people upgrading Testopia from 1.3 to 2.0+.
    r=mkanat, a=mkanat (module owner)

Bugzilla/DB/Schema/Pg.pm

"""
    val result = CommitParser.parse(CommitParser.commitList, log)
    assert(result.successful === true)
    assert(result.get.length === 5)
    assert(result.get.map(x => (x.author == "Max Kanat-Alexander <mkanat@bugzilla.org>")).toList.reduce((x, y) => (x && y)))
    assert(result.get(0).filenames.length===1)
    assert(result.get(1).filenames.length===1)
    assert(result.get(2).filenames.length===0)
    assert(result.get(3).filenames.length===0)
    assert(result.get(4).filenames.length===1)
  }

  test("parrsing correct log containing commit with no files") {
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