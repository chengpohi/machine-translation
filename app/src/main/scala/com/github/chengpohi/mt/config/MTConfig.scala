package com.github.chengpohi.mt.config

import java.io.File

import com.typesafe.config.ConfigFactory

import scala.collection.JavaConverters._

/**
  * Created by chengpohi on 05/02/2017.
  */
object MTConfig {
  private val MT_CONFIG = ConfigFactory.load("mt.conf").getConfig("mt")
  val TRAIN_DATA: Map[String, File] = {
    val trainData = MT_CONFIG.getConfig("train_data")
    trainData.root().asScala.map(t => {
      (t._1, new File(trainData.getString(t._1)))
    }).toMap
  }
}
