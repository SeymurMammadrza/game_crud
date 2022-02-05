package controllers

import models.Game
import play.api.libs.json.{JsValue, Json}

import javax.inject._
import play.api.mvc._
import reactivemongo.bson.BSONObjectID
import repositories.GameRepository

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class GameController @Inject()(
                                implicit executionContext: ExecutionContext,
                                val gameRepository: GameRepository,
                                val controllerComponents: ControllerComponents)
  extends BaseController {
  def findAll(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    gameRepository.findAll().map {
      games => Ok(Json.toJson(games))
    }
  }

  def findOne(id: String): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    val objectIdTryResult = BSONObjectID.parse(id)
    objectIdTryResult match {
      case Success(objectId) => gameRepository.findOne(objectId).map {
        game => Ok(Json.toJson(game))
      }
      case Failure(_) => Future.successful(BadRequest("Cannot parse the game id"))
    }
  }

  def create(): Action[JsValue] = Action.async(controllerComponents.parsers.json) { implicit request => {

    request.body.validate[Game].fold(
      _ => Future.successful(BadRequest("Cannot parse request body")),
      game =>
        gameRepository.create(game).map {
          _ => Created(Json.toJson(game))
        }
    )
  }
  }

  def update(
              id: String): Action[JsValue] = Action.async(controllerComponents.parsers.json) { implicit request => {
    request.body.validate[Game].fold(
      _ => Future.successful(BadRequest("Cannot parse request body")),
      game => {
        val objectIdTryResult = BSONObjectID.parse(id)
        objectIdTryResult match {
          case Success(objectId) => gameRepository.update(objectId, game).map {
            result => Ok(Json.toJson(result.ok))
          }
          case Failure(_) => Future.successful(BadRequest("Cannot parse the game id"))
        }
      }
    )
  }
  }

  def delete(id: String): Action[AnyContent] = Action.async { implicit request => {
    val objectIdTryResult = BSONObjectID.parse(id)
    objectIdTryResult match {
      case Success(objectId) => gameRepository.delete(objectId).map {
        _ => NoContent
      }
      case Failure(_) => Future.successful(BadRequest("Cannot parse the game id"))
    }
  }
  }
}
