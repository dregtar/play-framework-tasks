package controllers

import models.Task

import javax.inject._
import play.api.libs.json.Json
import play.api.mvc._
import services.ToDoListServices

import scala.concurrent.ExecutionContext

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class ToDoListController @Inject()(toDoListServices: ToDoListServices)(implicit ec: ExecutionContext) extends InjectedController {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def allTasks(onlyActive: Boolean) = Action.async {
    toDoListServices.listAll(onlyActive).map(tasks => Ok(Json.toJson(tasks)))
  }

  def getTask(id: Long) = Action.async {
    toDoListServices.getTask(id).map(tasks => Ok(Json.toJson(tasks)))
  }

  def addTask(description: String) = Action.async { _ =>
    toDoListServices.addTask(Task(description = description)).map(_ => NoContent)
  }

  def updateTask(id: Long, description: String, isActive: Boolean) = Action.async { _ =>
    toDoListServices.updateTask(Task(id, description, isActive)).map(_ => NoContent)
  }

  def resetStatus(id: Long, isActive: Boolean) = Action.async { _ =>
    toDoListServices.resetStatus(id,isActive).map(_ => NoContent)
  }

  def resetAllStatuses(isActive: Boolean) = Action.async { _ =>
    toDoListServices.resetAllStatuses(isActive).map(_ => NoContent)
  }

  def deleteTask(id: Long) = Action.async { _ =>
    toDoListServices.deleteTask(id).map(_ => NoContent)
  }

  def deleteInactive() = Action.async { _ =>
    toDoListServices.deleteInactive.map(_ => NoContent)
  }
}
