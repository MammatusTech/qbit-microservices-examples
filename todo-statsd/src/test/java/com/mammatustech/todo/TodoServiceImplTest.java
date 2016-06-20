package com.mammatustech.todo;

import io.advantageous.qbit.admin.ManagedServiceBuilder;
import io.advantageous.qbit.admin.ServiceManagementBundle;
import io.advantageous.qbit.queue.QueueCallBackHandler;
import io.advantageous.qbit.service.ServiceBuilder;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static io.advantageous.qbit.admin.ManagedServiceBuilder.managedServiceBuilder;
import static io.advantageous.qbit.admin.ServiceManagementBundleBuilder.serviceManagementBundleBuilder;
import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

public class TodoServiceImplTest {
    @Test
    public void test() throws Exception {
        final TodoService todoService = createTodoService();

        final Todo rick = new Todo("foo", "rick", 1L);

        //Add Rick
        assertTrue(todoService
                .addTodo(rick)
                .invokeAsBlockingPromise().get());


        //Add Diana
        assertTrue(todoService
                .addTodo(new Todo("bar", "diana", 1L))
                .invokeAsBlockingPromise().get());

        //Remove Rick
        assertTrue(todoService.removeTodo(rick.getId())
                .invokeAsBlockingPromise().get());

        //Make sure Diana is in the listTodos
        assertTrue(todoService.listTodos()
                .invokeAsBlockingPromise()
                .get()
                .stream()
                .filter(
                        todo -> todo.getDescription().equals("diana")

                )
                .findFirst()
                .isPresent()
        );


        //Make sure Rick is not in the listTodos
        assertFalse(todoService.listTodos()
                .invokeAsBlockingPromise()
                .get()
                .stream()
                .filter(
                        todo -> todo.getDescription().equals("rick")

                )
                .findFirst()
                .isPresent()
        );

    }

    private TodoService createTodoService() {
    /* Create the ManagedServiceBuilder which manages a clean shutdown, health, stats, etc. */
        final ManagedServiceBuilder managedServiceBuilder = managedServiceBuilder(); //Defaults to 8080 or environment variable PORT


        /** Create the management bundle for this service. */
        final ServiceManagementBundle serviceManagementBundle =
                serviceManagementBundleBuilder().setServiceName("TodoService")
                        .setManagedServiceBuilder(managedServiceBuilder).build();

        final TodoService todoServiceImpl = new TodoServiceImpl(serviceManagementBundle);


        return ServiceBuilder.serviceBuilder().setServiceObject(todoServiceImpl).addQueueCallbackHandler(
                new QueueCallBackHandler() {
                    @Override
                    public void queueProcess() {
                        serviceManagementBundle.process();
                    }
                })
                .buildAndStartAll()
                .createProxyWithAutoFlush(TodoService.class, 50, TimeUnit.MILLISECONDS);

    }
}