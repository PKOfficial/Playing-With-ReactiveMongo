package com.knoldus.movie

import org.scalatest.FunSuite
import org.scalatest.concurrent.ScalaFutures
import reactivemongo.bson.{BSONDocument, BSONInteger, BSONString}

import scala.concurrent.Await
import scala.concurrent.duration._

class MovieDetailsTest extends FunSuite with ScalaFutures{

  val mD = new MovieDetails

  test("collection that either won or were nominated for a best picture Oscar"){
    pending
    val bson = BSONDocument("genres"-> "Comedy", "genres" -> "Crime")
    val filter = BSONDocument(
      "_id" -> 0)
    val result = mD.findData(bson,filter)
    val futureResult = Await.result(result, 10.second)
    assert(futureResult.size === 56)
  }

  test("collection list both \"Comedy\" and \"Crime\" as genres regardless of how many other genres are listed"){
    val bson = BSONDocument("genres"-> "Comedy", "genres" -> "Crime")
    val filter = BSONDocument(
      "_id" -> 0)
    val result = mD.findData(bson,filter)
    val futureResult = Await.result(result, 10.second)
    assert(futureResult.size === 56)
  }

  test("collection list just the following two genres: \"Comedy\" and \"Crime\" with \"Comedy\" listed first"){
    val bson = BSONDocument("genres"-> List("Comedy","Crime"))
    val filter = BSONDocument(
      "_id" -> 0)
    val result = mD.findData(bson,filter)
    val futureResult = Await.result(result, 10.second)
    assert(futureResult.size === 20)
  }

  test("movies list \"Sweden\" second in the the list of countries"){
    val bson = BSONDocument("countries.1"->"Sweden")
    val filter = BSONDocument(
      "_id" -> 0)
    val result = mD.findData(bson,filter)
    val futureResult = Await.result(result, 10.second)
    assert(futureResult.size == 6)
  }

  test("movie with Title Only"){
    val bson = BSONDocument()
    val filter = BSONDocument(
      "title" -> 1,
      "_id" -> 0)
    val result = mD.findData(bson,filter)
    val futureResult = Await.result(result, 10.second)
    futureResult.map { bsonDocument =>
      //println(BSONDocument.pretty(bsonDocument))
      assert(bsonDocument.get("title").isDefined)
      assert(bsonDocument.get("year") == None)
      assert(bsonDocument.get("rated") == None)
      assert(bsonDocument.get("awards.wins") == None)
    }
  }

  test("movie from the year 2013 that is rated PG-13 and won no awards"){
    val bson = BSONDocument("year"->2013, "rated" -> "PG-13", "awards.wins" -> 0)
    val filter = BSONDocument(
      //"title" -> 1,
      "_id" -> 0)
    val result = mD.findData(bson,filter)
    val futureResult = Await.result(result, 10.second)
    futureResult.map { bsonDocument =>
      //println(BSONDocument.pretty(bsonDocument))
      assert(bsonDocument.get("year").get == BSONInteger(2013))
      assert(bsonDocument.get("rated").get == BSONString("PG-13"))
     // println(bsonDocument.get("awards"))
      assert(bsonDocument.get("awards.wins") == None)
    }
  }

  test("Remove Data"){
    val bson = BSONDocument("imdb.votes" -> "$lt :10000", "year" -> "$lte : 2013 , $gte : 2010" ,
      "tomato.consensus" -> null)
    val result = mD.removeData(bson)
    val futureResult = Await.result(result,1.second)
    println("Future Result: " + futureResult)
  }


}
