package com.mammatustech.todo;

import io.advantageous.qbit.service.ServiceBundle;
import io.advantageous.qbit.service.ServiceBundleBuilder;
import io.advantageous.qbit.service.stats.StatsCollector;
import io.advantageous.reakt.promise.Promise;
import io.advantageous.reakt.reactor.Reactor;
import org.junit.Test;

import java.util.List;

import static io.advantageous.qbit.service.ServiceBundleBuilder.*;
import static io.advantageous.reakt.promise.Promises.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TodoManagerTest {


    @Test
    public void testManager() throws Exception {

        /** Create service bundle . */
        final ServiceBundleBuilder serviceBundleBuilder = serviceBundleBuilder();
        serviceBundleBuilder.getRequestQueueBuilder().setBatchSize(1);
        final ServiceBundle serviceBundle = serviceBundleBuilder.build();

        /** Create implementation. */
        final TodoManagerImpl todoManagerImpl = new TodoManagerImpl(Reactor.reactor(), new StatsCollector() {
        });

        /** Add implementation to service bundle. */
        serviceBundle.addServiceObject("todo", todoManagerImpl);
        final TodoManager todoManager = serviceBundle.createLocalProxy(TodoManager.class, "todo");
        serviceBundle.start();

        /** Add a Todo. */
        final Promise<Boolean> addPromise = blockingPromise();
        todoManager.add(new Todo("Buy Tesla", "Buy new Tesla", System.currentTimeMillis()))
                .catchError(Throwable::printStackTrace).invokeWithPromise(addPromise);
        assertTrue(addPromise.get());

        /** Call list to get a list of Todos. */
        final Promise<List<Todo>> listPromise = blockingPromise();
        todoManager.list().invokeWithPromise(listPromise);
        final List<Todo> todos = listPromise.get();
        assertEquals(1, todos.size());
        assertEquals("Buy Tesla", todos.get(0).getName());

        /** Get the id of the Todo to remove it. */
        final String id = todos.get(0).getId();

        /** Remove the todo with the todo id.  */
        final Promise<Boolean> removePromise = blockingPromise();
        todoManager.remove(id).invokeWithPromise(removePromise);
        assertTrue(removePromise.get());

        /** See if the todo was removed.  */
        final Promise<List<Todo>> listPromise2 = blockingPromise();
        todoManager.list().invokeWithPromise(listPromise2);
        final List<Todo> todos2 = listPromise2.get();
        assertEquals(0, todos2.size());

    }
}