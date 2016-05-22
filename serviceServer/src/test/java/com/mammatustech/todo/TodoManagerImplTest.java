package com.mammatustech.todo;

import io.advantageous.qbit.client.Client;
import io.advantageous.qbit.client.ClientBuilder;
import io.advantageous.qbit.server.EndpointServerBuilder;
import io.advantageous.qbit.server.ServiceEndpointServer;
import static io.advantageous.qbit.server.EndpointServerBuilder.endpointServerBuilder;
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
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TodoManagerImplTest {


    private final Timer timer = Timer.timer();


    /** QBit WebSocket Client */
    private Client webSocketClient;

    /** Object address to the todoManagerImpl service actor. */
    private final String todoAddress = "todoService";
    /** Object address to the auditorService service actor. */
    private final String auditorAddress = "auditorService";
    /** Service Server */
    private ServiceEndpointServer serviceEndpointServer;

    /** Client service proxy to the todoManager */
    private TodoManagerClient client;
    /** Client service proxy to the auditor. */
    private Auditor auditor;

    @Before
    public void setup() {


        /* Create the serviceBundleBuilder. */
        final EndpointServerBuilder endpointServerBuilder = endpointServerBuilder();

        endpointServerBuilder.addService(auditorAddress, new AuditorImpl());


        /* Create the service server. */
        serviceEndpointServer = endpointServerBuilder.build();


        /* Create a service client proxy for the auditor. */
        auditor = serviceEndpointServer.serviceBundle().createLocalProxy(Auditor.class, auditorAddress);

        /* Create a todo manager and pass the client proxy of the auditor to it. */
        final TodoManagerImpl todoManager = new TodoManagerImpl(auditor);

        // Add the todoManager to the serviceBundle.
        serviceEndpointServer.serviceBundle()
                .addServiceObject(todoAddress, todoManager);

        //Create a client proxy to communicate with the service actor.
        //client = serviceEndpointServer.serviceBundle().createLocalProxy(TodoManagerClient.class, todoAddress);

        /* Start the service endpoint server and wait until it starts. */
        serviceEndpointServer.startServerAndWait();



        /* Create the WebSocket Client. */
        final ClientBuilder clientBuilder = ClientBuilder.clientBuilder();

        /** Build the webSocketClient. */
        webSocketClient = clientBuilder.setHost("localhost")
                                        .setPort(8080)
                                        .build();

        /* Create a REMOTE client proxy to communicate with the service actor. */
        client = webSocketClient.createProxy(TodoManagerClient.class, todoAddress);

        /* Start the remote client. */
        webSocketClient.start();
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
        }).catchError(e -> {
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
    public void tearDown() throws Exception{
        Thread.sleep(100);
        serviceEndpointServer.stop();
        webSocketClient.stop();
    }


}