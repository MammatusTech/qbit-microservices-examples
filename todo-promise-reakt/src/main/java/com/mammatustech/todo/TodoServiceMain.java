package com.mammatustech.todo;


import io.advantageous.qbit.admin.ManagedServiceBuilder;
import io.advantageous.qbit.service.ServiceBundle;
import io.advantageous.qbit.service.stats.StatsCollector;
import io.advantageous.reakt.reactor.Reactor;

import java.net.URI;
import java.util.Objects;

public class TodoServiceMain {


    public static void main(final String... args) throws Exception {

        //To test locally use https://hub.docker.com/r/samuelebistoletti/docker-statsd-influxdb-grafana/
        final URI statsdURI = URI.create("udp://192.168.99.100:8125");

        //For timer
        final Reactor reactor = Reactor.reactor();


        /* Create the ManagedServiceBuilder which manages a clean shutdown, health, stats, etc. */
        final ManagedServiceBuilder managedServiceBuilder =
                ManagedServiceBuilder.managedServiceBuilder()
                        .setRootURI("/v1") //Defaults to services
                        .setPort(8888); //Defaults to 8080 or environment variable PORT

        /** Enable statsD */
        enableStatsD(managedServiceBuilder, statsdURI);
        final StatsCollector statsCollector = managedServiceBuilder.createStatsCollector();


        /** Create todo impl. */
        final TodoManagerImpl impl = new TodoManagerImpl(reactor, statsCollector);


        /** Create service bundle for internal todo manager. */
        final ServiceBundle serviceBundle = managedServiceBuilder.createServiceBundleBuilder().build();
        serviceBundle.addServiceObject("todoManager", impl).startServiceBundle();


        /** Create TodoManager. */
        final TodoManager todoManager = serviceBundle.createLocalProxy(TodoManager.class, "todoManager");

        /** Start the REST/Websocket service. */
        managedServiceBuilder.addEndpointService(new TodoService(todoManager)).getEndpointServerBuilder()
                .build().startServer();

        /* Start the admin builder which exposes health end-points and swagger meta data. */
        managedServiceBuilder.getAdminBuilder().build().startServer();

        System.out.println("Todo Server and Admin Server started");

    }

    /**
     * Enable Stats D.
     *
     * @param host statsD host
     * @param port statsD port
     */
    public static void enableStatsD(ManagedServiceBuilder managedServiceBuilder, String host, int port) {
        if (port < 1) throw new IllegalStateException("StatsD port must be set");
        Objects.requireNonNull(host, "StatsD Host cannot be null");
        if (host.isEmpty()) throw new IllegalStateException("StatsD Host name must not be empty");
        managedServiceBuilder.getStatsDReplicatorBuilder().setHost(host).setPort(port);
        managedServiceBuilder.setEnableStatsD(true);
    }

    /**
     * Enable Stats D.
     *
     * @param uri for statsd
     */
    public static void enableStatsD(ManagedServiceBuilder managedServiceBuilder, URI uri) {
        if (!uri.getScheme().equals("udp")) throw new IllegalStateException("Scheme must be udp");
        enableStatsD(managedServiceBuilder, uri.getHost(), uri.getPort());
    }
}
