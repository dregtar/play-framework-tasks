package models

import play.api.libs.json.{Json, Writes}

case class Task (id: Long = 0, description: String, isActive: Boolean = true)

object Task {

  implicit val writes: Writes[Task] = (task: Task) => {
    Json.obj(
      "id" -> task.id,
      "description" -> task.description,
      "is_active" -> task.isActive
    )
  }
}