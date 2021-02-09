package services

import javax.inject.{Inject, Singleton}

import scala.concurrent.Future

import daos.Tasks
import models.Task


@Singleton
class ToDoListServices @Inject()(dao: Tasks) {

  def listAll(onlyActive: Boolean): Future[Seq[Task]] = dao.listAll(onlyActive)

  def getTask(id: Long): Future[Option[Task]] = dao.get(id)

  def addTask(taskToAdd: Task): Future[String] = dao.add(taskToAdd)

  def updateTask(taskToUpdate: Task): Future[Int] = dao.update(taskToUpdate)

  def resetStatus(id: Long, isActive: Boolean): Future[Int] = dao.resetStatus(id, isActive)

  def resetAllStatuses(isActive: Boolean): Future[Int] = dao.resetAllStatuses(isActive)

  def deleteTask(id: Long): Future[Int] = dao.delete(id)

  def deleteInactive(): Future[Int] = dao.deleteInactive()
}
