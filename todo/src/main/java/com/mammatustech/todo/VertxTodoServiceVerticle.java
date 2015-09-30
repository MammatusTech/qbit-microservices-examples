package com.mammatustech.todo;

import io.advantageous.qbit.admin.ManagedServiceBuilder;
import io.advantageous.qbit.http.config.HttpServerConfig;
import io.advantageous.qbit.http.server.HttpServer;
import io.advantageous.qbit.meta.builder.ContextMetaBuilder;
import io.advantageous.qbit.system.QBitSystemManager;
import io.advantageous.qbit.vertx.http.VertxHttpServerBuilder;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * This is an alternate example that uses QBit Embedded inside of Vertx
 * instead of running QBit Standalone.
 */
public class VertxTodoServiceVerticle extends AbstractVerticle {

    /** Used to startup QBit and use features like swagger, etc. */
    private final ManagedServiceBuilder managedServiceBuilder;
    /** Used to shutdown QBit services cleanly. */
    private QBitSystemManager systemManager;

    /**
     * Create this verticle
     * @param managedServiceBuilder managedServiceBuilder
     */
    public VertxTodoServiceVerticle(ManagedServiceBuilder managedServiceBuilder) {
        this.managedServiceBuilder = managedServiceBuilder;
    }

    public static void main(final String... args) throws Exception{
        final ManagedServiceBuilder managedServiceBuilder = createManagedServiceBuilder();


        final CountDownLatch latch = new CountDownLatch(1);
        final Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new VertxTodoServiceVerticle(managedServiceBuilder), result -> {
            if (result.succeeded()) {
                System.out.println("Deployment id is: " + result.result());
            } else {
                System.out.println("Deployment failed!");
                result.cause().printStackTrace();
            }
            latch.countDown();
        });


        latch.await(5, TimeUnit.SECONDS);


        System.out.println("QBit and Vertx are open for e-Busbies");
    }


    @Override
    public void start() throws Exception {


        /* Vertx HTTP Server. */
        final io.vertx.core.http.HttpServer vertxHttpServer =
                this.getVertx().createHttpServer();

        /* Route one call to a vertx handler. */
        final Router router = createRouterAndVertxOnlyRoute();

        final HttpServer httpServer = getHttpServerAndRoutes(vertxHttpServer, router);

        systemManager = managedServiceBuilder.getSystemManager();

        managedServiceBuilder.addEndpointService(new TodoService());


        /*
        * Create and start new service endpointServer.
        */
        managedServiceBuilder
                .getEndpointServerBuilder()
                .setHttpServer(httpServer)
                .build()
                .startServer();


        /*
         * Associate the router as a request handler for the vertxHttpServer.
         */
        vertxHttpServer.requestHandler(router::accept).listen(
                managedServiceBuilder.getPort());
    }

    private HttpServer getHttpServerAndRoutes(final io.vertx.core.http.HttpServer vertxHttpServer, final Router router) {

        /* Route everything under /v1 to QBit http server. */
        final Route qbitRoute = router.route().path("/v1/*");



        /*
         * Use the VertxHttpServerBuilder which is a special builder for Vertx/Qbit integration.
         */
        return VertxHttpServerBuilder.vertxHttpServerBuilder()
                .setRoute(qbitRoute)
                .setHttpServer(vertxHttpServer)
                .setVertx(getVertx())
                .setConfig(new HttpServerConfig()) //not needed in master branch of qbit workaround for bug
                .build();
    }

    private Router createRouterAndVertxOnlyRoute() {
        final Router router = Router.router(getVertx()); //Vertx router
        router.route("/svr/rout1/").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response.setStatusCode(202);
            response.end("route1 this does not have very much data to transfer");
        });
        return router;
    }

    @Override
    public void stop() throws Exception {
        if (systemManager!=null) {
            systemManager.shutDown();
        }
    }


    private static ManagedServiceBuilder createManagedServiceBuilder() {
    /* Create the ManagedServiceBuilder which manages a clean shutdown, health, stats, etc. */
        final ManagedServiceBuilder managedServiceBuilder =
                ManagedServiceBuilder.managedServiceBuilder()
                        .setRootURI("/v1") //Defaults to services
                        .setPort(8888); //Defaults to 8080 or environment variable PORT

        /* Context meta builder to document this endpoint.
        * Gets used by swagger support.
        */
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
        return managedServiceBuilder;
    }


}
