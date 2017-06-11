package com.github.chengpohi.mt.runner

import com.github.chengpohi.mt.translator.Translator
import org.jline.reader.impl.DefaultHighlighter
import org.jline.reader.{EndOfFileException, LineReaderBuilder, UserInterruptException}
import org.jline.terminal.TerminalBuilder

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object TranslatorRepl {
  private val translator = Translator.apply
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
    while (true) {
      try {
        val line = reader.readLine("translator>")
        if (line == "exit") System.exit(0)
        line.trim.isEmpty match {
          case true =>
          case false =>
            val result = translator.translate(line)
            val t = Await.result(result, Duration.Inf)
            t.foreach(println)
        }
      } catch {
        case e: UserInterruptException => System.exit(0)
        case e: EndOfFileException => System.exit(0)
      }
    }
  }

}
