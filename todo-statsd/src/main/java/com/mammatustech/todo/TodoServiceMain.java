package com.mammatustech.todo;


import io.advantageous.qbit.admin.ManagedServiceBuilder;
import io.advantageous.qbit.admin.ServiceManagementBundle;

import java.net.URI;

import static io.advantageous.qbit.admin.ManagedServiceBuilder.managedServiceBuilder;
import static io.advantageous.qbit.admin.ServiceManagementBundleBuilder.serviceManagementBundleBuilder;

public class TodoServiceMain {


    public static void main(final String... args) throws Exception {



        /* Create the ManagedServiceBuilder which manages a clean shutdown, health, stats, etc. */
        final ManagedServiceBuilder managedServiceBuilder = managedServiceBuilder()
                .setRootURI("/v1") //Defaults to services
                .setPort(8888); //Defaults to 8080 or environment variable PORT

        managedServiceBuilder.enableStatsD(URI.create("udp://192.168.99.100:8125"));
        managedServiceBuilder.getContextMetaBuilder().setTitle("TodoMicroService");

        /** Create the management bundle for this service. */
        final ServiceManagementBundle serviceManagementBundle =
                serviceManagementBundleBuilder().setServiceName("TodoServiceImpl")
                        .setManagedServiceBuilder(managedServiceBuilder).build();

        final TodoService todoService = new TodoServiceImpl(serviceManagementBundle);

        /* Start the service. */
        managedServiceBuilder
                //Register TodoServiceImpl
                .addEndpointServiceWithServiceManagmentBundle(todoService, serviceManagementBundle)
                //Build and start the server.
                .startApplication();

        /* Start the admin builder which exposes health end-points and swagger meta data. */
        managedServiceBuilder.getAdminBuilder().build().startServer();

        System.out.println("Todo Server and Admin Server started");

    }
}
