package io.example;

import io.swagger.client.api.DefaultApi;
import io.swagger.client.model.Todo;

import java.util.List;
import java.util.function.Consumer;

public class Main {


    public static void main(String... args) throws Exception {
        DefaultApi defaultApi = new DefaultApi();

        Todo todo = new Todo();
        todo.setDescription("Show demo to group");
        todo.setName("Show demo");
        todo.setCreateTime(123L);

        defaultApi.add(todo);

        List<Todo> list = defaultApi.list();

        list.forEach(new Consumer<Todo>() {
            @Override
            public void accept(Todo todo) {
                System.out.println(todo);
            }
        });
    }
}
