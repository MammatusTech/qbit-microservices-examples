package com.mammatustech

import io.advantageous.qbit.admin.ManagedServiceBuilder
import io.advantageous.qbit.annotation.RequestMapping

/**
 * Created by fadi on 10/12/15.
 */
@RequestMapping(Array("/hello")) object HelloWorldService {
  def main(args: Array[String]) {
    val managedServiceBuilder: ManagedServiceBuilder = ManagedServiceBuilder.managedServiceBuilder.setRootURI("/root")
    managedServiceBuilder.addEndpointService(new HelloWorldService).getEndpointServerBuilder.build.startServer
    managedServiceBuilder.getAdminBuilder.build.startServer
    System.out.println("Servers started")
  }
}

@RequestMapping(Array("/hello")) class HelloWorldService {
  @RequestMapping(Array("/hello")) def hello: String = {
    return "hello " + System.currentTimeMillis
  }
}