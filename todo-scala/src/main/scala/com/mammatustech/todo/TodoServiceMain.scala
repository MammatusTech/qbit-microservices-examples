package com.mammatustech.todo

import io.advantageous.qbit.admin.ManagedServiceBuilder._

object TodoServiceMain {
  @throws(classOf[Exception])
  def main(args: String*) {
    val builder = managedServiceBuilder().setRootURI("/v1").setPort(8888)
    val contextMetaBuilder = managedServiceBuilder.getContextMetaBuilder
    contextMetaBuilder.setContactEmail("lunati-not-real-email@gmail.com")
    contextMetaBuilder.setDescription("A great service to show building a todo list")
    contextMetaBuilder.setContactURL("http://www.bwbl.lunati/master/of/rodeo")
    contextMetaBuilder.setContactName("Buffalo Wild Bill Lunati")
    contextMetaBuilder.setLicenseName("Commercial")
    contextMetaBuilder.setLicenseURL("http://www.canttouchthis.com")
    contextMetaBuilder.setTitle("Todo Title")
    contextMetaBuilder.setVersion("47.0")
    builder.getStatsDReplicatorBuilder.setHost("192.168.59.103")
    builder.setEnableStatsD(true)
    builder.addEndpointService(new TodoService).getEndpointServerBuilder.build.startServer
    builder.getAdminBuilder.build.startServer
    println("Todo Server and Admin Server started")
  }
}