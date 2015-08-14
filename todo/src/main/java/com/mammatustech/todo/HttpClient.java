package com.mammatustech.todo;

import io.advantageous.boon.json.JsonFactory;
import io.advantageous.qbit.http.HTTP;

public class HttpClient {

    public static void main(final String... args) throws Exception {

        for (int index = 0; index < 100; index++) {

            HTTP.postJSON("http://localhost:8888/v1/todo-service/todo",
                    JsonFactory.toJson(new Todo("name" + index, "desc" + index, System.currentTimeMillis() )));
            System.out.print(".");
        }
    }

}
