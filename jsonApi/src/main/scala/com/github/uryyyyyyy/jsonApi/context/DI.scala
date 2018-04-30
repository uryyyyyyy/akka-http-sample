package com.github.uryyyyyyy.jsonApi.context

import akka.actor.ActorSystem
import com.github.uryyyyyyy.jsonApi.controller.HelloController
import com.github.uryyyyyyy.jsonApi.service.{HelloService, HelloServiceImpl}
import com.google.inject.{AbstractModule, Guice, Injector}

object DI {
  def generateInjector(system: ActorSystem): Injector = Guice.createInjector(new AbstractModule() {
    override protected def configure(): Unit = {
      bind(classOf[HelloService]).to(classOf[HelloServiceImpl])
      bind(classOf[ActorSystem]).toInstance(system)
      bind(classOf[HelloController])
    }
  })
}
