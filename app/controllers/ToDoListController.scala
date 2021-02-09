package controllers

import javax.inject._

import scala.concurrent.ExecutionContext

import play.api.libs.json.Json
import play.api.mvc._

import models.Task
import services.ToDoListServices

@Singleton
class ToDoListController @Inject()(
  toDoListServices: ToDoListServices)
  (implicit ec: ExecutionContext) extends InjectedController {

  def index(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def allTasks(onlyActive: Boolean): Action[AnyContent] = Action.async {
    toDoListServices.listAll(onlyActive).map(tasks => Ok(Json.toJson(tasks)))
  }

  def getTask(id: Long): Action[AnyContent] = Action.async {
    toDoListServices.getTask(id).map(tasks => Ok(Json.toJson(tasks)))
  }

  def addTask(description: String): Action[AnyContent] = Action.async {
    toDoListServices.addTask(Task(description = description)).map(_ => NoContent)
  }

  def updateTask(id: Long, description: String, isActive: Boolean): Action[AnyContent] = Action.async {
    toDoListServices.updateTask(Task(id, description, isActive)).map(_ => NoContent)
  }

  def resetStatus(id: Long, isActive: Boolean): Action[AnyContent] = Action.async {
    toDoListServices.resetStatus(id, isActive).map(_ => NoContent)
  }

  def resetAllStatuses(isActive: Boolean): Action[AnyContent] = Action.async {
    toDoListServices.resetAllStatuses(isActive).map(_ => NoContent)
  }

  def deleteTask(id: Long): Action[AnyContent] = Action.async {
    toDoListServices.deleteTask(id).map(_ => NoContent)
  }

  def deleteInactive(): Action[AnyContent] = Action.async {
    toDoListServices.deleteInactive().map(_ => NoContent)
  }
}
