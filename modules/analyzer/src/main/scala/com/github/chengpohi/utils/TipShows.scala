package com.github.chengpohi.utils

import scalaz.Show

object TipShows {
  implicit val tipShow: Show[String] = new Show[String] {
    override def show(f: String) = f
  }
}
