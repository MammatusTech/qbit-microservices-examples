package com.mammatustech.todo;

import io.advantageous.qbit.annotation.RequestMapping;
import io.advantageous.qbit.annotation.RequestMethod;
import io.advantageous.qbit.annotation.RequestParam;

import java.util.*;


@RequestMapping("/todo-service")
public class TodoService {


    private final Map<String, Todo> todoMap = new TreeMap<>();


    @RequestMapping(value = "/todo", method = RequestMethod.POST)
    public void add(final Todo todo) {

        todoMap.put(todo.getId(), todo);
    }



    @RequestMapping(value = "/todo", method = RequestMethod.DELETE)
    public void remove(@RequestParam("id") String id) {

        todoMap.remove(id);
    }



    @RequestMapping(value = "/todo", method = RequestMethod.GET)
    public List<Todo> list() {
        return new ArrayList<>(todoMap.values());
    }

}
