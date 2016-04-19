package com.mongo.models

import spray.json.{DefaultJsonProtocol, JsString, JsValue, RootJsonFormat}
import spray.json._
import scala.util._
import reactivemongo.bson.{BSONDocumentWriter, BSONDocument, BSONDocumentReader, BSONObjectID}
/**
  * Created by rahul on 19/04/16.
  */

trait MongoRequire{
  implicit object BSONObjectIdProtocol extends RootJsonFormat[BSONObjectID] {
    override def write(obj: BSONObjectID): JsValue = JsString(obj.stringify)
    override def read(json: JsValue): BSONObjectID = json match {
      case JsString(id) => BSONObjectID.parse(id) match {
        case Success(validId) => validId
        case _ => deserializationError("Invalid BSON Object Id")
      }
      case _ => deserializationError("BSON Object Id expected")
    }
  }
}

case class JobInfoEntity(jobID: String,jobName: String)
case class JobEntity(id: String,
                     date_of_create: String,jobInfo: JobInfo)

object JobInfoEntity {

  implicit def toJobInfoEntity(jobInfo: JobInfo) =
    JobInfoEntity(jobID = jobInfo.jobID, jobName = jobInfo.jobName)

  implicit object JobInfoEntityBSONReader extends BSONDocumentReader[JobInfoEntity] {
    def read(doc: BSONDocument): JobInfoEntity =
      JobInfoEntity(
        jobID = doc.getAs[String]("jobID").get,
        jobName = doc.getAs[String]("jobName").get
      )
  }

  implicit object JobEntityBSONWriter extends BSONDocumentWriter[JobInfoEntity] {
    def write(jobInfoEntity: JobInfoEntity): BSONDocument =
      BSONDocument(
        "jobID" -> jobInfoEntity.jobID,
        "jobName" -> jobInfoEntity.jobName
      )
  }
}

object JobInfoEntityProtocol extends DefaultJsonProtocol with MongoRequire{

  implicit val JobInfoEntityFormat = jsonFormat2(JobInfoEntity.apply)
}


object JobEntity {

  implicit def toJobEntity(job: Job) =
    JobEntity(id = job.id, date_of_create = job.date_of_create, jobInfo = job.jobInfo)

  implicit object JobEntityBSONReader extends BSONDocumentReader[JobEntity] {
    def read(doc: BSONDocument): JobEntity =
      JobEntity(
        id = doc.getAs[String]("id").get,
        date_of_create = doc.getAs[String]("date_of_create").get,
        jobInfo = doc.getAs[JobInfo]("jobInfo").get
      )
  }

  implicit object JobEntityBSONWriter extends BSONDocumentWriter[JobEntity] {
    def write(JobEntity: JobEntity): BSONDocument =
      BSONDocument(
        "id" -> JobEntity.id,
        "date_of_create" -> JobEntity.date_of_create,
        "jobInfo" -> JobEntity.jobInfo
      )
  }
}

object JobEntityProtocol extends DefaultJsonProtocol with MongoRequire {

  import JobInfoProtocol._

  implicit val JobEntityFormat = jsonFormat3(JobEntity.apply)
}
