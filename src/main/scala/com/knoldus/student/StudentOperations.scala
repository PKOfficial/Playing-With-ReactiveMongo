package com.knoldus.student

import reactivemongo.api._
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.commands.{UpdateWriteResult, WriteResult}
import reactivemongo.bson._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

class StudentOperations {
  val driver = new MongoDriver
  val connection = driver.connection(List("localhost"))

  val db = connection("student")

  Thread.sleep(1000)

  def insert(query: BSONDocument) {
    val collection = db.collection[BSONCollection]("student")
    val future = collection.insert(query)
    future.onComplete {
      case Failure(e) => throw e
      case Success(lastError) => {
        println("successfully inserted document with lastError = " + lastError)
      }
    }
  }

  def remove(query: BSONDocument): Future[WriteResult] = {
    val collection = db.collection[BSONCollection]("student")
    collection.remove(query)
  }

  def update(selector: BSONDocument, modifiers: BSONDocument): Future[UpdateWriteResult] = {
    //db.student.update({"name":"Knoldus Noida"},{$set:{"name":"Noida Knoldus"}})
    val collection = db.collection[BSONCollection]("student")
    collection.update(selector, modifiers)
  }

  def print(bson: BSONDocument, filter: BSONDocument): Unit = {
    val result = findData(bson, filter)
    result.onComplete{
      case Failure(e) => throw e
      case Success(list) => {
        list.foreach{ bsonDoc =>
          println(BSONDocument.pretty(bsonDoc))
        }
      }
    }
  }

  def findData(query: BSONDocument, filter: BSONDocument): Future[List[BSONDocument]] = {
    val collection = db.collection[BSONCollection]("student")
    collection.find(query, filter).cursor[BSONDocument]().collect[List]()
  }

}
