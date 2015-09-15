package com.mammatustech.todo;


import io.advantageous.qbit.admin.ManagedServiceBuilder;
import io.advantageous.qbit.meta.builder.ContextMetaBuilder;

public class TodoServiceMain {


    public static void main(final String... args) throws Exception {

        /* Create the ManagedServiceBuilder which manages a clean shutdown, health, stats, etc. */
        final ManagedServiceBuilder managedServiceBuilder =
                ManagedServiceBuilder.managedServiceBuilder()
                        .setRootURI("/v1") //Defaults to services
                        .setPort(8888); //Defaults to 8080 or environment variable PORT

        /* Context meta builder to document this endpoint.  */
        ContextMetaBuilder contextMetaBuilder = managedServiceBuilder.getContextMetaBuilder();
        contextMetaBuilder.setContactEmail("lunati-not-real-email@gmail.com");
        contextMetaBuilder.setDescription("A great service to show building a todo list");
        contextMetaBuilder.setContactURL("http://www.bwbl.lunati/master/of/rodeo");
        contextMetaBuilder.setContactName("Buffalo Wild Bill Lunati");
        contextMetaBuilder.setLicenseName("Commercial");
        contextMetaBuilder.setLicenseURL("http://www.canttouchthis.com");
        contextMetaBuilder.setTitle("Todo Title");
        contextMetaBuilder.setVersion("47.0");


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
