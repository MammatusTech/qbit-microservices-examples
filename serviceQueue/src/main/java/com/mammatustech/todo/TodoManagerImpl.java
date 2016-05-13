package com.mammatustech.todo;

import io.advantageous.qbit.reactive.Callback;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class TodoManagerImpl {

    private final Map<String, Todo> todoMap = new TreeMap<>();

    public TodoManagerImpl() {
    }

    public void add(final Callback<Boolean> callback, final Todo todo) {
        todoMap.put(todo.getId(), todo);
        callback.resolve(true);
    }

    public void remove(final Callback<Boolean> callback, final String id) {
        final Todo removed = todoMap.remove(id);
        callback.resolve(removed != null);
    }

    public void list(final Callback<ArrayList<Todo>> callback) {
        callback.accept(new ArrayList<>(todoMap.values()));
    }
}
