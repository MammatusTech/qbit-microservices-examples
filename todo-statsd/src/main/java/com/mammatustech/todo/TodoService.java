package com.mammatustech.todo;

import io.advantageous.qbit.admin.ServiceManagementBundle;
import io.advantageous.qbit.annotation.Named;
import io.advantageous.qbit.annotation.RequestMapping;
import io.advantageous.qbit.annotation.RequestMethod;
import io.advantageous.qbit.annotation.RequestParam;
import io.advantageous.qbit.annotation.http.DELETE;
import io.advantageous.qbit.annotation.http.GET;
import io.advantageous.qbit.annotation.http.POST;
import io.advantageous.qbit.reactive.Callback;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;


/**
 * Default port for admin is 7777.
 * Default port for main endpoint is 8888.
 *
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
@Named("TodoService")
@RequestMapping("/todo-service")
public class TodoService {


    private final Map<String, Todo> todoMap = new TreeMap<>();

    private final ServiceManagementBundle mgmt;

    public TodoService(ServiceManagementBundle mgmt) {

        this.mgmt = mgmt;

        /** Send stat count i.am.alive every three seconds.  */
        mgmt.reactor().addRepeatingTask(Duration.ofSeconds(3),
                () -> mgmt.increment("i.am.alive"));

    }


    @POST(value = "/todo")
    public void add(final Callback<Boolean> callback, final Todo todo) {

        /** Send KPI add called every time the add method gets called. */
        mgmt.increment("add.called");
        todoMap.put(todo.getId(), todo);
        callback.accept(true);
    }


    @DELETE(value = "/todo")
    public void remove(final Callback<Boolean> callback, final @RequestParam("id") String id) {

        /** Send KPI add.removed every time the remove method gets called. */
        mgmt.increment("remove.called");
        Todo remove = todoMap.remove(id);
        callback.accept(remove != null);

    }


    @GET(value = "/todo", method = RequestMethod.GET)
    public void list(final Callback<ArrayList<Todo>> callback) {

        /** Send KPI add.list every time the list method gets called. */
        mgmt.increment("list.called");

        callback.accept(new ArrayList<>(todoMap.values()));
    }


}
