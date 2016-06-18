package com.mammatustech.todo;

import io.advantageous.reakt.promise.Promise;

import java.util.ArrayList;

/**
 * Created by rick on 6/18/16.
 */
public interface TodoService {
    Promise<Boolean> addTodo(Todo todo);

    Promise<Boolean> removeTodo(String id);

    Promise<ArrayList<Todo>> listTodos();
}
