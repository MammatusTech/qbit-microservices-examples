package com.mammatustech.todo;

import scala.collection.JavaConversions;

import java.util.Collections;

public class ClientMain {

    public static void main(final String... args) throws Exception{
        HttpClientExample.main(JavaConversions.asScalaBuffer(Collections.singletonList("don't wait")));
    }
}
