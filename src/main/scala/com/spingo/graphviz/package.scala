package com.spingo

import java.io.{File,PrintWriter, Writer}
import org.apache.commons.lang3.StringEscapeUtils.escapeJava

package object graphviz {

  type GvParameter = (String, GvValue)
  object GvParameters {
    def apply(l: GvParameter*) = l
  }
  trait GvValue { def render: String; def value: Any }
  case class GvString(value: String) extends GvValue {
    def render = "\"%s\"" format escapeJava(value)
  }
  case class GvNumber[A](value: A)(implicit numeric: Numeric[A]) extends GvValue {
    def render = value.toString
  }

  implicit def numericToGvNumber[A](n: A)(implicit numeric: Numeric[A]) = GvNumber(n)
  implicit def stringToGvString(s: String) = GvString(s)

  class Digraph(output: PrintWriter, name: String) extends java.io.Closeable {
    output.write(s"digraph ${name}{\n")

    private def toParameters(params: Seq[GvParameter]) =
      params map { case (k, v) => "%s=%s" format (k, v.render) } mkString " "

    def node(node: String, params: GvParameter*): Unit =
      output.write(s"""  "${escapeJava(node)}" [${toParameters(params)}]\n""")

    def edge(nodeA: String, nodeB: String, params: GvParameter*): Unit =
      output.write(s"""  "${escapeJava(nodeA)}" -> "${escapeJava(nodeB)}" [${toParameters(params)}]\n""")
    def close = {
      output.write("}\n")
      output.close
    }
  }

  object Digraph {
    def apply(name: String, writer: PrintWriter) = new Digraph(writer, name)
    def apply(name: String, writer: Writer)      = new Digraph(new PrintWriter(writer), name)
    def apply(name: String, file: File)          = new Digraph(new PrintWriter(file), name)
    def apply(writer: PrintWriter): Digraph = apply("g", writer)
    def apply(writer: Writer): Digraph      = apply("g", writer)
    def apply(file: File): Digraph          = apply("g", new PrintWriter(file))
  }

  class ColorCycler(colors: String*) {
    var i = -1
    def next = {
      i = (i + 1) % colors.length
      colors(i)
    }
  }

  object ColorCycler {
    lazy val default = new ColorCycler(
      "#1D8BD1", "#F1683C", "#2AD62A", "#DBDC25", "#8FBC8B", "#D2B48C", "#20B2AA", "#B0C4DE", "#DDA0DD",
      "#9C9AFF", "#9C3063", "#630063", "#FF8284", "#0065CE", "#CECFFF", "#000084", "#FF00FF", "#840084",
      "#840000", "#008284", "#0000FF", "#00CFFF", "#FF9ACE", "#CE9AFF", "#FFCF9C", "#3165FF", "#31CFCE",
      "#9CCF00", "#FFCF00", "#FF9A00", "#FF6500")
  }
}
