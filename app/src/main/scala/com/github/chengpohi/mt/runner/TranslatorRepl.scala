package com.github.chengpohi.mt.runner

import com.github.chengpohi.mt.config.MTDSLContext
import com.github.chengpohi.mt.translator.Translator
import org.jline.reader.impl.DefaultHighlighter
import org.jline.reader.{EndOfFileException, LineReaderBuilder, UserInterruptException}
import org.jline.terminal.TerminalBuilder

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object TranslatorRepl extends MTDSLContext {
  private val terminal = TerminalBuilder
    .builder()
    .system(true)
    .build()
  private val reader = LineReaderBuilder
    .builder()
    .terminal(terminal)
    .highlighter(new DefaultHighlighter)
    .build()

  def main(args: Array[String]): Unit = {
    import dsl._
    val translator = Translator.apply

    val health = DSL {
      cluster health
    }.toJson

    println(health)

    while (true) {
      try {
        val line = reader.readLine("translator>")
        if (line == "exit") System.exit(0)
        line.trim.isEmpty match {
          case true =>
          case false =>
            val result = translator.translate(line, "en")
            val r = Await.result(result, Duration.Inf)
            r match {
              case h #:: l => println(h)
              case _ => println("not found")
            }
        }
      } catch {
        case e: UserInterruptException => System.exit(0)
        case e: EndOfFileException => System.exit(0)
      }
    }
  }

}
