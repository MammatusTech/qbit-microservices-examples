package com.mammatustech.todo

import io.advantageous.boon.json.JsonFactory
import io.advantageous.qbit.http.HTTP

object HttpClientExample {
  @throws(classOf[Exception])
  def main(args: String*) {

      var index: Int = 0
      while (index < 100) {
        {
          HTTP.postJSON("http://localhost:8888/v1/todo-service/todo",
            JsonFactory.toJson(
              new Todo("name" + index, "desc" + index,
                System.currentTimeMillis)))
            print(".")
        }
        {
          index += 1
        }
      }
    }

}