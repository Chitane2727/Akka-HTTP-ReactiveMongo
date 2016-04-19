package com.mongo.db

import com.mongo.models._
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.{BSONDocument, BSONObjectID}
import reactivemongo.api.QueryOpts
import reactivemongo.core.commands.Count
import reactivemongo.bson.{BSONDocument, BSONObjectID}
import reactivemongo.api.collections.bson.BSONCollection
import scala.concurrent.ExecutionContext

object JobManager {
  import MongoDB._

  val collection = db[BSONCollection]("jobs")

  def saveJob(jobEntity: JobEntity)(implicit ec: ExecutionContext) =
    collection.insert(jobEntity)//.map(_ => Created(jobEntity.id))

  def save(jobEntity: JobEntity)(implicit ec: ExecutionContext) =
    collection.insert(jobEntity).map(_ => Created(jobEntity.id))
  
  def findById(id: String)(implicit ec: ExecutionContext) =
    collection.find(queryById(id)).one[JobEntity]

  def deleteById(id: String)(implicit ec: ExecutionContext) =
    collection.remove(queryById(id)).map(_ => Deleted)

  def find(implicit ec: ExecutionContext) =
    collection.find(emptyQuery).cursor[BSONDocument].collect[List]()

  private def queryById(id: String) = BSONDocument("_id" -> BSONObjectID(id))

  private def emptyQuery = BSONDocument()
}