package repositories

import models.Game
import org.joda.time.DateTime
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.bson.collection.BSONCollection

import scala.concurrent.{ExecutionContext, Future}
import reactivemongo.api.{Cursor, ReadPreference}
import reactivemongo.bson.{BSONDocument, BSONObjectID}
import reactivemongo.api.bson.compat.{toDocumentReader, toDocumentWriter}
import reactivemongo.api.commands.WriteResult

import javax.inject.{Inject, Singleton}

@Singleton
class GameRepository @Inject()(
                                 implicit executionContext: ExecutionContext,
                                 reactiveMongoApi: ReactiveMongoApi
                               ) {
  def collection: Future[BSONCollection] = reactiveMongoApi.database.map(db => db.collection("games"))

  def findAll(limit: Int = 100): Future[Seq[Game]] = {

    collection.flatMap(
      _.find(BSONDocument(), Option.empty[Game])
        .cursor[Game](ReadPreference.Primary)
        .collect[Seq](limit, Cursor.FailOnError[Seq[Game]]())
    )
  }

  def findOne(id: BSONObjectID): Future[Option[Game]] = {
    collection.flatMap(_.find(BSONDocument("_id" -> id), Option.empty[Game]).one[Game])
  }

  def create(game: Game): Future[WriteResult] = {
    collection.flatMap(_.insert(ordered = false)
      .one(game.copy(_creationDate = Some(new DateTime()), _updateDate = Some(new DateTime()))))
  }

  def update(id: BSONObjectID, game: Game):Future[WriteResult] = {

    collection.flatMap(
      _.update(ordered = false).one(BSONDocument("_id" -> id),
        game.copy(
          _updateDate = Some(new DateTime())))
    )
  }

  def delete(id: BSONObjectID):Future[WriteResult] = {
    collection.flatMap(
      _.delete().one(BSONDocument("_id" -> id), Some(1))
    )
  }


}
