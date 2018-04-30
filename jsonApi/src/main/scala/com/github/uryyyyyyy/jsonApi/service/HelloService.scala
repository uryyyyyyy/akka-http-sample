package com.github.uryyyyyyy.jsonApi.service

trait HelloService {
  def hello(): Unit
}

class HelloServiceImpl extends HelloService{
  def hello(): Unit = {
    println("impl")
  }
}

class HelloServiceMock extends HelloService{
  def hello(): Unit = {
    println("mock")
  }
}
