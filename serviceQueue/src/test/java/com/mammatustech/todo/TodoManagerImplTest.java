package com.mammatustech.todo;

import io.advantageous.qbit.service.ServiceBuilder;
import io.advantageous.qbit.service.ServiceQueue;
import io.advantageous.qbit.time.Duration;
import io.advantageous.qbit.util.Timer;
import io.advantageous.reakt.promise.Promise;
import io.advantageous.reakt.promise.Promises;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static io.advantageous.qbit.service.ServiceBuilder.serviceBuilder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TodoManagerImplTest {

    TodoManagerClient client;
    ServiceQueue serviceQueue;
    final Timer timer = Timer.timer();

    @Before
    public void setup() {

        // Create a serviceQueue with a serviceBuilder.
        final ServiceBuilder serviceBuilder = serviceBuilder();

        //Start the serviceQueue.
        serviceQueue = serviceBuilder
                .setServiceObject(new TodoManagerImpl())
                .buildAndStartAll();

        //Create a client proxy to communicate with the service actor.
        client = serviceQueue.createProxyWithAutoFlush(TodoManagerClient.class,
                Duration.milliseconds(5));

    }

    @Test
    public void test() throws Exception {
        final Promise<Boolean> promise = Promises.blockingPromiseBoolean();

        // Add the todo item.
        client.add(new Todo("write", "Write tutorial", timer.time()))
                .invokeWithPromise(promise);


        assertTrue("The call was successful", promise.success());
        assertTrue("The return from the add call", promise.get());

        final Promise<List<Todo>> promiseList = Promises.blockingPromiseList(Todo.class);

        // Get a list of todo items.
        client.list().invokeWithPromise(promiseList);

        // See if the Todo item we created is in the listing.
        final List<Todo> todoList = promiseList.get().stream()
                .filter(todo -> todo.getName().equals("write")
                        && todo.getDescription().equals("Write tutorial")).collect(Collectors.toList());

        // Make sure we found it.
        assertEquals("Make sure there is one", 1, todoList.size());


        // Remove promise
        final Promise<Boolean> removePromise = Promises.blockingPromiseBoolean();
        client.remove(todoList.get(0).getId()).invokeWithPromise(removePromise);



        final Promise<List<Todo>> promiseList2 = Promises.blockingPromiseList(Todo.class);

        // Make sure it is removed.
        client.list().invokeWithPromise(promiseList2);

        // See if the Todo item we created is removed.
        final List<Todo> todoList2 = promiseList2.get().stream()
                .filter(todo -> todo.getName().equals("write")
                        && todo.getDescription().equals("Write tutorial")).collect(Collectors.toList());

        // Make sure we don't find it.
        assertEquals("Make sure there is one", 0, todoList2.size());

    }


    @Test
    public void testUsingAll() throws Exception {

        /* A list of promises for things we want to do all at once. */
        final List<Promise<Boolean>> promises = new ArrayList<>(3);
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicBoolean success = new AtomicBoolean();


        /** Add a todoItem to the client add method */
        final Todo todo = new Todo("write", "Write tutorial", timer.time());
        final Promise<Boolean> promise
                = client.add(todo);
        promises.add(promise);

        /** Add two more. */
        promises.add(client.add(new Todo("callMom", "Call Mom", timer.time())));
        promises.add(client.add(new Todo("callSis", "Call Sister", timer.time())));

        /** Now async wait for them all to come back. */
        Promises.all(promises).then(done -> {
            success.set(true);
            latch.countDown();
        }).catchError(e-> {
            success.set(false);
            latch.countDown();
        });

        /** Invoke the promises. */
        promises.forEach(Promise::invoke);

        /** They are all going to come back async. */
        latch.await();
        assertTrue(success.get());
    }

    @After
    public void tearDown() {
        serviceQueue.stop();
    }


}