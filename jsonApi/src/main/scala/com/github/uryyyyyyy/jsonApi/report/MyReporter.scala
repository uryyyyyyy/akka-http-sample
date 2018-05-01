package com.github.uryyyyyyy.jsonApi.report

import com.typesafe.config.Config
import kamon.MetricReporter
import kamon.metric.PeriodSnapshot
import org.slf4j.LoggerFactory

class MyReporter extends MetricReporter {

  private val logger = LoggerFactory.getLogger(classOf[MyReporter])

  override def reportPeriodSnapshot(snapshot: PeriodSnapshot): Unit = {
    logger.info("reportTickSnapshot")
    snapshot.metrics.counters.foreach(metric => {
      logger.info(s"name: ${metric.name}, value: ${metric.value}")
    })
    snapshot.metrics.histograms.foreach(metric => {
      logger.info(s"name: ${metric.name}, value-sum: ${metric.distribution.sum}")
    })
    snapshot.metrics.rangeSamplers.foreach(metric => {
      logger.info(s"name: ${metric.name}, value-max: ${metric.distribution.max}")
    })
  }

  override def start(): Unit = {
    logger.info("MyReporter start")
  }

  override def stop(): Unit = {
    logger.info("MyReporter stop")
  }

  override def reconfigure(config: Config): Unit = {
    logger.info("MyReporter reconfigure")
  }
}
