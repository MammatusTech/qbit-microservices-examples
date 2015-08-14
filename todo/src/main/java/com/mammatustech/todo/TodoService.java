package com.mammatustech.todo;

import io.advantageous.qbit.annotation.RequestMapping;
import io.advantageous.qbit.annotation.RequestMethod;
import io.advantageous.qbit.annotation.RequestParam;

import java.util.*;


@RequestMapping(value = "/todo-service", description = "Todo service")
public class TodoService {


    private final Map<String, Todo> todoMap = new TreeMap<>();


    @RequestMapping(value = "/todo", method = RequestMethod.POST,
            description = "add a todo item to the list", summary = "adds todo",
            returnDescription = "returns true if successful")
    public boolean add(final Todo todo) {

        todoMap.put(todo.getId(), todo);
        return true;
    }



    @RequestMapping(value = "/todo", method = RequestMethod.DELETE)
    public void remove(@RequestParam(value = "id", description = "id of Todo item to delete")
                           final String id) {

        todoMap.remove(id);
    }



    @RequestMapping(value = "/todo", method = RequestMethod.GET)
    public List<Todo> list() {
        return new ArrayList<>(todoMap.values());
    }

}
