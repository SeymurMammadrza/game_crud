package models

import org.joda.time.DateTime
import play.api.libs.json._
import reactivemongo.bson._

import scala.util.Try

case class Game(
                 _id: Option[BSONObjectID],
                 _creationDate: Option[DateTime],
                 _updateDate: Option[DateTime],
                 name: String,
                 genre: String
               )

object Game {

  implicit object BSONObjectIDFormat extends Format[BSONObjectID] {
    def writes(objectId: BSONObjectID): JsValue = JsString(objectId.toString())
    def reads(json: JsValue): JsResult[BSONObjectID] = json match {
      case JsString(x) => {
        val maybeOID: Try[BSONObjectID] = BSONObjectID.parse(x)
        if(maybeOID.isSuccess) JsSuccess(maybeOID.get) else {
          JsError("Expected BSONObjectID as JsString")
        }
      }
      case _ => JsError("Expected BSONObjectID as JsString")
    }
  }
  implicit val dateTimeWriter: Writes[DateTime] = JodaWrites.jodaDateWrites("dd/MM/yyyy HH:mm:ss")

  implicit val dateTimeJsReader: Reads[DateTime] = JodaReads.jodaDateReads("yyyyMMddHHmmss")

  implicit val fmt: Format[Game] = Json.format[Game]


  implicit object gameBSONReader extends BSONDocumentReader[Game] {
    def read(doc: BSONDocument): Game = {
      Game(
        doc.getAs[BSONObjectID]("_id"),
        doc.getAs[BSONDateTime]("_creationDate").map(dt => new DateTime(dt.value)),
        doc.getAs[BSONDateTime]("_updateDate").map(dt => new DateTime(dt.value)),
        doc.getAs[String]("name").get,
        doc.getAs[String]("genre").get)
    }
  }

  implicit object gameBSONWriter extends BSONDocumentWriter[Game] {
    def write(game: Game): BSONDocument = {
      BSONDocument(
        "_id" -> game._id,
        "_creationDate" -> game._creationDate.map(date => BSONDateTime(date.getMillis)),
        "_updateDate" -> game._updateDate.map(date => BSONDateTime(date.getMillis)),
        "name" -> game.name,
        "genre" -> game.genre

      )
    }
  }
}
