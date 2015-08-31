package com.mammatustech;


import io.advantageous.qbit.admin.ManagedServiceBuilder;
import io.advantageous.qbit.annotation.RequestMapping;

/**
 * Default port for admin is 7777.
 * Default port for main endpoint is 8080.
 *
 * <pre>
 * <code>
 *
 *     Access the service:
 *
 *    $ curl http://localhost:8080/root/hello/hello
 *
 *
 *     To see swagger file for this service:
 *
 *    $ curl http://localhost:7777/__admin/meta/
 *
 *     To see health for this service:
 *
 *    $ curl http://localhost:8080/__health
 *     Returns "ok" if all registered health systems are healthy.
 *
 *     OR if same port endpoint health is disabled then:
 *
 *    $ curl http://localhost:7777/__admin/ok
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
 *    $ curl http://localhost:8080/__stats/instance
 * </code>
 * </pre>
 */
@RequestMapping("/hello")
public class HelloWorldService {


    @RequestMapping("/hello")
    public String hello() {
        return "hello " + System.currentTimeMillis();
    }

    public static void main(final String... args) {
        final ManagedServiceBuilder managedServiceBuilder =
                ManagedServiceBuilder.managedServiceBuilder().setRootURI("/root");

        /* Start the service. */
        managedServiceBuilder.addEndpointService(new HelloWorldService())
                .getEndpointServerBuilder()
                .build().startServer();

        /* Start the admin builder which exposes health end-points and meta data. */
        managedServiceBuilder.getAdminBuilder().build().startServer();

        System.out.println("Servers started");


    }


}
