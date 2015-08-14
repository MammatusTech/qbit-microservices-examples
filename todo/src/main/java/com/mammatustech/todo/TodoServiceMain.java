package com.mammatustech.todo;


import io.advantageous.qbit.admin.ManagedServiceBuilder;

public class TodoServiceMain {


    public static void main(final String... args) throws Exception {

        /* Create the ManagedServiceBuilder which manages a clean shutdown, health, stats, etc. */
        final ManagedServiceBuilder managedServiceBuilder =
                ManagedServiceBuilder.managedServiceBuilder()
                        .setRootURI("/v1") //Defaults to services
                        .setPort(8888); //Defaults to 8080 or environment variable PORT

        managedServiceBuilder.getStatsDReplicatorBuilder().setHost("192.168.59.103");
        managedServiceBuilder.setEnableStatsD(true);


        /* Start the service. */
        managedServiceBuilder.addEndpointService(new TodoService()) //Register TodoService
                .getEndpointServerBuilder()
                .build().startServer();

        /* Start the admin builder which exposes health end-points and swagger meta data. */
        managedServiceBuilder.getAdminBuilder().build().startServer();

        System.out.println("Todo Server and Admin Server started");

    }
}
