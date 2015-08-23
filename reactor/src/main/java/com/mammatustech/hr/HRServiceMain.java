package com.mammatustech.hr;


import io.advantageous.qbit.admin.ManagedServiceBuilder;
import io.advantageous.qbit.reactive.Reactor;
import io.advantageous.qbit.reactive.ReactorBuilder;
import io.advantageous.qbit.service.ServiceQueue;

import java.util.concurrent.TimeUnit;


/**
 * Default port for admin is 7777.
 * Default port for main endpoint is 8080.
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
 *    $ curl http://localhost:8888/__health
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
 *    $ curl http://localhost:8888/__stats/instance
 * </code>
 * </pre>
 */
public class HRServiceMain {


    public static void main(final String... args) throws Exception {

        /* Create the ManagedServiceBuilder which manages a clean shutdown, health, stats, etc. */
        final ManagedServiceBuilder managedServiceBuilder =
                ManagedServiceBuilder.managedServiceBuilder()
                        .setRootURI("/v1") //Defaults to services
                        .setPort(8888); //Defaults to 8080 or environment variable PORT


        /* Build the reactor. */
        final Reactor reactor = ReactorBuilder.reactorBuilder()
                                .setDefaultTimeOut(1)
                                .setTimeUnit(TimeUnit.SECONDS)
                                .build();


        /* Build the service queue for DepartmentRepo. */
        final ServiceQueue departmentRepoServiceQueue = managedServiceBuilder
                .createServiceBuilderForServiceObject(new DepartmentRepo()).build();

        departmentRepoServiceQueue.startServiceQueue().startCallBackHandler();

        /* Build the remote interface for department repo. */
        final DepartmentRepoAsync departmentRepoAsync =
                departmentRepoServiceQueue.createProxy(DepartmentRepoAsync.class);



        /* Start the service. */
        managedServiceBuilder.addEndpointService(new HRService(reactor, departmentRepoAsync)) //Register HRService
                .getEndpointServerBuilder()
                .build().startServer();

        /* Start the admin builder which exposes health end-points and swagger meta data. */
        managedServiceBuilder.getAdminBuilder().build().startServer();

        System.out.println("HR Server and Admin Server started");

    }
}
