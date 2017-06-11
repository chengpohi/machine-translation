package com.github.chengpohi.mt.runner

import com.github.chengpohi.mt.config.MTConfig

import scala.io.StdIn

/**
  * Created by chengpohi on 05/02/2017.
  */
object AppRunner {
  def main(args: Array[String]): Unit = {
    BootStrap()
    println(s"Started")
    StdIn.readLine()
  }
}

object BootStrap {
  def apply(): Unit = {
    val f = MTConfig.TRAIN_DATA.filter(!_._2.exists())
    if (f.nonEmpty) {
      Console.err.println(s"""train_data file not exist: ${f}""")
      System.exit(-1)
    }
  }
}

object Foo {
  implicit def strFoo(s: String): Int = 1
}
