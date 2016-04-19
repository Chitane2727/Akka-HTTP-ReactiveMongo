package com.mongo
import spray.json._
import DefaultJsonProtocol._
import akka.actor.ActorSystem
import scala.concurrent.Future
import akka.stream.{ ActorMaterializer, Materializer }
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import com.mongo.models._
import akka.http.scaladsl.model.StatusCodes._
import com.mongo.db.JobManager
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import scala.concurrent.ExecutionContext

import scala.concurrent.ExecutionContext

/**
  * Created by rahul on 19/04/16.
  */
trait RestApi {
 import com.mongo.db._
  import com.mongo.models.JobInfoProtocol._
  import com.mongo.models.JobInfoEntity._
  import com.mongo.models.JobInfoEntityProtocol._

  import com.mongo.models.JobProtocol._
  import com.mongo.models.JobEntity._
  import com.mongo.models.JobEntityProtocol._

  implicit val system: ActorSystem
  implicit val materializer: Materializer
  implicit val ec: ExecutionContext

  val route =
    pathPrefix("jobs"){
      (post & entity(as[Job])) { job =>
        complete {
          JobManager.saveJob(job) map { r =>
           OK -> r
          }
        }
      }
    }
}

object Api extends App with RestApi {

  override implicit val system = ActorSystem("rest-api")

  override implicit val materializer = ActorMaterializer()

  override implicit val ec = system.dispatcher

  val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  Console.readLine()

  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ => system.shutdown())

}