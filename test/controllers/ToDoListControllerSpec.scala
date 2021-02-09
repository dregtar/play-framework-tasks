
package controllers

import models.Task
import org.mockito.Mockito.{doThrow, when}
import org.scalatestplus.mockito.MockitoSugar.mock
import org.scalatestplus.play._
import play.api.libs.json.Json
import play.api.mvc.ControllerComponents
import play.api.test._
import play.api.test.Helpers._
import services.ToDoListServices

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ToDoListControllerSpec extends PlaySpec  {
  val toDoListServicesMock: ToDoListServices = mock[ToDoListServices]
  val controller = new ToDoListController(toDoListServicesMock){
    override def controllerComponents: ControllerComponents = Helpers.stubControllerComponents()}

  "ToDoListController GET" should {

    "render the index page from a new instance of controller" in {
      val home = controller.index().apply(FakeRequest(GET, "/"))
      0 mustBe 0
      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("Welcome to Play")
    }


    when(toDoListServicesMock.getTask(-1)).thenReturn(Future.successful(None))
    "Not found test" in {
      val resp = controller.getTask(-1).apply(FakeRequest(GET, "/tasks/-1"))
      status(resp) mustBe OK
      contentAsString(resp) mustBe "null"
    }

    val testTask = Task(1,"big gleb smotrit za formatirovaniem", true)
    when(toDoListServicesMock.getTask(1)).thenReturn(Future.successful(Some(testTask)))
    "Successful call" in {
      val resp = controller.getTask(1).apply(FakeRequest(GET, "/tasks/1"))
      status(resp) mustBe OK
      contentAsString(resp) mustBe Json.toJson(testTask).toString()
    }

  }
}

