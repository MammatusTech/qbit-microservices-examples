package com.mammatustech.todo;

import io.advantageous.qbit.annotation.*;
import io.advantageous.qbit.annotation.http.DELETE;
import io.advantageous.qbit.annotation.http.GET;
import io.advantageous.qbit.annotation.http.PUT;
import io.advantageous.qbit.reactive.Callback;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


import static io.advantageous.qbit.service.ServiceProxyUtils.flushServiceProxy;

@RequestMapping("/todo-service")
public class TodoManagerImpl {

    private final Map<String, Todo> todoMap = new TreeMap<>();
    private final Auditor auditor;

    public TodoManagerImpl(final Auditor auditor) {
        this.auditor = auditor;
    }


    @GET("/todo/count")
    public int size() {
        return todoMap.size();
    }



    @PUT("/todo/")
    public void add(final Callback<Boolean> callback, final Todo todo) {
        todoMap.put(todo.getId(), todo);
        auditor.audit("add", "added new todo");
        callback.resolve(true);
    }

    @DELETE("/todo/")
    public void remove(final Callback<Boolean> callback,
                       @RequestParam("id") final String id) {
        final Todo removed = todoMap.remove(id);

        auditor.audit("add", "removed new todo");
        callback.resolve(removed != null);
    }

    @GET("/todo/")
    public void list(final Callback<List<Todo>> callback) {
        auditor.audit("list", "auditor added");
        callback.accept(new ArrayList<>(todoMap.values()));
    }


    @QueueCallback({QueueCallbackType.LIMIT, QueueCallbackType.EMPTY, QueueCallbackType.IDLE})
    public void process() {
        flushServiceProxy(auditor);
    }

    @QueueCallback({QueueCallbackType.INIT})
    public void init() {
        auditor.audit("init", "init service");
    }

    @QueueCallback({QueueCallbackType.SHUTDOWN})
    public void shutdown() {
        System.out.println("operation shutdown, shutdown service");
        flushServiceProxy(auditor);
    }

}
