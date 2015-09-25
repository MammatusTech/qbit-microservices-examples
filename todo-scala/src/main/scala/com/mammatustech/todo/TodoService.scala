package com.mammatustech.todo

import io.advantageous.qbit.annotation.RequestMapping
import io.advantageous.qbit.annotation.RequestMethod
import io.advantageous.qbit.annotation.RequestParam
import java.util

/**
 * Default port for admin is 7777.
 * Default port for main endpoint is 8080.
 *
 * <pre>
 * <code>
 *
 * Access the service:
 *
 * $ curl http://localhost:8888/v1/...
 *
 *
 * To see swagger file for this service:
 *
 * $ curl http://localhost:7777/__admin/meta/
 *
 * To see health for this service:
 *
 * $ curl http://localhost:8888/__health
 * Returns "ok" if all registered health systems are healthy.
 *
 * OR if same port endpoint health is disabled then:
 *
 * $ curl http://localhost:7777/__admin/ok
 * Returns "true" if all registered health systems are healthy.
 *
 *
 * A node is a service, service bundle, queue, or server endpoint that is being monitored.
 *
 * List all service nodes or endpoints
 *
 * $ curl http://localhost:7777/__admin/all-nodes/
 *
 *
 * List healthy nodes by name:
 *
 * $ curl http://localhost:7777/__admin/healthy-nodes/
 *
 * List complete node information:
 *
 * $ curl http://localhost:7777/__admin/load-nodes/
 *
 *
 * Show service stats and metrics
 *
 * $ curl http://localhost:8888/__stats/instance
 * </code>
 * </pre>
 */
@RequestMapping(value = Array("/todo-service"), description = "Todo service")
class TodoService {
  private final val todoMap = new util.TreeMap[String, Todo]

  @RequestMapping(value = Array("/todo"),
    method = Array(RequestMethod.POST),
    description = "add a todo item to the list",
    summary = "adds todo",
    returnDescription = "returns true if successful")
  def add(todo: Todo) = {
    todoMap.put(todo.id, todo)
    true
  }

  @RequestMapping(value = Array("/todo"),
    method = Array(RequestMethod.DELETE),
    description = "Deletes an item by id",
    summary = "delete a todo item")
  def remove(@RequestParam(value = "id", description = "id of Todo item to delete")
             id: String) {
    todoMap.remove(id)
  }

  @RequestMapping(value = Array("/todo"),
    method = Array(RequestMethod.GET),
    description = "List all items in the system",
    summary = "list items",
    returnDescription = "return list of all items in system")
  def list = new util.ArrayList[Todo](todoMap.values)

}