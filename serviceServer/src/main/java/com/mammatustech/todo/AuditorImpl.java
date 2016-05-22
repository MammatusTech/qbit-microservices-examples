package com.mammatustech.todo;

public class AuditorImpl implements Auditor {

    public void audit(final String operation, final String log) {

        System.out.printf("operations %s, message %s log\n", operation, log);
    }
}
