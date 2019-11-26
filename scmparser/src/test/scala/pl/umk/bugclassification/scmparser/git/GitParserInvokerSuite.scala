package pl.umk.bugclassification.scmparser.git

import org.scalatest.FunSuite

class GitParserInvokerSuite extends FunSuite {
  test("check removing all lines on new file") {
    val diff =
      """
        |diff --git a/test/files/pos/t11640.scala b/test/files/pos/t11640.scala
        |new file mode 100644
        |index 0000000000..7b432f3695
        |--- /dev/null
        |+++ b/test/files/pos/t11640.scala
        |@@ -0,0 +1,6 @@
        |+object Test {
        |+  trait T[S]
        |+  type Foo[S <: Foo[S]] = T[S]
        |+
        |+  type X[A <: X[A]] = String
        |+}
        |
""".stripMargin.lines.toList
    val invoker = new GitParserInvoker("project", "repo", 500)

    val result = invoker.filterRemovedLines(diff)

    assert(result.isEmpty)
  }

  test("check preserving only removed lines for modified file") {
    val diff =
      """
        |diff --git a/src/repl/scala/tools/nsc/interpreter/MemberHandlers.scala b/src/repl/scala/tools/nsc/interpreter/MemberHandlers.scala
        |index 75b982bbb3..1a929b1930 100644
        |--- a/src/repl/scala/tools/nsc/interpreter/MemberHandlers.scala
        |+++ b/src/repl/scala/tools/nsc/interpreter/MemberHandlers.scala
        |@@ -26,7 +26,7 @@ trait MemberHandlers {
        |   import global._
        |   import naming._
        |
        |-  import ReplStrings.{string2codeQuoted, string2code, any2stringOf}
        |+  import ReplStrings.{string2codeQuoted, string2code, any2stringOf, quotedString}
        |
        |   private def codegenln(leadingPlus: Boolean, xs: String*): String = codegen(leadingPlus, (xs ++ Array("\n")): _*)
        |   private def codegenln(xs: String*): String = codegenln(true, xs: _*)
        |
""".stripMargin.lines.toList
    val invoker = new GitParserInvoker("project", "repo", 500)

    val result = invoker.filterRemovedLines(diff)

    assert(result.size == 1)
    assert(result.contains("  import ReplStrings.{string2codeQuoted, string2code, any2stringOf}"))
  }
}
