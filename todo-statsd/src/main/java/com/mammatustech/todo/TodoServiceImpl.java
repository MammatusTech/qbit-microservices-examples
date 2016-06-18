package com.mammatustech.todo;

import io.advantageous.qbit.admin.ServiceManagementBundle;
import io.advantageous.qbit.annotation.RequestMapping;
import io.advantageous.qbit.annotation.RequestMethod;
import io.advantageous.qbit.annotation.RequestParam;
import io.advantageous.qbit.annotation.http.DELETE;
import io.advantageous.qbit.annotation.http.GET;
import io.advantageous.qbit.annotation.http.POST;
import io.advantageous.reakt.promise.Promise;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import static io.advantageous.reakt.promise.Promises.invokablePromise;


/**
 * Default port for admin is 7777.
 * Default port for main endpoint is 8888.
 * <p>
 * <pre>
 * <code>
 *
 *     Access the service:
 *
 *    $ curl http://localhost:8888/v1/...
 *
 *
 *     To see swagger file for this service:
 *
 *    $ curl http://localhost:7777/__admin/meta/
 *
 *     To see health for this service:
 *
 *    $ curl http://localhost:8888/__health -v
 *     Returns "ok" if all registered health systems are healthy.
 *
 *     OR if same port endpoint health is disabled then:
 *
 *    $ curl http://localhost:7777/__admin/ok -v
 *     Returns "true" if all registered health systems are healthy.
 *
 *
 *     A node is a service, service bundle, queue, or server endpoint that is being monitored.
 *
 *     List all service nodes or endpoints
 *
 *    $ curl http://localhost:7777/__admin/all-nodes/
 *
 *
 *      List healthy nodes by name:
 *
 *    $ curl http://localhost:7777/__admin/healthy-nodes/
 *
 *      List complete node information:
 *
 *    $ curl http://localhost:7777/__admin/load-nodes/
 *
 *
 *      Show service stats and metrics
 *
 *    $ curl http://localhost:8888/__stats/instance
 * </code>
 * </pre>
 */
@RequestMapping("/todo-service")
public class TodoServiceImpl implements TodoService {


    private final Map<String, Todo> todoMap = new TreeMap<>();

    private final ServiceManagementBundle mgmt;

    public TodoServiceImpl(ServiceManagementBundle mgmt) {
        this.mgmt = mgmt;
        /** Send stat count i.am.alive every three seconds.  */
        mgmt.reactor().addRepeatingTask(Duration.ofSeconds(3),
                () -> mgmt.increment("i.am.alive"));

    }


    @Override
    @POST(value = "/todo")
    public Promise<Boolean> addTodo(final Todo todo) {
        return invokablePromise(promise -> {
            /** Send KPI addTodo called every time the addTodo method gets called. */
            mgmt.increment("addTodo.called");
            todoMap.put(todo.getId(), todo);
            promise.accept(true);
        });
    }


    @Override
    @DELETE(value = "/todo")
    public final Promise<Boolean> removeTodo(final @RequestParam("id") String id) {
        return invokablePromise(promise -> {
            /** Send KPI addTodo.removed every time the removeTodo method gets called. */
            mgmt.increment("removeTodo.called");
            todoMap.remove(id);
            promise.accept(true);
        });
    }


    @Override
    @GET(value = "/todo", method = RequestMethod.GET)
    public final Promise<ArrayList<Todo>> listTodos() {
        return invokablePromise(promise -> {
            /** Send KPI addTodo.listTodos every time the listTodos method gets called. */
            mgmt.increment("listTodos.called");
            promise.accept(new ArrayList<>(todoMap.values()));
        });
    }


}
