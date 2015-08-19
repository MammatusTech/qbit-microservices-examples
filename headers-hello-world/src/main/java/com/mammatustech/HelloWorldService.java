package com.mammatustech;


import io.advantageous.qbit.admin.ManagedServiceBuilder;
import io.advantageous.qbit.annotation.RequestMapping;
import io.advantageous.qbit.http.request.HttpRequest;
import io.advantageous.qbit.http.server.HttpServerBuilder;

import java.util.function.Predicate;

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
 *    This above will respond shove off.
 *
 *    $ curl --header "X-SECURITY-TOKEN: shibboleth" http://localhost:8080/root/hello/hello
 *
 *    This will get your hello message.
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

        final HttpServerBuilder httpServerBuilder = managedServiceBuilder.getHttpServerBuilder();

        /** We can register our security token here. */
        httpServerBuilder.addShouldContinueHttpRequestPredicate(HelloWorldService::checkAuth);

        /* Start the service. */
        managedServiceBuilder.addEndpointService(new HelloWorldService())
                .getEndpointServerBuilder()
                .build().startServer();

        /* Start the admin builder which exposes health end-points and meta data. */
        managedServiceBuilder.getAdminBuilder().build().startServer();

        System.out.println("Servers started");


    }

    /**
     * Checks to see if the header <code>X-SECURITY-TOKEN</code> is set to "shibboleth".
     * @param httpRequest http request
     * @return true if we should continue, i.e., auth passed, false otherwise.
     */
    private static boolean checkAuth(final HttpRequest httpRequest) {

        /* Only check uri's that start with /root/hello. */
        if (httpRequest.getUri().startsWith("/root/hello")) {

            final String x_security_token = httpRequest.headers().getFirst("X-SECURITY-TOKEN");

            /* If the security token is set to "shibboleth" then continue processing the request. */
            if ("shibboleth".equals(x_security_token)) {
                return true;
            } else {
                /* Security token was not what we expected so send a 401 auth failed. */
                httpRequest.getReceiver().response(401, "application/json", "\"shove off\"");
                return false;
            }
        }
        return true;
    }
}
