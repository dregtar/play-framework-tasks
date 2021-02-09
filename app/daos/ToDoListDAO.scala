package daos

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._
import slick.lifted.ProvenShape

import models.Task


class TasksTableDef(tag: Tag) extends Table[Task](tag, "tasks") {

  def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def description: Rep[String] = column[String]("description")

  def isActive: Rep[Boolean] = column[Boolean]("is_active")

  override def * : ProvenShape[Task] =
    (id, description, isActive) <> ((Task.apply _).tupled, Task.unapply)
}

class Tasks @Inject()(
  protected val dbConfigProvider: DatabaseConfigProvider)(
  implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {

  val tasks = TableQuery[TasksTableDef]

  def listAll(onlyActive: Boolean): Future[Seq[Task]] = {
    if (onlyActive)
      dbConfig.db.run(tasks.filter(_.isActive).result)
    else
      dbConfig.db.run(tasks.result)
  }

  def get(id: Long): Future[Option[Task]] = {
    dbConfig.db.run(tasks.filter(_.id === id).result.headOption)
  }

  def add(task: Task): Future[String] = dbConfig.db.run(tasks += task).map(res => "Task successfully added") recover {
    case ex: Exception => ex.getCause.getMessage
  }

  def update(task: Task): Future[Int] = {
    dbConfig.db.run(tasks.filter(_.id === task.id).update(task))
  }

  def delete(id: Long): Future[Int] = {
    dbConfig.db.run(tasks.filter(_.id === id).delete)
  }

  def deleteInactive(): Future[Int] = {
    dbConfig.db.run(tasks.filter(!_.isActive).delete)
  }

  def resetStatus(id: Long, isActive: Boolean): Future[Int] = {
    val q = for {t <- tasks if t.id === id} yield t.isActive
    dbConfig.db.run(q.update(isActive))
  }

  def resetAllStatuses(isActive: Boolean): Future[Int] = {
    val q = for {t <- tasks} yield t.isActive
    dbConfig.db.run(q.update(isActive))
  }
}
