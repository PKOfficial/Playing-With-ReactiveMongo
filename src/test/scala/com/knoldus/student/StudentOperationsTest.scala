package com.knoldus.student

import org.scalatest.FunSuite
import reactivemongo.bson.{BSONArray, BSONDocument}

import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * Created by prabhat on 11/4/16.
  */
class StudentOperationsTest extends FunSuite {
  val studentOperations = new StudentOperations

  test("Insert Data") {
    val bson = BSONDocument("name" -> "Knoldus Noida", "scores" ->
      BSONArray(BSONDocument("type" -> "Exam", "score" -> 1.4),
        BSONDocument("type" -> "quiz", "score" -> 11.7),
        BSONDocument("type" -> "quiz", "score" -> 11.7),
        BSONDocument("type" -> "homework", "score" -> 6.67),
        BSONDocument("type" -> "assignment", "score" -> 35.87)
      )
    )
    studentOperations.insert(bson)
    val newBson = BSONDocument("name" -> "Knoldus Noida")
    val filter = BSONDocument(
      "_id" -> 0)
    studentOperations.print(newBson,filter)
//    val futureResult = Await.result(result, 10.second)
//    println("Future Result: " + futureResult)
  }

  test("Find document/documents of person having name \"Gisela Levin\"") {
    val bson = BSONDocument("name" -> "Gisela Levin")
    val filter = BSONDocument(
      "_id" -> 0)
    val result = studentOperations.findData(bson, filter)
    val futureResult = Await.result(result, 10.second)
    futureResult.map { bsonDoc =>
      println(BSONDocument.pretty(bsonDoc))
    }
  }

  test("person having name \"Gisela Levin\" and display (project) only scores list") {
    val bson = BSONDocument("name" -> "Gisela Levin")
    val filter = BSONDocument(
      "_id" -> 0,
      "scores" -> 1)
    val result = studentOperations.findData(bson, filter)
    val futureResult = Await.result(result, 10.second)
    futureResult.map { bsonDoc =>
      println(BSONDocument.pretty(bsonDoc))
    }
  }

  test("Change name of person \"Knoldus Noida\" to \"Noida Knoldus\" "){
    val selector = BSONDocument("name" -> "Knoldus Noida")
    val modifier = BSONDocument(
      "$set" -> BSONDocument("name" -> "Noida Knoldus")
    )
    studentOperations.update(selector,modifier)
    val newBson = BSONDocument("name" -> "Noida Knoldus")
    val filter = BSONDocument(
      "_id" -> 0)
    studentOperations.print(newBson,filter)
  }

  test("Add new score {type: \"assignment\", score: 60} to all documents belonging to person having name \"Gisela Levin\""){
    val selector = BSONDocument("name" -> "Gisela Levin")
    val modifier = BSONDocument(
      "$push" -> BSONDocument("scores"-> BSONArray(BSONDocument("type" -> "assignment", "score" -> 60)))
    )
    studentOperations.update(selector,modifier)
    val newBson = BSONDocument("name" -> "Gisela Levin")
    val filter = BSONDocument(
      "_id" -> 0)
    studentOperations.print(newBson,filter)
  }

  test("Find document with score of type assignment"){
    val bson = BSONDocument("scores.type" ->"assignment")
    val filter = BSONDocument(
      "_id" -> 0)
    val result = studentOperations.findData(bson, filter)
    val futureResult = Await.result(result, 10.second)
    futureResult.foreach { bsonDoc =>
      println(BSONDocument.pretty(bsonDoc))
    }
  }

  test("Remove document of person haven name \"Noida Knoldus\""){
    val bson = BSONDocument("name" -> "Noida Knoldus")
    studentOperations.remove(bson)
    val newBson = BSONDocument("name" -> "Noida Knoldus")
    val filter = BSONDocument(
      "_id" -> 0)
    studentOperations.print(newBson,filter)

  }

}
