package com.mammatustech.todo;

import io.advantageous.reakt.promise.Promise;
import java.util.List;


public interface TodoManagerClient {
    Promise<Boolean> add(Todo todo);
    Promise<Boolean> remove(String id);
    Promise<List<Todo>> list();
}
