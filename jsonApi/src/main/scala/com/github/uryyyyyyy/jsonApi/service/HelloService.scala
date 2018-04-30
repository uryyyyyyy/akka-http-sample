package com.github.uryyyyyyy.jsonApi.service

trait HelloService {
  def hello(): String
}

class HelloServiceImpl extends HelloService{
  def hello(): String = {
    "impl"
  }
}

class HelloServiceMock extends HelloService{
  def hello(): String = {
    "mock"
  }
}
