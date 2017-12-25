package com.github.chengpohi.nlp.config

import java.io.File

import com.github.chengpohi.connector.ELKDSLConfig
import com.github.chengpohi.registry.ELKDSLContext
import com.typesafe.config.{Config, ConfigFactory}

import scala.collection.JavaConverters._

/**
  * Created by chengpohi on 05/02/2017.
  */
object MTConfig {
  private val MT_CONFIG = ConfigFactory.load("mt.conf").getConfig("mt")
  val TRAIN_DATA: Map[String, File] = {
    val trainData = MT_CONFIG.getConfig("train_data")
    trainData
      .root()
      .asScala
      .map(t => {
        (t._1, new File(trainData.getString(t._1)))
      })
      .toMap
  }
}

trait MTDSLConfig extends ELKDSLConfig {
  override val config: Config =
    ConfigFactory.load("mt.conf").getConfig("elasticdsl")
}

trait MTDSLContext extends MTDSLConfig with ELKDSLContext
