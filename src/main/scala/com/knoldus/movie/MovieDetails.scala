package com.knoldus.movie

import reactivemongo.api._
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class MovieDetails {
  val driver = new MongoDriver
  val connection = driver.connection(List("localhost"))

  val db = connection("video")

  def removeData(query: BSONDocument): Future[WriteResult] = {
    val collection = db.collection[BSONCollection]("movieDetails")
    collection.remove(query)
  }

  def findData(query: BSONDocument, filter: BSONDocument): Future[List[BSONDocument]] = {
    val collection = db.collection[BSONCollection]("movieDetails")
    collection.find(query, filter).cursor[BSONDocument]().collect[List]()
  }

}
