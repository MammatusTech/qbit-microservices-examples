package com.mammatustech.todo;

import io.advantageous.qbit.server.EndpointServerBuilder;
import io.advantageous.qbit.server.ServiceEndpointServer;

import static io.advantageous.qbit.server.EndpointServerBuilder.endpointServerBuilder;

public class TodoServiceMain {

    public static void main(final String... args) {


        /** Object address to the auditorService service actor. */
        final String auditorAddress = "auditorService";


        /* Create the serviceBundleBuilder. */
        final EndpointServerBuilder endpointServerBuilder = endpointServerBuilder();

        endpointServerBuilder.setPort(8080).setUri("/");

        endpointServerBuilder.addService(auditorAddress, new AuditorImpl());


        /* Create the service server. */
        final ServiceEndpointServer serviceEndpointServer = endpointServerBuilder.build();


        /* Create a service client proxy for the auditor. */
        final Auditor auditor = serviceEndpointServer.serviceBundle()
                .createLocalProxy(Auditor.class, auditorAddress);

        /* Create a todo manager and pass the client proxy of the auditor to it. */
        final TodoManagerImpl todoManager = new TodoManagerImpl(auditor);

        // Add the todoManager to the serviceBundle.
        serviceEndpointServer.addService(todoManager);

        /* Start the service endpoint server and wait until it starts. */
        serviceEndpointServer.startServerAndWait();

        System.out.println("Started");
    }

}
