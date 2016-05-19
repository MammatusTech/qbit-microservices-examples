package com.mammatustech.todo;

import io.advantageous.qbit.annotation.QueueCallback;
import io.advantageous.qbit.annotation.QueueCallbackType;
import io.advantageous.qbit.reactive.Callback;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;


import static io.advantageous.qbit.service.ServiceProxyUtils.flushServiceProxy;

public class TodoManagerImpl {

    private final Map<String, Todo> todoMap = new TreeMap<>();
    private final Auditor auditor;

    public TodoManagerImpl(final Auditor auditor) {
        this.auditor = auditor;
    }

    public void add(final Callback<Boolean> callback, final Todo todo) {
        todoMap.put(todo.getId(), todo);
        auditor.audit("add", "added new todo");
        callback.resolve(true);
    }

    public void remove(final Callback<Boolean> callback, final String id) {
        final Todo removed = todoMap.remove(id);

        auditor.audit("add", "removed new todo");
        callback.resolve(removed != null);
    }

    public void list(final Callback<ArrayList<Todo>> callback) {
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
