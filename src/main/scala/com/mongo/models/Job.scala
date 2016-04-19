package com.mongo.models

import javax.print.attribute.standard.JobName

import reactivemongo.bson.{BSONDocument, BSONDocumentReader, BSONDocumentWriter}
import spray.json.DefaultJsonProtocol

/**
  * Created by rahul on 19/04/16.
  */

case class JobInfo(jobID: String,jobName: String)
case class Job(id:String,date_of_create:String,jobInfo: JobInfo)

object JobInfo {

  implicit object JobInfoBSONReader extends BSONDocumentReader[JobInfo] {
    def read(doc: BSONDocument): JobInfo =
      JobInfo(
        jobID = doc.getAs[String]("jobID").get,
        jobName = doc.getAs[String]("jobName").get
      )
  }

  implicit object JobInfoBSONWriter extends BSONDocumentWriter[JobInfo] {
    def write(JobInfo: JobInfo): BSONDocument =
      BSONDocument(
        "jobID" -> JobInfo.jobID,
        "jobName" -> JobInfo.jobName
      )
  }
}

object JobInfoProtocol extends DefaultJsonProtocol {
  implicit val JobInfoFormat = jsonFormat2(JobInfo.apply)
}

object JobProtocol extends DefaultJsonProtocol {
  import JobInfoProtocol._
  implicit val JobFormat = jsonFormat3(Job.apply)
}