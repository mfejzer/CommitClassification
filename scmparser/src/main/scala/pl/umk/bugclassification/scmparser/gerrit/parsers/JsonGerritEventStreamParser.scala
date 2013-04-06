package pl.umk.bugclassification.scmparser.gerrit.parsers

import net.liftweb.json._
object JsonGerritEventStreamParser {
  implicit val formats = DefaultFormats
  def processEvent(line: String): Option[PatchSetCreatedGerritEvent] = {
    val parsed = parse(line)
    val eventType = parsed.\("type").extract[String]

    if ((eventType.equals("patchset-created"))) {
      val extracted = parsed.extract[PatchSetCreatedGerritEvent]
      Option(extracted)
    } else {
      Option.empty
    }
  }
}


case class PatchSetCreatedGerritEvent(
  val `type`: String,
  val change: Change,
  val patchSet: PatchSet,
  val uploader: User)

case class Change(val project: String,
  val branch: String,
  val id: String,
  val number: String,
  val subject: String,
  val owner: User,
  url: String)

case class PatchSet(val number: String,
  val revision: String,
  val parents: List[String],
  val ref: String,
  val uploader: User,
  val createdOn: String,
  val author: User,
  val sizeInsertions: String,
  val sizeDeletions: String)

case class User(val name: String,
  val email: String,
  val username: String)
