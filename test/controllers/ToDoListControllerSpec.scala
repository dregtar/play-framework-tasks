
package controllers

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar.mock
import org.scalatestplus.play._

import play.api.libs.json.Json
import play.api.mvc.ControllerComponents
import play.api.test.Helpers._
import play.api.test.{FakeRequest, Helpers}

import models.Task
import services.ToDoListServices





class ToDoListControllerSpec extends PlaySpec {
  val toDoListServicesMock: ToDoListServices = mock[ToDoListServices]

  val toDoListController = new ToDoListController(toDoListServicesMock) {
    override def controllerComponents: ControllerComponents = Helpers.stubControllerComponents()
  }

  "ToDoListController GET" should {

    "render the index page from a new instance of controller" in {
      val home = toDoListController.index().apply(FakeRequest(GET, "/"))

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include("Welcome to Play")
    }

    when(toDoListServicesMock.getTask(-1)).thenReturn(Future.successful(None))

    "return empty JSON if result not found" in {
      val response = toDoListController.getTask(-1).apply(FakeRequest(GET, "/tasks/-1"))

      status(response) mustBe OK
      contentAsString(response) mustBe "null"
    }

    val testTask = Task(1, "big gleb smotrit za formatirovaniem", true)
    when(toDoListServicesMock.getTask(1)).thenReturn(Future.successful(Some(testTask)))

    "return formatted JSON if task was found" in {
      val response = toDoListController.getTask(1).apply(FakeRequest(GET, "/tasks/1"))

      status(response) mustBe OK
      contentAsString(response) mustBe Json.toJson(testTask).toString()
    }

  }
}

