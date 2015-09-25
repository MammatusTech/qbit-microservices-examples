package com.mammatustech.todo;

import scala.collection.JavaConversions;

import java.util.Collections;

public class Main {

    public static void main(final String... args) throws Exception{
        TodoServiceMain.main(JavaConversions.asScalaBuffer(Collections.singletonList("don't wait")));
    }
}
